# Task Implementation Plan — Modul 1: Navigasi Kurikulum (KMP / CMP Edition)

**Arsitektur & Tech Stack:**
- **Framework:** Kotlin Multiplatform (KMP) & Compose Multiplatform (CMP)
- **Target Platform:** Android (`androidMain`) & iOS (`iosMain`)
- **Shared Codebase:** `composeApp/src/commonMain/kotlin`
- **Serialization:** `kotlinx.serialization` (JSON)
- **State Management & ViewModel:** Jetpack Compose / Lifecycle ViewModel Multiplatform (`androidx.lifecycle.viewmodel.compose`)
- **Local Persistence:** Multiplatform Settings / KV Storage (in `commonMain`)
- **Design Tokens:** Design System & UI Kit Free (`Corporate/Purple` `#9D3FE7`, `Informing/Approval` `#00B998`, Poppins typography)

**Dokumen Rujukan:**
- PRD: [`.docs/00_fase/01_PRD.md`](../00_fase/01_PRD.md)
- Fase 1 (Use Case): [`.docs/01_fase/01_USE_CASE.md`](../01_fase/01_USE_CASE.md)
- Fase 1 (Activity Diagram): [`.docs/01_fase/02_ACTIVITY_DIAGRAM.md`](../01_fase/02_ACTIVITY_DIAGRAM.md)
- Fase 3 (Sequence Diagram): [`.docs/02_fase_paralel/01_module/system/01_MODULE_1_SEQUENCE_DIAGRAM.md`](../02_fase_paralel/01_module/system/01_MODULE_1_SEQUENCE_DIAGRAM.md)
- Fase 3 (State Diagram): [`.docs/02_fase_paralel/01_module/system/02_MODULE_1_STATE_DIAGRAM.md`](../02_fase_paralel/01_module/system/02_MODULE_1_STATE_DIAGRAM.md)
- Design Tokens: [`.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md`](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)
- Hi-Fi Prototype: [`.docs/02_fase_paralel/01_module/uiux/04_MODULE_1_HIFI_PROTOTYPE_USABILITY_TEST.md`](../02_fase_paralel/01_module/uiux/04_MODULE_1_HIFI_PROTOTYPE_USABILITY_TEST.md)

---

## Ringkasan Rencana Task (Summary Matrix)

| Task ID | Judul Task | Source Set / Layer | Bobot / Est. | Milestone | Dependensi |
|---|---|---|---|---|---|
| **TASK-00** | Inisialisasi scaffolding project Kotlin Multiplatform & Compose Multiplatform | Root / `composeApp` | Medium (3 SP) | MVP v0.1 | - |
| **TASK-01** | Data models `@Serializable`, static JSON kurikulum, dan local progress storage | `commonMain` (Data/Domain) | Medium (3 SP) | MVP v0.1 | TASK-00 |
| **TASK-02** | SemesterHomeScreen dengan horizontal pill tabs dan daftar kartu MK | `commonMain` (CMP UI) | Medium (3 SP) | MVP v0.1 | TASK-01 |
| **TASK-03** | HeroProgressCard dengan maskot Cookies dan progress bar dinamis | `commonMain` (CMP UI) | Low (2 SP) | MVP v0.1 | TASK-01 |
| **TASK-04** | CourseSyllabusScreen dengan daftar 16 topik silabus dan checkbox manual | `commonMain` (CMP UI) | Medium (3 SP) | MVP v0.1 | TASK-01 |
| **TASK-05** | Unit tests di `commonTest`, negative control (Gate 3b), dan CMP preview validation | `commonTest` (Testing) | Medium (3 SP) | MVP v0.1 | TASK-01..04 |

---

## Rincian Task & 4-Pillar Acceptance Criteria

### TASK-00: Inisialisasi scaffolding project Kotlin Multiplatform & Compose Multiplatform
- **Layer:** Build Configuration & Infrastructure
- **Problem Statement:** Repositori belum memiliki konfigurasi build Gradle, version catalog, dan project module untuk menjalankan aplikasi berbasis Kotlin Multiplatform (KMP) dan Compose Multiplatform (CMP) untuk target Android dan iOS.
- **Technical Context:**
  - Standard CMP project setup (`composeApp/src/commonMain`, `androidMain`, `iosMain`).
  - `gradle/libs.versions.toml`, `settings.gradle.kts`, `build.gradle.kts`.
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Gradle Version Catalog (`libs.versions.toml`) terkonfigurasi dengan Kotlin 2.x+, Compose Multiplatform plugin, Android Gradle Plugin, dan `kotlinx.serialization`.
    - Modul `composeApp` berhasil mengompilasi kode bersama di `commonMain` untuk target Android dan iOS framework.
    - Gradle wrapper (`gradlew`) terpasang dan executable.
  - • **UI/UX States & Tokens:**
    - Setup dasar `MaterialTheme` di `commonMain` dengan token warna resmi: `Corporate/Purple` (`#9D3FE7`), `Corporate/DarkPurple` (`#602093`), dan font Poppins.
  - • **Observability & Telemetry:**
    - Build script menghasilkan exit code 0 saat `./gradlew check` atau `./gradlew assembleDebug`.
  - • **Product Analytics:** N/A (Scaffolding).

---

### TASK-01: Data models `@Serializable`, static JSON kurikulum, dan local progress storage
- **Layer:** Data & Domain Layer (`commonMain`)
- **Problem Statement:** Diperlukan representasi model data strongly-typed dalam Kotlin untuk kurikulum Semester 1–8 Teknik Informatika beserta mekanisme pembacaan JSON statis dan penyimpanan status progress di `commonMain`.
- **Technical Context:**
  - `01_MODULE_1_SEQUENCE_DIAGRAM.md` §3.1 Langkah 5–9.
  - `01_PRD.md` §4 FR-1.1, FR-1.2, FR-1.3.
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Model Kotlin `@Serializable`: `Semester`, `Course`, `Topic`, `ProgressSummary` terdefinisi di `commonMain`.
    - Repository membaca file kurikulum `curriculum_it_semesters.json` dari `composeResources`.
    - Agregasi progres semester `(completedCount.toFloat() / totalCount * 100).toInt()` akurat.
    - Persistensi status penyelesaian topik menggunakan `multiplatform-settings` (KV Storage) di `commonMain`.
  - • **UI/UX States & Tokens:** N/A (Domain/Data).
  - • **Observability & Telemetry:**
    - Inisialisasi menghasilkan Trace ID dengan format `trc_app_init_<timestamp>_<hex>`.
    - Penanganan graceful fallback jika JSON corrupt (Skenario E5).
  - • **Product Analytics:**
    - Menyiapkan payload untuk event analitik `app_opened`.

---

### TASK-02: SemesterHomeScreen dengan horizontal pill tabs dan daftar kartu MK
- **Layer:** Presentation Layer (`commonMain`)
- **Problem Statement:** Pengguna membutuhkan antarmuka visual responsif berbasis Compose Multiplatform untuk memilih tab Semester 1–8 dan melihat daftar kartu mata kuliah.
- **Technical Context:**
  - `01_USE_CASE.md` (UC-01, UC-02).
  - Figma Node `1842:24850` (Pill Tabs), Node `1842:24900` (Course Card).
  - `03_MODULE_1_UI_DESIGN_SYSTEM_TOKENS.md` (Token `#9D3FE7`, Poppins, Card Elevation).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - `SemesterViewModel` mengelola `HomeUiState` (Loading, Success, Error).
    - Memilih tab semester (1–8) secara instan memperbarui daftar mata kuliah tanpa glitch.
    - Klik kartu mata kuliah memicu callback navigasi ke layar silabus membawa `courseId`.
  - • **UI/UX States & Tokens:**
    - Composable `PillTabRow` dengan pill terisolasi radius 9999px.
    - Tab aktif menggunakan gradasi primer `Brush.linearGradient(listOf(Color(0xFF9D3FE7), Color(0xFF602093)))`.
    - Composable `CourseCard` menampilkan judul, jumlah pertemuan, dan mini progress indicator.
    - Standar viewport 375x812 dp (rasio 9:19.5).
  - • **Observability & Telemetry:**
    - Mengoper `trace_id` `trc_sem_switch_xxx` pada setiap pergantian tab.
    - Waktu respon pergantian tab di bawah 100 ms (`02_MODULE_1_STATE_DIAGRAM.md`).
  - • **Product Analytics:**
    - Mengirim event `semester_switched`: `{ previous_semester, selected_semester, total_courses }`.
    - Mengirim event `course_opened`: `{ course_id, course_title, semester, progress_percentage }`.

---

### TASK-03: HeroProgressCard dengan maskot Cookies dan progress bar dinamis
- **Layer:** Presentation Layer (`commonMain`)
- **Problem Statement:** Perlu komponen hero banner ringkasan progres di beranda semester untuk memotivasi pembelajar dan meminimalkan drop-off D1.
- **Technical Context:**
  - `01_USE_CASE.md` (UC-09).
  - Figma Node `1842:25160` (Maskot Cookies), Node `1842:25040` (Linear Progress Bar).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Menampilkan teks progres dinamis: *"X dari Y Topik Selesai"* dan persentase `Z%`.
    - Reaktif terhadap pembaruan state progress dari storage lokal.
  - • **UI/UX States & Tokens:**
    - Composable `HeroProgressCard` dengan elevasi bayangan ungu lembut `0 4px 16px rgba(157, 63, 231, 0.08)`.
    - Menampilkan ilustrasi vektor maskot Cookies (Node `1842:25160`).
    - Progress bar fill menggunakan warna sukses `Informing/Approval` (`#00B998`).
  - • **Observability & Telemetry:**
    - Log diagnostik lokal mencatat persentase progres terkalkulasi.
  - • **Product Analytics:**
    - Parameter progres disertakan pada event `app_opened` dan `semester_viewed`.

---

### TASK-04: CourseSyllabusScreen dengan daftar 16 topik silabus dan checkbox manual
- **Layer:** Presentation Layer (`commonMain`)
- **Problem Statement:** Pengguna memerlukan tampilan silabus 16 pertemuan terstruktur per mata kuliah dengan atribusi kanal YouTube dan penanda checklist di Compose Multiplatform.
- **Technical Context:**
  - `01_USE_CASE.md` (UC-03, UC-08).
  - Figma Node `1842:24920` (Syllabus Item), Node `1842:25010` (Checkbox).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Menampilkan daftar `LazyColumn` 16 pertemuan lengkap dengan nomor topik, judul, durasi menit, dan channel YouTube.
    - Checkbox manual langsung meng-update status selesai ke local storage secara idempotensial.
    - Klik topik memicu transisi navigasi menuju Video Player (Modul 2) membawa `topic_id`, `video_id`, dan `trace_id`.
  - • **UI/UX States & Tokens:**
    - Checkbox aktif menampilkan icon centang hijau (`#00B998`).
    - Highlight visual pada topik pertemuan aktif berikutnya (*Next Up*).
    - Top bar navigasi kembali (*Back*) mengembalikan user ke Beranda Semester.
  - • **Observability & Telemetry:**
    - Propagasi Trace ID `trc_topic_sel_xxx` saat topik dipilih.
    - Pembatalan user (Skenario E4) ditangani tanpa freeze atau memory leak.
  - • **Product Analytics:**
    - Mengirim event `topic_selected`: `{ topic_id, course_id, meeting_number, video_id, channel_name }`.
    - Mengirim event `video_completed`: `{ video_id, is_manual: true }` saat checkbox manual di-toggle.

---

### TASK-05: Unit tests di `commonTest`, negative control (Gate 3b), dan CMP preview validation
- **Layer:** Quality & Testing Layer (`commonTest`)
- **Problem Statement:** Menjamin logika bisnis kurikulum, kalkulasi progres, dan rendering UI di KMP/CMP terverifikasi secara otomatis bebas regresi.
- **Technical Context:**
  - [`.agents/workflows/auto-work.md`](../.agents/workflows/auto-work.md) § Step 6 (7 Gerbang Verifikasi Lokal).
  - [`scripts/verify-evidence.sh`](../../scripts/verify-evidence.sh).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Unit test di `commonTest` mencakup agregasi progress, repository parsing, dan state transitions ViewModel.
    - **Negative Control (Gerbang 3b Wajib):** Rusakkan 1 logika (misal ubah formula persentase) $\to$ buktikan **MERAH** (`./gradlew test` exit non-zero) $\to$ pulihkan $\to$ buktikan **HIJAU**. Laporkan rasio `N/N → (N-1)/N → N/N` beserta nama test method-nya.
  - • **UI/UX States & Tokens:**
    - Verifikasi render komponen Compose pada resolusi 375x812 dp tanpa clipping/overflow.
  - • **Observability & Telemetry:**
    - Verifikasi format `trace_id` memenuhi pola `trc_<action>_<timestamp>_<hex>`.
  - • **Product Analytics:**
    - Verifikasi skema payload 4 client events (`app_opened`, `semester_switched`, `course_opened`, `topic_selected`).
