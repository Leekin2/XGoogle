package io.github.leekin2.xgoogle;

import io.github.leekin2.xgoogle.migration.MigrationEntry;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;

public final class XGoogleModule extends XposedModule {
    private final HookInstaller hooks = new HookInstaller(this);
    private final NavigationHooks navigation = new NavigationHooks(hooks);
    private String processName;

    @Override public void onModuleLoaded(XposedModuleInterface.ModuleLoadedParam param) {
        processName = param.getProcessName();
        MigrationEntry.attach(this);
        try { FeatureConfig.attach(getRemotePreferences(FeatureConfig.GROUP)); }
        catch (Throwable failure) { hooks.error("Configuration unavailable; verified defaults retained",failure); }
        hooks.info("XGoogle " + BuildConfig.VERSION_NAME + " loaded in " + param.getProcessName() + "; framework=" + getFrameworkName()
                + " API=" + getApiVersion());
    }

    @Override public void onSystemServerStarting(XposedModuleInterface.SystemServerStartingParam param) {
        if (!SystemCompatibility.isSupported()) return;
        MigrationEntry.attach(this);
        MigrationEntry.hookSystem(param.getClassLoader());
        SignalModelHooks.install(hooks, param.getClassLoader());
        if (FeatureConfig.enabled(FeaturePolicy.PASSKEY)) PasskeyHooks.hookSystem(hooks, param.getClassLoader());
        hooks.info("SYSTEM native assistant and hotword initialization complete");
    }

    @Override public void onPackageReady(XposedModuleInterface.PackageReadyParam param) {
        String packageName = param.getPackageName();
        if (!SystemCompatibility.isSupported()) return;
        if ("com.android.launcher".equals(packageName)) SearchBarHooks.install(hooks, param.getClassLoader());
        // Wallet renames its code with every update; the preview unlock is found by its shape.
        if ("com.google.android.apps.walletnfcrel".equals(packageName))
            WalletPreviewHooks.install(hooks, param.getClassLoader(), param.getApplicationInfo().sourceDir,
                    param.getApplicationInfo().dataDir);
        if (TimelineHooks.handles(packageName) && FeatureConfig.enabled(FeaturePolicy.TIMELINE))
            TimelineHooks.install(hooks, param.getClassLoader(), packageName);
        if (PasskeyHooks.handles(packageName) && FeatureConfig.enabled(FeaturePolicy.PASSKEY))
            PasskeyHooks.install(hooks, param.getClassLoader(), packageName, param.getApplicationInfo().sourceDir);
        if (PhotosUnlimitedHooks.PHOTOS.equals(packageName) && FeatureConfig.enabled(FeaturePolicy.PHOTOS_UNLIMITED))
            PhotosUnlimitedHooks.install(hooks, processName);
        if (PhotosUnlimitedHooks.PHOTOS.equals(packageName) && FeatureConfig.enabled(FeaturePolicy.PHOTOS_DCIM))
            PhotosFolderHooks.install(hooks, param.getClassLoader(), processName, param.getApplicationInfo().sourceDir,
                    param.getApplicationInfo().dataDir);
        if ("com.android.camera".equals(packageName) && FeatureConfig.enabled(FeaturePolicy.CAMERA_LENS)) CameraLensHooks.install(hooks, param.getClassLoader());
        // The Settings entry hooks target ColorOS classes; HyperOS has none of them.
        boolean colorOs = SystemCompatibility.isColorOs17();
        if (colorOs && "com.android.settings".equals(packageName)) SettingsHooks.installGoogleSettingsEntry(hooks, param.getClassLoader());
        if ("com.android.systemui".equals(packageName) || "com.android.settings".equals(packageName)
                || "com.android.launcher".equals(packageName)
                || "com.oplus.wallpapers".equals(packageName)
                || "com.google.android.apps.walletnfcrel".equals(packageName)
                || "com.miui.voiceassist".equals(packageName)
                || "com.miui.aod".equals(packageName)
                || "com.miui.securitycore".equals(packageName)
                || "com.google.android.googlequicksearchbox".equals(packageName)) {
            MigrationEntry.attach(this);
            MigrationEntry.hookPackage(packageName, param.getClassLoader());
            if ("com.google.android.googlequicksearchbox".equals(packageName) && FeatureConfig.enabled(FeaturePolicy.LENS)) LensHooks.install(hooks, param.getClassLoader(),
                    param.getApplicationInfo().sourceDir, param.getApplicationInfo().dataDir);
            if ("com.google.android.googlequicksearchbox".equals(packageName) && FeatureConfig.enabled(FeaturePolicy.LIVE_TRANSLATE))
                LiveTranslateHooks.install(hooks, param.getClassLoader(), param.getApplicationInfo().sourceDir,
                        param.getApplicationInfo().dataDir);
            if (colorOs && "com.android.settings".equals(packageName) && FeatureConfig.enabled(FeaturePolicy.NAVIGATION)) SettingsHooks.installSettingsHiddenSupport(hooks, param.getClassLoader());
        }
        if ("com.android.launcher".equals(packageName)) navigation.install(param.getClassLoader());
    }

}
