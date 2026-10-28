#!/system/bin/sh
PACKAGE=com.google.android.googlequicksearchbox
APP_DIR=$MODPATH/system/priv-app/GoogleQuickSearchBox

# The bundled enrollment APKs target API 36 but declare no minSdk; Android 15 is the floor (contextual search).
if [ "$API" -lt 35 ]; then
    abort "! Android 15 or newer is required (this device: API $API)"
fi

# The module was called coogle_companion before 1.3.3: move its saved choices to the new names.
for name in keys_backup nav_off double_click_power_key; do
    [ -e "/data/adb/coogle_$name" ] && [ ! -e "/data/adb/xgoogle_$name" ] && mv "/data/adb/coogle_$name" "/data/adb/xgoogle_$name"
done
# Retire the old module: without its uninstall script it leaves the saved key choices in place,
# and the root manager removes it on the next boot.
for old in /data/adb/modules/coogle_companion /data/adb/modules_update/coogle_companion; do
    [ -d "$old" ] || continue
    rm -f "$old/uninstall.sh"
    touch "$old/remove"
    ui_print "- Old coogle_companion module will be removed on reboot"
done

mkdir -p "$APP_DIR"
find "$APP_DIR" -maxdepth 1 -type f -name '*.apk' -delete 2>/dev/null

paths=$(pm path "$PACKAGE" 2>/dev/null | sed -n 's/^package://p')
[ -n "$paths" ] || abort "! Google app not found; install it from the Play Store first"

# Loop in this shell (not a pipe subshell) so a failed copy can abort the install.
IFS='
'
for apk in $paths; do
    [ -f "$apk" ] || abort "! Could not read $apk"
    cp -f "$apk" "$APP_DIR/${apk##*/}" || abort "! Could not copy $apk"
done
unset IFS
[ -f "$APP_DIR/base.apk" ] || abort "! Could not copy the Google app"

# NoMount merges instead of replacing; the same check runs again on every boot (post-fs-data.sh).
. "$MODPATH/mount-google.sh"
prepare_google_mount "$MODPATH" || abort "! Could not prepare the Google folder"
if [ -f "$APP_DIR/.replace" ]; then ui_print "- Google folder in replace mode"; else ui_print "- NoMount detected, using merge mode"; fi

# Android Auto: a system stub; the Play Store version installs over it at any time.
case "$(/system/bin/sh "$MODPATH/systemize-aa.sh" "$MODPATH")" in
    missing) ui_print "- Android Auto stub added (install Android Auto from the Play Store any time)" ;;
    ok*) ui_print "- Android Auto runs as a system app" ;;
    *) ui_print "- Android Auto stub added; its permission list is refreshed at boot" ;;
esac

set_perm_recursive $MODPATH 0 0 0755 0644

set_perm_recursive "$MODPATH/system" 0 0 0755 0644
for script in boot-completed.sh service.sh setup.sh uninstall.sh prepare-audio.sh status.sh systemize-aa.sh grant-google.sh; do
    set_perm "$MODPATH/$script" 0 0 0755
done
/system/bin/sh "$MODPATH/prepare-audio.sh"
ui_print "Google system app, enrollment components and DSP support installed. Reboot."
