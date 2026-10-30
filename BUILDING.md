# Derleme / Building

**APK:** JDK 17, Android SDK Platform 37 ve Build Tools 36.0.0. `JAVA_HOME` ve `ANDROID_HOME` (veya `local.properties`) ayarlayıp `./build.ps1` çalıştırın. Yalnızca arm64-v8a derlenir.

**Ek modül:** `./build-companion.ps1` → `app/src/main/assets/xgoogle-companion.zip`. Modül kaynakları `companion/` klasöründedir; APK'yı derlemeden önce çalıştırın.

**Takvim senkronu aracı:** `companion/calendar/sync-setup.dex`, `companion-src/SyncSetup.java` dosyasından `javac` (Android SDK `android.jar` ile) ve Build Tools'taki `d8` ile derlenir. Kaynak değişmedikçe yeniden derlemek gerekmez.

**İmza:** APK, derleyen bilgisayarın Android debug anahtarıyla imzalanır. Güncellemelerin mevcut kurulumun üstüne kurulabilmesi için aynı anahtar kullanılmalıdır.

---

**APK:** JDK 17, Android SDK Platform 37 and Build Tools 36.0.0. Set `JAVA_HOME` and `ANDROID_HOME` (or `local.properties`) and run `./build.ps1`. Only arm64-v8a is built.

**Companion module:** `./build-companion.ps1` → `app/src/main/assets/xgoogle-companion.zip`. The module sources live in `companion/`; run it before building the APK.

**Calendar sync tool:** `companion/calendar/sync-setup.dex` is built from `companion-src/SyncSetup.java` with `javac` (against the Android SDK's `android.jar`) and `d8` from Build Tools. It only needs rebuilding when the source changes.

**Signing:** the APK is signed with the building machine's Android debug key. Updates must use the same key to install over an existing copy.
