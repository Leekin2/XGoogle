#!/system/bin/sh
# Android Auto runs as a privileged system app through the stub in
# system/product/priv-app/AndroidAutoStubPrebuilt: the Play Store version installs as its update,
# whenever it is installed. This keeps the privileged-permission allow-list in step with it.
# Usage: systemize-aa.sh <module dir>
# Prints "missing" when Android Auto is not installed yet (the shipped list is kept then).
M=$1
AA=com.google.android.projection.gearhead
XML=$M/system/product/etc/permissions/privapp-permissions-xgoogle-androidauto.xml

pm path "$AA" > /dev/null 2>&1 || { echo missing; exit 0; }

# HyperOS stops boot when a privileged app holds a privileged permission that is not
# allow-listed, so list everything the installed version requests (extra entries are harmless).
# The shipped list stays in as well, so a newer version that asks for more still boots.
perms=$( { dumpsys package "$AA" | awk '
    /requested permissions:/ { f = 1; next }
    f && /^ *[A-Za-z ]+:$/   { f = 0 }
    f { p = $1; sub(/:.*/, "", p); if (p ~ /\./) print p }'
    cat "$M/androidauto-permissions.txt" 2>/dev/null; } | grep '\.' | sort -u)
[ -n "$perms" ] || exit 1

mkdir -p "${XML%/*}"
{
    echo '<?xml version="1.0" encoding="utf-8"?>'
    echo '<permissions>'
    echo "    <privapp-permissions package=\"$AA\">"
    for p in $perms; do
        echo "        <permission name=\"$p\" />"
    done
    echo '    </privapp-permissions>'
    echo '</permissions>'
} > "$XML.tmp" && mv "$XML.tmp" "$XML"
echo "ok $(echo "$perms" | wc -l)"
