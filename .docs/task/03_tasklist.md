# Task Implementation Plan: Integrasi Kurasi Video YouTube Aktif & Verifikasi Pemutaran Dual Platform (Modul 3)

**Nomor Dokumen:** 03_tasklist  
**Target Rilis / Milestone:** MVP v0.1  
**Dokumen Asesmen Terkait:** [Curated YouTube Videos & Embeddability Assessment](../assesment/02_curated_youtube_videos_assessment.md)  
**Dokumen PRD:** [PRD 02: Video Player & Pelacakan Progres](../prd/02_prd.md), [PRD 00: Global MVP](../prd/00_prd.md)  
**Dokumen TRD:** [TRD 03: Integrasi Kurasi Video YouTube Aktif & Verifikasi Pemutaran Dual Platform](../trd/03_trd.md)  
**Arsitektur Target:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP)  
**Target Arsip Evidence:** `.docs/evidence/03/README.md` (dan pembaruan `.docs/evidence/02/demo.mp4`)  

---

## 1. Ikhtisar & Ringkasan Task

Dokumen ini memetakan seluruh kebutuhan teknis dari **TRD 03** ke dalam rencana eksekusi terstruktur (*Task Implementation Plan*) yang mematuhi standar **Acceptance Criteria 4-Pilar Wajib** (Core Business Logic, UI/UX States & Tokens, Observability & Telemetry, dan Product Analytics) serta **Mandatori Dual Platform (Android & iOS)**.

| Task ID | Issue GitHub | Judul Task | Layer / Source Set | Kompleksitas | Dependensi |
|---|---|---|---|---|---|
| **TASK-12** | [#15](https://github.com/tomdwipo/oss-elearning/issues/15) | Patching Dataset Kurikulum Video Aktif (cs101 16 Pertemuan & Topik Kunci cs102-104) di KMP Data Layer | `commonMain` (Data / Resources) | Medium (3 SP) | - |
| **TASK-13** | [#16](https://github.com/tomdwipo/oss-elearning/issues/16) | Test Suite Validasi Format Video ID (`CurriculumVideoContractTest`), Negative Control (Gate 3b), & Integritas Kurikulum | `commonTest` (Quality / Testing) | Medium (3 SP) | TASK-12 |
| **TASK-14** | [#17](https://github.com/tomdwipo/oss-elearning/issues/17) | Platform Bridge Live Playback Verification, Handling Fallback Galat CDN, & Sinkronisasi Progress State (Android & iOS) | `commonMain`, `androidMain`, `iosMain` | High (5 SP) | TASK-12..13 |
| **TASK-15** | [#18](https://github.com/tomdwipo/oss-elearning/issues/18) | Verifikasi Packaging Dual Platform (`assembleDebug` APK & `linkDebugFrameworkIosSimulatorArm64`), Walkthrough Live Video Playback, dan Pengarsipan Evidence Dual-Platform | `androidMain`, `iosMain`, Evidence Archive | High (5 SP) | TASK-12..14 |

---

## 2. Rincian Task & Acceptance Criteria (4-Pillar + Dual Platform Wajib)

### TASK-12: Patching Dataset Kurikulum Video Aktif (cs101 16 Pertemuan & Topik Kunci cs102-104) di KMP Data Layer — [#15](https://github.com/tomdwipo/oss-elearning/issues/15)
- **Layer:** `commonMain` (Data / Resources)
- **Kompleksitas:** Medium (3 SP)
- **Problem Statement:** Seluruh 480 topik kurikulum pada `curriculum_it_semesters.json` dan `CurriculumDataSource.kt` saat ini masih menggunakan string mock placeholder (`t101_01_v` dsb.), menyebabkan pemutar video `PlatformVideoPlayer` gagal memuat media stream dan hanya merender kotak gelap `#1E1E2E`.
- **Technical Context:**
  - `03_TRD.md` §1.2 (Current Implementation) & §1.3 (Target Implementation).
  - `02_curated_youtube_videos_assessment.md` §4.1–§4.4 (Matriks Kurasi Video Aktif 100% oEmbed 200 OK).
  - File Codebase:
    - [curriculum_it_semesters.json](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/composeResources/files/curriculum_it_semesters.json)
    - [CurriculumDataSource.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/data/CurriculumDataSource.kt)
    - [CurriculumRepository.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/data/CurriculumRepository.kt)
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Memperbarui 16 pertemuan mata kuliah `cs101` (*Algoritma & Pemrograman Dasar*) dengan ID 11-karakter YouTube resmi yang telah diverifikasi oEmbed 200 OK:
      - Pertemuan 01: `jGyYuQf-GeE` (*Web Programming UNPAS*)
      - Pertemuan 02: `1FAnrYu7LCM` (*Web Programming UNPAS*)
      - Pertemuan 03: `-9IyBehKm4g` (*Kelas Terbuka*)
      - Pertemuan 04: `ZeqJewFm7zc` (*Kelas Terbuka*)
      - Pertemuan 05: `iTUO1DWVUv8` (*Kelas Terbuka*)
      - Pertemuan 06: `-JV_YbJR1GY` (*Web Programming UNPAS*)
      - Pertemuan 07: `8WhUADLI4RQ` (*Kelas Terbuka*)
      - Pertemuan 08: `TanFwAcHBqE` (*Web Programming UNPAS*)
      - Pertemuan 09: `OpodtuA0xyI` (*Kelas Terbuka*)
      - Pertemuan 10: `O1kWNj5Ikro` (*Kelas Terbuka*)
      - Pertemuan 11: `ELCI_U4OF5w` (*Kelas Terbuka*)
      - Pertemuan 12: `QFC4DXvRu8o` (*Kelas Terbuka*)
      - Pertemuan 13: `G0cml-wvaBc` (*Kelas Terbuka*)
      - Pertemuan 14: `XgKfcZctwA8` (*Backend Magang*)
      - Pertemuan 15: `rS-mnrY4Djw` (*Kelas Terbuka*)
      - Pertemuan 16: `Mmf3SXHifBw` (*Web Programming UNPAS*)
    - Memperbarui topik terkurasi Semester 1 lainnya (`cs102` Matematika Diskrit, `cs103` Pengantar TI, `cs104` Arsitektur Komputer) sesuai asesmen.
    - Sinkronisasi kedua dataset (`curriculum_it_semesters.json` dan `CurriculumDataSource.kt`) sehingga identik dan konsisten.
  - • **UI/UX States & Tokens:**
    - Metadata channel pembuat konten dan estimasi durasi riil diperbarui sesuai data YouTube aktual.
  - • **Observability & Telemetry:**
    - Log konsol pada `LocalCurriculumRepository` mencatat keberhasilan deserialisasi dataset JSON tanpa error.
  - • **Product Analytics:**
    - Payload event `video_started` membawa `videoId` riil 11 karakter.

---

### TASK-13: Test Suite Validasi Format Video ID (`CurriculumVideoContractTest`), Negative Control (Gate 3b), & Integritas Kurikulum — [#16](https://github.com/tomdwipo/oss-elearning/issues/16)
- **Layer:** `commonTest` (Quality / Testing)
- **Kompleksitas:** Medium (3 SP)
- **Problem Statement:** Diperlukan unit test otomatis untuk menjamin bahwa seluruh video ID yang diinjeksi mematuhi pola regex YouTube 11-karakter (`^[a-zA-Z0-9_-]{11}$`) dan nama kanal tidak kosong, serta menyelenggarakan Gate 3b Negative Control untuk membuktikan ketatnya pengujian.
- **Technical Context:**
  - `03_TRD.md` §6 (Testing Requirements & 7 Local Verification Gates).
  - File Codebase:
    - [CurriculumRepositoryTest.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonTest/kotlin/org/opencampus/elearning/CurriculumRepositoryTest.kt)
    - [CurriculumVideoContractTest.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonTest/kotlin/org/opencampus/elearning/CurriculumVideoContractTest.kt)
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Membuat test class baru `CurriculumVideoContractTest` di `commonTest`.
    - `testCs101AllSixteenTopicsHaveValidYouTubeVideoIds()`: Memvalidasi 16 pertemuan `cs101` memiliki `videoId` sepanjang tepat 11 karakter dan lolos regex `^[a-zA-Z0-9_-]{11}$`.
    - `testCuratedTopicsChannelAttributionNotEmpty()`: Memastikan nama channel pada setiap topik terkurasi tidak kosong (*non-blank*).
    - Seluruh unit test eksisting di `CurriculumRepositoryTest`, `VideoPlayerViewModelTest`, dan `SemesterViewModelTest` tetap 100% HIJAU.
  - • **Gate 3b Negative Control (Wajib):**
    - Merusak 1 ID video di `CurriculumDataSource.kt` menjadi mock string `t101_01_v`.
    - Menjalankan `./gradlew test` dan membuktikan bahwa test GAGAL (**RED**, exit 1).
    - Memulihkan kembali ID ke `jGyYuQf-GeE` dan memverifikasi test KEMBALI HIJAU (**GREEN**, exit 0).
    - Melaporkan rasio pembuktian `N/N → (N-1)/N → N/N`.
  - • **Observability & Telemetry:**
    - Menghasilkan laporan hasil pengujian unit test di Gradle test runner report.

---

### TASK-14: Platform Bridge Live Playback Verification, Handling Fallback Galat CDN, & Sinkronisasi Progress State (Android & iOS) — [#17](https://github.com/tomdwipo/oss-elearning/issues/17)
- **Layer:** `commonMain`, `androidMain`, `iosMain` (Platform Bridge & Presentation)
- **Kompleksitas:** High (5 SP)
- **Problem Statement:** Diperlukan verifikasi bahwa injeksi video ID riil dapat di-render secara aktif oleh YouTube Iframe API di dalam Android `WebView` dan iOS `WKWebView`, serta sistem tetap memiliki ketahanan (*resilience*) dengan menampilkan fallback banner dan dialog pelaporan jika CDN mengembalikan galat (skenario E2).
- **Technical Context:**
  - `03_TRD.md` §3.1 (Sequence Diagram & Non-Happy Path Scenarios E1..E5) & §5.1 (Component Diagram).
  - File Codebase:
    - [PlatformVideoPlayer.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt)
    - [PlatformVideoPlayer.android.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/androidMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.android.kt)
    - [PlatformVideoPlayer.ios.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/iosMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.ios.kt)
    - [VideoPlayerScreen.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Video terkurasi dimuat ke dalam kontainer iframe dengan parameter `autoplay=1&playsinline=1&rel=0&modestbranding=1&controls=1&enablejsapi=1`.
    - Player memancarkan callback `onReady` dan memulai stream video asli.
    - Heartbeat `onTimeUpdate` secara dinamis memperbarui slider posisi waktu tonton.
    - Ambang batas $\ge 85\%$ secara otomatis mencentang status topik selesai dan menyimpan ke `ProgressRepository`.
    - Penanganan Galat (E2): Ketika simulasi error code 100/101/150 dipicu, UI beralih menampilkan banner informatif *"Video materi sedang diperbarui"* dan tombol *"Laporkan Link"* membuka `ReportBrokenVideoDialog`.
  - • **UI/UX States & Tokens:**
    - Player tidak lagi berupa layar hitam kosongan; menampilkan poster frame dan kontrol interaktif.
    - Tombol *"↗ Tonton di YouTube"* mengarahkan ke tautan video riil `https://www.youtube.com/watch?v={videoId}`.
  - • **Observability & Telemetry:**
    - Trace ID valid dipropagasikan pada event `video_started` dan `video_completed`.
  - • **Product Analytics:**
    - Payload event telemetri mencakup `videoId`, `topic_id`, dan `course_id`.

---

### TASK-15: Verifikasi Packaging Dual Platform (`assembleDebug` APK & `linkDebugFrameworkIosSimulatorArm64`), Walkthrough Live Video Playback, dan Pengarsipan Evidence Dual-Platform — [#18](https://github.com/tomdwipo/oss-elearning/issues/18)
- **Layer:** `androidMain`, `iosMain`, Evidence Archive
- **Kompleksitas:** High (5 SP)
- **Problem Statement:** Diperlukan pembuktian empiris tanpa asumsi bahwa kedua platform (Android dan iOS) berhasil di-compile secara dual-platform, serta menghasilkan rekaman audio-visual live pemutaran video aktif di emulator Android Pixel 9 Pro dan simulator iOS iPhone 16 Pro untuk menghapus artefak video berlayar hitam sebelumnya.
- **Technical Context:**
  - `03_TRD.md` §6 (Gate 5) & §7 (Standar Pengarsipan Evidence Dual-Platform).
  - Target Arsip: `.docs/evidence/03/` dan `.docs/evidence/02/demo.mp4`.
- **Acceptance Criteria (AC):**
  - • **Build Artifact & Verification (Dual Platform Wajib):**
    - Android: `./gradlew assembleDebug` berhasil dengan exit code 0 dan menghasilkan `composeApp-debug.apk`.
    - iOS: `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` berhasil dengan exit code 0 dan menghasilkan bundle framework `ComposeApp.framework`.
  - • **Dual-Platform Evidence Archive:**
    - Menyimpan log eksekusi build dan test pada `.docs/evidence/03/console-evidence.txt`.
    - Menyimpan video walkthrough live playback di Android Emulator Pixel 9 Pro pada `.docs/evidence/03/android/demo.mp4`.
    - Menyimpan video walkthrough di iOS Simulator iPhone 16 Pro pada `.docs/evidence/03/ios/demo.mp4`.
    - Menyimpan tangkapan layar pemutar video aktif di kedua direktori `.docs/evidence/03/android/screenshots/` dan `.docs/evidence/03/ios/screenshots/`.
    - Memperbarui file video demo di `.docs/evidence/02/demo.mp4` dengan video pemutaran nyata tanpa layar hitam.
    - Menyusun berkas rangkuman `.docs/evidence/03/README.md` yang memuat tabel 7 Gerbang Verifikasi Lokal dan rasio Gate 3b.
