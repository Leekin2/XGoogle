package io.github.leekin2.xgoogle;

/** Defaults retain verified features; newly added integrations are opt-in. */
public final class FeaturePolicy {
    public static final String NAVIGATION="navigation", GMS="gms",
            GOOGLE_ENTRY="google_entry", SEARCH_BAR="search_bar", HEY_GOOGLE="hey_google", LENS="lens",
            WALLET="wallet", QUICK_ENTRIES="quick_entries", LIVE_TRANSLATE="live_translate",
            CIRCLE_VIBRATE="circle_vibrate", CAMERA_LENS="camera_lens",
            // Per gesture: on opens Circle to Search, off leaves the gesture to XiaoAi.
            CIRCLE_LONG_PRESS="circle_long_press", CIRCLE_SIDE_HOLD="circle_side_hold", CIRCLE_DOUBLE_TAP="circle_double_tap",
            TIMELINE="timeline", PASSKEY="passkey", PHOTOS_UNLIMITED="photos_unlimited", PHOTOS_DCIM="photos_dcim";
    public static boolean defaultValue(String key) {
        return NAVIGATION.equals(key) || CIRCLE_LONG_PRESS.equals(key) || CIRCLE_SIDE_HOLD.equals(key) || LENS.equals(key) || LIVE_TRANSLATE.equals(key) || GOOGLE_ENTRY.equals(key);
    }
    public static boolean effective(String key, boolean requested, boolean companionReady) {
        return requested && (!HEY_GOOGLE.equals(key) || companionReady);
    }
    private FeaturePolicy() {}
}
