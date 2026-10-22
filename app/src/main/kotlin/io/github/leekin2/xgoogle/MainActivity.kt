package io.github.leekin2.xgoogle

import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import io.github.leekin2.xgoogle.ui.AppLanguage
import io.github.leekin2.xgoogle.ui.AssistantInfo
import io.github.leekin2.xgoogle.ui.Look
import io.github.leekin2.xgoogle.ui.MaterialScreen
import io.github.leekin2.xgoogle.ui.MiuixScreen
import io.github.leekin2.xgoogle.ui.XGoogleState
import io.github.leekin2.xgoogle.ui.XGoogleTheme
import io.github.leekin2.xgoogle.ui.CompanionState
import io.github.leekin2.xgoogle.ui.DialogKind
import io.github.leekin2.xgoogle.ui.textsFor
import io.github.libxposed.service.XposedService
import java.io.IOException
import java.util.Locale
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private val worker = Executors.newSingleThreadExecutor()
    private val liveKeys = setOf(FeaturePolicy.CIRCLE_VIBRATE, FeaturePolicy.HEY_GOOGLE,
        FeaturePolicy.CIRCLE_LONG_PRESS, FeaturePolicy.CIRCLE_SIDE_HOLD, FeaturePolicy.CIRCLE_DOUBLE_TAP)
    private val savingKeys = mutableSetOf<String>()
    private var preferences: SharedPreferences? = null
    private var companionCheckedThisResume = false
    private var companionCheckedAt: Long? = null
    private var companionUpdateOffered = false
    private var state by mutableStateOf(XGoogleState())
    private val texts get() = textsFor(this, state.language)
    private val uiPreferences by lazy { getSharedPreferences("xgoogle_ui", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // First run follows the system language; afterwards the user's choice wins.
        val saved = uiPreferences.getString("language", null)
        state = state.copy(language = saved?.let(AppLanguage::of) ?: AppLanguage.forSystem(Locale.getDefault().language),
            features = cachedFeatures(), look = Look(uiPreferences.getBoolean("hyper_style", false), uiPreferences.getInt("look_theme", 0),
                uiPreferences.getBoolean("look_monet", false), uiPreferences.getBoolean("look_blur", false)))
        // If LSPosed never answers (module disabled), stop waiting and show the real state.
        window.decorView.postDelayed({ if (!isDestroyed && state.loading) state = state.copy(loading = false) }, 2000)
        setContent {
            var tab by rememberSaveable { mutableIntStateOf(0) }
            XGoogleTheme(state.look) {
                if (state.look.hyper) MiuixScreen(state, ::changeSetting, ::openAssistant, ::changeIconVisibility,
                    ::openModuleManager, ::openRepository, ::openTelegram, ::changeLanguage,
                    ::changeLook, ::confirmDialog, ::dismissDialog, { showDialog(DialogKind.RESTART) }, ::checkUpdates, ::openSupport,
                    tab) { tab = it }
                else MaterialScreen(state, ::changeSetting, ::openAssistant, ::changeIconVisibility,
                    ::openModuleManager, ::openRepository, ::openTelegram, ::changeLanguage,
                    ::changeLook, ::confirmDialog, ::dismissDialog, { showDialog(DialogKind.RESTART) }, ::checkUpdates, ::openSupport,
                    tab) { tab = it }
            }
        }
        SettingsConnection.connect(this)
    }

    override fun onResume() {
        super.onResume()
        companionCheckedThisResume = false
        state = state.copy(assistant = readAssistant(), iconHidden = iconHidden())
        refreshScopes()
        if (preferences != null && usesCompanion()) checkCompanion(false)
    }

    override fun onDestroy() {
        SettingsConnection.detach(this)
        worker.shutdown()
        super.onDestroy()
    }

    fun onFrameworkChanged(service: XposedService?) {
        if (isDestroyed) return
        // The first callback arrives before LSPosed binds; keep showing the cached values.
        if (service == null && state.loading) return
        preferences = runCatching { service?.getRemotePreferences(FeatureConfig.GROUP) }.getOrNull()
        syncPreferences()
        if (service != null) requestMissingScope(service)
        refreshScopes()
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && usesCompanion()) checkCompanion(false)
    }

    /**
     * LSPosed drops an app from the scope when it is reinstalled (Google Wallet, the Google app),
     * and XGoogle then silently stops working there. Ask LSPosed once per installation to add the
     * installed apps XGoogle works in back; the camera only while its Lens button is on.
     */
    private fun requestMissingScope(service: XposedService) {
        runCatching {
            val scope = service.scope.toSet()
            val declared = javaClass.classLoader!!.getResourceAsStream("META-INF/xposed/scope.list")
                .bufferedReader().readLines().map(String::trim)
            val missing = declared.filter { it.isNotEmpty() && it != "system" && it !in scope }
                .filter { it != "com.android.camera" || state.features[FeaturePolicy.CAMERA_LENS] == true }
                .mapNotNull { name ->
                    val installed = runCatching { packageManager.getPackageInfo(name, 0).firstInstallTime }.getOrNull()
                    installed?.let { name to "scope_asked_${name}_$it" }
                }
                .filterNot { (_, asked) -> uiPreferences.getBoolean(asked, false) }
            if (missing.isEmpty()) return
            uiPreferences.edit().apply { missing.forEach { (_, asked) -> putBoolean(asked, true) } }.apply()
            service.requestScope(missing.map { it.first }, object : XposedService.OnScopeEventListener {})
        }
    }

    private fun readAssistant(): AssistantInfo = when (val label = AssistantSelection.label(this)) {
        null -> AssistantInfo.Hidden
        "" -> AssistantInfo.None
        else -> AssistantInfo.App(label)
    }

    private fun changeLanguage(language: AppLanguage) {
        uiPreferences.edit().putString("language", language.code).apply()
        state = state.copy(language = language)
    }

    private fun changeLook(look: Look) {
        uiPreferences.edit().putBoolean("hyper_style", look.hyper).putInt("look_theme", look.theme)
            .putBoolean("look_monet", look.monet).putBoolean("look_blur", look.blur).apply()
        state = state.copy(look = look)
    }

    private fun syncPreferences() {
        val remote = preferences
        val values = if (remote == null) state.features else XGoogleState.featureKeys.associateWith { key ->
            runCatching { remote.getBoolean(key, FeaturePolicy.defaultValue(key)) }
                .getOrNull() ?: FeaturePolicy.defaultValue(key)
        }
        state = state.copy(connected = remote != null, features = values, loading = false)
        if (remote != null) cacheFeatures(values)
    }

    /** Last values read from LSPosed, so the screen opens without switches flipping. */
    private fun cachedFeatures(): Map<String, Boolean> = XGoogleState.featureKeys.associateWith { key ->
        uiPreferences.getBoolean("feature_$key", FeaturePolicy.defaultValue(key))
    }

    private fun cacheFeatures(values: Map<String, Boolean>) {
        val editor = uiPreferences.edit()
        values.forEach { (key, enabled) -> editor.putBoolean("feature_$key", enabled) }
        editor.apply()
    }

    private fun changeSetting(key: String, enabled: Boolean) {
        if (state.busy || preferences == null || key in savingKeys) return
        if (key == FeaturePolicy.HEY_GOOGLE && enabled) checkCompanion(true)
        else saveSetting(key, enabled)
        // On HyperOS the navigation handle and live translate reach Google through the companion.
        if (enabled && key in COMPANION_FEATURES && state.companion == CompanionState.MISSING && SystemCompatibility.isHyperOs())
            showDialog(DialogKind.COMPANION_OFFER)
        if (enabled) SettingsConnection.requestScopes(this, key) { result ->
            if (isDestroyed) return@requestScopes
            refreshScopes()
            when (result) {
                SettingsConnection.ScopeResult.DENIED -> error(texts.scopeDenied)
                SettingsConnection.ScopeResult.SYSTEM_MISSING -> error(texts.scopeSystemMissing)
                else -> Unit
            }
        }
    }

    private fun saveSetting(key: String, enabled: Boolean, companionInstalled: Boolean? = null) {
        val settings = preferences ?: return
        if (!savingKeys.add(key)) return
        val t = texts
        val hyperOs = SystemCompatibility.isHyperOs()
        // HyperOS already has a default-wallet action for double-pressing power, and the
        // companion owns the nav-handle key: change the system first, save only after it worked.
        val system: ((Boolean) -> Unit)? = when {
            !hyperOs -> null
            key == FeaturePolicy.WALLET -> { on -> CompanionInstaller.applyHyperOsWalletPower(on) }
            key == FeaturePolicy.NAVIGATION && state.companion.installed -> { on -> CompanionInstaller.applyHyperOsNavigationKey(on) }
            else -> null
        }
        worker.execute {
            var paymentChanged = false
            val result = runCatching {
                // The double press opens the default wallet: make sure that is Google before saving.
                if (key == FeaturePolicy.WALLET && enabled) paymentChanged = WalletPayment(applicationContext).ensureGoogle()
                system?.let { apply ->
                    runCatching { apply(enabled) }.onFailure {
                        throw if (it is RootShell.Failure) it
                        else IOException(if (key == FeaturePolicy.WALLET) t.errWalletPower else t.errSaveGeneric)
                    }
                }
                val editor = settings.edit().putBoolean(key, enabled)
                companionInstalled?.let { editor.putBoolean("companion_ready", it) }
                if (!editor.commit()) {
                    system?.let { apply -> runCatching { apply(!enabled) } }
                    throw IOException(t.errSave)
                }
            }
            runOnUiThread {
                if (isDestroyed) return@runOnUiThread
                savingKeys.remove(key)
                result.onSuccess {
                    state = state.copy(features = state.features + (key to enabled))
                    cacheFeatures(state.features)
                    refreshScopes()
                    // Most hooks are put in place as their app starts (Wallet's too); the vibration
                    // switch is read live, and Hey Google has its own install dialog.
                    if (key !in liveKeys) android.widget.Toast.makeText(this, t.restartHint, android.widget.Toast.LENGTH_LONG).show()
                    if (paymentChanged) android.widget.Toast.makeText(this, t.walletDefaultSet, android.widget.Toast.LENGTH_LONG).show()
                }.onFailure { error(failureText(it, t.errSaveGeneric)) }
            }
        }
    }

    private fun checkCompanion(enableAfterCheck: Boolean) {
        if (state.busy) return
        val lastCheck = companionCheckedAt
        if (!enableAfterCheck && (companionCheckedThisResume ||
            (lastCheck != null && SystemClock.elapsedRealtime() - lastCheck < 30_000L))) return
        companionCheckedThisResume = true
        state = state.copy(busy = true, companion = CompanionState.CHECKING)
        worker.execute {
            val result = runCatching { CompanionState.valueOf(CompanionInstaller.check(this)) }
            runOnUiThread {
                if (isDestroyed) return@runOnUiThread
                state = state.copy(busy = false)
                result.onSuccess { installed ->
                    companionCheckedAt = SystemClock.elapsedRealtime()
                    state = state.copy(companion = installed)
                    if (enableAfterCheck) when (installed) {
                        CompanionState.READY, CompanionState.PENDING, CompanionState.OUTDATED -> saveSetting(FeaturePolicy.HEY_GOOGLE, true, true)
                        CompanionState.DISABLED -> showDialog(DialogKind.DISABLED)
                        else -> showDialog(DialogKind.INSTALL)
                    }
                    else if (!installed.installed && state.features[FeaturePolicy.HEY_GOOGLE] == true)
                        saveSetting(FeaturePolicy.HEY_GOOGLE, false, false)
                    if (enableAfterCheck || state.dialog != DialogKind.NONE) return@onSuccess
                    // Offer each once: an update per app run, the install once per phone.
                    if (installed == CompanionState.OUTDATED && !companionUpdateOffered) {
                        companionUpdateOffered = true
                        showDialog(DialogKind.COMPANION_UPDATE)
                    } else if (installed == CompanionState.MISSING && SystemCompatibility.isHyperOs()
                        && !uiPreferences.getBoolean("companion_offered", false)) {
                        uiPreferences.edit().putBoolean("companion_offered", true).apply()
                        showDialog(DialogKind.COMPANION_OFFER)
                    }
                }.onFailure {
                    companionCheckedAt = null
                    state = state.copy(companion = CompanionState.UNKNOWN)
                    if (enableAfterCheck) error(failureText(it, texts.errCheckCompanion))
                }
            }
        }
    }

    /** Hey Google turns itself on with the install; an update or the Circle to Search offer leaves it as it is. */
    private fun installCompanion(enableHeyGoogle: Boolean = true) {
        val settings = preferences ?: return
        companionCheckedAt = null
        state = state.copy(busy = true, companion = CompanionState.INSTALLING, dialog = DialogKind.NONE)
        val t = texts
        val navigationOff = state.features[FeaturePolicy.NAVIGATION] == false
        worker.execute {
            val result = runCatching {
                CompanionInstaller.install(this)
                // Without this marker the companion's first boot would take the nav handle.
                if (navigationOff && SystemCompatibility.isHyperOs()) CompanionInstaller.applyHyperOsNavigationKey(false)
                val editor = settings.edit().putBoolean("companion_ready", true)
                if (enableHeyGoogle) editor.putBoolean(FeaturePolicy.HEY_GOOGLE, true)
                if (!editor.commit())
                    throw IOException(t.errCompanionSaved)
            }
            runOnUiThread {
                if (isDestroyed) return@runOnUiThread
                state = state.copy(busy = false)
                syncPreferences()
                result.onSuccess {
                    state = state.copy(companion = CompanionState.PENDING)
                    showDialog(if (enableHeyGoogle) DialogKind.INSTALLED else DialogKind.COMPANION_DONE)
                }.onFailure {
                    state = state.copy(companion = CompanionState.UNKNOWN)
                    error(failureText(it, t.errCompanionInstall))
                }
            }
        }
    }

    private fun openAssistant() {
        runCatching { AssistantSelection.open(this) }.onFailure { error(texts.errOpenAssistant) }
    }

    // The alias class is under the code namespace, which differs from the application id.
    private val launcherAlias get() = ComponentName(this, "${MainActivity::class.java.`package`!!.name}.Launcher")

    private fun iconHidden() = packageManager.getComponentEnabledSetting(launcherAlias) ==
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED

    private fun changeIconVisibility(hidden: Boolean) {
        if (hidden) showDialog(DialogKind.HIDE_ICON) else setIconHidden(false)
    }

    private fun setIconHidden(hidden: Boolean) {
        runCatching {
            packageManager.setComponentEnabledSetting(launcherAlias,
                if (hidden) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
            state = state.copy(iconHidden = hidden)
        }.onFailure { error(texts.errIcon) }
    }

    private fun confirmDialog() {
        val dialog = state.dialog
        dismissDialog()
        when (dialog) {
            DialogKind.INSTALL -> installCompanion()
            DialogKind.COMPANION_UPDATE, DialogKind.COMPANION_OFFER -> installCompanion(false)
            DialogKind.HIDE_ICON -> setIconHidden(true)
            DialogKind.RESTART -> restartApps()
            DialogKind.UPDATE -> openUrl(UpdateChecker.RELEASES, texts.errRepository)
            else -> Unit
        }
    }

    /**
     * HyperOS hides LSPosed's scope request notice, so the status card lists what is missing:
     * the LSPosed scopes of every switched-on feature that are installed but not ticked.
     */
    private fun refreshScopes() {
        val scope = runCatching { SettingsConnection.currentScope() }.getOrNull()
        if (scope == null || !state.connected) { state = state.copy(missingScopes = emptyList()); return }
        val missing = state.features.filterValues { it }.keys.flatMap(FeatureScopes::required).distinct()
            .filter { it !in scope }
            .mapNotNull { name ->
                if (name == FeatureScopes.SYSTEM) name
                else runCatching { packageManager.getApplicationInfo(name, 0) }.getOrNull()
                    ?.let { packageManager.getApplicationLabel(it).toString() }
            }
        state = state.copy(missingScopes = missing)
    }

    /**
     * Opens LSPosed, asking for XGoogle's page (enable switch and scope list). LSPosed builds that
     * ignore outside links (2.2.1 on HyperOS) just open their main screen.
     */
    private fun usesCompanion() = state.features[FeaturePolicy.HEY_GOOGLE] == true ||
        (SystemCompatibility.isHyperOs() && COMPANION_FEATURES.any { state.features[it] == true })

    /** The status card: an outdated companion is updated from here, everything else is in LSPosed. */
    private fun openModuleManager() {
        if (state.missingScopes.isEmpty() && state.companion == CompanionState.OUTDATED) {
            showDialog(DialogKind.COMPANION_UPDATE)
            return
        }
        val user = android.os.Process.myUid() / 100000
        val module = Intent(Intent.ACTION_MAIN).setComponent(ComponentName("org.lsposed.manager", "org.lsposed.manager.ui.activity.MainActivity"))
            .setData(Uri.parse("module://$packageName:$user"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        runCatching { startActivity(module) }
            .recoverCatching { startActivity(packageManager.getLaunchIntentForPackage("org.lsposed.manager")!!) }
            .onFailure { error(texts.errLsposed) }
    }

    private fun checkUpdates() {
        if (state.update == io.github.leekin2.xgoogle.ui.UpdateStatus.CHECKING) return
        state = state.copy(update = io.github.leekin2.xgoogle.ui.UpdateStatus.CHECKING)
        worker.execute {
            val result = runCatching { UpdateChecker.latest() }
            runOnUiThread {
                if (isDestroyed) return@runOnUiThread
                result.onSuccess { release ->
                    if (UpdateChecker.isNewer(release.version, BuildConfig.VERSION_NAME)) {
                        state = state.copy(update = io.github.leekin2.xgoogle.ui.UpdateStatus.AVAILABLE,
                            updateVersion = release.version, dialog = DialogKind.UPDATE)
                    } else state = state.copy(update = io.github.leekin2.xgoogle.ui.UpdateStatus.LATEST)
                }.onFailure { state = state.copy(update = io.github.leekin2.xgoogle.ui.UpdateStatus.FAILED) }
            }
        }
    }

    private fun openSupport() = openUrl("https://t.me/Xgooogle", texts.errSupport)

    private fun openUrl(url: String, failure: String) {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }.onFailure { error(failure) }
    }

    /** Restarts the scope apps with root so most switches apply without a reboot. */
    private fun restartApps() {
        if (state.restarting) return
        val scope = SettingsConnection.currentScope() ?: return
        state = state.copy(restarting = true)
        val t = texts
        worker.execute {
            val result = runCatching { RootShell.run(ScopeRestart.command(scope, android.os.Process.myUid() / 100000), 30) }
            runOnUiThread {
                if (isDestroyed) return@runOnUiThread
                state = state.copy(restarting = false)
                result.onSuccess { android.widget.Toast.makeText(this, t.restartDone, android.widget.Toast.LENGTH_LONG).show() }
                    .onFailure { error(failureText(it, t.errRestart)) }
            }
        }
    }

    /** Localize known failures and keep internal diagnostics out of the translated UI. */
    private fun failureText(failure: Throwable, fallback: String): String {
        val t = texts
        return when (failure) {
            is RootShell.Failure -> when (failure.reason) {
                RootShell.Reason.AUTHORIZATION -> t.rootDenied
                RootShell.Reason.UNAVAILABLE -> t.rootMissing
                RootShell.Reason.TIMEOUT -> t.rootTimeout
                else -> fallback + "\n\n" + t.rootFailed
            }
            is WalletPayment.Failure -> when (failure.problem) {
                WalletPayment.Problem.NO_WALLET -> t.errNoWallet
                WalletPayment.Problem.NO_NFC -> t.errNoNfc
                else -> t.errWalletPayment
            }
            else -> fallback
        }
    }

    private fun showDialog(kind: DialogKind) { state = state.copy(dialog = kind) }
    private fun dismissDialog() { state = state.copy(dialog = DialogKind.NONE) }
    private fun error(message: String) { state = state.copy(dialog = DialogKind.ERROR, dialogText = message) }
    private fun openRepository() {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Leekin2/XGoogle"))) }
            .onFailure { error(texts.errRepository) }
    }
    private fun openTelegram() {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/emcleek"))) }
            .onFailure { error(texts.errTelegram) }
    }

}

/** HyperOS features that reach Google through the companion module, besides Hey Google. */
private val COMPANION_FEATURES = setOf(FeaturePolicy.NAVIGATION, FeaturePolicy.LIVE_TRANSLATE)
