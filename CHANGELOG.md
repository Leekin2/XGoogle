# Değişiklikler / Changelog

## Sonraki sürüm / Next release

- Google Konum Doğruluğu (isteğe bağlı): ağ konumu ve adres bilgisi Xiaomi'nin servisi yerine Google Play Hizmetleri'nden gelir; Play Hizmetleri kapatılırsa Xiaomi'nin servisi devralır.
  Google location accuracy (optional): network location and addresses come from Google Play services instead of Xiaomi's service; if Play services is turned off, Xiaomi's service takes over.
- Google Takvim senkronu: ek modül Google'ın takvim senkron bağdaştırıcısını bir kez kurar ve hiç ayarlanmamış Google hesaplarında takvim senkronunu açar.
  Google Calendar sync: the companion installs Google's calendar sync adapter once and turns calendar sync on for Google accounts that never had it set.
- Deprem uyarıları: Google'ın Android Deprem Uyarı Sistemi'nin ayar ekranı XGoogle'dan açılır (HyperOS Çin ROM'unun Ayarlar'ında bu menü yok).
  Earthquake alerts: the settings screen of Google's Android Earthquake Alerts System opens from XGoogle (HyperOS China's Settings has no menu for it).
- Ek modül dosyaları sisteme bağlanmadıysa (KernelSU'da mount metamodülü yoksa) XGoogle ve ek modülün açıklaması bunu söyler ve Hybrid Mount kurmayı önerir.
  If the companion's files are not mounted (no mount metamodule on KernelSU), XGoogle and the companion's description say so and suggest installing Hybrid Mount.
- Ek modül: Çin ROM'unda kapalı gelen yedekleme servisi etkinleştirilir (bmgr).
  Companion: the backup service is activated when a China ROM ships it off (bmgr).

## XGoogle 1.3.9

- Sıfırlamadan sonra izinler: ek modül kurulunca Google uygulamaları bütün izinlerini alır; HyperOS'un bu uygulamalar için sorduğu izinler her açılışta verilir (Hey Google artık sürekli izin istemez).
  Permissions after a reset: once the companion is installed, the Google apps get all their permissions; HyperOS's own prompts for them are allowed on every boot (Hey Google no longer keeps asking).
- Google yedeğinden geri yükle: Android Switch'in izni önce kontrol edilir, çökmek yerine uyarı gösterilir.
  Restore from Google backup: Android Switch's permission is checked first; a message is shown instead of a crash.
- Ek modül 1.3.9: grant-google.sh.
  Companion 1.3.9: grant-google.sh.

## XGoogle 1.3.8

- Android Auto ayrıcalıklı sistem uygulaması: ek modül Xiaomi global ROM'undaki Android Auto taslağını getirir, Play Store sürümü onun güncellemesi olur (fikir X Clarke).
  Android Auto as a privileged system app: the companion ships the Android Auto stub from Xiaomi's international ROM and the Play Store version becomes its update (idea X Clarke).
- Ek modül 1.3.8: HyperOS 4 global'deki Google ayrıcalıklı izin, sysconfig ve varsayılan izin listeleri.
  Companion 1.3.8: Google's privileged-permission, sysconfig and default-permission lists from HyperOS 4 international.
- Uygulama metinleri Android kaynaklarında, her hareket için ayrı açık/kapalı açıklaması (EricFL1998, PR #2).
  App texts as Android resources, separate on/off descriptions per gesture (EricFL1998, PR #2).

## XGoogle 1.3.7

- Play Integrity: XGoogle, Google Play Hizmetleri'nin bütünlük işleminde hiçbir şey yapmaz; Zaman Çizelgesi kapalıyken Google Play Hizmetleri ve GSF LSPosed kapsamından çıkarılır (#4).
  Play Integrity: XGoogle does nothing in Play services' integrity process; while Timeline is off, Google Play services and GSF leave the LSPosed scope (#4).
- Güç tuşu → Google asistanı anahtarı; kapalıyken uzun basma HyperOS'un kendi ayarında (XiaoAi) kalır (#3).
  Power button → Google assistant switch; when off, the long press keeps HyperOS's own choice (XiaoAi) (#3).
- Ek modül 1.3.7: güç tuşu anahtarı kapalıyken güç tuşunu Google'a yönlendirmez.
  Companion 1.3.7: does not route the power key to Google while the power switch is off.

## XGoogle 1.3.6

- Sınırsız Google Fotoğraflar yedeği (isteğe bağlı, GMS kısıtlamasını kaldır'ın altında): Google Fotoğraflar telefonu 2016 Pixel olarak görür, yeni yedekler depolama alanından düşmez. Rev4N1/GPhotosUnlimited'dan uyarlandı.
  Unlimited Google Photos backup (optional, under Remove GMS restrictions): Google Photos sees the 2016 Pixel, so new backups do not count against storage. Adapted from Rev4N1/GPhotosUnlimited.
- Kamera klasörü yedeğini seçebilme (isteğe bağlı): Google Fotoğraflar Kamera ve DCIM klasörlerini zorla yedeklemez, her klasör ayrı açılıp kapatılır. Fikir NexAlloy'dan.
  Choose camera folder backup (optional): Google Photos stops forcing backup of Camera and DCIM folders; each folder is switched on or off. Idea from NexAlloy.
- Google yedeğinden geri yükle (Ana sayfa → Sistem): Çin ROM'unda sıfırlamadan sonra Google'ın geri yükleme ekranını (Android Switch) açar; uygulamalar, arama geçmişi, SMS'ler ve ayarlar geri gelir.
  Restore from Google backup (Home → System): on China ROMs, opens Google's restore screen (Android Switch) after a reset; apps, call history, SMS and settings come back.
- Ek modül 1.3.6 (değişiklik yok, güncellemek isteğe bağlı).
  Companion 1.3.6 (no changes, updating is optional).

## XGoogle 1.3.5

- Güç tuşu: Google varsayılan asistan değilken uzun basınca HyperOS'un kapatma menüsü yine hemen açılır; kenarda Google ışığı yanmaz.
  Power button: when Google is not the default assistant, a long press opens the HyperOS power menu right away again; no Google light at the edge.
- Ana sayfa kısaldı: Kamerada Google Lens, Google Cüzdan ve GMS'e bağlı seçenekler ilgili satırın altında açılır.
  Shorter home screen: Camera Lens, Google Wallet and the GMS-dependent options fold under their parent rows.
- Beş dildeki yazılar gözden geçirildi; Circle to Search kutucuğu çevrildi.
  Texts reviewed in all five languages; the Circle to Search tile is translated.
- Ek modül 1.3.5 (değişiklik yok, güncellemek isteğe bağlı).
  Companion 1.3.5 (no changes, updating is optional).

## XGoogle 1.3.4

- Ek modül KernelSU modül deposuna (SukiSU, KernelSU) eklenmek üzere hazırlandı; uygulamada değişiklik yok.
  The companion module is prepared for the KernelSU module repository (SukiSU, KernelSU); no app changes.
- Ek modül 1.3.4.
  Companion 1.3.4.

## XGoogle 1.3.3

- Her şey artık XGoogle adını taşıyor. Ek modülün kimliği `xgoogle_companion` oldu: yeni ek modül kurulunca eski `coogle_companion` bir sonraki açılışta kendiliğinden kaldırılır, ayarlarınız korunur.
  Everything is named XGoogle now. The companion module's id is `xgoogle_companion`: installing it removes the old `coogle_companion` on the next boot, and your settings are kept.
- Güncellemeden sonra kontrol merkezindeki Lens ve Circle to Search kutucuklarını yeniden ekleyin; ana ekran simgesinin yeri değişebilir.
  After updating, add the Lens and Circle to Search tiles to the control center again; the home screen icon may move.
- Ek modül 1.3.3.
  Companion 1.3.3.

## XGoogle 1.3.2

- Hareketler artık tercihe bağlı: alt çubuğa basılı tutma, kenardan kaydırıp tutma ve çift dokunma için ayrı ayrı Circle to Search ya da XiaoAi seçilir (Circle to Search seçenekleri). Kapalı olan hareket XiaoAi'nin kendi işlevini açar (ekran tanıma, XiaoAi topu). Varsayılanlar 1.3.1 ile aynı.
  Gestures are now optional: choose Circle to Search or XiaoAi separately for the nav-handle hold, the side swipe-and-hold and the double tap (Circle to Search options). A gesture switched off opens XiaoAi's own function (screen recognition, XiaoAi's ball). Defaults are the same as 1.3.1.
- Canlı çeviri ekran kaydını durduruyor: bu Android sınırı (aynı anda tek ekran yakalama), README'de Sınırlamalar'a yazıldı.
  Live translate stops screen recording: this is an Android limit (one screen capture at a time), now listed under Limitations in the README.
- Google Haritalar Zaman Çizelgesi (isteğe bağlı, Sistem bölümünde): Google Play Hizmetleri ve Haritalar'a ABD operatörü bildirilir, konum geçmişi açılabilir. LSPosed'de Google Play Hizmetleri, Google Services Framework ve Haritalar işaretlenmeli.
  Google Maps Timeline (optional, in the System section): Google Play services and Maps see a US carrier so location history can be turned on. Tick Google Play services, Google Services Framework and Maps in LSPosed.
- Geçiş anahtarları ve şifre yöneticisi (isteğe bağlı): Çin ROM'unda Google geçiş anahtarları ve otomatik doldurma çalışır. HyperPasskey'den uyarlandı; ikisini birlikte açmayın. LSPosed'de sistem çerçevesi, Ayarlar, Güvenlik ve Tarayıcı işaretlenmeli.
  Passkeys and password manager (optional): Google passkeys and autofill work on the China ROM. Ported from HyperPasskey; do not use both. Tick the system framework, Settings, Security and Scanner in LSPosed.
- Quick Share'de "İndirilenleri görüntüle" artık HyperOS'un indirme yöneticisi yerine seçim ekranını açar, Dosya Yöneticisi seçilebilir. GMS kısıtlaması kaldırılmışken kendiliğinden çalışır; yeniden başlatma gerekir.
  Quick Share "View downloads" now opens a chooser instead of HyperOS's download manager, so File Manager can be picked. It works by itself with the GMS restriction removed; needs a reboot.
- Android 16 (HyperOS 3): alt çubuktan Circle to Search ContextualSearchConfig olmadan da açılır.
  Android 16 (HyperOS 3): Circle to Search opens from the navigation handle without ContextualSearchConfig too.
- Google 17.68: Lens'te Konuş ve Oluştur yeniden görünür.
  Google 17.68: Lens Talk and Create are back.
- Yeni isteğe bağlı özelliklerin uygulamaları (Haritalar, Google Play Hizmetleri, GSF, Güvenlik, Tarayıcı) LSPosed'de herkese otomatik işaretlenmez; özellik açılınca istenir, durum kartı eksikleri gösterir.
  The new optional features' apps (Maps, Google Play services, GSF, Security, Scanner) are no longer ticked for everyone in LSPosed; they are asked for when the feature is switched on, and the status card lists what is missing.
- Ek modül 1.3.2: Android 15'te de kurulabilir (önceden Android 16 gerekiyordu).
  Companion 1.3.2: installs on Android 15 too (Android 16 was required before).

## XGoogle 1.3.1

- Kenardan içeri kaydırıp tutunca XiaoAi topu yerine yeniden Circle to Search açılır. HyperOS bu hareketi yalnızca XiaoAi kuruluyken sunar: XiaoAi kurulu ve LSPosed'de işaretli olmalı, hareket XiaoAi topuna ayarlı olmalı.
  Swiping in from the side and holding opens Circle to Search again instead of XiaoAi's ball. HyperOS offers this gesture only while XiaoAi is installed: keep XiaoAi ticked in LSPosed and set the gesture to XiaoAi's ball.
- Kontrol merkezine Circle to Search kutucuğu: kontrol merkezini kapatıp alttaki uygulamada arama yapar.
  Circle to Search tile for the control center: it closes the control center and searches the app below.
- Ek modül artık /system/etc ve /system/framework'e dosya koymuyor (Circle to Search, canlı çeviri ve Hey Google dosyaları /product altında). Mountify'da görülen açılmama ve eksik canlı çeviri sorunlarını önler.
  The companion no longer puts files in /system/etc or /system/framework (the Circle to Search, live translate and Hey Google files are under /product). This avoids the bootloop and missing live translate seen with Mountify.
- Ek modül 1.3.1.
  Companion 1.3.1.

## XGoogle 1.3

- ⚠️ XiaoAi'ye artık gerek yok: kaldırılabilir. Tutulacaksa LSPosed'de işaretli kalsın (yedek olarak çalışır).
  ⚠️ XiaoAi is no longer needed: it can be uninstalled. If you keep it, leave it ticked in LSPosed (it stays as a fallback).
- Alt çubuktan Circle to Search daha hızlı açılır: dokunuştan ekrana yaklaşık 0,45 sn (XiaoAi kapalıyken önceden yaklaşık 1 sn). XiaoAi'nin başlamasını beklemiyor.
  Circle to Search opens faster from the navigation handle: about 0.45 s from touch to screen (about 1 s before when XiaoAi was not running). It no longer waits for XiaoAi to start.
- Quick Share kutucuğu kontrol merkezine eklenebilir (GMS kısıtlaması kaldırılmışken).
  The Quick Share tile can be added to the control center (with the GMS restriction removed).
- Kilit ekranında güç tuşuna çift basınca Google Cüzdan yeniden açılıyor; doğrulama yine isteniyor.
  Double-pressing power on the lock screen opens Google Wallet again; it still asks you to verify.
- Ek modül eskiyse XGoogle "Ek modül: güncelleme var" der ve tek dokunuşla günceller (durum kartı sarı olur).
  When the companion module is out of date, XGoogle says "Companion module: update available" and updates it with one tap (the status card turns yellow).
- Ek modül yalnızca Hey Google için değil: alt çubuktan Circle to Search ve canlı çeviri için de kurulması önerilir.
  The companion module is not only for Hey Google: XGoogle also offers it for Circle to Search from the navigation handle and live translate.
- Altta yüzen çubuk da buzlu cam görünümünde (her iki görünümde).
  The floating bar is frosted glass too (in both looks).
- Circle to Search kapatılınca alt çubuğa basılı tutma eski seçiminize döner ve açılışta korunur.
  Turning Circle to Search off gives the nav handle long press back to your own choice, kept after reboot.
- Düzeltmeler: açılışta asistan onarımı yalnızca Google seçiliyken çalışır; canlı çeviri desteklenmeyen Google sürümlerinde tekrar tekrar tarama yapmaz; Uygulamaları yeniden başlat hataları bildirir; Cüzdan anahtarı hata olursa ayarları geri alır.
  Fixes: the boot assistant repair runs only when Google is the assistant; live translate no longer rescans unsupported Google versions; Restart apps reports failures; the Wallet switch undoes its changes if something fails.
- Ek modül 1.3.
  Companion 1.3.

## XGoogle 1.2.6

- Altta yüzen Ana sayfa / Ayarlar çubuğu; dil, tema ve görünüm seçenekleri Ayarlar'da.
  A floating Home / Settings bar; language, theme and look options are in Settings.
- İsteğe bağlı HyperOS görünümü (Miuix): açık/koyu/sistem tema, duvar kağıdı renkleri, buzlu üst çubuk.
  Optional HyperOS look (Miuix): light/dark/system theme, wallpaper colors, frosted top bar.
- Durum kartı: yeşil çalışıyor, sarı LSPosed'de işaretlenmemiş uygulamaları listeler, kırmızı etkin değil; dokununca LSPosed açılır.
  Status card: green working, yellow lists apps unticked in LSPosed, red inactive; tap to open LSPosed.
- Bir özelliği açınca gereken uygulamalar LSPosed'dan istenir.
  Turning a feature on asks LSPosed for the apps it needs.
- Uygulamaları yeniden başlat: çoğu ayar telefonu yeniden başlatmadan uygulanır.
  Restart apps: most settings apply without rebooting.
- Ayarlar: güncelleme kontrolü ve destek grubu (t.me/Xgooogle).
  Settings: update check and support group (t.me/Xgooogle).
- Google Cüzdan açılınca Google varsayılan NFC ödeme uygulaması olur.
  Turning Google Wallet on makes Google the default NFC payment app.
- Root izni verilmediğinde ya da root bulunamadığında daha anlaşılır mesajlar.
  Clearer messages when root access is denied or missing.
- Ek modül 1.2.6: KernelSU NoMount her açılışta kontrol edilir.
  Companion 1.2.6: KernelSU NoMount is checked on every boot.

## XGoogle 1.2.5

- Güvenlik düzeltmesi: kilit ekranında Google Cüzdan, doğrulama yapılmadan "Okuyucuya tutun" ekranına ulaşabiliyordu. XGoogle artık Cüzdan'ın doğrulamasını onun yerine cevaplamıyor; Cüzdan parmak izi/şifre istiyor.
  Security fix: on the lock screen, Google Wallet could reach "Hold to reader" without verifying you. XGoogle no longer answers Wallet's verification for it; Wallet now asks for your fingerprint/PIN.
- Kilit ekranındaki Cüzdan kısayolu, güç tuşuna çift basmadaki gibi ödeme ekranını açar.
  The lock screen Wallet shortcut opens the tap-to-pay screen like double-pressing power.
- Kilit ekranında Lens: "Konuş" artık kilit ekranının üstünde açılır.
  Lens on the lock screen: Talk now opens over the lock screen too.
- Circle to Search sadece alt çubuğa basılı tutunca açılır; çift dokunma ve sürükleyip tutma XiaoAi'de kalır.
  Circle to Search only takes the long press on the navigation handle; double tap and drag-and-hold stay with XiaoAi.
- Yeniden başlatma gerektiren bir ayar değişince kısa bir hatırlatma çıkar.
  A short reminder appears when a setting needs a restart.
- Ek modül 1.2.5: ksud'u /data/adb içinde tutan KernelSU sürümlerinde de kurulur; HyperOS'ta Hey Google durumu daha anlaşılır.
  Companion 1.2.5: installs on KernelSU builds that keep ksud in /data/adb; clearer Hey Google status on HyperOS.

## XGoogle 1.2.4

- Google Lens "Konuş" ve "Oluştur": Google güncellense de hedefleri kendisi bulur; Google 17.66.11'de yeniden çalışıyor.
  Google Lens Talk and Create: finds its targets even after Google updates; works again on Google 17.66.11.
- Google Cüzdan kilit ekranı önizlemesi de Cüzdan güncellemelerinden etkilenmiyor.
  Google Wallet's lock screen preview also survives Wallet updates.
- Ek modül 1.2.4: KernelSU NoMount ile Google artık sistem uygulaması olarak kalıyor; "Google sistem uygulaması" durumu doğru okunuyor.
  Companion 1.2.4: Google stays a system app under KernelSU NoMount; the "Google system app" status is read correctly.
- Daha sade ayar ekranı: bölümlere ayrıldı, açıklamalar kısaldı.
  Simpler settings screen: grouped into sections, shorter descriptions.
- Cüzdan yeniden kurulsa da güç tuşuyla açılan ödeme ekranı kilit ekranının üstünde açılır (HyperOS'un "kilit ekranında göster" izni gerekmez).
  Even after reinstalling Wallet, the power key's tap-to-pay screen opens over the lock screen (no HyperOS "show on lock screen" permission needed).
- Yeniden kurulan uygulamalar LSPosed kapsamından düşünce XGoogle açılışta onları geri eklemek için bir kez onay ister.
  When reinstalled apps drop out of the LSPosed scope, XGoogle asks once at launch to add them back.
- Kilit ekranındaki ödeme ekranında "Cüzdan'ı açın" artık kilidi ister ve kilit açılınca Cüzdan'ı açar (önceden tepki vermiyordu).
  On the lock screen tap-to-pay screen, "Open Wallet" now asks for the lock and opens Wallet once unlocked (it did nothing before).
- HyperOS'ta LSPosed kaydında ColorOS'a ait uyarılar artık çıkmıyor.
  No more ColorOS-only warnings in the LSPosed log on HyperOS.


## XGoogle 1.2.3

- Kilit ekranı kısayolları: Gemini ve Google Home cihaz kontrolleri kilit açılmadan açılır; Cüzdan önce kilidi ister.
  Lock screen shortcuts: Gemini and Google Home's device controls open without unlocking; Wallet asks for the lock first.
- Yeni "Kamerada Google Lens" düğmesi (varsayılan: kapalı): kameraya Lens butonu ekler, kilit ekranında da şifresiz açılır (fotoğraflar kilitli kalır).
  New "Google Lens in Camera" switch (off by default): adds a Lens button to the camera, which also opens over the lock screen (photos stay locked).
- Canlı çeviri ve "hep titreşsin" artık Circle to Search'ün altında açılır bir bölümde.
  Live translate and "always vibrate" now fold under Circle to Search.

## XGoogle 1.2.2

- Android Auto: araç projeksiyonu rolü artık veriliyor; Android Auto araca bağlanmak için gereken izinleri alıyor.
  Android Auto: the car projection role is now granted, so Android Auto gets the permissions it needs to connect to the car.
- Yeni "Ayarlar'da Google" düğmesi: Ayarlar'ın altındaki Google hizmetleri girişini gösterir ya da gizler (varsayılan: açık).
  New "Google in Settings" switch: shows or hides the Google services entry at the bottom of Settings (on by default).
- Ek modül 1.2.2: HyperOS'ta kullanılmayan ses modeli kayıt uygulamalarını kapatır; açılıştaki çökmeleri giderildi.
  Companion module 1.2.2: turns off the hotword enrollment apps HyperOS doesn't use, fixing their crashes at boot.
- HyperOS'ta LSPosed kaydında ColorOS'a ait Ayarlar hataları artık çıkmıyor.
  No more ColorOS-only Settings errors in the LSPosed log on HyperOS.

## XGoogle 1.2.1

- HyperOS "tek tuşla temizle" artık Google'ın asistanını ve Hey Google algılayıcısını kapatmıyor; güç tuşu ve Hey Google temizlikten sonra da çalışır.
  HyperOS one-key clean no longer kills the Google assistant and the Hey Google detector; the power key and Hey Google keep working after cleaning.
- Uygulama açılırken ayarlar artık bir anlığına kapalı görünmüyor.
  Settings no longer flash as off while the app opens.
- Ek modül HyperOS'ta DSP ses modeli kaydetmiyor (Hey Google'ı durdurabiliyordu); yazılım algılaması kullanılır.
  The companion module no longer enrolls a DSP keyphrase model on HyperOS (it could stop Hey Google); software detection is used.
- Ek modül durum yazısı HyperOS'ta yanlış uyarı göstermiyor.
  The companion status no longer shows a false warning on HyperOS.
- README: Google'ı varsayılan asistan yapma ve açılacak seçenekler eklendi.
  README: setting Google as default assistant and which options to enable.

## XGoogle 1.2

İlk sürüm: Xiaomi HyperOS 4 Çin ROM'u için.
First release, for the Xiaomi HyperOS 4 China ROM.

- HyperOS 4 desteği; ColorOS'a özel hook'lar HyperOS'ta kendiliğinden atlanır.
  HyperOS 4 support; ColorOS-only hooks are skipped on HyperOS.
- Alt çubuk → Circle to Search (XiaoAi üzerinden yönlendirme).
  Navigation handle → Circle to Search (routed through XiaoAi).
- Güç tuşu → Gemini / Google Asistan; açılışta kopan asistan servisi kendiliğinden yeniden bağlanır.
  Power key → Gemini / Google Assistant; the assistant service reconnects automatically after boot.
- Canlı çeviri (Circle to Search, kaydırarak çeviri); Google uygulaması taraması önbelleğe alınır.
  Live translate (Circle to Search, translate as you scroll); the Google app scan is cached.
- Android Auto: sistem uygulaması ve araç projeksiyonu rolü.
  Android Auto: system app and car projection role.
- Kilit ekranı kısayolları ve kontrol merkezi Lens kutucuğu.
  Lock screen shortcuts and control center Lens tile.
- Ek modül: Xiaomi ayrıcalıklı izin listesi, Android 16+ kontrolü, kurulum hata denetimi, Magisk/APatch desteği, kaldırırken tuş ayarlarının geri yüklenmesi, otomatik güncelleme.
  Companion module: Xiaomi privileged-permission allowlist, Android 16+ check, install failure checks, Magisk/APatch support, key settings restored on uninstall, automatic updates.
- Türkçe, English, Русский, Español, 简体中文 arayüz.
  Türkçe, English, Русский, Español, 简体中文 UI.
- Yeni paket kimliği: `io.github.leekin2.xgoogle`.
  New package id: `io.github.leekin2.xgoogle`.
