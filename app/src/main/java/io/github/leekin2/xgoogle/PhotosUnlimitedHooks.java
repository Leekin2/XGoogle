package io.github.leekin2.xgoogle;

import android.os.Build;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Unlimited Google Photos backup. Backups from the 2016 Pixel never count against Google storage,
 * so inside the Photos app the phone reports that Pixel: its Build fields (as on the August 2018
 * factory image) and the Pixel feature flags Photos reads to pick the backup tier. Only the main
 * Photos process is touched. Values and approach from Rev4N1/GPhotosUnlimited (GPL-3.0),
 * written here as LSPosed hooks.
 */
final class PhotosUnlimitedHooks {
    static final String PHOTOS = "com.google.android.apps.photos";
    private static final Object[][] BUILD = {
            {"MANUFACTURER", "Google"}, {"MODEL", "Pixel"}, {"BRAND", "google"}, {"PRODUCT", "sailfish"},
            {"DEVICE", "sailfish"}, {"HARDWARE", "marlin"}, {"BOARD", "sailfish"},
            {"FINGERPRINT", "google/sailfish/sailfish:9/PPR1.180610.009/4898911:user/release-keys"},
            {"ID", "PPR1.180610.009"}, {"DISPLAY", "PPR1.180610.009"}, {"TYPE", "user"}, {"TAGS", "release-keys"}};
    private static final Object[][] VERSION = {
            {"RELEASE", "9"}, {"SDK", "28"}, {"INCREMENTAL", "4898911"}, {"SECURITY_PATCH", "2018-08-05"},
            {"DEVICE_INITIAL_SDK_INT", 25}};
    private static final Set<String> FEATURES = Set.of(
            "com.google.android.feature.PIXEL_EXPERIENCE", "com.google.android.feature.GOOGLE_BUILD",
            "com.google.android.feature.GOOGLE_EXPERIENCE", "com.google.android.apps.photos.NEXUS_PRELOAD",
            "com.google.android.apps.photos.PIXEL_PRELOAD", "com.google.android.apps.photos.PIXEL_2016_PRELOAD");
    private static boolean done;

    static void install(HookInstaller hooks, String processName) {
        // Photos' helper processes (:remote, ...) do not decide the backup tier.
        if (!PHOTOS.equals(processName)) return;
        synchronized (PhotosUnlimitedHooks.class) {
            if (done) return;
            done = true;
        }
        int fields = write(hooks, Build.class, BUILD) + write(hooks, Build.VERSION.class, VERSION);
        int features = 0;
        try {
            for (Method has : Class.forName("android.app.ApplicationPackageManager").getDeclaredMethods()) {
                if (!has.getName().equals("hasSystemFeature") || has.getParameterCount() == 0
                        || has.getParameterTypes()[0] != String.class) continue;
                hooks.install(has, chain -> FEATURES.contains(chain.getArg(0)) ? Boolean.TRUE : chain.proceed());
                features++;
            }
        } catch (Throwable error) {
            hooks.error("PHOTOS: Pixel features not reported", error);
        }
        hooks.info("PHOTOS: 2016 Pixel reported (" + fields + " Build field(s), " + features + " feature method(s))");
    }

    private static int write(HookInstaller hooks, Class<?> type, Object[][] values) {
        int count = 0;
        for (Object[] entry : values) {
            try {
                Field field = type.getDeclaredField((String) entry[0]);
                FinalFields.set(field, null, entry[1]);
                count++;
            } catch (Throwable error) {
                hooks.error("PHOTOS: " + type.getSimpleName() + "." + entry[0] + " left as is", error);
            }
        }
        return count;
    }

    private PhotosUnlimitedHooks() { }
}
