# Evidence Archive: Modul 2 Video Player & Pelacakan Progres (KMP / CMP)

**Task & Issue References:**
- TASK-06: Data models Video Player, Report Reason, dan kontrak navigasi `ScreenDestination.VideoPlayer` ([#8](https://github.com/tomdwipo/oss-elearning/issues/8))
- TASK-07: Platform Bridge `expect/actual` `PlatformVideoPlayer` (Android WebView & iOS WKWebView iframe bridge) ([#9](https://github.com/tomdwipo/oss-elearning/issues/9))
- TASK-08: Implementasi `VideoPlayerScreen` dengan Player Controls, Speed Bar, dan Atribusi YouTube ([#10](https://github.com/tomdwipo/oss-elearning/issues/10))
- TASK-09: Logika Auto-Centang Progres ($\ge 85\%$) & Sinkronisasi Manual Checkbox ke `ProgressRepository` ([#11](https://github.com/tomdwipo/oss-elearning/issues/11))
- TASK-10: Fallback UI untuk Video Rusak & `ReportBrokenVideoDialog` (Pelaporan Link Rusak) ([#12](https://github.com/tomdwipo/oss-elearning/issues/12))
- TASK-11: Test Suite `VideoPlayerViewModelTest` di `commonTest`, Negative Control (Gate 3b), dan Verifikasi APK Debug ([#13](https://github.com/tomdwipo/oss-elearning/issues/13))

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
| **Serialization** | `kotlinx.serialization` 1.7.3 (JSON) |
| **Settings Storage** | `com.russhwolf:multiplatform-settings` 1.2.0 |
| **Virtual Device** | Android Emulator Pixel 9 Pro (API 35, 1280x2856) |

---

## 2. Tabel 7 Gerbang Verifikasi Lokal

| # | Gerbang | Deskripsi & Perintah | Status | Hasil / Bukti Konkret |
|---|---|---|---|---|
| **1** | **Linters & Formatters** | `./gradlew composeApp:lintDebug` | ✅ HIJAU | Exit 0, 0 error lint baru, HTML report tersimpan di `lint-results-debug.html`. |
| **2** | **Data & API Contracts** | `./gradlew test --tests "*VideoReport*" --tests "*TraceId*"` | ✅ HIJAU | Skema serialisasi `VideoReportPayload`, enum `PlaybackSpeed`, `ReportReason`, dan W3C Trace ID pattern `trc_<action>_<ts>_<hex>` tervalidasi. |
| **3a** | **Unit Tests** | `./gradlew test` | ✅ HIJAU | 22/22 tests passed di `commonTest` (ProgressRepository, SemesterViewModel, VideoPlayerViewModel, CurriculumRepository, TraceIdAndTelemetry). Exit 0. |
| **3b** | **Negative Control** | Mutasi ambang batas auto-centang `AUTO_COMPLETE_THRESHOLD_PERCENT` dari `85.0f` ke `95.0f` | ✅ TERVERIFIKASI | **MERAH** (Exit 1, 1 failed: `VideoPlayerViewModelTest.testAutoCompleteThresholdBoundaryEvaluation`) $\to$ dipulihkan $\to$ **HIJAU** (Exit 0, 22/22 passed). Rasio: `22/22 → 21/22 → 22/22`. |
| **4** | **Component & UI Viewport** | Standar viewport 375x812 dp & Design Tokens | ✅ HIJAU | Token `Corporate/Purple` (`#9D3FE7`), `DarkPurple` (`#602093`), `Informing/Approval` (`#00B998`), `Informing/Error` (`#D32F2F`), `Grayscale/Surface` (`#1E1E2E`) terkonfirmasi pada emulasi Pixel 9 Pro. |
| **5** | **Performance & Assets** | `./gradlew assembleDebug` & `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` | ✅ HIJAU | Exit 0, APK `composeApp-debug.apk` sebesar 10.0 MB dan binary framework iOS `ComposeApp.framework` berhasil diproduksi tanpa crash. |
| **6** | **Security & Secret Hygiene** | `git diff origin/main` | ✅ HIJAU | Tidak ada secret API key YouTube, token auth rahasia, atau kredensial backend hardcoded. |

---

## 3. Rincian Rasio Negative Control (Gerbang 3b)

- **Kondisi Awal (Baseline Hijau):** 22/22 passed (`./gradlew test` $\to$ Exit 0).
- **Mutasi (Injeksi Bug):** Mengubah threshold auto-complete di `VideoPlayerViewModel.AUTO_COMPLETE_THRESHOLD_PERCENT` dari `85.0f` menjadi `95.0f`.
- **Hasil Mutasi (Merah):** 21/22 passed, 1 failed (`./gradlew test` $\to$ Exit 1).
  - Gagal: `org.opencampus.elearning.VideoPlayerViewModelTest.testAutoCompleteThresholdBoundaryEvaluation` (`AssertionError` pada evaluasi batas 85.0%).
- **Pemulihan (Restored Hijau):** Nilai ambang batas dikembalikan normal ke `85.0f` $\to$ 22/22 passed (`./gradlew test` $\to$ Exit 0).
- **Rasio Pembuktian:** `22/22 → 21/22 → 22/22`.

---

## 4. Log Konsol
Dapat dilihat secara lengkap di [console-evidence.txt](./console-evidence.txt).

---

## 5. Tangkapan Layar (Screenshots Eksekusi Nyata)

| # | Layar / Skenario | Screenshot File | Deskripsi Bukti |
|---|---|---|---|
| **1** | **SemesterHomeScreen (Initial)** | [01_home_screen.png](./screenshots/01_home_screen.png) | Beranda semester dengan kartu mata kuliah dan progres semester. |
| **2** | **CourseSyllabusScreen** | [02_syllabus_screen.png](./screenshots/02_syllabus_screen.png) | Daftar 16 topik silabus dengan kartu ringkasan kurikulum dan item Topik 03 (Next Up). |
| **3** | **VideoPlayerScreen (Initial)** | [03_video_player_initial.png](./screenshots/03_video_player_initial.png) | Pemutar YouTube bebas distraksi, speed bar 1x aktif, metadata topik, checklist, dan tombol atribusi YouTube. |
| **4** | **VideoPlayerScreen (Speed Selected)** | [04_video_player_speed_selected.png](./screenshots/04_video_player_speed_selected.png) | Pemilihan kecepatan pemutaran 1.5x aktif dengan pill berlatar ungu `Corporate/Purple`. |
| **5** | **VideoPlayerScreen (Checked)** | [05_video_player_checked.png](./screenshots/05_video_player_checked.png) | Checklist manual ditandai selesai (`[x]`) dengan badge status *"Selesai"* warna hijau `#00B998`. |
| **6** | **Modal Dialog Laporkan Link** | [06_video_player_report_dialog.png](./screenshots/06_video_player_report_dialog.png) | Dialog pelaporan link rusak dengan 4 opsi alasan radio button dan catatan tambahan. |
| **7** | **Laporan Berhasil Dikirim** | [07_video_player_report_success.png](./screenshots/07_video_player_report_success.png) | Banner konfirmasi hijau *"Laporan berhasil dikirim"* setelah pelaporan tersimpan ke repository. |
| **8** | **CourseSyllabusScreen (Progress Updated)** | [08_syllabus_progress_updated.png](./screenshots/08_syllabus_progress_updated.png) | Navigasi kembali ke silabus menunjukkan checklist Topik 03 tercentang dan progres mata kuliah bertambah. |

---

## 6. Video Walkthrough Aplikasi (Video Evidence)

Rekaman walkthrough end-to-end eksekusi aplikasi Android di emulator Pixel 9 Pro:
- **File Video:** [`demo.mp4`](./demo.mp4) (H.264 / AAC MP4, 720x1280, 526 KB)
- **Alur yang Terbukti:**
  1. Navigasi dari Beranda Semester ke Layar Silabus.
  2. Membuka topik pembelajaran menuju `VideoPlayerScreen`.
  3. Pemutaran video tertanam dan pergantian kecepatan putar ke 1.5x.
  4. Membuka modal dialog pelaporan link rusak dan memilih alasan masalah.
  5. Mengirimkan laporan link rusak dan konfirmasi banner sukses.
  6. Menandai checkbox selesai dan navigasi kembali ke silabus dengan progres yang terbarui.
