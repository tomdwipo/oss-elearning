# TRD 02: Video Player & Pelacakan Progres (Modul 2)

**Nomor Modul:** 02  
**Fitur Utama:** Pemutar Video Pembelajaran Bebas Distraksi, Kontrol Playback, Auto-Centang Progres ($\ge 85\%$), Atribusi YouTube, dan Pelaporan Video Rusak  
**Target Platform:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP)  
**Dokumen PRD Terkait:** [PRD 02: Video Player & Pelacakan Progres](../prd/02_prd.md)  
**Dokumen Induk PRD:** [PRD 00: Global MVP](../prd/00_prd.md)  
**Dokumen Master Desain:** [Design System UI Kit Free](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Dokumen TRD Sebelumnya:** [TRD 01: Navigasi Kurikulum & Silabus](./01_trd.md)  
**Target Arsip Evidence:** `.docs/evidence/02/README.md`  

---

## 1. Arsitektur & Tech Stack

- **Framework:** Kotlin Multiplatform (KMP) & Compose Multiplatform (CMP)
- **Target Platform:** Android (`androidMain`) & iOS (`iosMain`)
- **Shared Codebase:** `composeApp/src/commonMain/kotlin`
- **State Management & ViewModel:** Jetpack Compose / Lifecycle ViewModel Multiplatform (`androidx.lifecycle.viewmodel.compose`)
- **Local Persistence:** Multiplatform Settings KV Store (`com.russhwolf:multiplatform-settings`)
- **Embedded Player Engine:** 
  - Abstraksi `expect` / `actual` Composable `PlatformVideoPlayer`
  - **Android (`androidMain`):** `AndroidView` membungkus `WebView` berkecepatan tinggi dengan YouTube Iframe Player API v3 (`playsinline=1`, `rel=0`, `modestbranding=1`).
  - **iOS (`iosMain`):** `UIKitView` membungkus `WKWebView` dengan script bridge YouTube Iframe Player API.
- **External App Launcher:** `PlatformUriHandler` / `LocalUriHandler` Compose untuk membuka tautan YouTube resmi.
- **Design Tokens:**
  - `Corporate/Purple`: `#9D3FE7` (Warna aksen kontrol, slider thumb, active speed pill)
  - `Corporate/DarkPurple`: `#602093` (Border active, dark state)
  - `Informing/Approval`: `#00B998` (Status topik selesai / checklist auto-complete)
  - `Informing/Error`: `#D32F2F` (Status video rusak & tombol pelaporan link)
  - `Grayscale/Surface`: `#1E1E2E` (Warna latar pemutar video imersif)

```text
+-------------------------------------------------------------------------+
|                           PRESENTATION LAYER                            |
|  - VideoPlayerScreen (PlayerHeader, VideoContainer, SpeedBar, ActionRow)|
|  - ReportBrokenVideoDialog (ReasonRadioGroup, ErrorDescriptionField)   |
|  - VideoPlayerViewModel (StateFlow<VideoPlayerUiState>, PlayerEvents)   |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                              DOMAIN LAYER                               |
|  - Models: VideoPlaybackState, PlaybackSpeed, VideoReportPayload        |
|  - Logic: 85% Auto-Complete Evaluator, Playback Time Tracker            |
|  - Telemetry & Analytics: Trace ID Generator & Video Event Contracts    |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                               DATA LAYER                                |
|  - ProgressRepository: Multiplatform Settings KV Store (Topic Progress) |
|  - VideoReportRepository: Local Logging & Mock HTTP Dispatcher          |
|  - CurriculumRepository: Query Topic & Video Metadata                   |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                     PLATFORM BRIDGE (expect / actual)                   |
|  - PlatformVideoPlayer (Android WebView vs iOS WKWebView Iframe Bridge) |
|  - PlatformUrlLauncher (Open YouTube App / Browser External Intent)     |
+-------------------------------------------------------------------------+
```

---

## 2. Sequence Diagram (Propagasi Trace ID & Alur Eksekusi)

Setiap aksi pemutaran dan pelaporan menghasilkan `trace_id` dengan format terstandar:  
`trc_<action>_<timestamp>_<hex>` (contoh: `trc_vid_play_1725542000_c4d5e6`).

### 2.1. Alur Membuka Video Player & Inisialisasi Pemutar
```text
User           TopicItem (Syllabus)      SemesterVM / PlayerVM       CurriculumRepo     PlatformVideoPlayer    AnalyticsService
 |                     |                         |                         |                    |                     |
 | 1. Tap Topik 01     |                         |                         |                    |                     |
 |-------------------->|                         |                         |                    |                     |
 |                     | 2. onTopicSelected()    |                         |                    |                     |
 |                     |------------------------>|                         |                    |                     |
 |                     |                         | 3. Gen Trace ID: `trc_vid_open_xxx`          |                     |
 |                     |                         |--+                      |                    |                     |
 |                     |                         |<-+                      |                    |                     |
 |                     |                         |                         |                    |                     |
 |                     |                         | 4. getTopic(topicId)    |                    |                     |
 |                     |                         |------------------------>|                    |                     |
 |                     |                         | 5. Return Topic Details |                    |                     |
 |                     |                         |<------------------------|                    |                     |
 |                     |                         |                                              |                     |
 |                     |                         | 6. Navigate: ScreenDestination.VideoPlayer   |                     |
 |                     |                         |    emit(VideoPlayerUiState)                  |                     |
 |                     |                         |--------------------------------------------->|                     |
 |                     |                         |                                              | 7. Init Iframe      |
 |                     |                         |                                              |    Embed HTML       |
 |                     |                         | 8. trackEvent("video_started", trace_id)     |                     |
 |                     |                         |------------------------------------------------------------------->|
 |                     |                         |                                              |                     |
 |                     |                         | 9. OnPlayerReady Callback                    |                     |
 |                     |                         |<---------------------------------------------|                     |
```

### 2.2. Alur Pemutaran Video, Heartbeat Progress, & Auto-Centang ($\ge 85\%$)
```text
PlatformVideoPlayer          PlayerVM / StateFlow          ProgressRepository          UI (Screen)           Analytics
        |                            |                             |                        |                    |
        | 1. onTimeUpdate(sec, dur)  |                             |                        |                    |
        |--------------------------->|                             |                        |                    |
        |                            | 2. Calculate % Watched      |                        |                    |
        |                            |    (e.g., 85.2%)            |                        |                    |
        |                            |--+                          |                        |                    |
        |                            |<-+                          |                        |                    |
        |                            |                             |                        |                    |
        |                            | 3. Check Condition:         |                        |                    |
        |                            |    pct >= 85% && !isDone    |                        |                    |
        |                            |--+                          |                        |                    |
        |                            |<-+                          |                        |                    |
        |                            |                             |                        |                    |
        |                            | 4. setTopicCompleted(id, true)                       |                    |
        |                            |---------------------------->|                        |                    |
        |                            | 5. Ack Persistent Save      |                        |                    |
        |                            |<----------------------------|                        |                    |
        |                            |                                                      |                    |
        |                            | 6. emit(isCompleted = true, autoCompleted = true)    |                    |
        |                            |----------------------------------------------------->|                    |
        |                            |                                                      | 7. Render Checkbox |
        |                            |                                                      |    [X] Selesai     |
        |                            | 8. trackEvent("video_completed", is_manual=false)    |                    |
        |                            |-------------------------------------------------------------------------->|
```

### 2.3. Alur Penanganan Video Rusak & Pelaporan Link (Fallback)
```text
User           PlatformVideoPlayer          PlayerVM                 VideoReportRepo           AnalyticsService
 |                     |                        |                           |                         |
 |                     | 1. onError(code: 150)  |                           |                         |
 |                     |----------------------->|                           |                         |
 |                     |                        | 2. emit(isError = true)   |                         |
 |                     |                        |--+                        |                         |
 |                     |                        |<-+                        |                         |
 |                     |                        | 3. trackEvent("video_error_encountered")           |
 |                     |                        |---------------------------------------------------->|
 |                     |                        |                                                     |
 | 4. Tampil Banner Fallback & Tombol "Laporkan Link"                                                 |
 |    (UI: "Video materi sedang diperbarui")                                                          |
 | 5. Tap "Laporkan"   |                        |                                                     |
 |--------------------------------------------->|                                                     |
 |                     |                        | 6. Buka Dialog Pelaporan Link                       |
 | 7. Pilih Alasan & Submit                     |                                                     |
 |--------------------------------------------->|                                                     |
 |                     |                        | 8. Gen Trace ID: `trc_rep_send_xxx`                 |
 |                     |                        | 9. submitReport(payload, trace_id)                  |
 |                     |                        |-------------------------->|                         |
 |                     |                        | 10. Ack Success           |                         |
 |                     |                        |<--------------------------|                         |
 |                     |                        | 11. trackEvent("video_link_reported")               |
 |                     |                        |---------------------------------------------------->|
 |                     |                        | 12. Tutup Modal & Tampilkan Toast Konfirmasi        |
```

---

## 3. State Machine & MVI Lifecycle Diagram

```text
                           +------------------------+
                           |   SYLLABUS_SELECTION   |
                           +------------------------+
                                        |
                                        | (Tap Topic Pertemuan)
                                        v
                           +------------------------+
                           |  PLAYER_INITIALIZING   | <-------------------------+
                           | (Loading Iframe/Media) |                           |
                           +------------------------+                           |
                                  /          \                                  |
            (Media Error / 100/150)          (OnReady / Play)                   |
                                /              \                                |
                               v                v                               |
                 +-------------------+    +--------------------+                |
                 | VIDEO_ERROR_STATE |    |   VIDEO_PLAYING    | <-------+      |
                 | (Fallback Banner) |    |  (Tracking Progress|         |      |
                 +-------------------+    +--------------------+         |      |
                    |              |         |            |              |      |
         (Tap Report|  (Tap YouTube|  (Pause)|    (Scrub) |   (Speed Chg)|      |
            Link)   |   Atribusi)  |         v            v              |      |
                    |              |      +--------------------+         |      |
                    |              |      |    VIDEO_PAUSED    | --------+      |
                    |              |      +--------------------+                |
                    |              |               |                            |
                    |              |       (Watch Progress >= 85%)              |
                    |              |               v                            |
                    |              |      +--------------------+                |
                    |              |      |   AUTO_COMPLETED   |                |
                    |              |      | (Checkbox Checked) |                |
                    |              |      +--------------------+                |
                    |              |               |                            |
                    v              v               |                            |
        +-------------------+  +------------------+|                            |
        | REPORT_MODAL_OPEN |  | LAUNCH_EXT_URL   ||                            |
        +-------------------+  +------------------+|                            |
           |             |                         |                            |
    (Submit Report)  (Batal)                       | (Tap Back Arrow)           |
           v             |                         v                            |
        +-------------------+             +------------------------+            |
        | REPORT_SUBMITTED  |             |  RETURN_TO_SYLLABUS    | -----------+
        +-------------------+             +------------------------+
```

---

## 4. Kontrak Data Model & Antarmuka Teknis (Kotlin Multiplatform)

### 4.1. Domain & Presentation Models
```kotlin
package org.opencampus.elearning.domain.model

import kotlinx.serialization.Serializable

enum class PlaybackSpeed(val speedMultiplier: Float, val label: String) {
    SPEED_0_75X(0.75f, "0.75x"),
    SPEED_1_0X(1.0f, "1x"),
    SPEED_1_25X(1.25f, "1.25x"),
    SPEED_1_5X(1.5f, "1.5x"),
    SPEED_2_0X(2.0f, "2x")
}

enum class ReportReason(val id: String, val label: String) {
    DELETED_OR_PRIVATE("deleted_private", "Video Dihapus / Private"),
    NOT_RELEVANT("not_relevant", "Video Tidak Sesuai Topik"),
    AUDIO_VISUAL_BROKEN("broken_media", "Audio / Visual Rusak"),
    OTHER("other", "Lainnya")
}

@Serializable
data class VideoReportPayload(
    val reportId: String,
    val topicId: String,
    val courseId: String,
    val videoId: String,
    val reason: String,
    val notes: String = "",
    val timestampEpochMs: Long,
    val traceId: String
)

data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val courseId: String = "",
    val topic: Topic? = null,
    val isCompleted: Boolean = false,
    val currentTimeSeconds: Float = 0f,
    val totalDurationSeconds: Float = 0f,
    val playbackSpeed: PlaybackSpeed = PlaybackSpeed.SPEED_1_0X,
    val isPlaying: Boolean = false,
    val isReportDialogOpen: Boolean = false,
    val isReportSubmittedSuccess: Boolean = false
)
```

### 4.2. Navigasi & Destinasi Layar Baru
Penambahan parameter destinasi pada `ScreenDestination`:
```kotlin
sealed interface ScreenDestination {
    object Home : ScreenDestination
    data class Syllabus(val courseId: String) : ScreenDestination
    data class VideoPlayer(val courseId: String, val topicId: String) : ScreenDestination
}
```

### 4.3. Platform Bridge (`expect` / `actual`) untuk Video Player
```kotlin
// File: commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt
@Composable
expect fun PlatformVideoPlayer(
    videoId: String,
    playbackSpeed: Float,
    isPlaying: Boolean,
    onProgressUpdate: (currentSec: Float, totalSec: Float) -> Unit,
    onError: (errorCode: Int, message: String) -> Unit,
    modifier: Modifier = Modifier
)
```

---

## 5. Ringkasan Task & Acceptance Criteria (AC)

Mengikuti penomoran sekuensial setelah Modul 1 (TASK-00 s.d. TASK-05):

| Task ID | Judul Task | Source Set / Layer | Milestone | Dependensi |
|---|---|---|---|---|
| **TASK-06** | Data models Video Player, Report Reason, dan kontrak navigasi `ScreenDestination.VideoPlayer` | `commonMain` (Domain/Model) | MVP v0.1 | Modul 1 (`#7`) |
| **TASK-07** | Abstraksi `expect/actual` `PlatformVideoPlayer` (Android WebView & iOS WKWebView iframe bridge) | `commonMain`, `androidMain`, `iosMain` | MVP v0.1 | TASK-06 |
| **TASK-08** | Implementasi `VideoPlayerScreen` lengkap dengan Player Controls, Speed Bar, dan Atribusi YouTube | `commonMain` (UI/CMP) | MVP v0.1 | TASK-07 |
| **TASK-09** | Logika Auto-Centang Progres ($\ge 85\%$) & Sinkronisasi Manual Checkbox ke `ProgressRepository` | `commonMain` (ViewModel/Data) | MVP v0.1 | TASK-08 |
| **TASK-10** | Fallback UI untuk Video Rusak & `ReportBrokenVideoDialog` (Pelaporan Link Rusak) | `commonMain` (UI/Data) | MVP v0.1 | TASK-08 |
| **TASK-11** | Test Suite `VideoPlayerTest` di `commonTest`, Negative Control (Gate 3b), dan Verifikasi APK | `commonTest` (Testing) | MVP v0.1 | TASK-06..10 |

---

## 6. Detail Acceptance Criteria per Task

### 🎯 TASK-06: Kontrak Model & Navigasi
- **AC-6.1:** `ScreenDestination.VideoPlayer(courseId, topicId)` terdaftar dan didukung pada back-navigation stack.
- **AC-6.2:** `VideoReportPayload`, `PlaybackSpeed`, dan `ReportReason` teruji serializable tanpa runtime crash.
- **AC-6.3:** Trace ID format `trc_vid_*` dan telemetry event `video_started` terdefinisi.

### 🎯 TASK-07: Platform Bridge Pemutar YouTube
- **AC-7.1:** Pemutar merender iframe resmi YouTube dengan parameter:  
  `autoplay=1&playsinline=1&rel=0&modestbranding=1&controls=1&enablejsapi=1`.
- **AC-7.2:** Tidak ada rekomendasi video sampingan (*related videos* terkunci pada channel yang sama / dinonaktifkan).
- **AC-7.3:** Callback JavaScript mengirimkan perubahan waktu (*timeupdate*) dan state player ke Kotlin.

### 🎯 TASK-08: UI Pemutar Video & Atribusi Resmi
- **AC-8.1:** Menampilkan judul materi, channel creator, dan durasi sesuai standar token typography Poppins.
- **AC-8.2:** Selector kecepatan pemutaran (0.75x, 1x, 1.25x, 1.5x, 2x) mengirimkan perintah langsung ke player engine.
- **AC-8.3:** Tombol *"Tonton di YouTube"* membuka canonical URL (`https://www.youtube.com/watch?v={videoId}`) melalui peramban luar / aplikasi YouTube native.

### 🎯 TASK-09: Auto-Centang ($\ge 85\%$) & Sinkronisasi Progres
- **AC-9.1:** Saat `currentTime / totalDuration >= 0.85`, sistem otomatis menandai topik sebagai selesai di local storage.
- **AC-9.2:** Icon status berubah menjadi hijau (`#00B998`) dan memicu pembaruan reaktif pada `HeroProgressCard` di Beranda.
- **AC-9.3:** Pengguna tetap dapat mencentang / membatalkan centang secara manual kapan saja tanpa terhambat auto-centang.

### 🎯 TASK-10: Fallback Video Error & Pelaporan Link Rusak
- **AC-10.1:** Jika player menangkap error (kode 100, 101, 150), tampilan player digantikan oleh banner informatif:  
  *"Video materi sedang diperbarui"* disertai tombol *"Laporkan Link"*.
- **AC-10.2:** Dialog laporan memuat pilihan radio: *Video Dihapus/Private*, *Video Tidak Sesuai*, *Audio/Visual Rusak*.
- **AC-10.3:** Laporan tersimpan di storage lokal / mock service dan memicu telemetry `video_link_reported`.

### 🎯 TASK-11: Testing & 7 Gerbang Verifikasi Lokal
- **AC-11.1:** 100% unit tests di `commonTest` lolos (`./gradlew test`).
- **AC-11.2:** Negative control mutasi ambang batas auto-centang ($85\% \to 95\%$) terbukti menghasilkan status MERAH, lalu HIJAU saat dipulihkan.
- **AC-11.3:** Build APK Android lolos via `./gradlew assembleDebug`.

---

## 7. Standar 7 Gerbang Verifikasi Lokal (Verification Gates)

| Gerbang | Uji Validasi | Kriteria Keberhasilan |
|---|---|---|
| **1. Linters & Syntax** | `./gradlew check -x test` | Exit code 0, nol error detektif sintaksis. |
| **2. Contract & Schema** | `./gradlew test --tests "*VideoReport*"` | Skema payload laporan dan trace ID W3C valid. |
| **3a. Unit Tests** | `./gradlew test` | Semua unit test di `commonTest` lulus hijau. |
| **3b. Negative Control** | Mutasi threshold auto-centang $85\% \to 95\%$ | Wajib MERAH saat diinjeksi, wajib HIJAU saat dikembalikan. |
| **4. UI & Token Audit** | Inspeksi Viewport 375x812 dp | Sesuai token `Corporate/Purple`, `Informing/Approval`, `Informing/Error`. |
| **5. Build Artifact** | `./gradlew assembleDebug` | Berhasil memproduksi `composeApp-debug.apk`. |
| **6. Secret Hygiene** | `git diff origin/main` | Bebas dari API key privat YouTube atau kredensial backend. |
