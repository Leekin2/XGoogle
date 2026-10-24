#!/system/bin/sh
MODDIR=${0%/*}
# The module was called coogle_companion before 1.3.3: move its saved choices to the new names.
for name in keys_backup nav_off double_click_power_key; do
    [ -e "/data/adb/coogle_$name" ] && [ ! -e "/data/adb/xgoogle_$name" ] && mv "/data/adb/coogle_$name" "/data/adb/xgoogle_$name"
done
# HyperOS: route the power key and nav handle to Google (the CN ROM resets the
# nav-handle key during boot, so reapply both every time).
if [ -n "$(getprop ro.mi.os.version.name)" ]; then
    # Keep the user's own choices once, outside the module, for uninstall.sh.
    backup=/data/adb/xgoogle_keys_backup
    if [ ! -f "$backup" ]; then
        power=$(settings get system long_press_power_key)
        home=$(settings get system long_press_home_key)
        # Already ours (e.g. an earlier install): the original is unknown, use the CN defaults.
        [ "$power" = launch_google_search ] && power=launch_voice_assistant
        [ "$home" = launch_google_search ] && home=launch_xiaoai_screen_identification
        printf 'power=%s\nhome=%s\n' "$power" "$home" > "$backup"
    fi
    # The power key belongs to XGoogle's power switch; the app leaves this marker while it is off.
    [ -f /data/adb/xgoogle_power_off ] || settings put system long_press_power_key launch_google_search
    # The nav handle belongs to XGoogle's Circle to Search switch; the app leaves this
    # marker while it is off (and has put the user's choice back).
    [ -f /data/adb/xgoogle_nav_off ] || settings put system long_press_home_key launch_google_search
    # HyperOS uses Google's software detection, so the DSP enrollment apps are unused. Root
    # managers hide module files from them, and they crash when started at boot.
    for package in com.android.hotwordenrollment.xgoogle com.android.hotwordenrollment.okgoogle; do
        pm list packages -d 2>/dev/null | grep -qx "package:$package" || pm disable-user --user 0 "$package" > /dev/null 2>&1
    done
fi
# Keep the Android Auto allow-list in step with Play Store updates for the next boot.
if [ -d "$MODDIR/system/product/priv-app/AndroidAuto" ]; then
    /system/bin/sh "$MODDIR/systemize-aa.sh" "$MODDIR" permissions-only > /dev/null 2>&1
    # The CN ROM never assigns the car projection role, even with the overlay's config.
    role=android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION
    cmd role get-role-holders --user 0 "$role" | grep -q gearhead ||
        cmd role add-role-holder --user 0 "$role" com.google.android.projection.gearhead 0
fi
sleep 30
# Binder shell commands require pipe FDs rather than module-directory FDs.
# setup.sh enrolls a DSP keyphrase model. On HyperOS the Qualcomm DSP path crashes the
# Google assistant and an enrolled model the Google app cannot use stops Hey Google
# entirely, so HyperOS stays on Google's software detection.
if [ -z "$(getprop ro.mi.os.version.name)" ]; then
    /system/bin/sh "$MODDIR/setup.sh" 2>&1 | cat > /dev/null
fi
/system/bin/sh "$MODDIR/status.sh" > /dev/null 2>&1

# HyperOS can kill the Google app during boot; the voice interaction service then stays
# bound but disconnected (power key and Hey Google stop responding). Restarting the app
# makes the system reconnect it. Only when Google is the chosen assistant.
for attempt in 1 2 3; do
    sleep 30
    case "$(settings get secure voice_interaction_service)" in
        com.google.android.googlequicksearchbox/*) ;;
        *) break ;;
    esac
    dumpsys voiceinteraction 2>/dev/null | grep -q 'mService=null' || break
    am force-stop com.google.android.googlequicksearchbox
done
