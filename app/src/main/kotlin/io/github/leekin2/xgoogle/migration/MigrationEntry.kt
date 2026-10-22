package io.github.leekin2.xgoogle.migration

import io.github.leekin2.xgoogle.FeatureConfig
import io.github.leekin2.xgoogle.FeaturePolicy
import io.github.leekin2.xgoogle.SystemCompatibility
import io.github.leekin2.xgoogle.migration.compat.ModernHookRuntime
import io.github.libxposed.api.XposedModule

/** Entry point for the Google-related features migrated from ONextBox. */
object MigrationEntry {
    @JvmStatic fun attach(module: XposedModule) = ModernHookRuntime.attach(module)
    @JvmStatic fun hookPackage(packageName: String, loader: ClassLoader) {
        if (packageName == "com.oplus.wallpapers") {
            if (FeatureConfig.enabled(FeaturePolicy.QUICK_ENTRIES)) WallpaperQuickEntryHooks.install(loader)
            return
        }
        // Wallet hooks check the switch on every press, so turning it on needs no restart.
        if (packageName == "com.android.systemui") WalletPowerHooks.hookSystemUi(loader)
        if (packageName == "com.android.systemui") wakeModuleApp()
        if (packageName == "com.android.systemui") AssistantShortcutReceiver.install()
        if (packageName == "com.android.systemui" && FeatureConfig.enabled(FeaturePolicy.GMS)) QuickShareTileHooks.install(loader)
        if (packageName == PowerKeyWalletOption.PACKAGE) {
            PowerKeyWalletOption.install(loader)
            return
        }
        if (packageName == "com.android.systemui" && FeatureConfig.enabled(FeaturePolicy.QUICK_ENTRIES)) {
            NativeQuickEntryHooks.install(loader)
            KeyguardShortcutHooks.installInSystemUi()
            HomeControlsHooks.install()
        } else if (packageName == "com.android.systemui") HomeControlsHooks.restore()
        if (packageName == KeyguardShortcutHooks.AOD_PACKAGE) {
            if (FeatureConfig.enabled(FeaturePolicy.QUICK_ENTRIES)) KeyguardShortcutHooks.install(loader)
            return
        }
        if (packageName == "com.android.settings" && SystemCompatibility.isHyperOs()) GoogleSettingsEntryHooks.install(loader)
        if (packageName == XiaoAiNavRedirectHooks.XIAOAI_PACKAGE) {
            if (FeatureConfig.enabled(FeaturePolicy.NAVIGATION)) XiaoAiNavRedirectHooks.install(loader)
            return
        }
        if (packageName == "com.google.android.apps.walletnfcrel") {
            if (FeatureConfig.enabled(FeaturePolicy.WALLET)) WalletPowerHooks.hookPackage(packageName, loader)
            return
        }
        if (packageName == "com.google.android.googlequicksearchbox") {
            OkGoogleHotwordCompatibilityHooker.hookGoogleApp(loader)
        } else {
            AssistantHooker.hook(packageName, loader)
        }
    }
    /**
     * Starts XGoogle's own process shortly after SystemUI so LSPosed can connect it and offer
     * scopes added in an update (it never adds them to existing installs by itself).
     */
    private fun wakeModuleApp() {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            runCatching {
                val context = Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null) as android.content.Context
                context.contentResolver.call(android.net.Uri.parse("content://io.github.leekin2.xgoogle.XposedService"), "wake", null, null)
            }.onFailure { HookLog.w("MigrationEntry", "XGoogle app not reachable for the scope check", it) }
        }, 30_000)
    }

    @JvmStatic fun hookSystem(loader: ClassLoader) {
        AssistantKeepAliveHooks.install(loader)
        AutomotiveRoleHooks.install(loader)
        WalletPowerHooks.hook(loader)
        GmsRegionRestrictionHooker.hook(loader)
        AssistantHooker.hook("system", loader)
        OkGoogleHotwordCompatibilityHooker.hookSystemServer(loader)
        HomeControlsHooks.hookSystem(loader)
        XiaoAiNavRedirectHooks.hookSystem(loader)
        PowerKeyAssistantHooks.hookSystem(loader)
        // Quick Share works only with the GMS restriction removed; its downloads link is fixed with it.
        if (FeatureConfig.enabled(FeaturePolicy.GMS)) QuickShareTileHooks.hookSystem(loader)
    }
}
