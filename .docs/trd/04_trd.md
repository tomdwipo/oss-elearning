# TRD 04: Detail View UI Parity, Layout Responsiveness, & Dual-Platform Visual Verification (Modul 4)

**Nomor Modul / TRD:** 04  
**Fitur Utama:** Rekayasa Presisi Tampilan Detail View, Perbaikan Layout Skiko iOS (Badge & Dialog), dan Verifikasi Visual Roborazzi Dual-Platform (Android & iOS)  
**Target Platform:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP 1.7.3 / Kotlin 2.1.0)  
**Dokumen Asesmen Terkait:** [Dual-Platform UI & Pixel-by-Pixel Assessment (`.docs/assesment/03_dual_platform_ui_pixel_assessment.md`)](../assesment/03_dual_platform_ui_pixel_assessment.md)  
**Dokumen PRD Terkait:** [PRD 03: Comprehensive Detail View Audit & Dual-Platform UI Parity](../prd/03_prd.md), [PRD 02: Video Player](../prd/02_prd.md), [PRD 00: Global MVP](../prd/00_prd.md)  
**Dokumen Master Desain:** [Design System UI Kit Free](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Target Arsip Evidence:** `.docs/evidence/04/README.md`  
**Penulis:** Technical Team (Mobile — Tommy Dwi Putranto)  
**Status Dokumen:** Lengkap (Tahap 2 — Menunggu Human Review Gate 2)  

---

## 1. Executive Summary & Scope

### 1.1. Overview & Arsitektur Ringkas
Pada rilis Modul 3, integrasi kurikulum aktif video YouTube berhasil mencapai status $100\%$ pemutaran streaming nyata pada Android dan iOS. Namun, evaluasi komparasi piksel dan visual ([`.docs/assesment/03_dual_platform_ui_pixel_assessment.md`](../assesment/03_dual_platform_ui_pixel_assessment.md)) mengidentifikasi 2 (dua) inkonsistensi layout visual (*UI layout glitches*) yang terjadi secara eksklusif pada target iOS (Skiko engine):
1. **Penyempitan Kolom Badge Status Progres:** Teks status `"Belum Selesai"` pada kartu detail video ([`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)) mengalami pemenggalan karakter vertikal (*letter-by-letter wrap*).
2. **Form Dialog Tumpang Tindih (Overlapping):** Kotak input teks pada dialog pelaporan link rusak ([`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)) menimpa radio button opsi ke-4 (*"Lainnya"*) dan menutupi label catatan akibat batas ketinggian dialog Skiko di iOS.

TRD 04 ini menetapkan rancangan teknis dan arsitektur perbaikan UI responsif, standarisasi constraint Compose Multiplatform lintas platform, serta verifikasi regresi visual otomatis berbasis snapshot Roborazzi.

### 1.2. Current Implementation (Grounded in Code)
- **Checklist Bar di `VideoPlayerScreen.kt#L336-L400`:**
  - Baris checklist kiri `Row(verticalAlignment = Alignment.CenterVertically)` tidak memiliki modifier `weight(1f)`.
  - Font SF Pro di iOS memiliki *tracking* lebih lebar dibandingkan Roboto di Android, menyebabkan Row kiri mengonsumsi $\approx 92\%$ lebar kontainer kartu.
  - Sisa lebar horizontal yang diterima `Box` badge di sebelah kanan $< 20\text{ dp}$, sehingga string `"Belum Selesai"` terbungkus vertikal menjadi kolom panjang per huruf.
- **Form Dialog di `ReportBrokenVideoDialog.kt#L68-L126`:**
  - `AlertDialog` Material3 di Skiko iOS memiliki batas kanvas vertikal tetap tanpa auto-scroll.
  - `Column` di dalam slot `text` dialog tidak menerapkan `Modifier.verticalScroll(rememberScrollState())`.
  - Total akumulasi tinggi elemen melebihi batas dialog, memicu `OutlinedTextField` digambar menindih radio button ke-4 (*"Lainnya"*) dan menyembunyikan label *"Catatan Tambahan (Opsional):"*.

### 1.3. Target Implementation
- **Layout Responsif Video Player:**
  - Menetapkan `Modifier.weight(1f).padding(end = 8.dp)` pada baris label checklist manual dengan pembatasan `maxLines = 1` dan `overflow = TextOverflow.Ellipsis`.
  - Mengunci badge status kanan dengan `maxLines = 1` dan `softWrap = false` serta padding horizontal $8\text{ dp}$ agar selalu tampil rapi dalam satu baris horizontal.
- **Scrollable & Adaptive Dialog Container:**
  - Mengintegrasikan `Modifier.verticalScroll(rememberScrollState())` pada `Column` konten `ReportBrokenVideoDialog`.
  - Memastikan seluruh opsi radio button, label, dan input teks terpisah secara hierarki dengan spacing yang konsisten pada semua resolusi viewport.
- **Dual-Platform Snapshot & Visual Parity Verification:**
  - Menjalankan build Android (`assembleDebug`) dan iOS Simulator ARM64 (`linkDebugFrameworkIosSimulatorArm64`).
  - Menghasilkan artefak komparasi Roborazzi di `.docs/evidence/04/` untuk membuktikan perbaikan telah bebas overlap dan 100% konsisten di kedua platform.

### 1.4. Scope (In Scope vs Out of Scope)
- **In Scope:**
  - Refactoring layout dan constraint responsif pada [`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt).
  - Penambahan scrollable container dan perbaikan spacing pada [`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt).
  - Unit test suite dan snapshot validation untuk memvalidasi stabilitas state UI.
  - Rekaman walkthrough video demo dan tangkapan layar verifikasi di `.docs/evidence/04/` (Android & iOS).
- **Out of Scope:**
  - Perubahan data kurikulum atau penambahan endpoint backend baru.
  - Perubahan arsitektur dasar platform bridge `PlatformVideoPlayer`.

---

## FASE 1: KEBUTUHAN & LOGIKA BISNIS

### 1.1 Use Case Diagram (System Boundary & Actors)

```text
+===================================================================================================+
|                                    OPENCAMPUS MOBILE SYSTEM BOUNDARY                              |
|                                                                                                   |
|   +-------------------+                                                                           |
|   |                   | ---- (UC-01: Buka Detail Pemutar Video)                                  |
|   |                   |                                                                           |
|   |                   | ---- (UC-02: Lihat Detail Materi & Status Progres)                       |
|   |                   |        │                                                                  |
|   |                   |        ├── <<include>> ---> (UC-02a: Render Checklist Horizontal)         |
|   |                   |        └── <<include>> ---> (UC-02b: Render Badge Pill Non-Wrapping)     |
|   |                   |                                                                           |
|   |                   | ---- (UC-03: Toggle Centang Progres Manual)                              |
|   |                   |                                                                           |
|   |   Pembelajar      | ---- (UC-04: Buka Dialog Pelaporan Link Rusak)                           |
|   |   (Mobile User)   |        │                                                                  |
|   |                   |        ├── <<include>> ---> (UC-04a: Pilih Alasan Kerusakan)              |
|   |                   |        ├── <<include>> ---> (UC-04b: Input Catatan Tambahan Bebas Tabrakan)|
|   |                   |        └── <<include>> ---> (UC-04c: Scroll Konten Form Dialog)           |
|   |                   |                                                                           |
|   |                   | ---- (UC-05: Kirim Laporan Link Rusak)                                    |
|   |                   |                                                                           |
|   +-------------------+                                                                           |
|             │                                                                                     |
+=============│=====================================================================================+
              │
              ▼
+------------------------------------+      +------------------------------------+
|         Local Storage              |      |         Skiko / Skia Engine        |
|    (Multiplatform Settings)        |      |  (Layout & Typography Measurement) |
+------------------------------------+      +------------------------------------+
```

### 1.2 Activity Diagram (Alur Proses & Decision Points)

```text
       ( User Membuka Halaman VideoPlayerScreen )
                          │
                          ▼
            [ Ukur Lebar Kartu Kontainer ]
                          │
                          ▼
      +───────────────────────────────────────+
      │ Alokasikan Space via Modifier.weight  │
      +───────────────────────────────────────+
                          │
        ┌─────────────────┴─────────────────┐
        ▼                                   ▼
 [ Render Sisi Kiri: ]              [ Render Sisi Kanan: ]
 [ Checkbox + Teks Label ]          [ Badge Status Pill ]
 [ (weight=1f, Ellipsis) ]          [ (maxLines=1, nowrap)]
        │                                   │
        └─────────────────┬─────────────────┘
                          │
                          ▼
             [ Tampilan Checklist Rapi & ]
             [ Sejajar Secara Horizontal ]
                          │
                          ▼
             ( User Klik "Laporkan Link" )
                          │
                          ▼
             [ Tampilkan AlertDialog ]
                          │
                          ▼
      [ Inisialisasi Column + verticalScroll ]
                          │
                          ▼
             < Apakah Konten Melebihi >
             < Tinggi Maksimum Dialog? >
                 /                \
          [ YA ]/                  \[ TIDAK ]
               ▼                    ▼
     [ Aktifkan Scrollbar ]  [ Render Seluruh Elemen ]
     [ Tetap Bebas Tumpang]  [ Bersih Berjarak 12dp  ]
     [ Tindih Antar-Field ]  [                       ]
               │                    │
               └──────────┬─────────┘
                          │
                          ▼
             [ User Memilih Alasan & ]
             [ Mengetik Catatan Form ]
                          │
                          ▼
             ( Kirim Laporan / Selesai )
```

### 1.3 Tabel Sitasi & Rujukan PRD §4

| No | Kebutuhan PRD 03 | ID Sitasi | Target Komponen / Kode | Rujukan Token Desain UI Kit |
|---|---|---|---|---|
| **1** | Layout Responsif Checklist Progres | PRD 03 §3 (FR-1) | [`VideoPlayerScreen.kt#L336-L400`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L336-L400) | `CorporatePurple` (`#9D3FE7`), `InformingApproval` (`#27AE60`), Radius 8dp |
| **2** | Badge Status Anti-Wrap | PRD 03 §3 (FR-1) | [`VideoPlayerScreen.kt#L383-L400`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L383-L400) | Pill Capsule (Radius 9999dp), Text `11.sp` Bold |
| **3** | Dialog Scrollable & Anti-Overlap | PRD 03 §3 (FR-2) | [`ReportBrokenVideoDialog.kt#L68-L126`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt#L68-L126) | `AlertDialog` Material3, Radius 16dp, OutlinedTextField |
| **4** | Paritas Tipografi Dual-Platform | PRD 03 §3 (FR-3) | [`Theme.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/theme/Theme.kt) | Standar Token Skia (Android) & Skiko SF Pro (iOS) |

---

## [PARALEL] FASE 2: DESAIN ANTARMUKA (UI/UX)

### 2.1 User Flow & Layout Constraints
```text
[ Course Syllabus ] ──(Click Topic Item)──> [ Video Player Screen ]
                                                    │
                      ┌─────────────────────────────┴─────────────────────────────┐
                      ▼                                                           ▼
         [ Toggle Manual Checklist ]                                 [ Open Report Dialog ]
                      │                                                           │
                      ▼                                                           ▼
         ┌─────────────────────────┐                                ┌───────────────────────────┐
         │ State: Completed        │                                │ Scrollable Modal Dialog   │
         │ - Box Checkbox (Checked)│                                │ - 4 Radio Buttons         │
         │ - Badge: "Selesai" (11sp│                                │ - Optional Notes (3 lines)│
         │   InformingApproval)    │                                │ - Actions: Batal / Kirim  │
         └─────────────────────────┘                                └───────────────────────────┘
```

### 2.2 UI Design System & Tokens
* **Corporate Primary:** `CorporatePurple` (`#9D3FE7`) — Aksen radio button, button primer, active speed.
* **Success / Approval:** `InformingApproval` (`#27AE60`) — Checkbox border checked, pill background `12%` alpha, badge font.
* **Danger / Error:** `InformingError` (`#EB5757`) — Tombol lapor broken link.
* **Neutral / Surface:** `GrayscaleWhite` (`#FFFFFF`), `GrayscaleBgLightGrey` (`#F8F9FA`), `GrayscaleSpacer` (`#F0F1F3`).
* **Radius Tokens:** 8.dp (kartu checklist & text field), 16.dp (modal container), 9999.dp (badge pill).

### 2.3 Matriks 6 Visual States (Design Handoff)

| Komponen UI | Default | Focused | Loading | Error | Empty | Disabled |
|---|---|---|---|---|---|---|
| **Checklist Row** | Background `#F8F9FA`, Checkbox uncheck | Border highlight | Shimmer skeleton | N/A | N/A | Alpha 0.5f |
| **Badge Status** | Pill `#F0F1F3`, Text `"Belum Selesai"` (`#8A90A2`) | N/A | N/A | N/A | N/A | N/A |
| **Badge Selesai** | Pill `#27AE60` (12%), Text `"Selesai"` (`#27AE60`) | N/A | N/A | N/A | N/A | N/A |
| **Dialog Radio Item** | Unselected (Border `#E2E4E8`) | Focus Ring | N/A | N/A | N/A | Disabled grey |
| **Dialog TextField** | Placeholder `"Contoh: Video bermasalah..."` | Active Border `#9D3FE7` | N/A | Border `#EB5757` | Placeholder text | Readonly |
| **Tombol Kirim** | Purple Solid `#9D3FE7` | Ripple Effect | Spinner indicator | Toast galat | N/A | Disabled Grey |

---

## [PARALEL] FASE 3: PERILAKU SISTEM & TELEMETRI

### 3.1 Sequence Diagram & Telemetry Events

```text
User            VideoPlayerScreen        VideoPlayerViewModel        ProgressRepository       MultiplatformSettings
 │                      │                         │                           │                         │
 │─── Open Topic ──────>│                         │                           │                         │
 │                      │── Init(course, topic) ─>│                           │                         │
 │                      │                         │── isTopicCompleted(id) ──>│                         │
 │                      │                         │                           │── getBoolean(key) ─────>│
 │                      │                         │                           │<─ false (Belum Selesai)─│
 │                      │                         │<─ ProgressState(false) ───│                         │
 │                      │<─ UiState(isComp=false)─│                           │                         │
 │                      │   (Render Pill Horiz)   │                           │                         │
 │                      │                         │                           │                         │
 │── Toggle Checklist ─>│                         │                           │                         │
 │                      │── toggleManual(id) ────>│                           │                         │
 │                      │                         │── setTopicCompleted(true)─>│                         │
 │                      │                         │   [Event: topic_toggled]  │── putBoolean(key,true)─>│
 │                      │                         │   [Trace: trc_toggle_123] │<─ Ack ──────────────────│
 │                      │<─ UiState(isComp=true) ─│                           │                         │
 │                      │   (Badge "Selesai")     │                           │                         │
 │                      │                         │                           │                         │
 │── Open Report ──────>│                         │                           │                         │
 │                      │── openReportDialog() ──>│                           │                         │
 │                      │<─ UiState(dialog=true) ─│                           │                         │
 │                      │   (Show Scroll Dialog)  │                           │                         │
 │── Submit Report ────>│                         │                           │                         │
 │                      │── submitReport(reason) ─>│                           │                         │
 │                      │                         │── [Log Telemetry: report] │                         │
 │                      │<─ UiState(dialog=false) │                           │                         │
```

### 3.2 5 Non-Happy Path Scenarios (E1–E5)

1. **E1 (Extreme Narrow Viewport / Small Device):** Pada layar dengan lebar sempit ($< 320\text{ dp}$), teks *"Tandai Selesai (Manual)"* terpotong elipsis rapi (`...`), sementara badge *"Belum Selesai"* tetap utuh horizontal tanpa wrap vertikal.
2. **E2 (Extended Multiline Report Reason):** Jika pengguna memilih opsi dan mengetik teks catatan panjang ($> 5$ baris), kontainer dialog otomatis memunculkan scrollbar vertikal tanpa mendorong tombol *"Kirim Laporan"* atau menutupi radio button.
3. **E3 (Orientation Change / Screen Resize):** Saat orientasi berubah ke landscape, dialog pelaporan tetap berada dalam bounds layar dan seluruh input field dapat di-scroll dengan mulus.
4. **E4 (Rapid Toggling Checklist):** Aksi toggle berulang-ulang ditangani secara atomik oleh `StateFlow` tanpa mengalami race condition atau *text flickering*.
5. **E5 (Process Recreation / Configuration Change):** State input form dialog (`selectedReason` dan `notes`) terjaga saat terjadi recomposition atau konfigurasi layar berubah.

---

## FASE 4: STRUKTUR DATA & KODE (Clean Architecture)

### 4.1 Class & Component Structure

```text
+-----------------------------------------------------------------------------------+
|                              PRESENTATION LAYER (UI)                              |
|                                                                                   |
|  +-------------------------------------+   +------------------------------------+ |
|  |         VideoPlayerScreen           |   |       ReportBrokenVideoDialog      | |
|  | - LazyColumn + Responsive Row       |   | - AlertDialog + verticalScroll     | |
|  | - Weight(1f) label + Non-wrap badge |   | - RadioButton items + Spacing 12dp | |
|  +-------------------------------------+   +------------------------------------+ |
|                                   │             │                                 |
|                                   ▼             ▼                                 |
|  +------------------------------------------------------------------------------+ |
|  |                             VideoPlayerViewModel                             | |
|  | - uiState: StateFlow<VideoPlayerUiState>                                     | |
|  | - toggleManualCompletion() / openReportDialog() / submitReport(...)          | |
|  +------------------------------------------------------------------------------+ |
+───────────────────────────────────────────┬───────────────────────────────────────+
                                            │
                                            ▼
+-----------------------------------------------------------------------------------+
|                                DOMAIN & DATA LAYER                                |
|                                                                                   |
|  +------------------------------------+    +------------------------------------+ |
|  |          ProgressRepository        |    |          CurriculumRepository      | |
|  | - isTopicCompleted(id: String)     |    | - getCourseById(id: String)        | |
|  | - setTopicCompleted(id, status)    |    | - getTopicById(courseId, topicId)  | |
|  +------------------------------------+    +------------------------------------+ |
+-----------------------------------------------------------------------------------+
```

---

## FASE 5: ARSITEKTUR FISIK RUNTIME MOBILE

```text
+-----------------------------------------------------------------------------------+
|                        COMPOSE MULTIPLATFORM RUNTIME ECOSYSTEM                    |
|                                                                                   |
|        +------------------------------------------------------------------+       |
|        |                   composeApp (commonMain)                        |       |
|        |  • VideoPlayerScreen (Layout Constraints & Weighting)            |       |
|        |  • ReportBrokenVideoDialog (Scrollable Column)                   |       |
|        +------------------------------------------------------------------+       |
|                             │                            │                        |
|              ┌──────────────┴──────────────┐             │                        |
|              ▼                             ▼             │                        |
|  +-----------------------+     +-----------------------+ │                        |
|  |  Android Runtime      |     |  iOS Runtime (Skiko)  | │                        |
|  |  • Skia Graphics      |     |  • Skiko CoreText     | │                        |
|  |  • Roboto Font Metric |     |  • SF Pro Font Metric | │                        |
|  |  • Android Dialog Win |     |  • UIKitView Overlay  | │                        |
|  +-----------------------+     +-----------------------+ │                        |
+--------------------------------──────────────────────────┼────────────────────────+
                                                           │
                                                           ▼
                                            +------------------------------+
                                            | Roborazzi Visual Diff Engine |
                                            | (Snapshot Parity Validator)  |
                                            +------------------------------+
```

---

## 6. Testing Requirements & 7 Local Verification Gates

- **Gate 1: Linter & Formatters:** `./gradlew composeApp:lintDebug` $\to$ Exit 0 (0 error lint).
- **Gate 2: Data & API Contracts:** `./gradlew test --tests "*CurriculumVideoContractTest*"` $\to$ 4/4 contract tests passed.
- **Gate 3a: Unit Tests:** `./gradlew test` $\to$ 26/26 tests passed di `commonTest`.
- **Gate 3b: Negative Control Wajib:**
  - *Mutasi:* Hapus `Modifier.weight(1f)` dari checklist row di unit test visual / contract validation $\to$ deteksi overflow string.
  - *Verifikasi:* RED $\to$ pulihkan $\to$ GREEN. Rasio: `26/26 → 25/26 → 26/26`.
- **Gate 4: Component & Viewport Parity:** Pengujian viewport $1280 \times 2856$ (Pixel 9 Pro) dan $1206 \times 2622$ (iPhone 16 Pro) memastikan badge horizontal dan form bebas overlap.
- **Gate 5: Performance & Packaging:**
  - Android: `./gradlew assembleDebug` $\to$ `composeApp-debug.apk` sukses terkompilasi.
  - iOS: `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` $\to$ `ComposeApp.framework` sukses terkompilasi.
- **Gate 6: Security & Secret Hygiene:** `git diff` bersih dari kredensial hardcoded atau API key rahasia.

---

## 7. Standar Pengarsipan Evidence (Dual-Platform: Android & iOS)

```text
.docs/evidence/04/
├── README.md               # Ringkasan 7 gerbang, tabel verifikasi Android vs iOS, rasio Gate 3b
├── console-evidence.txt    # Log eksekusi build & test (Android APK + iOS Framework)
├── visual_diff_roborazzi/  # Artefak komparasi visual 3-kolom (Android | iOS | Red Diff Mask)
├── android/
│   ├── demo.mp4            # Video walkthrough Android (Pixel 9 Pro)
│   └── screenshots/        # Tangkapan layar Android (01_player.png, 02_dialog.png)
└── ios/
    ├── demo.mp4            # Video walkthrough iOS (iPhone 16 Pro)
    └── screenshots/        # Tangkapan layar iOS (01_player_fixed.png, 02_dialog_fixed.png)
```
