#!/bin/sh
# Builds companion/system/product/overlay/XGoogleFrameworkOverlay.apk (run from overlay-src).
set -e
BT=${ANDROID_HOME:-$LOCALAPPDATA/Android/Sdk}/build-tools/36.0.0
JAR=${ANDROID_HOME:-$LOCALAPPDATA/Android/Sdk}/platforms/android-37.0/android.jar
rm -rf build && mkdir -p build
"$BT/aapt2.exe" compile --dir res -o build/res.zip
"$BT/aapt2.exe" link -o build/unsigned.apk -I "$JAR" --manifest AndroidManifest.xml --min-sdk-version 29 --target-sdk-version 37 build/res.zip
"$BT/zipalign.exe" -f 4 build/unsigned.apk build/aligned.apk
"$BT/apksigner.bat" sign --ks "$HOME/.android/debug.keystore" --ks-pass pass:android --v4-signing-enabled false --out ../companion/system/product/overlay/XGoogleFrameworkOverlay.apk build/aligned.apk
rm -rf build
