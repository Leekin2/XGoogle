package io.github.leekin2.xgoogle.migration

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import io.github.leekin2.xgoogle.migration.compat.ModernHookBridge as XposedBridge
import io.github.leekin2.xgoogle.migration.compat.ModernMethodHook as XC_MethodHook
import io.github.leekin2.xgoogle.migration.compat.ModernReflect as XposedHelpers
import io.github.leekin2.xgoogle.migration.AssistantRuntime.GOOGLE_APP_PACKAGE
import io.github.leekin2.xgoogle.migration.AssistantRuntime.SECURE_ASSISTANT
import io.github.leekin2.xgoogle.migration.AssistantRuntime.SECURE_VOICE_INTERACTION_SERVICE
import io.github.leekin2.xgoogle.migration.AssistantRuntime.findSystemContext
import io.github.leekin2.xgoogle.migration.AssistantRuntime.installedHooks
import io.github.leekin2.xgoogle.migration.AssistantRuntime.log
import io.github.leekin2.xgoogle.migration.AssistantRuntime.resolveDefaultAssistant

/**
 * HyperOS: the companion points the power-key long press at Google (launch_google_search).
 * While the user has chosen another assistant or none, the system sees the key as unassigned,
 * so a long press opens HyperOS's own power menu at its usual speed instead of Google's edge
 * light. A change of the default assistant takes effect at once.
 */
internal object PowerKeyAssistantHooks {
    private const val POWER_LONG_PRESS = "long_press_power_key"
    private const val GOOGLE_SEARCH = "launch_google_search"
    private const val NONE = "none"

    @Volatile
    private var observer: ContentObserver? = null

    private fun googleIsAssistant(): Boolean {
        val context = findSystemContext() ?: return true
        return runCatching { resolveDefaultAssistant(context)?.packageName == GOOGLE_APP_PACKAGE }.getOrDefault(true)
    }

    fun hookSystem(classLoader: ClassLoader) {
        val rule = XposedHelpers.findClassIfExists("com.android.server.policy.MiuiSingleKeyRule", classLoader) ?: return
        val powerRule = XposedHelpers.findClassIfExists(
            "com.android.server.input.shortcut.singlekeyrule.PowerKeyRule", classLoader) ?: return
        val key = "system:power-key-assistant:${powerRule.name}"
        if (!installedHooks.add(key)) return
        runCatching {
            XposedBridge.atomic {
                val getFunction = rule.declaredMethods.filter {
                    it.name == "getFunction" && it.parameterTypes.contentEquals(arrayOf(String::class.java))
                }
                check(getFunction.isNotEmpty()) { "MiuiSingleKeyRule.getFunction(String) was not found" }
                getFunction.forEach { method ->
                    XposedBridge.hookMethod(method, object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            if (param.args.firstOrNull() != POWER_LONG_PRESS || param.result != GOOGLE_SEARCH) return
                            if (!googleIsAssistant()) param.result = NONE
                        }
                    })
                }
                // The power menu timing is set from the function when the rule starts; follow the
                // default assistant from then on.
                XposedBridge.hookAllMethods(powerRule, "init", object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        watchAssistant(param.thisObject ?: return)
                    }
                })
            }
            log("Power-key long press goes to Google only while Google is the default assistant")
        }.onFailure {
            installedHooks.remove(key)
            log("Power-key assistant check unavailable", it)
        }
    }

    private fun watchAssistant(powerRule: Any) {
        if (observer != null) return
        val context = runCatching { XposedHelpers.getObjectField(powerRule, "mContext") as? android.content.Context }
            .getOrNull() ?: findSystemContext() ?: return
        val apply = Runnable {
            runCatching {
                val helper = XposedHelpers.getObjectField(powerRule, "mMiuiShortcutTriggerHelper") ?: return@runCatching
                val function = XposedHelpers.callMethod(powerRule, "getFunction", POWER_LONG_PRESS) as? String
                XposedHelpers.callMethod(helper, "setVeryLongPressPowerBehavior")
                XposedHelpers.callMethod(helper, "setLongPressPowerBehavior", function)
            }.onFailure { log("Power menu behavior not refreshed", it) }
        }
        val watcher = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) = apply.run()
        }
        synchronized(this) {
            if (observer != null) return
            observer = watcher
        }
        val resolver = context.contentResolver
        resolver.registerContentObserver(Settings.Secure.getUriFor(SECURE_ASSISTANT), false, watcher)
        resolver.registerContentObserver(Settings.Secure.getUriFor(SECURE_VOICE_INTERACTION_SERVICE), false, watcher)
        apply.run()
    }
}
