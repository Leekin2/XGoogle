package io.github.leekin2.xgoogle;

import java.util.List;

/**
 * The LSPosed scopes each switch works in on HyperOS. The default features' apps are recommended in
 * scope.list; the optional ones (Timeline, passkeys, Photos) are asked for when switched on, so
 * LSPosed does not tick Maps or Security for everyone.
 */
final class FeatureScopes {
    static final String SYSTEM = "system";
    private static final String GOOGLE = "com.google.android.googlequicksearchbox";
    private static final String SYSTEM_UI = "com.android.systemui";
    static List<String> required(String feature) {
        return switch (feature) {
            case FeaturePolicy.HEY_GOOGLE -> List.of(SYSTEM, GOOGLE);
            case FeaturePolicy.NAVIGATION -> List.of(SYSTEM, SYSTEM_UI, "com.android.launcher", "com.miui.voiceassist", GOOGLE);
            // The tick comes from the system now; XiaoAi only sends it when it is the one answering.
            case FeaturePolicy.CIRCLE_VIBRATE -> List.of(SYSTEM);
            // The gesture choices only refine Circle to Search, which already asks for its scopes.
            case FeaturePolicy.CIRCLE_LONG_PRESS, FeaturePolicy.CIRCLE_SIDE_HOLD, FeaturePolicy.CIRCLE_DOUBLE_TAP -> List.of();
            case FeaturePolicy.TIMELINE -> List.of(TimelineHooks.GMS, TimelineHooks.GSF, TimelineHooks.MAPS);
            case FeaturePolicy.PASSKEY -> List.of(SYSTEM, PasskeyHooks.SETTINGS, PasskeyHooks.SECURITY, PasskeyHooks.SCANNER);
            case FeaturePolicy.PHOTOS_UNLIMITED, FeaturePolicy.PHOTOS_DCIM -> List.of(PhotosUnlimitedHooks.PHOTOS);
            case FeaturePolicy.GOOGLE_ENTRY -> List.of("com.android.settings");
            case FeaturePolicy.GMS -> List.of(SYSTEM, SYSTEM_UI);
            case FeaturePolicy.LENS, FeaturePolicy.LIVE_TRANSLATE -> List.of(GOOGLE);
            case FeaturePolicy.CAMERA_LENS -> List.of(GOOGLE, "com.android.camera");
            case FeaturePolicy.WALLET -> List.of(SYSTEM, SYSTEM_UI, "com.miui.securitycore", "com.google.android.apps.walletnfcrel");
            case FeaturePolicy.QUICK_ENTRIES -> List.of(SYSTEM, SYSTEM_UI, "com.miui.aod");
            default -> List.of();
        };
    }
    private FeatureScopes() { }
}
