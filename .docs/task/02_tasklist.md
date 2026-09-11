# Task Implementation Plan: Video Player & Pelacakan Progres (Modul 2)

**Nomor Dokumen:** 02_tasklist  
**Target Rilis:** MVP v0.1  
**Dokumen PRD:** [PRD 02: Video Player & Pelacakan Progres](../prd/02_prd.md)  
**Dokumen TRD:** [TRD 02: Video Player & Pelacakan Progres](../trd/02_trd.md)  
**Arsitektur Target:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP)  
**Target Arsip Evidence:** `.docs/evidence/02/README.md`  

---

## 1. Ikhtisar & Ringkasan Task

Dokumen ini memetakan seluruh kebutuhan teknis dari **TRD 02** ke dalam rencana kerja terstruktur (*Task Implementation Plan*) yang menerapkan standar **Acceptance Criteria 4-Pilar Wajib** (Core Business Logic, UI/UX States & Tokens, Observability & Telemetry, dan Product Analytics).

| Task ID | Issue GitHub | Judul Task | Layer / Source Set | Kompleksitas | Dependensi |
|---|---|---|---|---|---|
| **TASK-06** | *TBD* | Data Models Video Player, Report Reason, dan Kontrak Navigasi `ScreenDestination.VideoPlayer` | `commonMain` (Domain/Model) | Low | Modul 1 (`#7`) |
| **TASK-07** | *TBD* | Platform Bridge `expect/actual` `PlatformVideoPlayer` (Android WebView & iOS WKWebView Iframe) | `commonMain`, `androidMain`, `iosMain` | High | TASK-06 |
| **TASK-08** | *TBD* | Implementasi `VideoPlayerScreen` dengan Player Controls, Speed Bar, dan Atribusi YouTube | `commonMain` (Presentation/CMP) | Medium | TASK-07 |
| **TASK-09** | *TBD* | Logika Auto-Centang Progres ($\ge 85\%$) & Sinkronisasi Manual Checkbox ke `ProgressRepository` | `commonMain` (Domain/Data) | Medium | TASK-08 |
| **TASK-10** | *TBD* | Fallback UI untuk Video Rusak & `ReportBrokenVideoDialog` (Pelaporan Link Rusak) | `commonMain` (Presentation/Data) | Medium | TASK-08 |
| **TASK-11** | *TBD* | Unit Tests di `commonTest`, Negative Control (Gate 3b), dan Verifikasi APK Debug | `commonTest` (Quality/Testing) | Medium | TASK-06..10 |

---

## 2. Rincian Task & Acceptance Criteria (4-Pilar Wajib)

### TASK-06: Data Models Video Player, Report Reason, dan Kontrak Navigasi `ScreenDestination.VideoPlayer`
- **Layer:** Domain & Presentation Layer (`commonMain`)
- **Kompleksitas:** Low
- **Problem Statement:** Diperlukan representasi model strongly-typed untuk state pemutaran video, konfigurasi kecepatan (*speed multiplier*), alasan pelaporan link rusak, data payload pelaporan, serta penambahan destinasi layar `ScreenDestination.VideoPlayer` pada state navigasi `SemesterViewModel` dan `App.kt`.
- **Technical Context:**
  - `02_TRD.md` §4.1 (Domain Models) & §4.2 (ScreenDestination).
  - `02_PRD.md` §2 (Ruang Lingkup) & §3.1 (Use Cases).
  - File referensi: [Models.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/domain/model/Models.kt), [SemesterViewModel.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/SemesterViewModel.kt), [App.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/App.kt).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Model Kotlin `@Serializable`: `PlaybackSpeed`, `ReportReason`, dan `VideoReportPayload` terdefinisi di `domain/model/Models.kt`.
    - Enum `PlaybackSpeed` mencakup: `SPEED_0_75X (0.75f)`, `SPEED_1_0X (1.0f)`, `SPEED_1_25X (1.25f)`, `SPEED_1_5X (1.5f)`, dan `SPEED_2_0X (2.0f)`.
    - Enum `ReportReason` mencakup: `DELETED_OR_PRIVATE`, `NOT_RELEVANT`, `AUDIO_VISUAL_BROKEN`, dan `OTHER`.
    - `ScreenDestination.VideoPlayer(val courseId: String, val topicId: String)` terintegrasi dalam sealed interface `ScreenDestination`.
    - Pemanggilan `navigateBack()` saat berada di `ScreenDestination.VideoPlayer` mengarahkan pengguna kembali ke `ScreenDestination.Syllabus(courseId)`.
  - • **UI/UX States & Tokens:**
    - State representasi `VideoPlayerUiState` memuat flag status: `isLoading`, `isError`, `isPlaying`, `isCompleted`, `isReportDialogOpen`, dan `isReportSubmittedSuccess`.
  - • **Observability & Telemetry:**
    - Transisi navigasi menuju layar pemutar video menghasilkan Trace ID dengan format `trc_vid_open_<timestamp>_<hex>`.
    - Skenario penanganan pembatalan navigasi/back-press (Skenario E4) tidak meninggalkan memory leak atau state coroutine menggantung.
  - • **Product Analytics:**
    - Mempersiapkan payload terstruktur untuk event `video_started` yang memuat `topic_id`, `course_id`, `video_id`, dan `trace_id`.

---

### TASK-07: Platform Bridge `expect/actual` `PlatformVideoPlayer` (Android WebView & iOS WKWebView Iframe)
- **Layer:** Platform Bridge Layer (`commonMain`, `androidMain`, `iosMain`)
- **Kompleksitas:** High
- **Problem Statement:** Diperlukan komponen pemutar video YouTube tersemat (*embedded*) lintas platform di Compose Multiplatform yang bebas dari elemen distraksi eksternal (rekomendasi video lain, kolom komentar, shorts) serta memiliki jembatan komunikasi dua arah (*bidirectional bridge*) untuk progress tracking dan error handling.
- **Technical Context:**
  - `02_TRD.md` §1 (Arsitektur & Tech Stack) & §4.3 (Platform Bridge).
  - `02_PRD.md` §4 (FR-2.1: Embedded Player tanpa rekomendasi luar).
  - Parameter YouTube Iframe: `autoplay=1&playsinline=1&rel=0&modestbranding=1&controls=1&enablejsapi=1`.
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Deklarasi Composable `expect fun PlatformVideoPlayer` di `commonMain`.
    - Implementasi `actual fun PlatformVideoPlayer` di `androidMain` menggunakan `AndroidView` membungkus `WebView` berkecepatan tinggi dengan JavaScript enabled dan YouTube Iframe HTML.
    - Implementasi `actual fun PlatformVideoPlayer` di `iosMain` menggunakan `UIKitView` membungkus `WKWebView` dengan script bridge Iframe API.
    - Callback Kotlin menerima pembaruan berkala `onProgressUpdate(currentSec: Float, totalSec: Float)` saat video diputar.
    - Callback Kotlin menerima sinyal error `onError(errorCode: Int, message: String)` ketika video berstatus private/dihapus (Error 100, 101, 150 - Skenario E2).
    - Kontrol kecepatan pemutaran (`playbackSpeed`) diteruskan secara dinamis ke player melalui eksekusi JavaScript `player.setPlaybackRate(speed)`.
  - • **UI/UX States & Tokens:**
    - Aspek rasio video tetap terjaga 16:9 (`Modifier.aspectRatio(16f / 9f)`).
    - Background container pemutar video menggunakan warna latar gelap imersif `Grayscale/Surface` (`#1E1E2E`).
  - • **Observability & Telemetry:**
    - Seluruh event status player (Ready, Playing, Paused, Ended, Error) tercatat dalam debug logging.
  - • **Product Analytics:** N/A (Platform Bridge Layer).

---

### TASK-08: Implementasi `VideoPlayerScreen` dengan Player Controls, Speed Bar, dan Atribusi YouTube
- **Layer:** Presentation Layer (`commonMain`)
- **Kompleksitas:** Medium
- **Problem Statement:** Pengguna memerlukan antarmuka layar pemutar materi belajar yang menyajikan kontrol minimalis (Speed controller bar), header navigasi kembali, metadata materi (nomor pertemuan, judul topik, channel kreator, durasi estimasi), tombol atribusi resmi *"Tonton di YouTube"*, dan tombol aksi pelaporan link.
- **Technical Context:**
  - `02_PRD.md` §4 (FR-2.2: Minimalist Controls, FR-2.3: Atribusi YouTube) & §5 (Lo-Fi Wireframe).
  - `02_TRD.md` §1 & §3 (State Machine).
  - [DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Composable `VideoPlayerScreen` merender `PlatformVideoPlayer` dan memuat informasi topik aktif secara akurat dari `CurriculumRepository`.
    - Tombol selector kecepatan (`0.75x`, `1x`, `1.25x`, `1.5x`, `2x`) mengubah state `playbackSpeed` dan langsung memperbarui kecepatan pemutaran.
    - Tombol *"Tonton di YouTube"* membuka canonical URL YouTube (`https://www.youtube.com/watch?v={videoId}`) via `LocalUriHandler`.
    - Tombol kembali (*Back*) di top bar memanggil `viewModel.navigateBack()` untuk kembali ke layar silabus.
  - • **UI/UX States & Tokens:**
    - Standar viewport target 375x812 dp (iPhone X / Android compact layout).
    - Top bar menampilkan judul materi dan tombol kembali dengan ikon chevron ungu `Corporate/Purple` (`#9D3FE7`).
    - Selector kecepatan menggunakan pill tabs dengan active state border dan background warna ungu `Corporate/Purple`.
    - Tipografi judul menggunakan font Poppins SemiBold, nama channel kreator menggunakan Poppins Medium dengan ikon channel terverifikasi.
  - • **Observability & Telemetry:**
    - Propagasi Trace ID `trc_vid_play_<timestamp>_<hex>` pada saat inisialisasi pemutaran.
  - • **Product Analytics:**
    - Mengirim event `video_started`: `{ video_id, course_id, topic_id, speed, trace_id }`.
    - Mengirim event `external_youtube_opened`: `{ video_id, canonical_url }` saat tombol atribusi ditekan.

---

### TASK-09: Logika Auto-Centang Progres ($\ge 85\%$) & Sinkronisasi Manual Checkbox ke `ProgressRepository`
- **Layer:** Domain & Data Integration Layer (`commonMain`)
- **Kompleksitas:** Medium
- **Problem Statement:** Diperlukan logika bisnis untuk secara otomatis mencentang topik sebagai "Selesai" saat durasi video yang ditonton mencapai ambang batas $\ge 85\%$, sekaligus memberikan fleksibilitas bagi pengguna untuk mencentang atau membatalkan centang secara manual di bawah player.
- **Technical Context:**
  - `02_PRD.md` §4 (FR-3.1: Auto-centang $\ge 85\%$, FR-3.2: Manual Checkbox, FR-3.3: Storage Sync).
  - `02_TRD.md` §2.2 (Sequence Auto-Centang).
  - File referensi: [ProgressRepository.kt](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/data/ProgressRepository.kt).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Listener `onProgressUpdate` menghitung persentase tontonan: `(currentTimeSeconds / totalDurationSeconds) * 100`.
    - Jika persentase $\ge 85.0\%$ dan status topik belum selesai:
      - Sistem secara otomatis memanggil `progressRepository.setTopicCompleted(topicId, true)`.
      - State checkbox langsung berubah menjadi checked reaktif tanpa reload layar.
      - Logika bersifat idempoten (tidak memicu penulisan berulang jika status sudah completed).
    - Checkbox manual di bawah player dapat diklik untuk toggle (selesai / belum selesai) secara bebas.
    - Status progres tersimpan persisten di KV storage dan memperbarui progres semester di `HeroProgressCard`.
  - • **UI/UX States & Tokens:**
    - Checkbox menggunakan token warna sukses `Informing/Approval` (`#00B998`).
    - Label checkbox menampilkan status yang jelas: *"Tandai Selesai (Manual)"* dengan badge status *"Selesai"* warna hijau.
  - • **Observability & Telemetry:**
    - Trace ID `trc_vid_comp_<timestamp>_<hex>` disertakan pada saat auto-centang terpicu.
    - Graceful fallback jika terjadi kegagalan penyimpanan lokal (Skenario E5).
  - • **Product Analytics:**
    - Mengirim event `video_completed`: `{ video_id, is_manual: false, duration_watched, percentage }` saat auto-centang.
    - Mengirim event `video_completed`: `{ video_id, is_manual: true, duration_watched, percentage }` saat toggle manual.

---

### TASK-10: Fallback UI untuk Video Rusak & `ReportBrokenVideoDialog` (Pelaporan Link Rusak)
- **Layer:** Presentation & Data Layer (`commonMain`)
- **Kompleksitas:** Medium
- **Problem Statement:** Jika video materi mengalami kerusakan teknis, dihapus oleh pembuat konten di YouTube, atau disetel ke private, sistem harus menyediakan fallback informatif *"Video materi sedang diperbarui"* serta dialog modal pelaporan link rusak agar kurator dapat segera memperbarui tautan materi.
- **Technical Context:**
  - `02_PRD.md` §4 (FR-3.4: Modal Pelaporan Link) & §5.2 (Penanganan Error Utama).
  - `02_TRD.md` §2.3 (Sequence Pelaporan Link) & §4.1 (`VideoReportPayload`, `ReportReason`).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Saat callback error dari player aktif (`errorCode` 100, 101, 150):
      - Area video menampilkan kartu fallback informatif: *"Video materi sedang diperbarui"*.
      - Menyajikan tombol aksi *"Laporkan Link"*.
    - Menekan tombol *"Laporkan Link"* membuka modal dialog `ReportBrokenVideoDialog`.
    - Form dialog menyediakan pilihan alasan pelaporan:
      1. Video Dihapus / Private (`DELETED_OR_PRIVATE`)
      2. Video Tidak Sesuai Topik (`NOT_RELEVANT`)
      3. Audio / Visual Rusak (`AUDIO_VISUAL_BROKEN`)
      4. Lainnya (`OTHER`) disertai field catatan opsional.
    - Menekan *"Kirim Laporan"* mengemas `VideoReportPayload`, menyimpannya ke repository lokal / mock dispatcher, menutup modal dialog, dan menampilkan snackbar/toast konfirmasi sukses.
  - • **UI/UX States & Tokens:**
    - Banner fallback menggunakan background lembut dengan aksen error `Informing/Error` (`#D32F2F`).
    - Modal dialog menggunakan `AlertDialog` dengan corner radius 16dp dan elevation 8dp.
    - Tombol *"Kirim Laporan"* menggunakan tombol primer ungu `Corporate/Purple` (`#9D3FE7`).
  - • **Observability & Telemetry:**
    - Propagasi Trace ID `trc_rep_send_<timestamp>_<hex>` pada pengiriman laporan.
  - • **Product Analytics:**
    - Mengirim event `video_error_encountered`: `{ video_id, error_code, error_message }`.
    - Mengirim event `video_link_reported`: `{ video_id, course_id, reason, notes, trace_id }`.

---

### TASK-11: Unit Tests di `commonTest`, Negative Control (Gate 3b), dan Verifikasi APK Debug
- **Layer:** Quality Assurance & Testing Layer (`commonTest` & Build Script)
- **Kompleksitas:** Medium
- **Problem Statement:** Seluruh fungsionalitas baru Modul 2 harus divalidasi oleh unit test otomatis di `commonTest`, memenuhi bukti kontrol negatif (Negative Control Gate 3b), dan terverifikasi bersih saat kompilasi APK Android Debug.
- **Technical Context:**
  - `02_TRD.md` §7 (Standar 7 Gerbang Verifikasi Lokal).
  - [Evidence 01 README.md](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/01/README.md) sebagai benchmark verifikasi.
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Test suite `VideoPlayerViewModelTest` memverifikasi:
      1. Evaluasi batas auto-centang: nilai 84.9% tidak memicu selesai, nilai 85.0% sukses memicu selesai.
      2. Toggle manual checkbox memperbarui status persisten di KV storage.
      3. Penanganan error player mengaktifkan state fallback secara benar.
      4. Pengiriman laporan link rusak menghasilkan payload yang valid.
    - **Negative Control (Gerbang 3b Wajib):**
      - Mutasi logika ambang batas auto-centang (ubah threshold dari `85%` menjadi `95%` atau `+ 10`).
      - Jalankan `./gradlew test` $\to$ buktikan **MERAH** (Exit code 1, test auto-centang gagal).
      - Pulihkan logika ke kondisi normal $\to$ jalankan `./gradlew test` $\to$ buktikan **HIJAU** (Exit code 0, semua test lulus).
      - Dokumentasikan rasio pembuktian `N/N → (N-1)/N → N/N`.
  - • **UI/UX States & Tokens:**
    - Verifikasi render layout pada standar viewport 375x812 dp bebas dari overlap/clipping.
  - • **Observability & Telemetry:**
    - Verifikasi format Trace ID W3C pattern `trc_vid_*` dan `trc_rep_*`.
  - • **Product Analytics:**
    - Verifikasi skema JSON untuk 5 telemetry events baru Modul 2.
  - • **Build Artifact:**
    - Perintah `./gradlew assembleDebug` berhasil dengan exit code 0 dan menghasilkan APK `composeApp-debug.apk`.
