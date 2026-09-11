---
description: Otomatisasi TRD ke GitHub Issues & Dokumen Fase Arsitektur: analisis TRD & codebase, susun Doc 1 (Use Case & Activity Diagram) dengan Human Review Gate 1, susun Doc 2 (Sequence & State Diagram + Trace ID & 5 Failure Scenarios) di .docs/, susun Doc 3 (Task Implementation Plan berformat 4-Pillar AC) dengan Human Review Gate 2, lalu eksekusi pembuatan GitHub Issues via 'gh issue create' dan update dokumen dengan hyperlink isu.
---

# TRD to GitHub Issue Implementation Plan Command

Transforms a Technical Requirement Document (TRD) or PRD module specification into structured architectural documents and actionable GitHub issues for **OpenCampus Mobile** (`oss-elearning`).

Arguments: `$ARGUMENTS`

Expected format: `<TRD_PATH_OR_MODULE> [--milestone <MILESTONE>] [--dry-run]`

Examples:
```bash
/trd-breakdown .docs/trd/TRD-navigasi-kurikulum.md
/trd-breakdown "Modul 1: Navigasi Kurikulum" --milestone "MVP v0.1"
```

---

## Execution Steps

### Step 1: Parse Arguments & Ingest Context
1. Extract:
   - `trd_path_or_module`: Path to TRD file (e.g. `.docs/trd/TRD-navigasi-kurikulum.md`) or module name in `.docs/`.
   - `milestone`: Target GitHub milestone (default: `MVP v0.1` per PRD).
   - `dry_run`: Flag if present.
2. Read and analyze the TRD content, [`.docs/00_fase/01_PRD.md`](../../.docs/00_fase/01_PRD.md), and relevant files in `.docs/01_fase/` and `.docs/02_fase_paralel/`.
3. Inspect codebase and design tokens in [`.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md`](../../.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) to ground all technical claims in facts.

---

### Step 2: Generate / Align Doc 1 — Use Case & Activity Diagram (Fase 1)
1. Write or align document in `.docs/01_fase/` (or child of TRD):
   - **1. Use Case Diagram (Format: ASCII Art Wajib)**:
     - Batasan Sistem (System Boundary: OpenCampus Mobile App, Local SQLite/KV Storage, YouTube Embedded Player SDK, Telemetri).
     - Identifikasi Aktor (Pembelajar Mandiri, Sistem Mobile, YouTube Services).
     - User Goals & Hubungan (`<<include>>`, `<<extend>>` seperti auto-centang progress >= 85%, pelaporan link rusak).
   - **2. Activity Diagram (Format: ASCII Art Wajib)**:
     - Alur proses logis end-to-end dengan swimlanes: User, UI View/Screen, Controller/BLoC, Repository/Local Storage, YouTube Player.
     - Decision Points & Percabangan eksplisit (validasi kelengkapan data, video available vs error/private, manual vs auto-complete progress).
   - **Tabel Sitasi & Dasar Rujukan**: Dokumentasikan sumber rujukan (PRD §4, USE_CASE.md, Figma node id).
   - **Figma Node Hyperlinks**: Setiap referensi komponen menyertakan Node ID Figma.

---

### Step 3: Human Review Gate 1 (MANDATORY — Do NOT Bypass)
⚠️ **WAJIB: Sebelum melanjutkan ke Dokumen Sequence & State Diagram, dokumen Use Case & Activity Diagram HARUS ditinjau dan disetujui oleh human reviewer terlebih dahulu.**

1. Tampilkan ringkasan Use Case & Activity Diagram (batasan sistem, aktor, daftar user goals, alur percabangan utama) dan path dokumen kepada user.
2. Minta konfirmasi eksplisit dari user:
   > *"Silakan review rancangan Use Case & Activity Diagram di: [Path Doc 1]. Apakah batasan sistem, aktor, user goals, dan alur percabangan sudah sesuai dan disetujui untuk lanjut ke tahap Sequence & State Diagram?"*
3. **BERHENTI DAN TUNGGU RESPON USER.**
   - **DILARANG KERAS** melanjutkan ke Step 4 sebelum ada persetujuan eksplisit dari user (*"ya"*, *"approved"*, *"lanjut"*, dsb).
   - Jika user meminta revisi, perbaiki Doc 1 terlebih dahulu dan mintakan review ulang.

---

### Step 4: Generate / Align Doc 2 — Sequence & State Diagram (Fase 3)
Setelah Gate 1 disetujui:
1. Write or align document in `.docs/02_fase_paralel/{module}/system/`:
   - **1. Sequence Diagram (+ Trace ID & Client Events)**:
     - Visualisasi aliran data antar-layer: UI Screen $\to$ Controller/BLoC $\to$ Repository $\to$ Local Storage / YouTube Player.
     - **Propagasi Trace ID**: Format W3C `traceparent` atau `trc_<action>_<timestamp>_<hex>`.
     - **Anotasi Client Events**: Pencatatan event telemetri (`app_opened`, `semester_switched`, `course_opened`, `video_started`, `video_completed`).
     - **5 Non-Happy Path Scenarios (Wajib)**:
       - E1: Network stall / offline mode saat pemutaran video.
       - E2: YouTube error code 100/101/150 (video private / deleted) $\to$ fallback banner "Video sedang diperbarui" + tombol "Laporkan Link".
       - E3: OS process death / Low Memory Killer (LMK) $\to$ pemulihan state dari local storage.
       - E4: User back press / cancellation saat video loading.
       - E5: Corrupted local progress storage $\to$ recovery & log event.
   - **2. State Diagram (+ State Metrics & Drop-offs)**:
     - Pemodelan status (Initial, Loading, Loaded, Playing, Completed, ErrorFallback).
     - Identifikasi titik kritis potensi drop-off / churn pengguna beserta event failure telemetri penandanya.

---

### Step 5: Generate Doc 3 — Task Implementation Plan (4-Pillar AC)
1. Write document in `.docs/trd/` or `.docs/02_fase_paralel/{module}/`:
   - Title: `Task Implementation Plan — {Feature Name}`
   - Format per task:
     ```text
     ├── Problem Statement: Gap teknis / kebutuhan yang diselesaikan.
     ├── Technical Context: Tautan diagram (Doc 1 & Doc 2), node Figma, path kode riil.
     └── Acceptance Criteria (AC) — 4 Pilar Wajib:
           • Core Business Logic: Aturan domain, validasi, penanganan error E1..E5.
           • UI/UX States & Tokens: Kepatuhan Design Tokens UI Kit Free, visual states (Loading, Content, Empty, Error).
           • Observability & Telemetry: Propagasi Trace ID, structured error logging.
           • Product Analytics: Funnel event tracking sesuai PRD §5.1.
     ```
   - Rincian estimasi kompleksitas (Low / Medium / High) dan layer yang disentuh.

---

### Step 6: Human Review Gate 2 (MANDATORY — Do NOT Bypass)
⚠️ **WAJIB: Sebelum membuat issue di GitHub, seluruh rencana task HARUS ditinjau dan disetujui oleh human reviewer terlebih dahulu.**

1. Tampilkan ringkasan daftar task (Judul task, Layer, Kompleksitas, Estimasi, Milestone) kepada user.
2. Minta konfirmasi eksplisit dari user:
   > *"Silakan review Task Implementation Plan di atas. Apakah rencana task dan acceptance criteria sudah sesuai dan disetujui untuk dibuatkan issue resminya di GitHub?"*
3. **BERHENTI DAN TUNGGU RESPON USER.**
   - **DILARANG KERAS** memanggil `gh issue create` sebelum ada persetujuan eksplisit dari user (*"ya"*, *"approved"*, *"lanjut buat issue"*, dsb).
   - Jika user meminta perubahan cakupan atau prioritas, perbaiki rencana task terlebih dahulu dan mintakan review ulang.

---

### Step 7: Eksekusi Pembuatan GitHub Issues (Setelah Approval Gate 2)
Setelah persetujuan eksplisit pada Gate 2 (dan `--dry-run` tidak aktif):
1. Cek issue yang sudah ada menggunakan `gh issue list` untuk mencegah duplikasi.
2. Untuk setiap task yang disetujui, jalankan `gh issue create`:
   ```bash
   gh issue create \
     --title "feat(LAYER): {judul-task}" \
     --body-file /tmp/task-body.md \
     --label "enhancement,{module-label}" \
     --milestone "{milestone}" \
     --assignee "@me"
   ```
3. Tangkap issue number (`#<number>`) dan URL dari output perintah.

---

### Step 8: Update Dokumen dengan Hyperlink Issue
1. Ganti placeholder task ID di dokumen TRD / Task Implementation Plan dengan nomor issue GitHub nyata (misal `#12`).
2. Format sebagai tautan markdown yang dapat diklik: `[#12](https://github.com/tomdwipo/oss-elearning/issues/12)`.
3. Commit pembaruan dokumen:
   ```bash
   git add .docs/
   git commit -m "docs(LAYER): link GitHub issues to {feature} implementation plan"
   git push origin main
   ```

---

### Step 9: Laporan Hasil Akhir
Tampilkan tabel ringkasan hasil eksekusi:
- Path dokumen arsitektur (Doc 1: Use Case/Activity, Doc 2: Sequence/State, Doc 3: Implementation Plan).
- Tabel daftar GitHub Issues yang berhasil dibuat beserta URL, label, milestone, dan kompleksitas.
