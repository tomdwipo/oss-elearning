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

---

## 2. Dependency Audit Matrix

Berikut adalah ringkasan perbandingan antara versi yang digunakan saat ini dengan versi rilis terbaru:

| Key Version | Coordinate / Artifact | Versi Saat Ini | Versi Stable Terbaru | Versi Terakhir (Inc. Pre-release) | Status / Gap |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **`agp`** | `com.android.tools.build:gradle` | `8.7.3` | **`9.4.0`** | `9.5.0-alpha05` | 🔴 Major behind (+29 stable) |
| **`kotlin`** | `org.jetbrains.kotlin:kotlin-gradle-plugin` | `2.1.0` | **`2.4.20`** | `2.4.20` | 🔴 Major behind (+14 stable) |
| **`compose-multiplatform`** | `org.jetbrains.compose:compose-gradle-plugin` | `1.7.3` | **`1.12.0`** | `1.13.0-alpha01` | 🔴 Major behind (+14 stable) |
| **`kotlinx-serialization`** | `org.jetbrains.kotlinx:kotlinx-serialization-json` | `1.7.3` | **`1.11.0`** | `1.12.0-RC` | 🟡 Minor behind (+5 stable) |
| **`kotlinx-coroutines`** | `org.jetbrains.kotlinx:kotlinx-coroutines-core` | `1.9.0` | **`1.11.0`** | `1.11.0` | 🟡 Minor behind (+4 stable) |
| **`multiplatform-settings`** | `com.russhwolf:multiplatform-settings` | `1.2.0` | **`1.3.0`** | `1.3.0` | 🟢 Minor behind (+1 stable) |
| **`androidx-lifecycle`** | `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel` | `2.8.4` | **`2.11.0`** | `2.11.0` | 🔴 Major behind (+9 stable) |
| **`androidx-activity-compose`** | `androidx.activity:activity-compose` | `1.9.3` | **`1.13.0`** | `1.14.0-alpha02` | 🟡 Minor behind (+9 stable) |
| **`androidx-appcompat`** | `androidx.appcompat:appcompat` | `1.7.0` | **`1.8.0`** | `1.8.0` | 🟢 Minor behind (+2 stable) |
| **`androidx-core-ktx`** | `androidx.core:core-ktx` | `1.15.0` | **`1.19.0`** | `1.19.0` | 🟡 Minor behind (+4 stable) |

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

### 3.2. Kotlin (`kotlin`)
- **Coordinate:** `org.jetbrains.kotlin:kotlin-gradle-plugin`
- **Versi Terpasang:** `2.1.0`
- **Versi Stable yang Sudah Tersedia (+14 versi):**
  - `2.1.x`: `2.1.10`, `2.1.20`, `2.1.21`
  - `2.2.x`: `2.2.0`, `2.2.10`, `2.2.20`, `2.2.21`
  - `2.3.x`: `2.3.0`, `2.3.10`, `2.3.20`, `2.3.21`
  - `2.4.x`: `2.4.0`, `2.4.10`, `2.4.20`
- **Versi Terakhir:** `2.4.20` (RC: `2.4.20-RC`, `2.4.20-RC2`, `2.4.20-RC3`)

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

### 3.4. Kotlinx Serialization (`kotlinx-serialization`)
- **Coordinate:** `org.jetbrains.kotlinx:kotlinx-serialization-json`
- **Versi Terpasang:** `1.7.3`
- **Versi Stable yang Sudah Tersedia (+5 versi):**
  - `1.8.0`, `1.8.1`, `1.9.0`, `1.10.0`, `1.11.0`
- **Versi Alpha/Beta/RC Terbaru:** `1.12.0-RC`

### 3.5. Kotlinx Coroutines (`kotlinx-coroutines`)
- **Coordinate:** `org.jetbrains.kotlinx:kotlinx-coroutines-core`
- **Versi Terpasang:** `1.9.0`
- **Versi Stable yang Sudah Tersedia (+4 versi):**
  - `1.10.0`, `1.10.1`, `1.10.2`, `1.11.0`
- **Versi Terakhir:** `1.11.0`

### 3.6. Multiplatform Settings (`multiplatform-settings`)
- **Coordinate:** `com.russhwolf:multiplatform-settings`
- **Versi Terpasang:** `1.2.0`
- **Versi Stable yang Sudah Tersedia (+1 versi):**
  - `1.3.0`
- **Versi Terakhir:** `1.3.0`

### 3.7. JetBrains AndroidX Lifecycle (`androidx-lifecycle`)
- **Coordinate:** `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel` & `lifecycle-viewmodel-compose`
- **Versi Terpasang:** `2.8.4`
- **Versi Stable yang Sudah Tersedia (+9 versi):**
  - `2.9.x`: `2.9.0`, `2.9.1`, `2.9.2`, `2.9.3`, `2.9.4`, `2.9.5`, `2.9.6`
  - `2.10.x`: `2.10.0`
  - `2.11.x`: `2.11.0`
- **Versi Terakhir:** `2.11.0`

### 3.8. AndroidX Activity Compose (`androidx-activity-compose`)
- **Coordinate:** `androidx.activity:activity-compose`
- **Versi Terpasang:** `1.9.3`
- **Versi Stable yang Sudah Tersedia (+9 versi):**
  - `1.10.0`, `1.10.1`, `1.11.0`, `1.12.0`, `1.12.1`, `1.12.2`, `1.12.3`, `1.12.4`, `1.13.0`
- **Versi Alpha/Beta/RC Terbaru:** `1.14.0-alpha02`

### 3.9. AndroidX AppCompat (`androidx-appcompat`)
- **Coordinate:** `androidx.appcompat:appcompat`
- **Versi Terpasang:** `1.7.0`
- **Versi Stable yang Sudah Tersedia (+2 versi):**
  - `1.7.1`, `1.8.0`
- **Versi Terakhir:** `1.8.0`

### 3.10. AndroidX Core KTX (`androidx-core-ktx`)
- **Coordinate:** `androidx.core:core-ktx`
- **Versi Terpasang:** `1.15.0`
- **Versi Stable yang Sudah Tersedia (+4 versi):**
  - `1.16.0`, `1.17.0`, `1.18.0`, `1.19.0`
- **Versi Terakhir:** `1.19.0`

---

## 4. Analisis Kompatibilitas & Risiko Upgrade

1. **Sinkronisasi Kotlin & Compose Multiplatform:**
   - Mulai Kotlin 2.0+, Compose Compiler dikelola langsung oleh plugin Kotlin (`org.jetbrains.kotlin.plugin.compose`). Namun, runtime Compose Multiplatform (`org.jetbrains.compose`) memiliki rentang kompatibilitas Kotlin tertentu. Meng-upgrade Kotlin ke `2.4.x` wajib dibarengi dengan upgrade `compose-multiplatform` yang sesuai.
2. **AGP & Gradle Compatibility:**
   - Gradle wrapper repository saat ini adalah `8.11.1`.
   - AGP `8.7.3` berjalan stabil pada Gradle `8.11.1`.
   - AGP `9.x` memerlukan Gradle versi 9+ dan Java toolchain tertentu. Jika ingin meng-upgrade AGP secara konservatif tanpa mengubah major Gradle, upgrade ke seri `8.11.x` atau `8.12.x` adalah opsi yang paling aman.
3. **KMP Library Alignment:**
   - `kotlinx-coroutines` dan `kotlinx-serialization` perlu diselaraskan dengan versi Kotlin compiler agar metadata Klib tetap kompatibel untuk target Apple (`iosX64`, `iosArm64`, `iosSimulatorArm64`) dan Android.

---

## 5. Rekomendasi Roadmap Upgrade (Bertahap)

| Tahapan | Lingkup Upgrade | Target Versi | Risiko | Aksi Pengujian |
| :--- | :--- | :--- | :---: | :--- |
| **Phase 1: Low-Risk / Standalone** | `multiplatform-settings`, `androidx-appcompat`, `androidx-core-ktx` | `1.3.0`, `1.8.0`, `1.16.0` | Rendah | `./gradlew test` & verify compilation |
| **Phase 2: Coroutines & Serialization** | `kotlinx-coroutines`, `kotlinx-serialization` | `1.10.x` / `1.11.0`, `1.8.x` / `1.10.0` | Rendah-Sedang | Test serialization & asynchronous flows |
| **Phase 3: Core KMP Framework** | `kotlin`, `compose-multiplatform`, `androidx-lifecycle`, `agp` | Align versi stabil Kotlin & Compose | Sedang-Tinggi | Full build Android APK & iOS Framework, regression testing UI |
