---
description: Bikin Technical Requirement Document (TRD) OpenCampus Mobile berbasis PRD (.docs/00_fase/01_PRD.md) + Figma (drilling UI/UX via fig-decode atau MCP) + codebase, dengan struktur arsitektur 5 Fase (Kebutuhan & Logika Bisnis, UI/UX Design Handoff, Perilaku Sistem & Telemetri, Struktur Data & Kode, Arsitektur Fisik) ke .docs/trd/. Fase pra-implementasi sebelum coding fitur baru.
---

# TRD Command (Phase 1: Pre-Implementation — Mobile Architecture)

Generate a Technical Requirement Document for: `$ARGUMENTS`

This command produces a mobile-side TRD grounded in **PRD (`.docs/00_fase/01_PRD.md`) + Figma (decoded via `fig-decode` or Figma MCP) + codebase + (optional) YouTube API / Storage contracts**, following the 5-Phase Mobile Architecture framework tailored for **OpenCampus Mobile** (`oss-elearning`).

---

## Instructions

### Step 1: Gather Inputs

**Primary inputs**: PRD (`.docs/00_fase/01_PRD.md`) · Figma (`design_system/assets/Design System _ Ui Kit Free (Community).fig` or live Figma URL) · existing codebase.

#### 1a. PRD (Source of Truth)
- Check `.docs/00_fase/01_PRD.md` and related phase docs in `.docs/01_fase/` and `.docs/02_fase_paralel/`.
- Extract: requirements table, acceptance criteria, business rules, user flows, data schema, client events, exact Indonesian copy, and embedded Figma node references.

#### 1b. Figma — Drill the Flow, Don't Guess (Critical for Fase 2)
**Priority order:**
1. **Local `.fig` + `fig-decode`** — check `design_system/assets/Design System _ Ui Kit Free (Community).fig` (and `~/Downloads/*.fig` as fallback).
   - Use the project's skill `fig-decode` (`.agents/skills/fig-decode/SKILL.md`) to extract the node tree.
   - Drill node-id from colon form (e.g. `1842:25160`) or dash form (`1842-25160`).
   - Extract child SECTION/FRAME names, decision points, and component visual states (*Default, Focused, Loading, Error, Empty, Disabled*).
   - **Record re-drillable node IDs** in the TRD for design handoff.
2. **`mcp__figma__get_file_nodes`** as cloud bridge when a live Figma URL with access token exists.
3. **Existing UI/UX specs**: Reference [`.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md`](../../.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) and [`.docs/02_fase_paralel/01_module/uiux/`](../../.docs/02_fase_paralel/01_module/uiux/).

#### 1c. Data & API Contracts
- Data Kurikulum statis (JSON kurasi semester 1–8 untuk Teknik Informatika).
- Kontrak YouTube Iframe Player API & TOS compliance (atribusi channel kreator & link "Tonton di YouTube").
- Local Progress Storage (SQLite / Key-Value persistensi 85% completion rule).

---

### Step 2: Analyze the Codebase (Ground-Truth Before Writing)

1. Review module architecture: Presentation/UI, State/Controller (BLoC/Provider), Domain/UseCases, Data/Repository, Local Storage, Telemetry.
2. Grep across repository for existing implementations, data models, routes, or assets.
3. **Verify symbols exist** — grep before writing. Plan docs are intent, code is truth (`doc-from-shipped-tree-not-plan`).
4. Map current state → target state with concrete file paths.

---

### Step 3: Generate TRD

- Path: `.docs/trd/TRD-{Feature-Name}.md` (kebab-case feature name).
- Format: Standard 5-Phase Architecture with ASCII diagrams and 4-pillar Acceptance Criteria.

---

### Step 4: Commit and Push

- Commit conventional by `LAYER`: `docs(LAYER): add <Feature> TRD — <one-line highlight>`
- Author: `Tommy Dwi Putranto` (`tommyputranto1@gmail.com`).
- Zero AI attribution.
- Push to current branch (default `main` or active feature branch):
  ```bash
  git push -u origin <branch>
  ```

---

## TRD Template (5-Phase Architecture)

```markdown
# Technical Requirement Document (TRD) — OpenCampus Mobile
## {Feature Name} — {one-line scope}

**Document Version:** 1.0  
**Date:** {YYYY-MM-DD}  
**Author:** Technical Team (Mobile — Tommy Dwi Putranto)  
**Status:** Draft / In Review  
**PRD Reference:** [.docs/00_fase/01_PRD.md](../../00_fase/01_PRD.md) — {status}  
**Figma Reference:** [`Design System _ Ui Kit Free (Community).fig`](../../design_system/assets/Design%20System%20_%20Ui%20Kit%20Free%20%28Community%29.fig) — canvas `{NNNN:NNNN}`  
**Design Tokens:** [.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md](../../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Target Platform:** Mobile (Android & iOS — Viewport 375x812 dp)  

---

## 1. Executive Summary & Scope

### 1.1 Overview
{2-3 kalimat ringkas — apa yang dibangun dan mengapa dalam konteks mobile learning OpenCampus}

### 1.2 Current Implementation (Grounded in Code)
{Daftar kondisi riil kode/dokumen saat ini dengan path file konkret. Sebutkan apa yang SUDAH ada dan apa yang BELUM ada.}

### 1.3 Target Implementation
{Daftar bernomor — setiap poin memetakan komponen mobile yang terpengaruh}

### 1.4 Scope (In Scope vs Out of Scope)
- **In Scope:** {Item atomic yang akan diimplementasikan}
- **Out of Scope:** {Fitur yang ditunda pasca-MVP sesuai PRD §3}

---

## FASE 1: KEBUTUHAN & LOGIKA BISNIS

### 1.1 Use Case Diagram (System Boundary & Actors)
*Batasan sistem mobile, aktor (Pembelajar Mandiri, OpenCampus App, Local Storage, YouTube Player, Telemetri).*

```text
{ASCII Use Case Diagram showing mobile boundary, actors, user goals, and <<include>> / <<extend>> relationships}
```

### 1.2 Activity Diagram (Alur Proses Logis & Percabangan)
*Alur data antar swimlane: User, UI View/Screen, Controller/Bloc, Repository, Local Storage, YouTube Player.*

```text
{ASCII Activity Diagram with Swimlanes showing all decision points, validation, and fallback branches}
```

---

## [PARALEL] FASE 2: DESAIN ANTARMUKA (UI/UX) — *Drilling Figma & Design System*

### 2.1 User Flow & Wireflow
- **Entry Point:** Route navigasi aplikasi (misal `SemesterHomeScreen`, `SyllabusTopicScreen`, `VideoPlayerSheet`).
- **Back-Stack Policy:** Aturan pop navigasi saat selesai atau batal.
- **Navigasi Percabangan:** Alur pergantian tab semester, pembukaan detail mata kuliah, dan pemutaran video.

### 2.2 Wireframe (Lo-Fi) & Layout Structure
- Struktur layout responsive standar 375x812 dp (rasio 9:19.5): Safe area insets (dynamic notch & home indicator), Header pill tabs, Course cards, Bottom sheet.

### 2.3 UI Design System & Tokens
*Seluruh komponen wajib mematuhi tokens resmi dari Design System & UI Kit Free:*
- **Color Tokens:** `Corporate/Purple` (`#9D3FE7`), `Corporate/DarkPurple` (`#602093`), `Informing/Approval` (`#00B998`), `Neutral/White` (`#FFFFFF`), `Neutral/DarkGrey` (`#1F1F1F`).
- **Typography Tokens:** Poppins (Heading H1-H4, Body Regular/Medium, Label).
- **Spacing & Radius:** Radius 4px, 8px, 12px, 9999px (pill). Elevasi bayangan: `0 4px 16px rgba(157, 63, 231, 0.08)`.

### 2.4 Design Handoff (Frames & Component States)
| Screen / Komponen | Figma Frame / Symbol | Node ID / Tautan | Visual States Tersedia |
|---|---|---|---|
| {Screen Name} | `{Frame Name}` | [`{NODE_ID}`](file://...) | Default, Focused, Loading, Error, Empty, Disabled |

---

## [PARALEL] FASE 3: PERILAKU SISTEM & TELEMETRI

### 3.1 Sequence Diagram (+ Trace ID & Client Events)
*Interaksi sinkron/asinkron antar-layer mobile, propagasi distributed tracing, dan telemetry logging.*

```text
{ASCII Sequence Diagram showing Screen -> Controller -> Repo -> Storage/Player,
 annotating trace_id format (trc_<action>_<timestamp>_<hex> or W3C traceparent) and client events}
```

#### 5 Non-Happy Path Scenarios (Wajib):
- **E1 (Network Stall / Offline):** Koneksi terputus saat memuat video $\to$ inline warning banner tanpa reset progress lokal.
- **E2 (YouTube Video Error / Private / Removed):** Player menangkap error code 100/101/150 $\to$ tampilkan state *"Video materi sedang diperbarui"* + tombol *"Laporkan Link"* (PRD §5.2).
- **E3 (OS Process Death / Low Memory Killer):** State restoration via local storage / persistent state.
- **E4 (User Cancellation saat Loading):** Batalkan pemutaran dan kembalikan state UI secara bersih.
- **E5 (Corrupted Local Storage / Migration):** Fallback ke default guest state dan log telemetry error.

### 3.2 State Diagram (+ State Metrics & Drop-offs)
*Memetakan alur UiState, latensi transisi, dan identifikasi titik potensi drop-off/churn pengguna.*

```text
{ASCII State Diagram showing state transitions, latency metrics, and drop-off failure events}
```

---

## FASE 4: STRUKTUR DATA & KODE (Clean Architecture)

### 4.1 Class Diagram (Clean Architecture)
```text
{ASCII Class Diagram showing:
 - Presentation: Screen, Controller/Bloc, UiState
 - Domain: UseCases, Entities, Repository Interfaces
 - Data: RepositoryImpl, LocalDataSource, StorageClient
 - Telemetry: TraceIdGenerator, AnalyticsService}
```

### 4.2 Data Storage, Schema & Idempotency
- **Curriculum Schema (JSON):** Model `Semester`, `Course`, `Topic`, `VideoMetadata`.
- **Local Progress Storage (SQLite / KV):** Menyimpan pasangan `topic_id` dan `completed_at`.
- **Idempotency Rule:** Mencegah double-recording event penyelesaian video (85% auto-complete trigger).

---

## FASE 5: ARSITEKTUR FISIK RUNTIME MOBILE

### 5.1 Component Diagram (Runtime Ecosystem)
```text
{ASCII Component Diagram showing:
 OpenCampus Mobile App
  -> Presentation Layer (UI Widgets)
  -> State Management (BLoC / Controller)
  -> Curriculum Repository & JSON Parser
  -> Embedded YouTube Player (Iframe SDK)
  -> Local Progress Database (SQLite / KV)
  -> Telemetry & Analytics Service}
```

---

## 6. Testing Requirements & 7 Local Verification Gates

- **Gate 1 (Linter & Formatters):** `flutter analyze` / `npm run lint` nol warning.
- **Gate 2 (Data & API Contracts):** Validasi JSON schema kurikulum & YouTube player contract.
- **Gate 3a (Unit Tests):** Unit testing logika agregasi semester & progress tracking 85% rule (coverage $\ge 80\%$).
- **Gate 3b (Negative Control):** Rusakkan 1 logika $\to$ RED $\to$ pulihkan $\to$ GREEN. Laporkan rasio `N/N → (N-1)/N → N/N` + nama method merah.
- **Gate 4 (Component & Viewport):** Uji viewport 375x812 dp (9:19.5) dan tema Light/Dark.
- **Gate 5 (Performance):** Audit bundle size dan asset rendering.
- **Gate 6 (Security & Secret Hygiene):** Pastikan nol exposed private API key.

---

## 7. Proposed GitHub Issues / Tasks (4-Pillar AC)

Setiap tiket dirancang atomic dengan format commit konvensional oleh `LAYER` dan 4 Pilar Acceptance Criteria:

```text
├── Problem Statement
├── Technical Context (Diagram Reference / Figma Node / Codebase File)
└── Acceptance Criteria (AC):
      • Core Business Logic
      • UI/UX States & Tokens
      • Observability & Telemetry (Trace ID, client events)
      • Product Analytics (Funnel Events)
```

| # | Task Title | Layer | Complexity | Est. |
|---|---|---|---|---|
| T1 | `feat(LAYER): ...` | Domain/Data/Presentation | Low/Medium/High | SP / Jam |

---

## Appendix — Decoded Figma References
| Component / Screen | Node ID | Deskripsi Handoff |
|---|---|---|
| {Screen Name} | `{NODE_ID}` | Tautan atau path ekstraksi fig-decode |
```
