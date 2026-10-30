#!/system/bin/sh
MODDIR=${0%/*}
google='⚠️Google is not a system app'
enrollment='⚠️Enrollment components not ready'
dsp='⚠️DSP not working'
# HyperOS uses Google's software detection by design; no session yet is not a fault.
[ -n "$(getprop ro.mi.os.version.name)" ] && dsp='⏳Waiting for the Hey Google session'
# The app's own flags line: permission lines can mention SYSTEM too.
package=$(dumpsys -t 10 package com.google.android.googlequicksearchbox 2>/dev/null | grep -m 1 '^    flags=\[' | grep '[[:space:]]SYSTEM[[:space:]]')
if [ -n "$package" ]; then
    google='✅Google is a system app'
fi
library=$(pm list libraries 2>/dev/null)
if pm path com.android.hotwordenrollment.xgoogle 2>/dev/null | grep -q '^package:' &&
   pm path com.android.hotwordenrollment.okgoogle 2>/dev/null | grep -q '^package:' &&
   printf '%s\n' "$library" | grep -q 'com.android.hotwordenrollment.common.util'; then
    enrollment='✅Enrollment components loaded'
fi
# BusyBox ships in a different place per root manager (KernelSU, Magisk, APatch).
for BB in /data/adb/ksu/bin/busybox /data/adb/magisk/busybox /data/adb/ap/bin/busybox; do
    [ -x "$BB" ] && break
done
state=$(dumpsys -t 10 voiceinteraction 2>/dev/null | "$BB" awk -f "$MODDIR/hotword-status.awk")
# The awk check sees a live, initialized detector session, not confirmed listening.
case "$state" in
    dsp) dsp='✅DSP session ready' ;;
    software) dsp='ℹ️Software detection session ready' ;;
    # HyperOS uses software detection, so an unknown DSP state is not news there.
    unknown) if [ -n "$(getprop ro.mi.os.version.name)" ]; then dsp='⏳Waiting for the Hey Google session'; else dsp='⏳DSP state unknown'; fi ;;
esac
# HyperOS keeps its own audio config elsewhere (and already enables HOTWORD_VOICE_UI),
# so the ColorOS audio patch is not applicable there; only report it on ColorOS.
if [ -z "$(getprop ro.mi.os.version.name)" ]; then
    audio=$(cat "$MODDIR/audio-state" 2>/dev/null)
    case "$audio" in
        unsupported|missing) dsp="$dsp; audio config not supported" ;;
        inherited-overlay) dsp="$dsp; original config is mounted, no overlay created" ;;
        io-failed) dsp="$dsp; audio config could not be written" ;;
        label-failed) dsp="$dsp; audio label could not be set" ;;
    esac
fi
description="[$google | $enrollment | $dsp]"
# Newer KernelSU builds mount module files only through a mount metamodule; without one none of the
# above can work, so say what is missing first.
if [ ! -f /product/etc/permissions/coogle-hyperos.xml ]; then
    description="[⚠️Module files not mounted: install a mount module (Hybrid Mount) and reboot | ${description#[}"
fi
sed '/^description=/d' "$MODDIR/module.prop" > "$MODDIR/module.prop.tmp" || exit 1
printf 'description=%s\n' "$description" >> "$MODDIR/module.prop.tmp"
chmod 0644 "$MODDIR/module.prop.tmp"
mv "$MODDIR/module.prop.tmp" "$MODDIR/module.prop"
printf '%s\n' "$description"
