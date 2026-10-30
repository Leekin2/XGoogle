#!/system/bin/sh
# A reset leaves the Google apps without their permissions (China ROMs have no Google setup
# wizard to grant them), and HyperOS keeps asking before Google may see the installed apps,
# show on the lock screen or start from the background, even after "always allow".
# Runtime permissions are granted once per installed version, so a permission the user
# revokes later stays revoked until that app updates. HyperOS's own app ops are allowed on
# every boot, because HyperOS resets them.
STATE=/data/adb/xgoogle_granted
DIR=${0%/*}
CALENDAR=com.google.android.syncadapters.calendar
touch "$STATE"
# International ROMs ship Google's calendar sync adapter, so Google accounts sync into the system
# calendar. It is installed once: if the user uninstalls it, it stays uninstalled.
if ! grep -qx "installed $CALENDAR" "$STATE" && [ -f "$DIR/calendar/GoogleCalendarSyncAdapter.apk" ]; then
    if ! pm path "$CALENDAR" > /dev/null 2>&1; then
        pm install -S "$(stat -c %s "$DIR/calendar/GoogleCalendarSyncAdapter.apk")" \
            < "$DIR/calendar/GoogleCalendarSyncAdapter.apk" > /dev/null 2>&1
        # The sync service lists the new adapter's accounts a moment after the install.
        sleep 3
    fi
    pm path "$CALENDAR" > /dev/null 2>&1 && echo "installed $CALENDAR" >> "$STATE"
fi
for pkg in com.google.android.googlequicksearchbox com.google.android.gms com.google.android.gsf \
        com.android.vending com.google.android.apps.restore com.google.android.apps.bard \
        com.google.android.apps.photos com.google.android.apps.walletnfcrel \
        com.google.android.projection.gearhead "$CALENDAR"; do
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
# A newly installed app stays stopped until it is opened, and the sync service skips stopped apps.
# Each Google account added before the adapter was installed gets calendar sync turned on, as
# when the account is added on an international ROM; a sync the user turned off stays off.
if pm path "$CALENDAR" > /dev/null 2>&1; then
    pm unstop --user 0 "$CALENDAR" > /dev/null 2>&1
    accounts=$(dumpsys content | grep -oE '[^ ]+/com\.google u0 \[com\.android\.calendar\]' | cut -d/ -f1 | sort -u)
    [ -n "$accounts" ] && [ -f "$DIR/calendar/sync-setup.dex" ] &&
        CLASSPATH="$DIR/calendar/sync-setup.dex" app_process /system/bin SyncSetup com.android.calendar $accounts > /dev/null 2>&1
fi
# Some China ROMs ship with the backup service deactivated; Google backup and restore need it.
# This only makes the service available: whether Google backs up stays the user's choice.
bmgr activated 2>/dev/null | grep -q 'currently activated' || bmgr activate true > /dev/null 2>&1
# Android Switch records the restore in a secure setting; installed from Play it crashes without this.
pm path com.google.android.apps.restore > /dev/null 2>&1 &&
    pm grant --user 0 com.google.android.apps.restore android.permission.WRITE_SECURE_SETTINGS > /dev/null 2>&1
exit 0
