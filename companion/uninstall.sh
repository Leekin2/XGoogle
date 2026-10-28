#!/system/bin/sh
for package in com.android.hotwordenrollment.xgoogle com.android.hotwordenrollment.okgoogle; do
    am compat reset 255038118 "$package"
done
# HyperOS: restore the key choices saved by boot-completed.sh. The root manager may run
# this before the settings service is up, so wait for boot in the background.
backup=/data/adb/xgoogle_keys_backup
if [ -f "$backup" ]; then
    (
        until [ "$(getprop sys.boot_completed)" = 1 ]; do sleep 2; done
        sleep 5
        for entry in power:long_press_power_key home:long_press_home_key; do
            value=$(sed -n "s/^${entry%%:*}=//p" "$backup")
            if [ -z "$value" ] || [ "$value" = null ]; then
                settings delete system "${entry#*:}"
            else
                settings put system "${entry#*:}" "$value"
            fi
        done
        rm -f "$backup" /data/adb/xgoogle_nav_off /data/adb/xgoogle_power_off /data/adb/xgoogle_granted
    ) > /dev/null 2>&1 &
fi
# User enrollment/model databases are intentionally not erased during uninstall.
