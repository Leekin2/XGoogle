#!/system/bin/sh
# A reset leaves the Google apps without their permissions (China ROMs have no Google setup
# wizard to grant them), and HyperOS keeps asking before Google may see the installed apps,
# show on the lock screen or start from the background, even after "always allow".
# Runtime permissions are granted once per installed version, so a permission the user
# revokes later stays revoked until that app updates. HyperOS's own app ops are allowed on
# every boot, because HyperOS resets them.
STATE=/data/adb/xgoogle_granted
touch "$STATE"
for pkg in com.google.android.googlequicksearchbox com.google.android.gms com.google.android.gsf \
        com.android.vending com.google.android.apps.restore com.google.android.apps.bard \
        com.google.android.apps.photos com.google.android.apps.walletnfcrel \
        com.google.android.projection.gearhead; do
    pm path "$pkg" > /dev/null 2>&1 || continue
    info=$(dumpsys package "$pkg")
    version=$(echo "$info" | grep -m1 -o 'versionCode=[0-9]*')
    if ! grep -qx "$pkg $version" "$STATE"; then
        # Fixed, hard-restricted and install-time permissions refuse the grant; that is fine.
        for perm in $(echo "$info" | grep -o '[a-zA-Z0-9_.]*: granted=false' | cut -d: -f1 | sort -u); do
            pm grant --user 0 "$pkg" "$perm" > /dev/null 2>&1
        done
        grep -v "^$pkg " "$STATE" > "$STATE.tmp"
        echo "$pkg $version" >> "$STATE.tmp"
        mv "$STATE.tmp" "$STATE"
    fi
    for op in $(appops get "$pkg" 2>/dev/null | grep -oE 'MIUIOP\([0-9]+\): (ask|ignore|deny)' | grep -oE '[0-9]+'); do
        appops set "$pkg" "$op" allow > /dev/null 2>&1
    done
done
# Android Switch records the restore in a secure setting; installed from Play it crashes without this.
pm path com.google.android.apps.restore > /dev/null 2>&1 &&
    pm grant --user 0 com.google.android.apps.restore android.permission.WRITE_SECURE_SETTINGS > /dev/null 2>&1
exit 0
