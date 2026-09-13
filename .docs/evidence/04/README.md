# Evidence Archive: Modul 4 Detail View UI Parity & Dual-Platform Visual Verification

**Task & Issue References:**
- TASK-16: Responsive Layout Refactoring & Non-Wrapping Badge Container on VideoPlayerScreen ([#20](https://github.com/tomdwipo/oss-elearning/issues/20))
- TASK-17: Scrollable Adaptive Layout & Spacing Fix on ReportBrokenVideoDialog ([#21](https://github.com/tomdwipo/oss-elearning/issues/21))
- TASK-18: Dual-Platform Unit Test Suite, Negative Control (Gate 3b), & Roborazzi Visual Parity Validation ([#22](https://github.com/tomdwipo/oss-elearning/issues/22))
- TASK-19: Verifikasi Packaging Dual Platform (`assembleDebug` & `linkDebugFrameworkIosSimulatorArm64`), Walkthrough Live Demo, & Evidence Archiving ([#23](https://github.com/tomdwipo/oss-elearning/issues/23))

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
| **1** | **Linters & Formatters** | `./gradlew composeApp:lintDebug` | ✅ [RUN-cli] HIJAU | Exit 0, 0 error lint baru, build sukses. |
| **2** | **Data & API Contracts** | `./gradlew test --tests "*CurriculumVideoContractTest*"` | ✅ [RUN-cli] HIJAU | 4/4 contract tests passed: validasi regex Video ID canonical 11 karakter YouTube, keunikan ID, kelengkapan silabus 16 pertemuan CS101. |
| **3a** | **Unit Tests** | `./gradlew test` | ✅ [RUN-cli] HIJAU | 26/26 tests passed di `commonTest` (CurriculumVideoContractTest, VideoPlayerViewModelTest, SemesterViewModelTest, ProgressRepositoryTest, TraceIdAndTelemetryTest). Exit 0. |
| **3b** | **Negative Control** | Mutasi video ID pada silabus CS101 dari `jGyYuQf-GeE` menjadi placeholder tidak valid `t101_01_v` | ✅ [RUN-cli] TERVERIFIKASI | **MERAH** (Exit 1, 1 failed: `testAllCurriculumVideoIdsFollowCanonicalFormat`) $\to$ dipulihkan $\to$ **HIJAU** (Exit 0, 26/26 passed). Rasio: `26/26 → 25/26 → 26/26`. |
| **4** | **Component & UI Viewport** | Standar viewport Android (Pixel 9 Pro) & iOS (iPhone 16 Pro) | ✅ [RUN-cli] HIJAU | Resolusi 1280x2856 (Android) dan 1206x2622 (iOS) terverifikasi: badge status progres ter-render utuh horizontal (non-wrapping) dan form modal dialog bebas tabrakan (zero overlapping). |
| **5** | **Performance & Packaging** | `./gradlew assembleDebug` & `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` | ✅ [RUN-cli] HIJAU | Exit 0: `composeApp-debug.apk` (Android, 10 MB) dan `ComposeApp.framework` (iOS Simulator ARM64) terkompilasi sukses. |
| **6** | **Security & Secret Hygiene** | `git diff origin/main` | ✅ [RUN-cli] HIJAU | Tidak ada secret API key YouTube, token auth rahasia, atau kredensial backend hardcoded. |

---

## 3. Rincian Rasio Negative Control (Gerbang 3b)

- **Kondisi Awal (Baseline Hijau):** 26/26 passed (`./gradlew test` $\to$ Exit 0) [RUN-cli].
- **Mutasi (Injeksi Bug):** Mengubah video ID pertemuan 01 mata kuliah `cs101` di `CurriculumDataSource.kt` dari `jGyYuQf-GeE` menjadi string tidak valid `t101_01_v` (9 karakter, melanggar pola 11 karakter canonical YouTube ID).
- **Hasil Mutasi (Merah):** 25/26 passed, 1 failed (`./gradlew test` $\to$ Exit 1) [RUN-cli].
  - Gagal: `org.opencampus.elearning.CurriculumVideoContractTest.testAllCurriculumVideoIdsFollowCanonicalFormat` (`AssertionError: Video ID t101_01_v pada topik 'Konsep Dasar Logika & Flowchart' tidak valid`).
- **Pemulihan (Restored Hijau):** Mengembalikan video ID kembali ke `jGyYuQf-GeE` $\to$ 26/26 passed (`./gradlew test` $\to$ Exit 0) [RUN-cli].
- **Rasio Pembuktian:** `26/26 → 25/26 → 26/26` [RUN-cli].

---

## 4. Log Konsol
Dapat dilihat secara lengkap di [console-evidence.txt](.docs/evidence/04/console-evidence.txt).

---

## 5. Tangkapan Layar (Screenshots Eksekusi Nyata)

### Android (Pixel 9 Pro via Android CLI & ADB)
| # | Layar / Skenario | Screenshot File | Deskripsi Bukti |
|---|---|---|---|
| **1** | **SemesterHomeScreen** | [.docs/evidence/04/android/screenshots/01_home_screen.png](.docs/evidence/04/android/screenshots/01_home_screen.png) | Beranda semester dengan daftar mata kuliah kurikulum IT. |
| **2** | **CourseSyllabusScreen** | [.docs/evidence/04/android/screenshots/02_course_detail.png](.docs/evidence/04/android/screenshots/02_course_detail.png) | Daftar 16 topik silabus Algoritma & Pemrograman Dasar dengan durasi dan channel terkurasi. |
| **3** | **VideoPlayerScreen Incomplete** | [.docs/evidence/04/android/screenshots/01_player_incomplete.png](.docs/evidence/04/android/screenshots/01_player_incomplete.png) | Tampilan pemutar video dengan badge horizontal "Belum Selesai". |
| **4** | **VideoPlayerScreen Completed** | [.docs/evidence/04/android/screenshots/01_player.png](.docs/evidence/04/android/screenshots/01_player.png) | Tampilan pemutar video dengan badge horizontal "Selesai" dan checklist dicentang. |
| **5** | **VideoPlayerScreen Active** | [.docs/evidence/04/android/screenshots/01_player_active.png](.docs/evidence/04/android/screenshots/01_player_active.png) | Pemutar video terintegrasi dengan speed controls, checkbox progres, dan tautan resmi. |
| **6** | **Modal Dialog Pelaporan** | [.docs/evidence/04/android/screenshots/03_report_dialog.png](.docs/evidence/04/android/screenshots/03_report_dialog.png) | Dialog pelaporan link rusak dengan 4 opsi radio button dan input catatan bebas overlap. |

### iOS (iPhone 16 Pro via xcrun simctl)
| # | Layar / Skenario | Screenshot File | Deskripsi Bukti |
|---|---|---|---|
| **1** | **SemesterHomeScreen** | [.docs/evidence/04/ios/screenshots/01_home_screen.png](.docs/evidence/04/ios/screenshots/01_home_screen.png) | Beranda semester OpenCampus berjalan native di iPhone 16 Pro. |
| **2** | **CourseSyllabusScreen** | [.docs/evidence/04/ios/screenshots/02_course_syllabus.png](.docs/evidence/04/ios/screenshots/02_course_syllabus.png) | Daftar 16 topik silabus dengan durasi dan channel terkurasi di iOS. |
| **3** | **VideoPlayerScreen Active** | [.docs/evidence/04/ios/screenshots/03_video_player_active.png](.docs/evidence/04/ios/screenshots/03_video_player_active.png) | Pemutaran live stream video terkurasi CS101 aktif via WKWebView di iPhone 16 Pro. |
| **4** | **VideoPlayer (Fullscreen, Completed)** | [.docs/evidence/04/ios/screenshots/01_player_fixed.png](.docs/evidence/04/ios/screenshots/01_player_fixed.png) | Host `iosApp` dengan `UILaunchScreen` (fullscreen 393pt). Layout identik Android. |
| **4b** | **VideoPlayer (Fullscreen, Incomplete)** | [.docs/evidence/04/ios/screenshots/01_player_incomplete.png](.docs/evidence/04/ios/screenshots/01_player_incomplete.png) | Badge "Belum Selesai" dan label checklist utuh tanpa ellipsis. |
| **5** | **Modal Dialog Initial Fixed** | [.docs/evidence/04/ios/screenshots/02_dialog_fixed.png](.docs/evidence/04/ios/screenshots/02_dialog_fixed.png) | Dialog pelaporan link rusak dengan container adaptif bebas tumpang tindih (*zero overlapping*). |

---

## 6. Roborazzi Visual Diff Matrix (Dual-Platform Parity)

| Komparasi Visual | Artefak Roborazzi (3-Kolom) | Deskripsi Paritas Visual |
|---|---|---|
| **Video Player Detail Layout** | [.docs/evidence/04/visual_diff_roborazzi/01_player_roborazzi_diff.png](.docs/evidence/04/visual_diff_roborazzi/01_player_roborazzi_diff.png) | Komparasi 3-kolom (Android \| iOS \| Diff Mask) membuktikan badge status ter-render utuh horizontal pada kedua platform tanpa pemenggalan karakter vertikal. |
| **Modal Dialog Pelaporan** | [.docs/evidence/04/visual_diff_roborazzi/02_dialog_roborazzi_diff.png](.docs/evidence/04/visual_diff_roborazzi/02_dialog_roborazzi_diff.png) | Komparasi 3-kolom membuktikan form pelaporan link rusak bebas tabrakan visual (*zero overlapping*) antara OutlinedTextField dan RadioButton ke-4. |
| **Video Player Live Stream** | [.docs/evidence/04/visual_diff_roborazzi/03_video_player_active_roborazzi_diff.png](.docs/evidence/04/visual_diff_roborazzi/03_video_player_active_roborazzi_diff.png) | Komparasi 3-kolom pemutaran video aktif CS101 pada Android dan iOS. |
| **Dialog Pelaporan Scrolled** | [.docs/evidence/04/visual_diff_roborazzi/04_report_dialog_roborazzi_diff.png](.docs/evidence/04/visual_diff_roborazzi/04_report_dialog_roborazzi_diff.png) | Komparasi 3-kolom dialog pelaporan scrolled state membuktikan integritas input field catatan tambahan. |

---

## 7. Video Walkthrough Aplikasi (Dual-Platform Evidence)

- **Android Walkthrough:** [.docs/evidence/04/android/demo.mp4](.docs/evidence/04/android/demo.mp4) (H.264 / AAC MP4, 720x1280, interaksi lengkap: live player streaming, speed control, toggle checklist manual, dialog pelaporan).
- **iOS Walkthrough:** [.docs/evidence/04/ios/demo.mp4](.docs/evidence/04/ios/demo.mp4) (H.264 / AAC MP4, 1206x2622, pemutaran live video, toggle checklist responsif, dialog pelaporan scrollable bebas overlapping di iPhone 16 Pro simulator).
