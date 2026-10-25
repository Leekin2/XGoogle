# XGoogle companion

Root module for [XGoogle](https://github.com/Leekin2/XGoogle), the LSPosed module that brings Google features to the **Xiaomi HyperOS China ROM**.

Install the XGoogle app (LSPosed) as well; the app offers to install and update this module by itself.

## What it does
- Makes the installed Google app a system app, so Circle to Search can be opened from the navigation handle.
- Declares the system features Google needs for live translate in Circle to Search.
- Adds the hotword enrollment components and audio settings for "Hey Google".
- Adds HyperOS 4 international's Google sysconfig, privileged-permission and default-permission lists that XGoogle needs.
- Adds the Android Auto system stub: the Play Store version installs over it (before or after the module) and runs as a system app with the car projection role.
- On HyperOS, routes long-press power and the navigation handle to Google; uninstalling restores your previous choices.

## Requirements
- Xiaomi HyperOS China ROM, Android 15 or newer
- KernelSU, SukiSU, KernelSU Next, APatch or Magisk
- Google app installed from the Play Store before installing the module

Uninstalling the module puts the system back as it was. Source: [companion/](https://github.com/Leekin2/XGoogle/tree/main/companion) in the XGoogle repository (GPL-3.0).
