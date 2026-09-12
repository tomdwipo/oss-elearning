---
description: Workflow komprehensif penyusunan TRD (.docs/trd/) dan Task Implementation Plan (.docs/task/) berbasis PRD + Figma + KMP Codebase, dilengkapi 2 Human Review Gates via GitHub dan otomatisasi pembuatan GitHub Issues via 'gh issue create'.
---

# TRD & Task Implementation Plan Workflow

Workflow terpadu untuk merumuskan **Technical Requirement Document (TRD)** di `.docs/trd/`, menyusun **Task Implementation Plan** di `.docs/task/`, serta mengeksekusi pembuatan **GitHub Issues** resmi untuk **OpenCampus Mobile** (`oss-elearning`).

Menerapkan arsitektur **Kotlin Multiplatform (KMP) & Compose Multiplatform (CMP)**, **5-Phase Mobile Architecture**, **5 Non-Happy Path Scenarios (E1–E5)**, **7 Local Verification Gates** (termasuk **Negative Control Gate 3b**), serta **2 Human Review Gates** berbasis commit & push langsung ke GitHub.

---

## Format Pemanggilan

```bash
/trd <NAMA_FITUR_ATAU_MODUL> [--milestone <MILESTONE>] [--dry-run]
```

### Parameter:
- `<NAMA_FITUR_ATAU_MODUL>`: Nama fitur, modul, atau path PRD terkait (misal: `"Modul 3: Video Player"` atau `.docs/prd/02_prd.md`).
- `--milestone`: Target milestone GitHub (default: `MVP v0.1`).
- `--dry-run`: Flag opsional untuk mensimulasikan rencana issue tanpa memanggil API GitHub.

### Contoh Pemanggilan:
```bash
/trd "Modul 3: Video Player & Pelacakan Progres"
/trd .docs/prd/02_prd.md --milestone "MVP v0.1"
/trd "Modul 4: Kuis Interaktif" --dry-run
```

---

## Diagram Alur Eksekusi (ASCII Flow)

```text
                     [ Trigger: /trd <feature> ]
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ TAHAP 1: ANALISIS & DRAFT TRD AWAL                              │
│ 1. Ingest PRD, Figma (fig-decode/tokens), & Codebase KMP/CMP    │
│ 2. Tulis draft awal .docs/trd/XX_trd.md:                        │
│    • Scope, Executive Summary, Codebase Grounding               │
│    • Fase 1: Use Case Diagram (ASCII + Boundary + Actors)       │
│    • Fase 1: Activity Diagram (ASCII + Swimlanes + Decisions)   │
│    • Tabel Sitasi PRD §4 & Figma Node Hyperlinks                │
│ 3. Git commit & push draft awal ke GitHub                       │
└─────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ 🔴 HUMAN REVIEW GATE 1 (Wajib Stop)                             │
│ • Dokumen TRD awal sudah ter-push ke repo GitHub.               │
│ • Agent kirim URL GitHub file TRD ke chat lalu BERHENTI.        │
│ • User review langsung di GitHub: Logika bisnis & diagram alur. │
│ • DILARANG lanjut ke Tahap 2 sebelum ada pesan persetujuan!     │
└─────────────────────────────────────────────────────────────────┘
                                 │
                         [ User: "Approved" ]
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ TAHAP 2: LENGKAPI TRD TEKNIS & SUSUN TASKLIST                   │
│ 1. Lengkapi .docs/trd/XX_trd.md:                                │
│    • Fase 2: UI/UX Handoff, Tokens, 6 Visual States             │
│    • Fase 3: Sequence Diagram (Trace ID & Client Events)        │
│              5 Non-Happy Path Scenarios (E1..E5)                │
│              State Diagram (Drop-offs & Failure Events)         │
│    • Fase 4: Class Diagram (Clean Arch) & Schema (Idempotency)  │
│    • Fase 5: Component Diagram (Runtime Ecosystem Mobile)       │
│    • Testing: 7 Local Verification Gates (Gate 3b Negative Ctr) │
│ 2. Tulis .docs/task/XX_tasklist.md:                             │
│    • Matriks Ringkasan Task (ID, Judul, Layer, Bobot SP, Dep)   │
│    • Rincian Task dengan 4-Pillar Acceptance Criteria:          │
│      (Core Logic, UI/UX States, Observability, Analytics)       │
│ 3. Git commit & push dokumen lengkap ke GitHub                  │
└─────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ 🔴 HUMAN REVIEW GATE 2 (Wajib Stop)                             │
│ • Dokumen TRD penuh & Tasklist sudah ter-push ke GitHub.        │
│ • Agent kirim URL GitHub file tasklist ke chat lalu BERHENTI.   │
│ • User review rincian task, bobot SP, dan 4-Pillar AC di GitHub.│
│ • DILARANG memanggil `gh issue create` sebelum disetujui!       │
└─────────────────────────────────────────────────────────────────┘
                                 │
                         [ User: "Approved" ]
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ TAHAP 3: EKSEKUSI GITHUB ISSUES & PENUTUPAN                     │
│ 1. Cek duplikasi via `gh issue list`                            │
│ 2. Eksekusi `gh issue create` untuk setiap task                 │
│ 3. Update .docs/task/XX_tasklist.md dengan link [#XX](url) riil │
│ 4. Git commit & push final ke GitHub (by LAYER)                 │
│ 5. Tampilkan tabel ringkasan hasil akhir ke user                │
└─────────────────────────────────────────────────────────────────┘
```

---

## Petunjuk Langkah Demi Langkah (Step-by-Step Instructions)

### TAHAP 1: ANALISIS & DRAFT TRD AWAL

#### Step 1: Parse Arguments & Ingest Context
1. Ekstrak argumen: `feature_name`, `milestone` (default `MVP v0.1`), dan `dry_run`.
2. Tentukan nomor modul berikutnya berdasarkan file yang ada di `.docs/trd/` (misal `03_trd.md`).
3. Kumpulkan data primer:
   - **PRD**: Baca `.docs/prd/` dan `.docs/00_fase/01_PRD.md`. Ekstrak tabel kebutuhan, aturan bisnis, copy teks, dan referensi desain.
   - **Figma Drilling**: Jalankan skill `fig-decode` pada `design_system/assets/Design System _ Ui Kit Free (Community).fig` (atau via MCP). Ekstrak Node ID, Frame, dan 6 visual states (*Default, Focused, Loading, Error, Empty, Disabled*).
   - **Codebase Inspection**: Grep di `composeApp/src/commonMain`, `androidMain`, dan `iosMain` untuk memverifikasi simbol class, interface, viewmodel, platform bridge, dan repositori yang sudah ada. Jangan berasumsi; validasi fakta kode terlebih dahulu (*truth in code*).
   - **Kontrak Eksternal**: Kontrak JSON kurikulum, TOS & embed YouTube Player Iframe API, serta persistensi lokal (Multiplatform Settings / SQLite).

#### Step 2: Tulis Draft Awal TRD (`.docs/trd/XX_trd.md`)
Buat file baru di `.docs/trd/XX_trd.md` yang memuat:
1. **Header Metadata**: Versi, Author (`Technical Team (Mobile — Tommy Dwi Putranto)`), Status, Rujukan PRD, Rujukan Figma, Target Platform (KMP & CMP: Android & iOS).
2. **1. Executive Summary & Scope**:
   - 1.1 Overview & Arsitektur Ringkas.
   - 1.2 Current Implementation (grounded in code).
   - 1.3 Target Implementation.
   - 1.4 Scope (In Scope vs Out of Scope per PRD).
3. **FASE 1: KEBUTUHAN & LOGIKA BISNIS**:
   - **1.1 Use Case Diagram (Format: Wajib ASCII Art)**:
     - Batasan Sistem (System Boundary: OpenCampus Mobile App, Local Storage, YouTube Iframe SDK, Telemetri).
     - Identifikasi Aktor (Pembelajar Mandiri, Sistem Mobile, YouTube Services).
     - User Goals & Relasi (`<<include>>`, `<<extend>>` seperti auto-centang progress $\ge 85\%$, pelaporan link rusak).
   - **1.2 Activity Diagram (Format: Wajib ASCII Art)**:
     - Swimlanes: User, UI Screen, ViewModel/Controller, Repository, Local Storage, YouTube Player.
     - Decision points eksplisit (validasi data, video available vs error, auto-complete vs manual toggle).
   - **Tabel Sitasi & Rujukan PRD**: Pemetaan ke nomor pasal PRD (§4 dsb) dan Figma Node ID.

#### Step 3: Git Commit & Push Step 1 ke GitHub
1. Stage file TRD awal:
   ```bash
   git add .docs/trd/XX_trd.md
   ```
2. Commit dengan konvensi `LAYER` (tanpa atribusi AI):
   ```bash
   git commit -m "docs(LAYER): draft initial TRD (Use Case & Activity) for <Feature>"
   ```
3. Push ke branch aktif:
   ```bash
   git push origin <branch>
   ```

#### Step 4: 🔴 HUMAN REVIEW GATE 1 (MANDATORY STOP)
⚠️ **WAJIB: DILARANG melanjutkan ke penulisan detail teknis sebelum rancangan awal di-review dan disetujui oleh user di GitHub.**

1. Sajikan link GitHub file TRD yang baru di-push:
   - Format: `https://github.com/tomdwipo/oss-elearning/blob/<branch>/.docs/trd/XX_trd.md`
2. Berikan pesan ringkas konfirmasi:
   > *"Draft awal TRD untuk `<Feature>` telah berhasil di-push ke GitHub:*  
   > *[Lihat Dokumen di GitHub](https://github.com/tomdwipo/oss-elearning/blob/<branch>/.docs/trd/XX_trd.md)*  
   >  
   > *Silakan review batasan sistem, aktor, serta alur Use Case & Activity Diagram langsung di GitHub. Apakah rancangan logika bisnis awal ini disetujui untuk lanjut ke spesifikasi arsitektur teknis dan penyusunan tasklist?"*
3. **BERHENTI TOTAL DAN TUNGGU RESPON USER.**
   - Dilarang memanggil tools pembuatan file lanjutan sebelum ada persetujuan eksplisit (*"ya"*, *"approved"*, *"lanjut"*, dsb).
   - Jika ada koreksi, perbaiki file, commit & push ulang, lalu mintakan review kembali.

---

### TAHAP 2: LENGKAPI TRD TEKNIS & SUSUN TASKLIST

#### Step 5: Lengkapi `.docs/trd/XX_trd.md` dengan Arsitektur Teknis
Perbarui dan lengkapi file `.docs/trd/XX_trd.md` dengan bagian-bagian berikut:
1. **[PARALEL] FASE 2: DESAIN ANTARMUKA (UI/UX)**:
   - 2.1 User Flow & Wireflow (Entry point, Back-stack policy, percabangan).
   - 2.2 Wireframe (Lo-Fi) & Struktur Layout (375x812 dp, rasio 9:19.5, safe area insets untuk Android & iOS).
   - 2.3 UI Design System & Tokens (Warna: `#9D3FE7`, `#602093`, `#00B998`, `#FFFFFF`, `#1F1F1F`; Font: Poppins; Radius; Shadow).
   - 2.4 Design Handoff: Tabel pemetaan komponen, Node ID Figma, dan 6 Visual States (*Default, Focused, Loading, Error, Empty, Disabled*).
2. **[PARALEL] FASE 3: PERILAKU SISTEM & TELEMETRI**:
   - **3.1 Sequence Diagram (Wajib ASCII Art)**:
     - Aliran data antar-layer mobile (UI Screen $\to$ ViewModel $\to$ Repo $\to$ Storage/Player).
     - **Propagasi Trace ID**: Format `trc_<action>_<timestamp>_<hex>` atau W3C `traceparent`.
     - **Anotasi Client Events**: Tracking event telemetri (`app_opened`, `semester_switched`, `course_opened`, `video_started`, `video_completed`, dsb).
     - **5 Non-Happy Path Scenarios (Wajib E1–E5)**:
       - `E1 (Network Stall / Offline)`: Koneksi terputus saat memuat video $\to$ inline banner tanpa reset state.
       - `E2 (YouTube Video Error / Private / Removed)`: Player menangkap error code 100/101/150 $\to$ fallback banner *"Video materi sedang diperbarui"* + tombol *"Laporkan Link"*.
       - `E3 (OS Process Death / Low Memory Killer)`: Pemulihan UiState dari local storage (didukung pada lifecycle Android & iOS).
       - `E4 (User Cancellation saat Loading)`: Back-press / gesture kembali saat loading video dibatalkan secara bersih tanpa leak coroutine.
       - `E5 (Corrupted Local Storage / Migration)`: Recovery ke state default guest + log error telemetri.
   - **3.2 State Diagram (Wajib ASCII Art)**:
     - Pemetaan UiState lifecycle, latensi transisi, dan titik kritis potensi *user drop-off / churn*.
3. **FASE 4: STRUKTUR DATA & KODE (Clean Architecture)**:
   - 4.1 Class Diagram (Wajib ASCII Art): Pemisahan layer `Presentation`, `Domain`, `Data`, dan `Telemetry`.
   - 4.2 Data Storage, Schema & Idempotency: Struktur model `@Serializable`, skema KV / SQLite, serta idempotency rule penyelesaian video $\ge 85\%$.
4. **FASE 5: ARSITEKTUR FISIK RUNTIME MOBILE**:
   - 5.1 Component Diagram (Wajib ASCII Art): Ekosistem runtime Compose Multiplatform (`composeApp`, platform bridge `expect/actual` untuk Android [`AndroidView`/`WebView`] dan iOS [`UIKitView`/`WKWebView`]).
5. **6. Testing Requirements & 7 Local Verification Gates**:
   - Gate 1: Linter & Formatter (`./gradlew check -x test` / linter nol error).
   - Gate 2: Data & API Contracts (validasi JSON kurikulum & YouTube contract).
   - Gate 3a: Unit Tests (Coverage logika domain $\ge 80\%$).
   - **Gate 3b: Negative Control Wajib**: Rusakkan 1 logika domain $\to$ verifikasi unit test RED $\to$ pulihkan $\to$ verifikasi GREEN. Laporkan rasio `N/N → (N-1)/N → N/N` beserta nama method yang dites.
   - Gate 4: Component & Viewport (375x812 dp, Light/Dark theme di Android & iOS).
   - Gate 5: Performance & Build Artifacts: Verifikasi packaging dual platform — Android APK (`./gradlew assembleDebug`) dan iOS Framework (`./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64`).
   - Gate 6: Security & Secret Hygiene (nol exposed private keys).
6. **7. Standar Pengarsipan Evidence (Wajib Dual-Platform: Android & iOS)**:
   Mewajibkan rancangan evidence di `.docs/evidence/{XX}/` memuat artefak verifikasi untuk **keduanya** (Android dan iOS):
   ```text
   .docs/evidence/{XX}/
   ├── README.md               # Ringkasan 7 gerbang, tabel verifikasi Android vs iOS, rasio Gate 3b
   ├── console-evidence.txt    # Log eksekusi build & test (Android APK + iOS Framework)
   ├── android/
   │   ├── demo.mp4            # Video walkthrough Android (Pixel_9_Pro via android CLI)
   │   └── screenshots/        # Tangkapan layar Android (01_initial.png, 02_action.png, dst.)
   └── ios/
       ├── demo.mp4            # Video walkthrough iOS (iPhone 16 Pro via xcrun simctl)
       └── screenshots/        # Tangkapan layar iOS (01_initial.png, 02_action.png, dst.)
   ```

#### Step 6: Tulis Task Implementation Plan (`.docs/task/XX_tasklist.md`)
Buat file baru `.docs/task/XX_tasklist.md` mengikuti standar proyek:
1. **Header Metadata**: Mengaitkan dokumen ke PRD, TRD, arsitektur target KMP (Android & iOS), dan target evidence.
2. **Bagian 1: Ringkasan Rencana Task (Summary Matrix)**:
   - Tabel kolom: `| Task ID | Issue GitHub | Judul Task | Layer / Source Set | Bobot / Est. | Dependensi |`
   - Kolom `Issue GitHub` diisi nilai sementara: `(Pending Gate 2)`.
   - ⚠️ **MANDATORI DUAL PLATFORM (Android & iOS):** Setiap fitur yang melibatkan platform bridge (`expect/actual`), interop UI native, deliverable build, atau pengarsipan evidence WAJIB menyertakan layer/source set Android (`androidMain`) dan iOS (`iosMain`) secara berimbang, serta mencakup verifikasi build dan visual evidence untuk kedua platform.
3. **Bagian 2: Rincian Task & Acceptance Criteria (4-Pillar + Build & Evidence Verification Wajib)**:
   Setiap task diuraikan dengan format:
   ```markdown
   ### TASK-XX: {Judul Task} — (Pending Gate 2)
   - **Layer:** {Source set / layer, misal: commonMain (Domain/Data) atau commonMain, androidMain, iosMain (Platform Bridge)}
   - **Kompleksitas:** {Low / Medium / High (atau Story Points)}
   - **Problem Statement:** {Masalah teknis konkret yang diselesaikan}
   - **Technical Context:**
     - Rujukan TRD: `XX_TRD.md` §X.
     - Rujukan PRD: `XX_PRD.md` §X.
     - Figma Node ID: [`{NODE_ID}`](file://...)
     - File Codebase: [PathFile.kt](file:///...)
   - **Acceptance Criteria (AC):**
     - • **Core Business Logic:** Aturan domain, validasi, navigasi, implementasi bridge Android & iOS, penanganan skenario E1..E5.
     - • **UI/UX States & Tokens:** Tokens warna (`#9D3FE7`, `#00B998`), font Poppins, 6 visual states.
     - • **Observability & Telemetry:** Format Trace ID, structured error logging.
     - • **Product Analytics:** Event tracking, nama event, payload.
     - • **Build Artifact & Verification (Wajib Dual Platform jika ada perubahan native/bridge):**
       - Android: `./gradlew assembleDebug` menghasilkan `composeApp-debug.apk`.
       - iOS: `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` menghasilkan `ComposeApp.framework`.
     - • **Dual-Platform Evidence Archive (Wajib untuk task testing/verifikasi):**
       - Menghasilkan tangkapan layar dan rekaman interaksi lengkap pada kedua platform di `.docs/evidence/{XX}/android/` dan `.docs/evidence/{XX}/ios/`.
   ```

#### Step 7: Git Commit & Push Step 2 ke GitHub
1. Stage file TRD lengkap dan Tasklist:
   ```bash
   git add .docs/trd/XX_trd.md .docs/task/XX_tasklist.md
   ```
2. Commit dengan konvensi `LAYER`:
   ```bash
   git commit -m "docs(LAYER): finalize technical TRD and tasklist for <Feature>"
   ```
3. Push ke branch aktif:
   ```bash
   git push origin <branch>
   ```

#### Step 8: 🔴 HUMAN REVIEW GATE 2 (MANDATORY STOP)
⚠️ **WAJIB: DILARANG KERAS memanggil `gh issue create` sebelum seluruh daftar task ditinjau dan disetujui oleh user di GitHub.**

1. Sajikan link GitHub file tasklist yang baru di-push:
   - Format: `https://github.com/tomdwipo/oss-elearning/blob/<branch>/.docs/task/XX_tasklist.md`
2. Tampilkan ringkasan matriks task di chat (Task ID, Judul, Layer, Kompleksitas).
3. Berikan pesan konfirmasi:
   > *"Dokumen TRD teknis dan Task Implementation Plan untuk `<Feature>` telah berhasil di-push ke GitHub:*  
   > *[Lihat Tasklist di GitHub](https://github.com/tomdwipo/oss-elearning/blob/<branch>/.docs/task/XX_tasklist.md)*  
   >  
   > *Silakan review pembagian task, dependensi, bobot SP, serta 4-Pillar Acceptance Criteria langsung di GitHub. Apakah rencana task ini disetujui untuk dibuatkan tiket issue resminya di GitHub?"*
4. **BERHENTI TOTAL DAN TUNGGU RESPON USER.**
   - Dilarang membuat issue GitHub sebelum ada persetujuan eksplisit (*"ya"*, *"approved"*, *"lanjut buat issue"*, dsb).

---

### TAHAP 3: EKSEKUSI GITHUB ISSUES & PENUTUPAN

#### Step 9: Eksekusi Pembuatan GitHub Issues
Setelah Gate 2 disetujui dan `--dry-run` tidak aktif:
1. Jalankan `gh issue list` untuk memeriksa tiket yang sudah ada demi mencegah duplikasi.
2. Untuk setiap task yang disetujui, tulis isi deskripsi (Problem Statement, Technical Context, 4-Pillar AC) ke file temporer, lalu jalankan:
   ```bash
   gh issue create \
     --title "feat(LAYER): {Judul Task}" \
     --body-file /tmp/task-body.md \
     --label "enhancement,{module-label},{layer-label}" \
     --milestone "{milestone}" \
     --assignee "@me"
   ```
3. Tangkap nomor issue resmi (`#<number>`) dan URL issue dari output.

#### Step 10: Inject Link Issue ke Dokumen Tasklist
1. Buka `.docs/task/XX_tasklist.md`.
2. Ganti seluruh placeholder `(Pending Gate 2)` pada tabel matrix dan sub-judul task dengan tautan aktif issue GitHub nyata, contoh:
   `[#12](https://github.com/tomdwipo/oss-elearning/issues/12)`
3. Pastikan tautan rapi dan dapat diklik langsung.

#### Step 11: Git Commit & Push Final ke GitHub
1. Stage pembaruan tasklist:
   ```bash
   git add .docs/task/XX_tasklist.md
   ```
2. Commit dengan konvensi `LAYER`:
   ```bash
   git commit -m "docs(LAYER): link GitHub issues to <Feature> tasklist"
   ```
3. Push ke branch aktif:
   ```bash
   git push origin <branch>
   ```

#### Step 12: Laporan Hasil Akhir
Tampilkan laporan penutupan di chat:
- Path file TRD dan Tasklist di repo lokal & link GitHub.
- Tabel daftar GitHub Issues yang berhasil dibuat (Nomor Issue, Judul, URL, Layer, Milestone).
- Informasi kesiapan untuk mulai coding pada task pertama.

---

## Standar Format Template

### Template Dokumen TRD (`.docs/trd/XX_trd.md`)
```markdown
# TRD {XX}: {Nama Fitur / Modul}

**Nomor Modul:** {XX}  
**Fitur Utama:** {Scope ringkas}  
**Target Platform:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP)  
**Dokumen PRD Terkait:** [PRD {XX}: ...](../prd/{XX}_prd.md)  
**Dokumen Master Desain:** [Design System UI Kit Free](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Target Arsip Evidence:** `.docs/evidence/{XX}/README.md`  

---

## 1. Executive Summary & Scope
### 1.1 Overview & Arsitektur Ringkas
### 1.2 Current Implementation (Grounded in Code)
### 1.3 Target Implementation
### 1.4 Scope (In Scope vs Out of Scope)

---

## FASE 1: KEBUTUHAN & LOGIKA BISNIS
### 1.1 Use Case Diagram (System Boundary & Actors) - ASCII Art Wajib
### 1.2 Activity Diagram (Alur Proses & Swimlanes) - ASCII Art Wajib
### 1.3 Tabel Sitasi & Rujukan PRD §4

---

## [PARALEL] FASE 2: DESAIN ANTARMUKA (UI/UX)
### 2.1 User Flow & Wireflow
### 2.2 Wireframe Layout (375x812 dp, 9:19.5)
### 2.3 UI Design System & Tokens (#9D3FE7, #00B998, Poppins)
### 2.4 Design Handoff & 6 Visual States Matrix (Default..Disabled)

---

## [PARALEL] FASE 3: PERILAKU SISTEM & TELEMETRI
### 3.1 Sequence Diagram (+ Trace ID & Client Events) - ASCII Art Wajib
#### 5 Non-Happy Path Scenarios Wajib (E1..E5)
### 3.2 State Diagram (+ Latency & Drop-offs) - ASCII Art Wajib

---

## FASE 4: STRUKTUR DATA & KODE (Clean Architecture)
### 4.1 Class Diagram - ASCII Art Wajib
### 4.2 Kontrak Data Model Kotlin (@Serializable) & 85% Idempotency Rule

---

## FASE 5: ARSITEKTUR FISIK RUNTIME MOBILE
### 5.1 Component Diagram (KMP Runtime Ecosystem: Android & iOS Bridge) - ASCII Art Wajib

---

## 6. Testing Requirements & 7 Local Verification Gates
- Gate 1: Linter & Formatters (`./gradlew check -x test`)
- Gate 2: Data & API Contracts (`./gradlew test --tests ...`)
- Gate 3a: Unit Tests (>= 80% coverage di `commonTest`)
- Gate 3b: Negative Control (Break logic -> RED -> GREEN, laporkan N/N -> (N-1)/N -> N/N)
- Gate 4: Viewport 375x812 dp, Themes, & Platform Adaptation (Android & iOS)
- Gate 5: Performance & Build Artifacts (Android `./gradlew assembleDebug` & iOS `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64`)
- Gate 6: Security & Zero Secret Hygiene

---

## 7. Standar Pengarsipan Evidence (Dual-Platform: Android & iOS)
- **Konsol:** `.docs/evidence/{XX}/console-evidence.txt` (Log build & test Android + iOS)
- **Android Visual:** `.docs/evidence/{XX}/android/screenshots/` dan `demo.mp4`
- **iOS Visual:** `.docs/evidence/{XX}/ios/screenshots/` dan `demo.mp4`
```

---

### Template Dokumen Tasklist (`.docs/task/XX_tasklist.md`)
```markdown
# Task Implementation Plan: {Nama Fitur} (Modul {XX})

**Nomor Dokumen:** {XX}_tasklist  
**Target Rilis / Milestone:** MVP v0.1  
**Dokumen PRD:** [PRD {XX}: ...](../prd/{XX}_prd.md)  
**Dokumen TRD:** [TRD {XX}: ...](../trd/{XX}_trd.md)  
**Arsitektur Target:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP)  
**Target Arsip Evidence:** `.docs/evidence/{XX}/README.md`  

---

## 1. Ikhtisar & Ringkasan Task

| Task ID | Issue GitHub | Judul Task | Layer / Source Set | Kompleksitas | Dependensi |
|---|---|---|---|---|---|
| **TASK-01** | (Pending Gate 2) | Data Models & Kontrak Serialisasi | `commonMain` (Domain/Model) | Low (2 SP) | - |
| **TASK-02** | (Pending Gate 2) | Platform Bridge expect/actual (Android & iOS) | `commonMain`, `androidMain`, `iosMain` | High (5 SP) | TASK-01 |
| **TASK-03** | (Pending Gate 2) | Unit Tests & Verifikasi Build Dual Platform (Android & iOS) | `commonTest`, `androidMain`, `iosMain` | Medium (3 SP) | TASK-01..02 |

---

## 2. Rincian Task & Acceptance Criteria (4-Pillar + Build & Evidence Verification Wajib)

### TASK-03: Unit Tests & Verifikasi Build Dual Platform (Android & iOS) — (Pending Gate 2)
- **Layer:** `commonTest`, `androidMain`, `iosMain`
- **Kompleksitas:** Medium (3 SP)
- **Problem Statement:** ...
- **Technical Context:** ...
- **Acceptance Criteria (AC):**
  - • **Core Business Logic:** Seluruh test suite lolos di JVM/Native.
  - • **UI/UX States & Tokens:** Tata letak teruji di resolusi target 375x812 dp.
  - • **Observability & Telemetry:** Format Trace ID & logging valid.
  - • **Product Analytics:** Payload schema events valid.
  - • **Build Artifact & Verification (Dual Platform):**
    - Android: `./gradlew assembleDebug` menghasilkan `composeApp-debug.apk`.
    - iOS: `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` menghasilkan `ComposeApp.framework`.
  - • **Dual-Platform Evidence Archive:**
    - Menyimpan log eksekusi build/test di `.docs/evidence/{XX}/console-evidence.txt`.
    - Menyimpan rekaman dan tangkapan layar Android di `.docs/evidence/{XX}/android/`.
    - Menyimpan rekaman dan tangkapan layar iOS di `.docs/evidence/{XX}/ios/`.
```
