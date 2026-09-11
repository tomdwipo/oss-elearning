# Dependency Version Audit & Assessment

> **Audit Target:** [`gradle/libs.versions.toml`](file:///Users/tommy-amarbank/Documents/oss-elearning/gradle/libs.versions.toml)  
> **Audited Repository:** `oss-elearning`  
> **Environment Context:** Gradle `8.11.1`, JVM `21.0.11`, Android `compileSdk 35` / `targetSdk 35` / `minSdk 24`  
> **Assessment Date:** 2026-09-11  

---

## 1. Executive Summary

Audit ini dilakukan terhadap seluruh dependency yang terdaftar pada file [`gradle/libs.versions.toml`](file:///Users/tommy-amarbank/Documents/oss-elearning/gradle/libs.versions.toml). Berdasarkan metadata resmi dari Google Maven Repository (`dl.google.com`) dan Maven Central Mirror, seluruh dependency (10/10) saat ini berada beberapa versi di belakang versi rilis stabil (*stable*) maupun pra-rilis (*pre-release*).

Penamaan berkas laporan ini ditetapkan sebagai:
**`.docs/assesment/01_dependency_versions_assessment.md`**  
*(Melanjutkan konvensi penomoran dokumen di `.docs/prd/`, `.docs/trd/`, `.docs/task/`, yang diawali prefix `01_`)*.

Seluruh data versi dan pembaruan dalam laporan ini dilengkapi dengan **tautan sitasi resmi yang dapat langsung diklik** (Release Notes GitHub, Android Developers Portal, Google Maven, dan Maven Central) untuk mempermudah verifikasi langsung.

---

## 2. Dependency Audit Matrix & Sitasi Verifikasi

Berikut adalah ringkasan perbandingan antara versi saat ini dengan versi stabil terbaru beserta tautan verifikasi langsung:

| Key Version | Coordinate / Artifact | Versi Saat Ini | Versi Stable Terbaru | Versi Terakhir (Inc. Pre-release) | Tautan Verifikasi / Sitasi |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **`agp`** | `com.android.tools.build:gradle` | `8.7.3` | **`9.4.0`** | `9.5.0-alpha05` | [Android Developers AGP Notes](https://developer.android.com/build/releases/gradle-plugin) \| [Google Maven Metadata](https://dl.google.com/android/maven2/com/android/tools/build/gradle/maven-metadata.xml) |
| **`kotlin`** | `org.jetbrains.kotlin:kotlin-gradle-plugin` | `2.1.0` | **`2.4.20`** | `2.4.20` | [GitHub Release v2.4.20](https://github.com/JetBrains/kotlin/releases/tag/v2.4.20) \| [Kotlin Releases Docs](https://kotlinlang.org/docs/releases.html) |
| **`compose-multiplatform`** | `org.jetbrains.compose:compose-gradle-plugin` | `1.7.3` | **`1.12.0`** | `1.13.0-alpha01` | [GitHub Release v1.12.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0) \| [Compose Multiplatform Repo](https://github.com/JetBrains/compose-multiplatform) |
| **`kotlinx-serialization`** | `org.jetbrains.kotlinx:kotlinx-serialization-json` | `1.7.3` | **`1.11.0`** | `1.12.0-RC` | [GitHub Release v1.11.0](https://github.com/Kotlin/kotlinx.serialization/releases/tag/v1.11.0) \| [Serialization Maven Central](https://central.sonatype.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json) |
| **`kotlinx-coroutines`** | `org.jetbrains.kotlinx:kotlinx-coroutines-core` | `1.9.0` | **`1.11.0`** | `1.11.0` | [GitHub Release 1.11.0](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.11.0) \| [Coroutines Maven Central](https://central.sonatype.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core) |
| **`multiplatform-settings`** | `com.russhwolf:multiplatform-settings` | `1.2.0` | **`1.3.0`** | `1.3.0` | [GitHub Release v1.3.0](https://github.com/russhwolf/multiplatform-settings/releases/tag/v1.3.0) \| [Settings Maven Central](https://central.sonatype.com/artifact/com.russhwolf/multiplatform-settings) |
| **`androidx-lifecycle`** | `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel` | `2.8.4` | **`2.11.0`** | `2.11.0` | [Maven Central Metadata](https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/androidx/lifecycle/lifecycle-viewmodel/maven-metadata.xml) \| [Compose Multiplatform 1.12.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0) |
| **`androidx-activity-compose`** | `androidx.activity:activity-compose` | `1.9.3` | **`1.13.0`** | `1.14.0-alpha02` | [AndroidX Activity Releases](https://developer.android.com/jetpack/androidx/releases/activity) \| [Google Maven Metadata](https://dl.google.com/android/maven2/androidx/activity/activity-compose/maven-metadata.xml) |
| **`androidx-appcompat`** | `androidx.appcompat:appcompat` | `1.7.0` | **`1.8.0`** | `1.8.0` | [AndroidX AppCompat Releases](https://developer.android.com/jetpack/androidx/releases/appcompat) \| [Google Maven Metadata](https://dl.google.com/android/maven2/androidx/appcompat/appcompat/maven-metadata.xml) |
| **`androidx-core-ktx`** | `androidx.core:core-ktx` | `1.15.0` | **`1.19.0`** | `1.19.0` | [AndroidX Core Releases](https://developer.android.com/jetpack/androidx/releases/core) \| [Google Maven Metadata](https://dl.google.com/android/maven2/androidx/core/core-ktx/maven-metadata.xml) |

---

## 3. Detail Versi yang Sudah Rilis per Dependency

Berikut adalah rincian seluruh versi yang telah dirilis semenjak versi yang digunakan saat ini:

### 3.1. Android Gradle Plugin (`agp`)
- **Coordinate:** `com.android.tools.build:gradle`
- **Versi Terpasang:** `8.7.3`
- **Versi Stable yang Sudah Tersedia (+29 versi):**
  - `8.8.x`: `8.8.0`, `8.8.1`, `8.8.2`
  - `8.9.x`: `8.9.0`, `8.9.1`, `8.9.2`, `8.9.3`
  - `8.10.x`: `8.10.0`, `8.10.1`
  - `8.11.x`: `8.11.0`, `8.11.1`, `8.11.2`
  - `8.12.x`: `8.12.0`, `8.12.1`, `8.12.2`, `8.12.3`
  - `8.13.x`: `8.13.0`, `8.13.1`, `8.13.2`
  - `9.0.x` - `9.4.x`: `9.0.0`, `9.0.1`, `9.1.0`, `9.1.1`, `9.2.0`, `9.2.1`, `9.3.0`, `9.3.1`, `9.3.2`, `9.4.0`
- **Versi Alpha/Beta/RC Terbaru:** `9.5.0-alpha05`
- **Verifikasi Metadata:** [Google Maven Index](https://dl.google.com/android/maven2/com/android/tools/build/gradle/maven-metadata.xml)

### 3.2. Kotlin (`kotlin`)
- **Coordinate:** `org.jetbrains.kotlin:kotlin-gradle-plugin`
- **Versi Terpasang:** `2.1.0`
- **Versi Stable yang Sudah Tersedia (+14 versi):**
  - `2.1.x`: `2.1.10`, `2.1.20`, `2.1.21`
  - `2.2.x`: `2.2.0`, `2.2.10`, `2.2.20`, `2.2.21`
  - `2.3.x`: `2.3.0`, `2.3.10`, `2.3.20`, `2.3.21`
  - `2.4.x`: `2.4.0`, `2.4.10`, `2.4.20`
- **Versi Terakhir:** `2.4.20` (RC: `2.4.20-RC`, `2.4.20-RC2`, `2.4.20-RC3`)
- **Verifikasi Metadata:** [Maven Central Kotlin Stdlib](https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/kotlin/kotlin-stdlib/maven-metadata.xml) \| [GitHub Tag v2.4.20](https://github.com/JetBrains/kotlin/releases/tag/v2.4.20)

### 3.3. Compose Multiplatform (`compose-multiplatform`)
- **Coordinate:** `org.jetbrains.compose:compose-gradle-plugin`
- **Versi Terpasang:** `1.7.3`
- **Versi Stable yang Sudah Tersedia (+14 versi):**
  - `1.8.x`: `1.8.0`, `1.8.1`, `1.8.2`
  - `1.9.x`: `1.9.0`, `1.9.1`, `1.9.2`, `1.9.3`
  - `1.10.x`: `1.10.0`, `1.10.1`, `1.10.2`, `1.10.3`
  - `1.11.x`: `1.11.0`, `1.11.1`
  - `1.12.x`: `1.12.0`
- **Versi Alpha/Beta/RC Terbaru:** `1.13.0-alpha01`
- **Verifikasi Metadata:** [Compose Gradle Plugin Metadata](https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/compose/org.jetbrains.compose.gradle.plugin/maven-metadata.xml) \| [GitHub Tag v1.12.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0)

### 3.4. Kotlinx Serialization (`kotlinx-serialization`)
- **Coordinate:** `org.jetbrains.kotlinx:kotlinx-serialization-json`
- **Versi Terpasang:** `1.7.3`
- **Versi Stable yang Sudah Tersedia (+5 versi):**
  - `1.8.0`, `1.8.1`, `1.9.0`, `1.10.0`, `1.11.0`
- **Versi Alpha/Beta/RC Terbaru:** `1.12.0-RC`
- **Verifikasi Metadata:** [Serialization JSON Metadata](https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/kotlinx/kotlinx-serialization-json/maven-metadata.xml) \| [GitHub Tag v1.11.0](https://github.com/Kotlin/kotlinx.serialization/releases/tag/v1.11.0)

### 3.5. Kotlinx Coroutines (`kotlinx-coroutines`)
- **Coordinate:** `org.jetbrains.kotlinx:kotlinx-coroutines-core`
- **Versi Terpasang:** `1.9.0`
- **Versi Stable yang Sudah Tersedia (+4 versi):**
  - `1.10.0`, `1.10.1`, `1.10.2`, `1.11.0`
- **Versi Terakhir:** `1.11.0`
- **Verifikasi Metadata:** [Coroutines Core Metadata](https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/kotlinx/kotlinx-coroutines-core/maven-metadata.xml) \| [GitHub Tag 1.11.0](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.11.0)

### 3.6. Multiplatform Settings (`multiplatform-settings`)
- **Coordinate:** `com.russhwolf:multiplatform-settings`
- **Versi Terpasang:** `1.2.0`
- **Versi Stable yang Sudah Tersedia (+1 versi):**
  - `1.3.0`
- **Versi Terakhir:** `1.3.0`
- **Verifikasi Metadata:** [Multiplatform Settings Metadata](https://maven-central.storage-download.googleapis.com/maven2/com/russhwolf/multiplatform-settings/maven-metadata.xml) \| [GitHub Tag v1.3.0](https://github.com/russhwolf/multiplatform-settings/releases/tag/v1.3.0)

### 3.7. JetBrains AndroidX Lifecycle (`androidx-lifecycle`)
- **Coordinate:** `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel` & `lifecycle-viewmodel-compose`
- **Versi Terpasang:** `2.8.4`
- **Versi Stable yang Sudah Tersedia (+9 versi):**
  - `2.9.x`: `2.9.0`, `2.9.1`, `2.9.2`, `2.9.3`, `2.9.4`, `2.9.5`, `2.9.6`
  - `2.10.x`: `2.10.0`
  - `2.11.x`: `2.11.0`
- **Versi Terakhir:** `2.11.0`
- **Verifikasi Metadata:** [JetBrains Lifecycle ViewModel Metadata](https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/androidx/lifecycle/lifecycle-viewmodel/maven-metadata.xml)

### 3.8. AndroidX Activity Compose (`androidx-activity-compose`)
- **Coordinate:** `androidx.activity:activity-compose`
- **Versi Terpasang:** `1.9.3`
- **Versi Stable yang Sudah Tersedia (+9 versi):**
  - `1.10.0`, `1.10.1`, `1.11.0`, `1.12.0`, `1.12.1`, `1.12.2`, `1.12.3`, `1.12.4`, `1.13.0`
- **Versi Alpha/Beta/RC Terbaru:** `1.14.0-alpha02`
- **Verifikasi Metadata:** [Google Maven Activity Compose Metadata](https://dl.google.com/android/maven2/androidx/activity/activity-compose/maven-metadata.xml)

### 3.9. AndroidX AppCompat (`androidx-appcompat`)
- **Coordinate:** `androidx.appcompat:appcompat`
- **Versi Terpasang:** `1.7.0`
- **Versi Stable yang Sudah Tersedia (+2 versi):**
  - `1.7.1`, `1.8.0`
- **Versi Terakhir:** `1.8.0`
- **Verifikasi Metadata:** [Google Maven AppCompat Metadata](https://dl.google.com/android/maven2/androidx/appcompat/appcompat/maven-metadata.xml)

### 3.10. AndroidX Core KTX (`androidx-core-ktx`)
- **Coordinate:** `androidx.core:core-ktx`
- **Versi Terpasang:** `1.15.0`
- **Versi Stable yang Sudah Tersedia (+4 versi):**
  - `1.16.0`, `1.17.0`, `1.18.0`, `1.19.0`
- **Versi Terakhir:** `1.19.0`
- **Verifikasi Metadata:** [Google Maven Core KTX Metadata](https://dl.google.com/android/maven2/androidx/core/core-ktx/maven-metadata.xml)

---

## 4. Rincian Fitur Baru & Pembaruan pada Versi Stable Terbaru

Berikut adalah intisari pembaruan signifikan, peningkatan performa, dan perubahan API yang dihadirkan pada versi stabil terbaru dari masing-masing dependensi beserta referensi rilis resminya:

### 4.1. Android Gradle Plugin (AGP `8.7.3` ➔ `9.4.0`)
*(Sitasi: [Android Studio & AGP Release Notes](https://developer.android.com/build/releases/gradle-plugin))*
- **Dukungan Penuh Android 15 & 16 (API 35/36):** Optimalisasi kompilasi, dexing, dan manifest merging untuk target SDK terbaru.
- **Transisi Baseline Java 21:** AGP seri 9.x secara resmi mengadopsi JDK 21 sebagai baseline runtime build toolchain.
- **Peningkatan Configuration Cache:** Eksekusi build bertambah cepat dengan perbaikan caching task dan parallel execution.
- **Modernisasi DSL Gradle:** Penghapusan beberapa blok DSL lama yang sudah deprecated (misal pembaruan konfigurasi `packaging` dan `sourceSets`).
- **Optimalisasi R8/D8 Shrinker:** Peningkatan algoritma dead-code elimination dan optimasi ukuran APK/AAB rilis.

### 4.2. Kotlin Compiler & Plugins (`2.1.0` ➔ `2.4.20`)
*(Sitasi: [Kotlin 2.4.20 GitHub Release](https://github.com/JetBrains/kotlin/releases/tag/v2.4.20) \| [Kotlin What's New Documentation](https://kotlinlang.org/docs/whatsnew.html))*
- **Maturitas K2 Compiler & Analysis API:** K2 compiler mencapai kestabilan penuh dengan kecepatan kompilasi kode KMP yang signifikan lebih cepat dan konsumsi memori yang lebih rendah.
- **KMP Swift Export (`swift-export`):** Peningkatan integrasi ekspor kode Kotlin langsung ke Swift tanpa perantara Objective-C header yang rumit.
- **Perbaikan Type Inference & Smart Casts:** Penyelesaian isu-isu false positive pada resolusi overload generik dan kontrak smart-casting K2.
- **Optimasi Backend Klib:** Reduksi ukuran biner klib untuk target multiplatform iOS (`iosX64`, `iosArm64`, `iosSimulatorArm64`).

### 4.3. Compose Multiplatform (`1.7.3` ➔ `1.12.0`)
*(Sitasi: [Compose Multiplatform 1.12.0 Release Notes](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0))*
- **iOS Native Feel (Cupertino Overscroll Default):** Fitur overscroll khas iOS (bouncing effect) kini aktif secara *default* pada seluruh scrollable components di iOS.
- **Aksesibilitas iOS (VoiceOver):** Perbaikan signifikan pada rekonstruksi elemen VoiceOver dan penanganan safe area/display cutout saat rotasi perangkat.
- **Gestur & Pop Gesture Interaktif:** Perbaikan interop sentuhan multitouch dan swipe back gesture agar tidak bentrok dengan navigasi native iOS.
- **Compose Hot Reload & AI Agent Interop:** Integrasi protokol MCP untuk Compose Hot Reload secara real-time pada desktop/runtime testing ([PR #5671](https://github.com/JetBrains/compose-multiplatform/pull/5671)).
- **Evolusi `BasicTextField` (TextFieldState):** Peningkatan stabilitas pengetikan, seleksi kursor, hardware keyboard navigation, dan pencegahan crash pengukuran lebar teks.

### 4.4. Kotlinx Serialization (`1.7.3` ➔ `1.11.0`)
*(Sitasi: [Kotlinx Serialization 1.11.0 Release Notes](https://github.com/Kotlin/kotlinx.serialization/releases/tag/v1.11.0))*
- **Struktur Exception Publik (`JsonException` API):** Kelas `JsonException`, `JsonDecodingException`, dan `JsonEncodingException` kini menjadi kelas publik yang mengekspos properti `shortMessage`, `path`, dan `offset` untuk mempermudah error handling dan logging API ([Issue #1930](https://github.com/Kotlin/kotlinx.serialization/issues/1930), [#1877](https://github.com/Kotlin/kotlinx.serialization/issues/1877)).
- **Fitur Privasi & Sanitasi Data:** Kemampuan untuk menyembunyikan (*mask*) payload input sensitif dari pesan error exception agar tidak bocor ke sistem log/monitoring.
- **Kompatibilitas Kompiler:** Sinkronisasi penuh dengan standar metadata Kotlin 2.2+ s.d. 2.4+.

### 4.5. Kotlinx Coroutines (`1.9.0` ➔ `1.11.0`)
*(Sitasi: [Kotlinx Coroutines 1.11.0 Release Notes](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.11.0))*
- **Pembaruan Arsitektur Web/Wasm:** Migrasi fungsi berbasis `Promise` ke target web terpadu dengan pengetikan `JsAny` yang lebih aman ([PR #4563](https://github.com/Kotlin/kotlinx.coroutines/pull/4563)).
- **Perombakan Dokumentasi & KDoc:** Penulisan ulang panduan resmi mengenai *Structured Concurrency* dan penanganan exception/error pada aliran asinkron ([PR #4433](https://github.com/Kotlin/kotlinx.coroutines/pull/4433), [#4596](https://github.com/Kotlin/kotlinx.coroutines/pull/4596)).
- **Efisiensi Dispatcher & Memory Footprint:** Reduksi alokasi objek pada operasi `flow` dan perbaikan propagasi unhandled exceptions.

### 4.6. Multiplatform Settings (`1.2.0` ➔ `1.3.0`)
*(Sitasi: [Multiplatform Settings 1.3.0 Release Notes](https://github.com/russhwolf/multiplatform-settings/releases/tag/v1.3.0))*
- **Dukungan Toolchain Modern:** Penyesuaian baseline ke Kotlin 2.1+, Gradle 8.11, dan AGP 8.7+.
- **Dukungan Target Baru:** Menambahkan dukungan target `wasmWasi` pada modul `multiplatform-settings-coroutines` dan `multiplatform-settings-serialization`.
- **Perbaikan Bug Serialization:** Memperbaiki bug pada delegasi serialization yang sebelumnya berpotensi mengembalikan nilai cached yang salah atau crash ([Issue #217](https://github.com/russhwolf/multiplatform-settings/issues/217)).

### 4.7. JetBrains AndroidX Lifecycle (`2.8.4` ➔ `2.11.0`)
*(Sitasi: [JetBrains AndroidX Lifecycle Artifacts](https://central.sonatype.com/artifact/org.jetbrains.androidx.lifecycle/lifecycle-viewmodel) \| [Compose Multiplatform Integration](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.0))*
- **Ekspansi KMP Parity:** Dukungan penuh multiplatform untuk `ViewModel`, `ViewModelProvider`, dan `SavedStateHandle` di seluruh platform (Android, iOS, Desktop, Wasm).
- **Integrasi `viewModelScope`:** Penyelarasan siklus hidup coroutine scope dengan library coroutines versi terbaru.
- **Dukungan Navigasi Compose:** Lifecycle awareness terintegrasi untuk transisi antar layar berbasis Compose Multiplatform.

### 4.8. AndroidX Activity Compose (`1.9.3` ➔ `1.13.0`)
*(Sitasi: [Android Developers Activity Release Notes](https://developer.android.com/jetpack/androidx/releases/activity))*
- **Peningkatan Predictive Back Gesture:** Dukungan penuh API `PredictiveBackHandler` untuk animasi sistem kembali (*predictive back*) pada Android 14/15.
- **Integrasi `enableEdgeToEdge()` Lebih Bersih:** Konfigurasi otomatis transparansi status bar dan navigasi bar yang lebih adaptif dengan tema gelap/terang.
- **Stabilitas ActivityResult:** Optimalisasi callback handling saat menerima hasil intent kamera/galeri di dalam layar Compose.

### 4.9. AndroidX AppCompat (`1.7.0` ➔ `1.8.0`)
*(Sitasi: [Android Developers AppCompat Release Notes](https://developer.android.com/jetpack/androidx/releases/appcompat))*
- **Penyempurnaan Edge-to-Edge:** Penyesuaian tema dan window decoration untuk komponen AppCompat klasik agar konsisten dengan standar Android 15.
- **Per-App Language Preferences:** Perbaikan bug sinkronisasi bahasa aplikasi independen dari bahasa sistem perangkat.

### 4.10. AndroidX Core KTX (`1.15.0` ➔ `1.19.0`)
*(Sitasi: [Android Developers Core Release Notes](https://developer.android.com/jetpack/androidx/releases/core))*
- **Kompatibilitas Platform Android 15 & 16:** Penyediaan wrapper dan shim resmi untuk fitur-fitur OS terbaru.
- **Ekstensi `WindowInsetsCompat` & `ViewCompat`:** Penyederhanaan pembacaan cutout, keyboard ime, dan status insets dalam layout.

---

## 5. Analisis Kompatibilitas & Risiko Upgrade

1. **Sinkronisasi Kotlin & Compose Multiplatform:**
   - Mulai Kotlin 2.0+, Compose Compiler dikelola langsung oleh plugin Kotlin (`org.jetbrains.kotlin.plugin.compose`). Namun, runtime Compose Multiplatform (`org.jetbrains.compose`) memiliki rentang kompatibilitas Kotlin tertentu. Meng-upgrade Kotlin ke `2.4.x` wajib dibarengi dengan upgrade `compose-multiplatform` yang sesuai.
2. **AGP & Gradle Compatibility:**
   - Gradle wrapper repository saat ini adalah `8.11.1`.
   - AGP `8.7.3` berjalan stabil pada Gradle `8.11.1`.
   - AGP `9.x` memerlukan Gradle versi 9+ dan Java toolchain tertentu. Jika ingin meng-upgrade AGP secara konservatif tanpa mengubah major Gradle, upgrade ke seri `8.11.x` atau `8.12.x` adalah opsi yang paling aman.
3. **KMP Library Alignment:**
   - `kotlinx-coroutines` dan `kotlinx-serialization` perlu diselaraskan dengan versi Kotlin compiler agar metadata Klib tetap kompatibel untuk target Apple (`iosX64`, `iosArm64`, `iosSimulatorArm64`) dan Android.

---

## 6. Rekomendasi Roadmap Upgrade (Bertahap)

| Tahapan | Lingkup Upgrade | Target Versi | Risiko | Aksi Pengujian |
| :--- | :--- | :--- | :---: | :--- |
| **Phase 1: Low-Risk / Standalone** | `multiplatform-settings`, `androidx-appcompat`, `androidx-core-ktx` | `1.3.0`, `1.8.0`, `1.16.0` | Rendah | `./gradlew test` & verify compilation |
| **Phase 2: Coroutines & Serialization** | `kotlinx-coroutines`, `kotlinx-serialization` | `1.10.x` / `1.11.0`, `1.8.x` / `1.10.0` | Rendah-Sedang | Test serialization & asynchronous flows |
| **Phase 3: Core KMP Framework** | `kotlin`, `compose-multiplatform`, `androidx-lifecycle`, `agp` | Align versi stabil Kotlin & Compose | Sedang-Tinggi | Full build Android APK & iOS Framework, regression testing UI |

---

## 7. Referensi Sumber & Endpoint Verifikasi

Untuk keperluan audit berulang, seluruh data versi dapat diverifikasi langsung melalui endpoint XML / JSON berikut:

- **Google Maven Repository Index:**
  - AGP: `https://dl.google.com/android/maven2/com/android/tools/build/gradle/maven-metadata.xml`
  - Activity Compose: `https://dl.google.com/android/maven2/androidx/activity/activity-compose/maven-metadata.xml`
  - AppCompat: `https://dl.google.com/android/maven2/androidx/appcompat/appcompat/maven-metadata.xml`
  - Core KTX: `https://dl.google.com/android/maven2/androidx/core/core-ktx/maven-metadata.xml`
- **Maven Central Repository Mirror:**
  - Kotlin Stdlib: `https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/kotlin/kotlin-stdlib/maven-metadata.xml`
  - Compose Multiplatform: `https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/compose/org.jetbrains.compose.gradle.plugin/maven-metadata.xml`
  - Coroutines: `https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/kotlinx/kotlinx-coroutines-core/maven-metadata.xml`
  - Serialization: `https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/kotlinx/kotlinx-serialization-json/maven-metadata.xml`
  - Multiplatform Settings: `https://maven-central.storage-download.googleapis.com/maven2/com/russhwolf/multiplatform-settings/maven-metadata.xml`
  - JetBrains Lifecycle: `https://maven-central.storage-download.googleapis.com/maven2/org/jetbrains/androidx/lifecycle/lifecycle-viewmodel/maven-metadata.xml`
