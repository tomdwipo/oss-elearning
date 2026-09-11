# Task Implementation Plan — Modul 1: Navigasi Kurikulum

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

| Task ID | Judul Task | Layer | Bobot / Est. | Milestone | Dependensi |
|---|---|---|---|---|---|
| **TASK-01** | Data models, static JSON kurikulum, dan local progress storage | Data / Domain | Medium (3 SP) | MVP v0.1 | - |
| **TASK-02** | SemesterHomeScreen dengan horizontal pill tabs dan daftar kartu MK | Presentation / UI | Medium (3 SP) | MVP v0.1 | TASK-01 |
| **TASK-03** | HeroProgressCard dengan maskot Cookies dan progress bar dinamis | Presentation / UI | Low (2 SP) | MVP v0.1 | TASK-01 |
| **TASK-04** | CourseSyllabusScreen dengan daftar 16 topik silabus dan checkbox manual | Presentation / UI | Medium (3 SP) | MVP v0.1 | TASK-01 |
| **TASK-05** | Unit tests, negative control (Gate 3b), dan viewport integration tests | Quality / Testing | Medium (3 SP) | MVP v0.1 | TASK-01..04 |

---

## Rincian Task & 4-Pillar Acceptance Criteria

### TASK-01: Data models, static JSON kurikulum, dan local progress storage
- **Layer:** Data & Domain Layer
- **Problem Statement:** Aplikasi belum memiliki struktur entitas data untuk kurikulum Semester 1–8 Teknik Informatika serta penyimpanan lokal untuk mencatat status topik selesai.
- **Technical Context:**
  - `01_MODULE_1_SEQUENCE_DIAGRAM.md` §3.1 Langkah 5–9 (Pemuatan JSON & baca storage).
  - `01_PRD.md` §4 FR-1.1, FR-1.2, FR-1.3.
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Model data `Semester`, `Course`, `Topic`, dan `ProgressSummary` terdefinisi secara type-safe.
    - JSON kurikulum Semester 1–8 (Informatika) termuat dari bundle asset aplikasi (*zero network latency*).
    - Perhitungan agregasi progres per semester `(completed / total) * 100` akurat dengan pembulatan integer.
    - Local storage (Key-Value / SQLite) menyimpan Set of `completed_topic_ids` secara persisten.
  - • **UI/UX States & Tokens:** N/A (Backend / Local Data).
  - • **Observability & Telemetry:**
    - Trace ID `trc_app_init_xxx` dihasilkan saat inisialisasi awal repositori data.
    - Penanganan error fallback jika JSON corrupt (Skenario E5) dengan default guest state.
  - • **Product Analytics:** Menyiapkan parameter data untuk event `app_opened`.

---

### TASK-02: SemesterHomeScreen dengan horizontal pill tabs dan daftar kartu MK
- **Layer:** Presentation Layer
- **Problem Statement:** Pengguna memerlukan antarmuka visual responsif untuk memilih tab Semester 1 s.d. 8 dan meninjau kartu mata kuliah.
- **Technical Context:**
  - `01_USE_CASE.md` (UC-01, UC-02).
  - Figma Node `1842:24850` (Pill Tabs), Node `1842:24900` (Course Card).
  - `03_MODULE_1_UI_DESIGN_SYSTEM_TOKENS.md` (Token warna `#9D3FE7`, Poppins typography).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Tap pada Tab Semester (1–8) langsung mengalihkan daftar mata kuliah tanpa reload aplikasi.
    - Tap pada kartu mata kuliah memicu navigasi menuju Halaman Silabus dengan oper `course_id`.
  - • **UI/UX States & Tokens:**
    - Mengikuti standar viewport 375x812 dp (rasio 9:19.5).
    - Tab aktif menggunakan gradasi ungu primer `linear-gradient(135deg, #9D3FE7 0%, #602093 100%)`.
    - Kartu mata kuliah memuat judul, estimasi jumlah pertemuan, dan mini progress bar.
    - Visual states lengkap: `Default`, `Focused/Active`, `Loading`, `Empty`.
  - • **Observability & Telemetry:**
    - Propagasi Trace ID `trc_sem_switch_xxx` pada setiap pergantian tab.
    - Transisi pergantian tab memenuhi SLA `< 100 ms` (`02_MODULE_1_STATE_DIAGRAM.md` §3).
  - • **Product Analytics:**
    - Pengiriman event `semester_switched`: `{ previous_semester, selected_semester, total_courses }`.
    - Pengiriman event `course_opened`: `{ course_id, course_title, semester, progress_percentage }`.

---

### TASK-03: HeroProgressCard dengan maskot Cookies dan progress bar dinamis
- **Layer:** Presentation Layer
- **Problem Statement:** Perlu komponen hero banner ringkasan progres di beranda semester untuk memotivasi pembelajar dan meminimalkan drop-off D1.
- **Technical Context:**
  - `01_USE_CASE.md` (UC-09).
  - Figma Node `1842:25160` (Maskot Cookies), Node `1842:25040` (Linear Progress Bar).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Menampilkan kalkulasi progres semester aktif: *"X dari Y Topik Selesai"* beserta persentase `Z%`.
    - Reaktif memperbarui angka dan lebar progress bar saat ada topik yang diselesaikan.
  - • **UI/UX States & Tokens:**
    - Menampilkan maskot karakter kartun Cookies (Node `1842:25160`).
    - Elevasi bayangan ungu lembut `0 4px 16px rgba(157, 63, 231, 0.08)`.
    - Progress fill menggunakan warna sukses `Informing/Approval` (`#00B998`).
  - • **Observability & Telemetry:**
    - Nilai persentase progres dicatat dalam log diagnostik lokal.
  - • **Product Analytics:**
    - Menyertakan data progres ke dalam payload event `app_opened` dan `semester_viewed`.

---

### TASK-04: CourseSyllabusScreen dengan daftar 16 topik silabus dan checkbox manual
- **Layer:** Presentation Layer
- **Problem Statement:** Pengguna memerlukan tampilan silabus 16 pertemuan terstruktur per mata kuliah dengan atribusi kanal YouTube dan penanda checklist.
- **Technical Context:**
  - `01_USE_CASE.md` (UC-03, UC-08).
  - Figma Node `1842:24920` (Syllabus Item), Node `1842:25010` (Checkbox).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Menampilkan daftar urut 16 pertemuan lengkap dengan nomor pertemuan, judul topik, durasi (menit), dan nama channel.
    - Tap checkbox manual meng-update status selesai ke penyimpanan lokal secara idempotensial.
    - Tap item topik memicu navigasi menuju Halaman Video Player (Modul 2) membawa `topic_id`, `video_id`, dan `trace_id`.
  - • **UI/UX States & Tokens:**
    - Checkbox aktif menampilkan icon centang hijau (`#00B998`).
    - Highlight visual pada pertemuan aktif berikutnya (*Next Up*) untuk meminimalkan drop-off D2.
    - Tombol navigasi kembali (*Back button*) di header mengembalikan user ke Beranda Semester tanpa me-reset scroll.
  - • **Observability & Telemetry:**
    - Propagasi Trace ID `trc_topic_sel_xxx` saat topik dipilih.
    - Penanganan pembatalan user (Skenario E4) saat loading navigasi.
  - • **Product Analytics:**
    - Pengiriman event `topic_selected`: `{ topic_id, course_id, meeting_number, video_id, channel_name }`.
    - Pengiriman event `video_completed`: `{ video_id, is_manual: true }` saat checkbox manual di-toggle.

---

### TASK-05: Unit tests, negative control (Gate 3b), dan viewport integration tests
- **Layer:** Quality & Verification Layer
- **Problem Statement:** Menjamin keandalan fungsional, kepatuhan batas viewport 375x812 dp, dan anti-ilusi test hijau via kontrol negatif.
- **Technical Context:**
  - [`.agents/workflows/auto-work.md`](../.agents/workflows/auto-work.md) § Step 6 (7 Gerbang Verifikasi Lokal).
  - [`scripts/verify-evidence.sh`](../../scripts/verify-evidence.sh).
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:**
    - Unit test mencakup kalkulasi agregasi progres, toggle checklist, dan parsing JSON kurikulum.
    - **Negative Control (Gerbang 3b Wajib):** Rusakkan 1 logika (misal ubah formula persentase) $\to$ buktikan **MERAH** $\to$ pulihkan $\to$ buktikan **HIJAU**. Laporkan rasio `N/N → (N-1)/N → N/N` beserta nama test method-nya.
  - • **UI/UX States & Tokens:**
    - Uji rendering viewport pada resolusi 375x812 dp (rasio 9:19.5), pastikan nol overflow pada notch safe area.
  - • **Observability & Telemetry:**
    - Verifikasi format `trace_id` memenuhi pola `trc_<action>_<timestamp>_<hex>`.
  - • **Product Analytics:**
    - Verifikasi skema payload 4 client events (`app_opened`, `semester_switched`, `course_opened`, `topic_selected`).
