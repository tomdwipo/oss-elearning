# Task Implementation Plan: Detail View UI Parity & Dual-Platform Visual Verification (Modul 4)

**Nomor Dokumen:** 04_tasklist  
**Target Rilis / Milestone:** MVP v0.2.1  
**Dokumen Asesmen Terkait:** [Dual-Platform UI & Pixel-by-Pixel Assessment](../assesment/03_dual_platform_ui_pixel_assessment.md)  
**Dokumen PRD:** [PRD 03: Comprehensive Detail View Audit & Dual-Platform UI Parity](../prd/03_prd.md), [PRD 02: Video Player](../prd/02_prd.md), [PRD 00: Global MVP](../prd/00_prd.md)  
**Dokumen TRD:** [TRD 04: Detail View UI Parity, Layout Responsiveness, & Dual-Platform Visual Verification](../trd/04_trd.md)  
**Arsitektur Target:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP 1.7.3 / Kotlin 2.1.0)  
**Target Arsip Evidence:** `.docs/evidence/04/README.md`  

---

## 1. Ikhtisar & Ringkasan Task

Dokumen ini memetakan seluruh kebutuhan teknis dari **TRD 04** ke dalam rencana eksekusi terstruktur (*Task Implementation Plan*) yang mematuhi standar **Acceptance Criteria 4-Pilar Wajib** (Core Business Logic, UI/UX States & Tokens, Observability & Telemetry, dan Product Analytics) serta **Mandatori Dual Platform (Android & iOS)**.

| Task ID | Issue GitHub | Judul Task | Layer / Source Set | Kompleksitas | Dependensi |
|---|---|---|---|---|---|
| **TASK-16** | (Pending Gate 2) | Responsive Layout Refactoring & Non-Wrapping Badge Container on VideoPlayerScreen | `commonMain` (UI / Presentation) | Medium (3 SP) | - |
| **TASK-17** | (Pending Gate 2) | Scrollable Adaptive Layout & Spacing Fix on ReportBrokenVideoDialog | `commonMain` (UI / Presentation) | Medium (3 SP) | - |
| **TASK-18** | (Pending Gate 2) | Dual-Platform Unit Test Suite, Negative Control (Gate 3b), & Roborazzi Visual Parity Validation | `commonTest` (Quality / Testing) | Medium (3 SP) | TASK-16..17 |
| **TASK-19** | (Pending Gate 2) | Verifikasi Packaging Dual Platform (`assembleDebug` & `linkDebugFrameworkIosSimulatorArm64`), Walkthrough Live Demo, & Evidence Archiving | `androidMain`, `iosMain`, Evidence Archive | High (5 SP) | TASK-16..18 |

---

## 2. Rincian Task & Acceptance Criteria (4-Pillar + Dual Platform Wajib)

### TASK-16: Responsive Layout Refactoring & Non-Wrapping Badge Container on VideoPlayerScreen — (Pending Gate 2)
- **Layer:** `commonMain` (UI / Presentation)
- **Kompleksitas:** Medium (3 SP)
- **Problem Statement:** Pada layar iOS, teks badge status `"Belum Selesai"` tertekan oleh baris checklist di sebelah kiri akibat ketiadaan `weight(1f)` dan perbedaan lebar font SF Pro di iOS, sehingga terbungkus ke bawah per huruf secara vertikal (*letter-by-letter wrap*).
- **Technical Context:**
  - `04_TRD.md` §1.2 (Current Implementation) & §1.3 (Target Implementation).
  - `03_prd.md` §4 (FR-1: Responsivitas & Proteksi Layout Badge Status Progres).
  - File Codebase:
    - [`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)
    - [`Theme.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/theme/Theme.kt)
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Baris checklist kiri mengimplementasikan `Modifier.weight(1f).padding(end = 8.dp)`.
    - Teks label `"Tandai Selesai (Manual)"` menerapkan `maxLines = 1` dan `overflow = TextOverflow.Ellipsis`.
    - Teks status pada `Box` badge (`"Selesai"` dan `"Belum Selesai"`) dikunci dengan `maxLines = 1` dan `softWrap = false`.
  - • **UI/UX States & Tokens:**
    - State `"Belum Selesai"`: background `#F0F1F3`, teks `#8A90A2`, font `11.sp` Bold.
    - State `"Selesai"`: background `#27AE60` (alpha 12%), teks `#27AE60`, font `11.sp` Bold.
    - Checkbox: `22.dp` rounded `4.dp`, border `1.5.dp`.
  - • **Observability & Telemetry:**
    - Aksi klik checklist memicu log debug `[VideoPlayerScreen] Toggle manual completion for topic: $topicId`.
  - • **Product Analytics:**
    - Event tracking `topic_completion_toggled` dengan payload `{ course_id, topic_id, is_completed, source: "manual" }`.
  - • **Build & Platform Parity:**
    - Ter-render utuh horizontal pada Android (Pixel 9 Pro) dan iOS Simulator (iPhone 16 Pro).

---

### TASK-17: Scrollable Adaptive Layout & Spacing Fix on ReportBrokenVideoDialog — (Pending Gate 2)
- **Layer:** `commonMain` (UI / Presentation)
- **Kompleksitas:** Medium (3 SP)
- **Problem Statement:** Pada iOS target (Skiko `AlertDialog`), konten dialog yang melebihi batas kanvas vertikal memicu `OutlinedTextField` menabrak dan menindih baris radio button ke-4 (*"Lainnya"*), serta menyembunyikan label catatan opsional.
- **Technical Context:**
  - `04_TRD.md` §1.2 & §4.1.
  - `03_prd.md` §4 (FR-2: Scrollable & Adaptive Layout pada Dialog Pelaporan).
  - File Codebase:
    - [`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Kontainer `Column` di dalam slot `text` dialog mengimplementasikan `Modifier.fillMaxWidth().verticalScroll(rememberScrollState())`.
    - Spacing antar-elemen: `6.dp` di bawah label "Pilih Alasan Masalah", `4.dp` vertical padding per item radio button, `12.dp` spacer sebelum label catatan tambahan.
    - `OutlinedTextField` memiliki batas `maxLines = 3` dengan shape `RoundedCornerShape(8.dp)`.
  - • **UI/UX States & Tokens:**
    - Dialog container background: `GrayscaleWhite` (`#FFFFFF`), corner radius `16.dp`.
    - RadioButton colors: `selectedColor = CorporatePurple` (`#9D3FE7`), `unselectedColor = GrayscaleBorder` (`#E2E4E8`).
    - Tombol Kirim: `CorporatePurple` (`#9D3FE7`), corner radius `8.dp`.
    - Tombol Batal: `TextButton` dengan warna `GrayscaleHintText` (`#8A90A2`).
  - • **Observability & Telemetry:**
    - Pengiriman laporan mencatat log structured `[ReportBrokenVideoDialog] Submitting report reason: $reason, notes_length: ${notes.length}`.
  - • **Product Analytics:**
    - Event `broken_video_reported` dengan payload `{ topic_id, reason: reason.name, has_notes: notes.isNotEmpty() }`.
  - • **Build & Platform Parity:**
    - Tidak ada elemen yang tumpang tindih (*zero overlapping*) pada Android dan iOS.

---

### TASK-18: Dual-Platform Unit Test Suite, Negative Control (Gate 3b), & Roborazzi Visual Parity Validation — (Pending Gate 2)
- **Layer:** `commonTest` (Quality / Testing)
- **Kompleksitas:** Medium (3 SP)
- **Problem Statement:** Perluasan test suite untuk memvalidasi bahwa seluruh perbaikan UI state, penanganan non-happy path E1–E5, serta integrasi viewmodel tidak mengalami regresi pada platform JVM maupun Native iOS.
- **Technical Context:**
  - `04_TRD.md` §6 (Testing Requirements & 7 Local Verification Gates).
  - File Codebase:
    - `composeApp/src/commonTest/kotlin/org/opencampus/elearning/VideoPlayerViewModelTest.kt`
    - `composeApp/src/commonTest/kotlin/org/opencampus/elearning/CurriculumVideoContractTest.kt`
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Seluruh unit test di `commonTest` lolos 100% (`./gradlew test` $\to$ Exit 0).
    - Memvalidasi transisi state `VideoPlayerUiState` (isCompleted, isReportDialogOpen, isReportSubmittedSuccess).
  - • **Negative Control Validation (Gate 3b):**
    - Sengaja memutasikan satu logika UI state / contract $\to$ verifikasi unit test gagal (RED) $\to$ pulihkan $\to$ verifikasi lolos (GREEN).
    - Rasio pembuktian tercatat di log: `26/26 → 25/26 → 26/26`.
  - • **Roborazzi Visual Diff Matrix:**
    - Memverifikasi artefak komparasi 3-kolom `visual_diff_roborazzi` bebas dari glitch overlapping dan karakter vertikal.
  - • **Observability & Analytics:**
    - Trace ID dan telemetri terverifikasi pada level unit test via `TraceIdAndTelemetryTest`.

---

### TASK-19: Verifikasi Packaging Dual Platform (`assembleDebug` & `linkDebugFrameworkIosSimulatorArm64`), Walkthrough Live Demo, & Evidence Archiving — (Pending Gate 2)
- **Layer:** `androidMain`, `iosMain`, Evidence Archive
- **Kompleksitas:** High (5 SP)
- **Problem Statement:** Menjamin build packaging kedua target platform berhasil bersih tanpa warning/error, mendokumentasikan bukti eksekusi nyata pada Android Emulator Pixel 9 Pro dan iOS Simulator iPhone 16 Pro, serta memperbarui arsip evidence di `.docs/evidence/04/`.
- **Technical Context:**
  - `04_TRD.md` §7 (Standar Pengarsipan Evidence).
  - Tools: Gradle, Android CLI / ADB, `xcrun simctl`.
- **Acceptance Criteria (AC):**
  - • **Build Packaging Dual-Platform (Gate 5):**
    - Android: `./gradlew assembleDebug` menghasilkan `composeApp-debug.apk` (Exit 0).
    - iOS: `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` menghasilkan `ComposeApp.framework` (Exit 0).
  - • **Linter Gate 1:**
    - `./gradlew composeApp:lintDebug` $\to$ Exit 0 (0 error lint baru).
  - • **Dual-Platform Evidence Archiving:**
    - Menyimpan log eksekusi build di `.docs/evidence/04/console-evidence.txt`.
    - Menyimpan tangkapan layar Android di `.docs/evidence/04/android/screenshots/`.
    - Menyimpan rekaman walkthrough Android di `.docs/evidence/04/android/demo.mp4`.
    - Menyimpan tangkapan layar iOS (memperlihatkan badge horizontal & form dialog bebas overlap) di `.docs/evidence/04/ios/screenshots/`.
    - Menyimpan rekaman walkthrough iOS di `.docs/evidence/04/ios/demo.mp4`.
    - Membuat berkas ringkasan [`.docs/evidence/04/README.md`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/04/README.md).
