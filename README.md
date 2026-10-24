# XGoogle

**An LSPosed module bringing Google features to the Xiaomi HyperOS 4 China ROM.**

📥 **Download:** [Latest release](https://github.com/Leekin2/XGoogle/releases/latest) · 💬 **Feedback and complaints:** Telegram [@emcleek](https://t.me/emcleek) · 👥 **Support group:** [Xgoogle for HyperOS](https://t.me/Xgooogle)

[Türkçe aşağıda](#türkçe)

## Features

- ✅ **Long-press power → Gemini / Google Assistant** (only while Google is the default assistant; otherwise the HyperOS power menu opens)
- ✅ **Long-press the navigation handle → Circle to Search**
- ✅ **Swipe in from the side and hold → Circle to Search** (needs XiaoAi, see below)
- ✅ **Gestures are optional:** for the nav-handle hold, the side swipe-and-hold and the double tap you choose Circle to Search or XiaoAi (the function set in the system)
- ✅ **"Hey Google"** voice activation, also with the screen off
- ✅ **Live translate:** in Circle to Search, tap Translate and keep translating as you scroll
- ✅ **Google Lens:** Talk and Create included; keeps working after Google app updates
- ✅ **Android Auto:** system app and car projection role
- ✅ **Removes the GMS China restriction** (Quick Share included, with its control center tile; its "View downloads" link opens File Manager)
- 🧪 **Google Maps Timeline** (optional): Google Play services and Maps see a US carrier so location history can be turned on; on the mainland the map stays aligned. Spoofing the carrier may break Google's terms; use at your own risk
- 🧪 **Passkeys and password manager** (optional): Google passkeys, phone-as-key sign-in and autofill through Google Play services; Security no longer resets the chosen password manager. Needs Google Play services on; do not use together with HyperPasskey
- 🧪 **Unlimited Google Photos backup** (optional): Google Photos sees the 2016 Pixel, so newly backed-up photos and videos do not count against your storage. Do not use together with the GPhotosUnlimited or Pixelify modules
- 🧪 **Choose camera folder backup** (optional): Google Photos no longer forces backup of Camera and other DCIM folders; turn each folder's backup on or off in Photos. On a new install no folder is backed up
- 🧪 **Lock screen shortcuts:** Gemini, Lens/QR, Wallet, Home controls. They open over the lock screen; Wallet asks you to verify when it needs to
- 🧪 **Double-press power → Google Wallet**, also on the lock screen; Google becomes the default NFC payment app
- 🧪 **Google Lens button in the camera** (optional), also over the lock screen
- 🧪 Control center Lens/QR and Circle to Search tiles
- 🌐 UI languages: Türkçe, English, Русский, Español, 简体中文
- 🎨 Two looks: Material, or HyperOS style (light/dark/system theme, wallpaper colors, frosted bar)
- 🚦 Status card: green when everything works, yellow with the list of apps still unticked in LSPosed, red when inactive
- ☁️ **Restore from Google backup:** China ROMs have no Google setup wizard, so after a reset XGoogle opens Google's own restore screen (Android Switch) to bring back your apps, call history, SMS and settings
- 🔄 Restart apps without rebooting, and check for updates from the app

Every feature can be turned on or off on its own; turning one off brings back the stock behavior.

## Requirements

- Xiaomi HyperOS 4 China ROM (Android 17). Tested on Xiaomi 15 Ultra.
- Root: KernelSU / SukiSU (Magisk and APatch support added, untested)
- LSPosed (API 102)
- Google app (Play Store)

## Installation

1. Download and install the **XGoogle APK** from the [latest release](https://github.com/Leekin2/XGoogle/releases/latest).
2. Enable **XGoogle** in **LSPosed → Modules** and select all recommended scopes:
   System Framework, System UI, Settings, Google, XiaoAi (`com.miui.voiceassist`, only if you keep XiaoAi), Always-on display (`com.miui.aod`), Security (`com.miui.securitycore`), Google Wallet. Add Camera too if you want the camera Lens button.
3. Grant XGoogle **root access** in your root manager.
4. **Settings → Apps → Default apps → Digital assistant** → choose **Google**.
5. Open XGoogle and turn on **Hey Google** and **Remove GMS restriction**. Circle to Search, Lens and live translate are on by default.
   The **companion module** is needed for Hey Google, for Circle to Search from the navigation handle and for live translate: XGoogle offers to install it (with Hey Google it installs automatically).
6. Reboot, then complete **Voice Match** in the Google app.

> ℹ️ **XiaoAi is optional.** The launcher still hands the navigation handle long press to XiaoAi, but XGoogle answers it in the system and opens Circle to Search directly, so it works with XiaoAi removed too. If you reinstall XiaoAi later, tick it again in LSPosed.
>
> The **side swipe-and-hold** is different: HyperOS offers it only while XiaoAi is installed. Keep XiaoAi, tick it in LSPosed and set the side swipe-and-hold to XiaoAi's ball in the navigation gesture settings; XGoogle then opens Circle to Search instead of the ball.

## Using the features

- **Lock screen shortcuts:** turn on *Lock screen shortcuts* in XGoogle and reboot. Then pick Gemini, Google Lens, Google Wallet or Home controls for the left or right shortcut in the lock screen editor.
- **Google Wallet:** turn on *Google Wallet* in XGoogle; double-pressing power is set to Google Wallet and Google becomes the default NFC payment app. On the lock screen the tap-to-pay screen opens; *Open Wallet* asks for your fingerprint/PIN.
- **Google Lens in Camera:** turn on *Google Lens in Camera*, approve the Camera scope in LSPosed and reboot. The Lens button is in the camera menu (the arrow at the top).
- **Home controls:** a home with devices must be set up in the Google Home app; otherwise the panel shows "Set up home".
- **Lens and Circle to Search tiles:** edit the control center and add them. The Circle to Search tile closes the control center and searches the app below.
- **After a reset:** add your Google account, install XGoogle, then tap *Restore from Google backup* (Home → System). Install Android Switch from the Play Store if asked, then pick this phone's newest backup.
- **After changing a setting:** use *Restart apps* (Home → System) instead of rebooting; the system framework still needs a reboot.
- **Gestures:** open the Circle to Search options (the arrow next to its switch). Each gesture's switch on opens Circle to Search; off leaves it to XiaoAi, so screen recognition or XiaoAi's ball work as set in Navigation → Quick functions. By default the hold and the side swipe-and-hold open Circle to Search and the double tap stays with XiaoAi.
- **Settings tab:** language, look and theme, update check, support group.

## Updating

1. Install the new APK over the current one.
2. If the companion module changed, XGoogle shows "Companion module: update available" (the status card turns yellow) and your root manager offers the update; install it from either.
3. Reboot.

## Troubleshooting

- **Hey Google does not work:** check the companion module's description in your root manager; it should show "✅Google is a system app" and "✅Enrollment components loaded". Make sure Google is the default digital assistant and Voice Match is complete.
- **The status card is yellow / I reinstalled Wallet or Google:** LSPosed drops a reinstalled app from the scope, and HyperOS may hide LSPosed's *Scope request* notification. The card lists the missing apps; tap it to open LSPosed, tick them in XGoogle's scope, then use *Restart apps*.
- **No Lens button in the camera:** check that *Google Lens in Camera* is on and the Camera scope is selected in LSPosed, then reboot.
- **The navigation handle does not open Circle to Search:** Circle to Search must be on in XGoogle, the companion module installed, and the phone rebooted after updating XGoogle.
- **Navigation handle hidden:** HyperOS ignores the long press when the handle is hidden, so Circle to Search needs the handle shown. With 3-button navigation, long-press Home.
- **Side swipe-and-hold shows XiaoAi's ball:** update XGoogle and tick XiaoAi in LSPosed, then use *Restart apps*. Without XiaoAi installed HyperOS turns this gesture off.
- **Bootloop or missing features with Mountify:** Mountify can hide the ROM's own system files. Switch the metamodule to Hybrid Mount.
- **Still stuck:** message [@emcleek](https://t.me/emcleek) on Telegram, with your LSPosed log if possible.

## Specs

- **Size:** app ~4 MB, companion module ~0.45 MB
- **RAM:** no extra cost. The Google assistant process uses ~90 MB, as on any Android phone.
- **Auto-update:** the companion module shows updates in your root manager.
- **Safe uninstall:** removing the companion module restores your key settings.
- **Survives cleanup:** HyperOS one-key clean no longer kills the Google assistant.
- **KernelSU NoMount:** the companion checks the mount mode on every boot.
- **Internet:** used only when you tap *Check for updates*.

## Limitations

- Google search bar / Discover on the home screen: the HyperOS 4 launcher is native (Rust) code and cannot be hooked.
- "Hey Google" uses software detection (not DSP).
- Live translate and screen recording cannot run together: both capture the screen, and Android allows one screen capture at a time, so starting one ends the other. This is how Android works, not something XGoogle adds.

## License and credits

GPL-3.0. See [LICENSE](LICENSE), [NOTICE.txt](NOTICE.txt) and the [CHANGELOG](CHANGELOG.md).
Xiaomi HyperOS port: **Leekinn**.

Thanks — this project is built on their work:
- [@MiToverG422](https://github.com/MiToverG422) — [ONextBox](https://github.com/MiToverG422/ONextBox), origin of the assistant hooks
- [@luckyzyx](https://github.com/luckyzyx) — [LuckyTool](https://github.com/luckyzyx/LuckyTool), code reference
- [@tiann](https://github.com/tiann) — [KernelSU](https://github.com/tiann/KernelSU), UI design
- [@LuckyPray](https://github.com/LuckyPray) — [DexKit](https://github.com/LuckyPray/DexKit)
- [compose-miuix-ui](https://github.com/compose-miuix-ui/miuix) — [Miuix](https://github.com/compose-miuix-ui/miuix), the HyperOS look
- [@Rev4N1](https://github.com/Rev4N1) — [GPhotosUnlimited](https://github.com/Rev4N1/GPhotosUnlimited), unlimited Google Photos backup
- [NexAlloy](https://github.com/NexAlloy/NexAlloy) — camera folder backup control idea

This project is not affiliated with Google or Xiaomi; trademarks belong to their owners.

---

## Türkçe

**Xiaomi HyperOS 4 Çin ROM'una Google özellikleri getiren LSPosed modülü.**

📥 **İndir:** [Son sürüm](https://github.com/Leekin2/XGoogle/releases/latest) · 💬 **Geri bildirim ve şikayet:** Telegram [@emcleek](https://t.me/emcleek) · 👥 **Destek grubu:** [Xgoogle for HyperOS](https://t.me/Xgooogle)

### Özellikler

- ✅ **Güç tuşuna basılı tut → Gemini / Google Asistan** (yalnızca Google varsayılan asistanken; değilse HyperOS'un kapatma menüsü açılır)
- ✅ **Alt çubuğa basılı tut → Circle to Search**
- ✅ **Kenardan içeri kaydırıp tut → Circle to Search** (XiaoAi gerekir, aşağıya bakın)
- ✅ **Hareketler tercihe bağlı:** alt çubuğa basılı tutma, kenardan kaydırıp tutma ve çift dokunma için Circle to Search ya da XiaoAi (sistemde seçili işlev) seçilir
- ✅ **"Hey Google"** sesli uyandırma, ekran kapalıyken de
- ✅ **Canlı çeviri:** Circle to Search'te Çevir'e basıp kaydırınca çeviri devam eder
- ✅ **Google Lens:** Konuş ve Oluştur dahil; Google güncellense de çalışmaya devam eder
- ✅ **Android Auto:** sistem uygulaması ve araç projeksiyonu rolü
- ✅ **GMS Çin kısıtlamasının kaldırılması** (Quick Share ve kontrol merkezi kutucuğu dahil; "İndirilenleri görüntüle" Dosya Yöneticisi'ni açar)
- 🧪 **Google Haritalar Zaman Çizelgesi** (isteğe bağlı): Google Play Hizmetleri ve Haritalar ABD operatörü görür, konum geçmişi açılabilir; Çin anakarasında harita kaymaz. Operatörü taklit etmek Google koşullarına aykırı olabilir, risk size aittir
- 🧪 **Geçiş anahtarları ve şifre yöneticisi** (isteğe bağlı): Google geçiş anahtarları, telefonla giriş ve otomatik doldurma Google Play Hizmetleri üzerinden çalışır; Güvenlik uygulaması seçilen şifre yöneticisini artık sıfırlamaz. Google Play Hizmetleri açık olmalı; HyperPasskey ile birlikte kullanmayın
- 🧪 **Sınırsız Google Fotoğraflar yedeği** (isteğe bağlı): Google Fotoğraflar telefonu 2016 Pixel olarak görür, yeni yedeklenen fotoğraf ve videolar depolama alanınızdan düşmez. GPhotosUnlimited ya da Pixelify modülleriyle birlikte kullanmayın
- 🧪 **Kamera klasörü yedeğini seçebilme** (isteğe bağlı): Google Fotoğraflar Kamera ve diğer DCIM klasörlerini zorla yedeklemez; her klasörün yedeğini Fotoğraflar'da açıp kapatırsınız. Yeni kurulumda hiçbir klasör yedeklenmez
- 🧪 **Kilit ekranı kısayolları:** Gemini, Lens/QR, Cüzdan, Ev kontrolleri. Kilit ekranının üstünde açılır; Cüzdan gerektiğinde doğrulama ister
- 🧪 **Güç tuşuna çift bas → Google Cüzdan**, kilit ekranında da açılır; Google varsayılan NFC ödeme uygulaması olur
- 🧪 **Kamerada Google Lens butonu** (isteğe bağlı), kilit ekranında da çalışır
- 🧪 Kontrol merkezi Lens/QR ve Circle to Search kutucukları
- 🌐 Arayüz dilleri: Türkçe, English, Русский, Español, 简体中文
- 🎨 İki görünüm: Material ya da HyperOS tarzı (açık/koyu/sistem tema, duvar kağıdı renkleri, buzlu çubuk)
- 🚦 Durum kartı: her şey çalışıyorsa yeşil, LSPosed'de işaretlenmemiş uygulama varsa listesiyle sarı, etkin değilse kırmızı
- ☁️ **Google yedeğinden geri yükleme:** Çin ROM'larında Google kurulum sihirbazı olmadığı için XGoogle, sıfırlamadan sonra Google'ın kendi geri yükleme ekranını (Android Switch) açar; uygulamalar, arama geçmişi, SMS'ler ve ayarlar geri gelir
- 🔄 Telefonu yeniden başlatmadan uygulamaları yeniden başlatma ve uygulamadan güncelleme kontrolü

Her özellik uygulamadan ayrı ayrı açılıp kapatılabilir; kapatınca sistem eski haline döner.

### Gereksinimler

- Xiaomi HyperOS 4 Çin ROM (Android 17). Xiaomi 15 Ultra'da test edildi.
- Root: KernelSU / SukiSU (Magisk ve APatch desteği eklendi, test edilmedi)
- LSPosed (API 102)
- Google uygulaması (Play Store)

### Kurulum

1. [Son sürüm](https://github.com/Leekin2/XGoogle/releases/latest) sayfasından **XGoogle APK**'sını indirip kurun.
2. **LSPosed → Modüller → XGoogle**'ı etkinleştirin ve önerilen kapsamların hepsini seçin:
   Sistem Çerçevesi, Sistem Arayüzü, Ayarlar, Google, XiaoAi (`com.miui.voiceassist`, yalnızca XiaoAi'yi tutuyorsanız), Her Zaman Açık Ekran (`com.miui.aod`), Güvenlik (`com.miui.securitycore`), Google Cüzdan. Kameradaki Lens butonunu kullanacaksanız Kamera'yı da seçin.
3. Root yöneticinizde XGoogle'a **root izni** verin.
4. **Ayarlar → Uygulamalar → Varsayılan uygulamalar → Dijital asistan** → **Google** seçin.
5. XGoogle'ı açın ve **Hey Google**'ı ve **GMS kısıtlamasını kaldır**'ı açın. Circle to Search, Lens ve canlı çeviri varsayılan olarak açıktır.
   **Ek modül** Hey Google, alt çubuktan Circle to Search ve canlı çeviri için gereklidir: XGoogle kurmayı önerir (Hey Google açılınca otomatik kurulur).
6. Telefonu yeniden başlatın. Ardından Google uygulamasından **Voice Match**'i tamamlayın.

> ℹ️ **XiaoAi isteğe bağlı.** Başlatıcı alt çubuğa uzun basmayı hâlâ XiaoAi'ye gönderir, ama XGoogle bunu sistemde yakalayıp Circle to Search'ü doğrudan açar; XiaoAi kaldırılmışken de çalışır. XiaoAi'yi sonra yeniden kurarsanız LSPosed'de tekrar işaretleyin.
>
> **Kenardan kaydırıp tutma** farklıdır: HyperOS bu hareketi yalnızca XiaoAi kuruluyken sunar. XiaoAi'yi tutun, LSPosed'de işaretleyin ve gezinme hareketi ayarlarında kenardan kaydırıp tutmayı XiaoAi topuna ayarlayın; XGoogle topun yerine Circle to Search'ü açar.

### Özellikleri kullanma

- **Kilit ekranı kısayolları:** XGoogle'da *Kilit ekranı kısayolları*'nı açıp yeniden başlatın. Sonra kilit ekranı düzenleyicisinden sol veya sağ kısayola Gemini, Google Lens, Google Cüzdan ya da Ev kontrollerini seçin.
- **Google Cüzdan:** XGoogle'da *Google Cüzdan*'ı açın; güç tuşuna çift basma Google Cüzdan'a ayarlanır ve Google varsayılan NFC ödeme uygulaması olur. Kilit ekranında ödeme ekranı açılır, *Cüzdan'ı açın* parmak izi/şifre ister.
- **Kamerada Google Lens:** XGoogle'da *Kamerada Google Lens*'i açın, LSPosed'da Kamera kapsamını onaylayıp yeniden başlatın. Lens butonu kamera menüsünde (üstteki ok) çıkar.
- **Ev kontrolleri:** Google Home uygulamasında bir ev ve cihaz kurulu olmalıdır; yoksa panelde "Ev kurulumu yap" görünür.
- **Lens ve Circle to Search kutucukları:** Kontrol merkezini düzenleyip ekleyin. Circle to Search kutucuğu kontrol merkezini kapatıp alttaki uygulamada arama yapar.
- **Sıfırladıktan sonra:** Google hesabınızı ekleyin, XGoogle'ı kurun, ardından *Google yedeğinden geri yükle*'ye (Ana sayfa → Sistem) dokunun. İstenirse Play Store'dan Android Switch'i kurun, sonra bu telefonun en yeni yedeğini seçin.
- **Ayar değiştirdikten sonra:** telefonu yeniden başlatmak yerine *Uygulamaları yeniden başlat*'ı (Ana sayfa → Sistem) kullanın; sistem çerçevesi için yine yeniden başlatma gerekir.
- **Hareketler:** Circle to Search seçeneklerini açın (anahtarının yanındaki ok). Bir hareketin anahtarı açıksa Circle to Search açılır; kapalıysa hareket XiaoAi'ye kalır, ekran tanıma ya da XiaoAi topu Gezinme → Hızlı işlevler'deki gibi çalışır. Varsayılan: basılı tutma ve kenardan kaydırıp tutma Circle to Search, çift dokunma XiaoAi.
- **Ayarlar sekmesi:** dil, görünüm ve tema, güncelleme kontrolü, destek grubu.

### Güncelleme

1. Yeni APK'yı mevcut sürümün üstüne kurun.
2. Ek modül değiştiyse XGoogle "Ek modül: güncelleme var" der (durum kartı sarı olur) ve root yöneticiniz de güncellemeyi gösterir; ikisinden biriyle güncelleyin.
3. Telefonu yeniden başlatın.

### Sorun giderme

- **Hey Google çalışmıyor:** Root yöneticinizde ek modülün açıklamasına bakın; "✅Google is a system app" ve "✅Enrollment components loaded" yazmalı. Google'ın varsayılan dijital asistan olduğundan ve Voice Match'in tamamlandığından emin olun.
- **Durum kartı sarı / Cüzdan'ı ya da Google'ı yeniden kurdum:** LSPosed yeniden kurulan uygulamayı kapsamdan çıkarır, HyperOS da LSPosed'ın *Kapsam talebi* bildirimini gizleyebilir. Kart eksik uygulamaları listeler; karta dokunup LSPosed'ı açın, XGoogle'ın kapsamında işaretleyin, sonra *Uygulamaları yeniden başlat*'ı kullanın.
- **Kamerada Lens butonu yok:** *Kamerada Google Lens* açık mı, LSPosed'da Kamera kapsamı seçili mi kontrol edin, sonra yeniden başlatın.
- **Alt çubuk Circle to Search açmıyor:** XGoogle'da Circle to Search açık, ek modül kurulu olmalı; XGoogle'ı güncelledikten sonra telefonu yeniden başlatın.
- **Alt çubuk gizli:** HyperOS, çubuk gizliyken uzun basmayı hiç işlemez; Circle to Search için çubuk görünür olmalı. 3 tuşlu gezinmede Ana Sayfa tuşuna uzun basın.
- **Kenardan kaydırıp tutunca XiaoAi topu çıkıyor:** XGoogle'ı güncelleyin ve LSPosed'de XiaoAi'yi işaretleyin, sonra *Uygulamaları yeniden başlat*'ı kullanın. XiaoAi kurulu değilse HyperOS bu hareketi kapatır.
- **Mountify ile açılmama ya da eksik özellik:** Mountify ROM'un kendi sistem dosyalarını gizleyebilir. Metamodülü Hybrid Mount'a çevirin.
- **Hâlâ çözülmediyse:** Telegram'dan [@emcleek](https://t.me/emcleek)'e yazın; mümkünse LSPosed kaydını ekleyin.

### Nitelikler

- **Boyut:** Uygulama ~4 MB, ek modül ~0,45 MB
- **RAM:** Ek yük yok. Google asistan işlemi ~90 MB kullanır, bu her Android telefonda zaten böyledir.
- **Otomatik güncelleme:** Ek modül, root yöneticinizde yeni sürüm çıkınca güncelleme bildirir.
- **Güvenli kaldırma:** Ek modül kaldırılınca değiştirdiği tuş ayarları eski haline döner.
- **Temizlikten etkilenmez:** HyperOS "tek tuşla temizle" Google asistanını kapatmaz.
- **KernelSU NoMount:** Ek modül bağlama modunu her açılışta kontrol eder.
- **İnternet:** Sadece *Güncellemeleri kontrol et*'e dokununca kullanılır.

### Sınırlamalar

- Ana ekranda Google arama çubuğu / Discover: HyperOS 4 başlatıcısı yerel (Rust) koddur, hook edilemez.
- "Hey Google" yazılım algılamasıyla çalışır (DSP değil).
- Canlı çeviri ve ekran kaydı birlikte çalışmaz: ikisi de ekranı yakalar, Android aynı anda yalnızca bir ekran yakalamaya izin verir; biri başlayınca diğeri biter. Bu XGoogle'dan değil, Android'in kendisinden gelir.

### Lisans ve teşekkür

GPL-3.0. Ayrıntılar için [LICENSE](LICENSE), [NOTICE.txt](NOTICE.txt) ve [CHANGELOG](CHANGELOG.md).
Xiaomi HyperOS uyarlaması: **Leekinn**.

Teşekkürler — bu proje onların emeği üzerine kurulu:
- [@MiToverG422](https://github.com/MiToverG422) — [ONextBox](https://github.com/MiToverG422/ONextBox), asistan hook'larının kaynağı
- [@luckyzyx](https://github.com/luckyzyx) — [LuckyTool](https://github.com/luckyzyx/LuckyTool), kod referansı
- [@tiann](https://github.com/tiann) — [KernelSU](https://github.com/tiann/KernelSU), arayüz tasarımı
- [@LuckyPray](https://github.com/LuckyPray) — [DexKit](https://github.com/LuckyPray/DexKit)
- [compose-miuix-ui](https://github.com/compose-miuix-ui/miuix) — [Miuix](https://github.com/compose-miuix-ui/miuix), HyperOS görünümü
- [@Rev4N1](https://github.com/Rev4N1) — [GPhotosUnlimited](https://github.com/Rev4N1/GPhotosUnlimited), sınırsız Google Fotoğraflar yedeği
- [NexAlloy](https://github.com/NexAlloy/NexAlloy) — kamera klasörü yedeği seçimi fikri

Bu proje Google veya Xiaomi ile bağlantılı değildir; markalar sahiplerine aittir.
