package io.github.leekin2.xgoogle;

import android.content.Context;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static io.github.leekin2.xgoogle.RootShell.run;

/** Root is used only after the user asks to check/install the companion. */
public final class CompanionInstaller {
    private static final String DIR="/data/adb/modules/xgoogle_companion";
    /** The module id before 1.3.3; an installed one is reported as out of date so it gets replaced. */
    private static final String OLD_DIR="/data/adb/modules/coogle_companion";
    /** Moves the old module's saved choices to the new names before they are used. */
    private static final String MIGRATE="for n in keys_backup nav_off double_click_power_key; do "
            +"[ -e /data/adb/coogle_$n ] && [ ! -e /data/adb/xgoogle_$n ] && mv /data/adb/coogle_$n /data/adb/xgoogle_$n; done; ";
    /**
     * A file only the module puts on the system, looked up in init's mount view: newer KernelSU
     * builds mount module files only through a mount metamodule, and without one nothing shows up.
     */
    private static final String MOUNTED="/proc/1/root/product/etc/permissions/coogle-hyperos.xml";
    /**
     * MISSING, DISABLED, PENDING, READY, OUTDATED when the installed one is older than the bundled
     * one, or NOT_MOUNTED when it is installed but its files are not on the system.
     */
    public static String check(Context context) throws Exception {
        String output = run("d="+DIR+"; u=/data/adb/modules_update/xgoogle_companion; o="+OLD_DIR+"; "
                +"if [ -f \"$u/module.prop\" ]; then "
                +"if [ -f \"$u/disable\" ] || [ -f \"$u/remove\" ]; then echo DISABLED; else echo PENDING; fi; "
                +"elif [ ! -f \"$d/module.prop\" ]; then if [ -f \"$o/module.prop\" ] && [ ! -f \"$o/remove\" ]; then echo OLD; else echo MISSING; fi; "
                +"elif [ -f \"$d/disable\" ] || [ -f \"$d/remove\" ]; then echo DISABLED; "
                +"else echo \"READY $(sed -n 's/^versionCode=//p' \"$d/module.prop\") $([ -f "+MOUNTED+" ] && echo mounted)\"; fi",20).trim();
        String[] last = output.substring(output.lastIndexOf('\n') + 1).trim().split(" ");
        String state = last[0];
        if ("OLD".equals(state)) return "OUTDATED";
        if (!java.util.Set.of("MISSING", "DISABLED", "PENDING", "READY").contains(state))
            throw new java.io.IOException("Companion module state unreadable");
        if ("READY".equals(state) && last.length > 1) {
            try { if (Integer.parseInt(last[1].trim()) < bundledVersion(context)) return "OUTDATED"; }
            catch (NumberFormatException unreadable) { /* an unreadable version is not reported as outdated */ }
            if (last.length < 3) return "NOT_MOUNTED";
        }
        return state;
    }
    /** versionCode of the companion module packed into the APK. */
    static int bundledVersion(Context context) throws java.io.IOException {
        try (java.util.zip.ZipInputStream zip = new java.util.zip.ZipInputStream(context.getAssets().open("xgoogle-companion.zip"))) {
            for (java.util.zip.ZipEntry entry; (entry = zip.getNextEntry()) != null; ) {
                if (!"module.prop".equals(entry.getName())) continue;
                for (String line : new String(zip.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8).split("\n"))
                    if (line.startsWith("versionCode=")) return Integer.parseInt(line.substring(12).trim());
            }
        }
        throw new java.io.IOException("Bundled companion version missing");
    }
    public static void install(Context context) throws Exception {
        Path zip=context.getCacheDir().toPath().resolve("xgoogle-companion.zip");
        try (InputStream source=context.getAssets().open("xgoogle-companion.zip")) { Files.copy(source,zip,java.nio.file.StandardCopyOption.REPLACE_EXISTING); }
        String quoted="'"+zip.toString().replace("'","'\\''")+"'";
        // Some KernelSU-Next / SukiSU builds keep ksud at /data/adb/ksud.
        run("if [ -x /data/adb/ksu/bin/ksud ]; then /data/adb/ksu/bin/ksud module install "+quoted+"; "
                +"elif [ -x /data/adb/ksud ]; then /data/adb/ksud module install "+quoted+"; "
                +"elif [ -x /data/adb/apd ]; then /data/adb/apd module install "+quoted+"; "
                +"else magisk --install-module "+quoted+"; fi",120);
        // Grant the Google apps their permissions now rather than at the next boot. The module
        // waits in modules_update until then; a failure here must not fail the install.
        try {
            run("for d in /data/adb/modules_update/xgoogle_companion "+DIR+"; do "
                    +"[ -f \"$d/grant-google.sh\" ] && { /system/bin/sh \"$d/grant-google.sh\"; break; }; done",180);
        } catch (Exception failure) {
            android.util.Log.w("XGoogle", "Google permissions are granted at the next boot instead", failure);
        }
    }
    /**
     * HyperOS: point the native double-press-power function at the default wallet
     * (launch_QAW_wallet), remembering the previous choice so turning it off restores it.
     */
    public static void applyHyperOsWalletPower(boolean enabled) throws Exception {
        String saved="/data/adb/xgoogle_double_click_power_key";
        if (enabled) run(MIGRATE+"cur=$(settings get system double_click_power_key); "
                +"[ \"$cur\" = launch_QAW_wallet ] || echo \"$cur\" > "+saved+" && "
                +"settings put system double_click_power_key launch_QAW_wallet",20);
        // Keep the saved choice until it is really back in place.
        else run(MIGRATE+"prev=$(cat "+saved+" 2>/dev/null); [ -n \"$prev\" ] && [ \"$prev\" != null ] || prev=none; "
                +"settings put system double_click_power_key \"$prev\" && rm -f "+saved,20);
    }
    /**
     * HyperOS: the companion routes the nav-handle long press to Google on every boot. While
     * Circle to Search is off, leave a marker so it stops, and put the user's own choice back.
     */
    public static void applyHyperOsNavigationKey(boolean enabled) throws Exception {
        String backup="/data/adb/xgoogle_keys_backup", off="/data/adb/xgoogle_nav_off";
        if (enabled) run(MIGRATE+"rm -f "+off+" && if [ -f "+backup+" ]; then "
                +"settings put system long_press_home_key launch_google_search; fi",20);
        else run(MIGRATE+"touch "+off+" && if [ -f "+backup+" ] && "
                +"[ \"$(settings get system long_press_home_key)\" = launch_google_search ]; then "
                +"home=$(sed -n 's/^home=//p' "+backup+"); "
                +"if [ -z \"$home\" ] || [ \"$home\" = null ]; then settings delete system long_press_home_key; "
                +"else settings put system long_press_home_key \"$home\"; fi; fi",20);
    }
    /**
     * HyperOS: the companion routes the power-key long press to Google on every boot. While the
     * power switch is off, leave a marker so it stops, and put the user's own choice (XiaoAi) back.
     */
    public static void applyHyperOsPowerKey(boolean enabled) throws Exception {
        String backup="/data/adb/xgoogle_keys_backup", off="/data/adb/xgoogle_power_off";
        if (enabled) run(MIGRATE+"rm -f "+off+" && if [ -f "+backup+" ]; then "
                +"settings put system long_press_power_key launch_google_search; fi",20);
        else run(MIGRATE+"touch "+off+" && if [ \"$(settings get system long_press_power_key)\" = launch_google_search ]; then "
                +"power=$(sed -n 's/^power=//p' "+backup+" 2>/dev/null); "
                +"if [ -z \"$power\" ] || [ \"$power\" = null ]; then power=launch_voice_assistant; fi; "
                +"settings put system long_press_power_key \"$power\"; fi",20);
    }
    private CompanionInstaller() {}
}
