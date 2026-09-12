# Evidence Archive: Modul 3 Kurikulum Video Aktif & Dual-Platform Verification

**Task & Issue References:**
- TASK-12: Patching Dataset Kurikulum Video Aktif (16 Video CS101 + Topik Kunci CS102-104) di KMP Data Layer ([#15](https://github.com/tomdwipo/oss-elearning/issues/15))
- TASK-13: Test Suite Validasi Format Video ID (`CurriculumVideoContractTest`), Negative Control (Gate 3b), & Integritas Kurikulum ([#16](https://github.com/tomdwipo/oss-elearning/issues/16))
- TASK-14: Platform Bridge Live Playback Verification, Handling Fallback Galat CDN, & Sinkronisasi Progress State (Android & iOS) ([#17](https://github.com/tomdwipo/oss-elearning/issues/17))
- TASK-15: Verifikasi Packaging Dual Platform (`assembleDebug` APK & `linkDebugFrameworkIosSimulatorArm64`), Walkthrough Live Video Playback, dan Pengarsipan Evidence Dual-Platform ([#18](https://github.com/tomdwipo/oss-elearning/issues/18))

---

## 1. Tabel Lingkungan Eksekusi (Environment)

| Parameter | Nilai / Spesifikasi |
|---|---|
| **OS** | macOS Darwin 24.6.0 (Mac OS X 15.6 aarch64) |
| **Java JDK** | OpenJDK 17.0.12 (Homebrew 17.0.12+0) |
| **Gradle** | 8.11.1 (Tencent mirror distribution) |
| **Kotlin** | 2.1.0 (KMP multiplatform plugin) |
| **Compose Multiplatform** | 1.7.3 (JetBrains CMP) |
| **Android Gradle Plugin** | 8.7.3 (compileSdk 35, minSdk 24) |
| **Android Virtual Device** | Android Emulator Pixel 9 Pro (API 35, 1280x2856) |
| **iOS Simulator Device** | iPhone 16 Pro (iOS 18.5, UUID `2AC7DCD9-4B36-496F-8F7C-B2B981C1347B`) |
| **Android CLI** | v1.0.15985488 (`~/.local/bin/android`) |

---

## 2. Tabel 7 Gerbang Verifikasi Lokal

| # | Gerbang | Deskripsi & Perintah | Status | Hasil / Bukti Konkret |
|---|---|---|---|---|
| **1** | **Linters & Formatters** | `./gradlew composeApp:lintDebug` | ✅ HIJAU | Exit 0, 0 error lint baru, HTML report tersimpan. |
| **2** | **Data & API Contracts** | `./gradlew test --tests "*CurriculumVideoContractTest*"` | ✅ HIJAU | 4/4 contract tests passed: validasi regex Video ID `^[a-zA-Z0-9_-]{11}$`, keunikan Video ID, kelengkapan silabus 16 pertemuan CS101, dan validitas topik kunci. |
| **3a** | **Unit Tests** | `./gradlew test` | ✅ HIJAU | 26/26 tests passed di `commonTest` (CurriculumVideoContractTest, VideoPlayerViewModelTest, SemesterViewModelTest, ProgressRepositoryTest, TraceIdAndTelemetryTest). Exit 0. |
| **3b** | **Negative Control** | Mutasi video ID pada silabus CS101 dari `jGyYuQf-GeE` menjadi placeholder tidak valid `t101_01_v` | ✅ TERVERIFIKASI | **MERAH** (Exit 1, 1 failed: `testAllCurriculumVideoIdsFollowCanonicalFormat`) $\to$ dipulihkan $\to$ **HIJAU** (Exit 0, 26/26 passed). Rasio: `26/26 → 25/26 → 26/26`. |
| **4** | **Component & UI Viewport** | Standar viewport Android (Pixel 9 Pro) & iOS (iPhone 16 Pro) | ✅ HIJAU | Resolusi 1280x2856 (Android) dan 1206x2622 (iOS) terverifikasi dengan token `Corporate/Purple` (`#9D3FE7`), `DarkPurple` (`#602093`), dan dark surface `#1E1E2E`. |
| **5** | **Performance & Packaging** | `./gradlew assembleDebug` & `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` | ✅ HIJAU | Exit 0: `composeApp-debug.apk` (Android) dan `ComposeApp.framework` (iOS Simulator ARM64) terkompilasi sukses. |
| **6** | **Security & Secret Hygiene** | `git diff origin/main` | ✅ HIJAU | Tidak ada secret API key YouTube, token auth rahasia, atau kredensial backend hardcoded. |

---

## 3. Rincian Rasio Negative Control (Gerbang 3b)

- **Kondisi Awal (Baseline Hijau):** 26/26 passed (`./gradlew test` $\to$ Exit 0).
- **Mutasi (Injeksi Bug):** Mengubah video ID pertemuan 01 mata kuliah `cs101` di `curriculum_it_semesters.json` dari `jGyYuQf-GeE` menjadi string tidak valid `t101_01_v` (9 karakter, melanggar pola 11 karakter canonical YouTube ID).
- **Hasil Mutasi (Merah):** 25/26 passed, 1 failed (`./gradlew test` $\to$ Exit 1).
  - Gagal: `org.opencampus.elearning.CurriculumVideoContractTest.testAllCurriculumVideoIdsFollowCanonicalFormat` (`AssertionError: Video ID t101_01_v pada topik 'Konsep Dasar Logika & Flowchart' tidak valid`).
- **Pemulihan (Restored Hijau):** Mengembalikan video ID kembali ke `jGyYuQf-GeE` $\to$ 26/26 passed (`./gradlew test` $\to$ Exit 0).
- **Rasio Pembuktian:** `26/26 → 25/26 → 26/26`.

---

## 4. Log Konsol
Dapat dilihat secara lengkap di [console-evidence.txt](./console-evidence.txt).

---

## 5. Tangkapan Layar (Screenshots Eksekusi Nyata)

### Android (Pixel 9 Pro via Android CLI & ADB)
| # | Layar / Skenario | Screenshot File | Deskripsi Bukti |
|---|---|---|---|
| **1** | **SemesterHomeScreen** | [01_home_screen.png](./android/screenshots/01_home_screen.png) | Beranda semester dengan daftar mata kuliah kurikulum IT. |
| **2** | **CourseSyllabusScreen** | [02_course_detail.png](./android/screenshots/02_course_detail.png) | Daftar 16 topik silabus Algoritma & Pemrograman Dasar dengan durasi dan channel terkurasi. |
| **3** | **VideoPlayerScreen Active** | [01_player_active.png](./android/screenshots/01_player_active.png) | Pemutar video terintegrasi dengan speed controls, checkbox progres, dan tautan resmi. |
| **4** | **Modal Dialog Pelaporan** | [03_report_dialog.png](./android/screenshots/03_report_dialog.png) | Dialog pelaporan link rusak dengan 4 opsi radio button dan catatan tambahan. |

### iOS (iPhone 16 Pro via xcrun simctl)
| # | Layar / Skenario | Screenshot File | Deskripsi Bukti |
|---|---|---|---|
| **1** | **iOS Simulator Booted** | [01_ios_simulator.png](./ios/screenshots/01_ios_simulator.png) | Simulator iPhone 16 Pro (iOS 18.5) booted dan siap eksekusi. |
| **2** | **SemesterHomeScreen** | [01_home_screen.png](./ios/screenshots/01_home_screen.png) | Beranda semester OpenCampus berjalan native di iPhone 16 Pro. |
| **3** | **CourseSyllabusScreen** | [02_course_syllabus.png](./ios/screenshots/02_course_syllabus.png) | Daftar 16 topik silabus dengan durasi dan channel terkurasi di iOS. |
| **4** | **VideoPlayerScreen Active** | [03_video_player_active.png](./ios/screenshots/03_video_player_active.png) | Pemutaran live stream video terkurasi CS101 (Web Programming UNPAS) aktif di dalam aplikasi OpenCampus via WKWebView. |
| **5** | **Modal Dialog Pelaporan** | [04_report_dialog.png](./ios/screenshots/04_report_dialog.png) | Dialog pelaporan link rusak dengan 4 opsi radio button di iOS. |

---

## 6. Video Walkthrough Aplikasi (Dual-Platform Evidence)

- **Android Walkthrough:** [`./android/demo.mp4`](./android/demo.mp4) (H.264 / AAC MP4, 720x1280, interaksi lengkap: live player streaming tanpa layar hitam, speed control, checkbox manual, dialog pelaporan).
- **iOS Walkthrough:** [`./ios/demo.mp4`](./ios/demo.mp4) (Apple QuickTime / HEVC, 1206x2622, pemutaran live video kurikulum aktif di dalam aplikasi OpenCampus di iPhone 16 Pro simulator).
- **Root Evidence Sync:** [`../02/demo.mp4`](../02/demo.mp4) disinkronkan dengan video rekaman playback nyata tanpa layar hitam.
