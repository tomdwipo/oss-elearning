# PRD 03: Perbaikan UI & Detail View Audit Dual-Platform (Android & iOS)

**Nomor Modul / PRD:** 03  
**Fitur Utama:** Audit Detail View & Perbaikan Keselarasan UI Lintas Platform (Android & iOS)  
**Target Rilis:** v0.2.1  
**Platform:** Mobile (Android & iOS — Compose Multiplatform)  
**Dokumen Asesmen Rujukan:** [Asesmen Dual-Platform UI & Piksel (`.docs/assesment/03_dual_platform_ui_pixel_assessment.md`)](../assesment/03_dual_platform_ui_pixel_assessment.md)  
**Dokumen Induk:** [PRD 00 (Global MVP)](./00_prd.md) & [PRD 02 (Video Player & Pelacakan Progres)](./02_prd.md)  

---

## 1. Background & Problem Statement

Berdasarkan hasil asesmen empiris UI dan analisis piksel pada modul video player ([`03_dual_platform_ui_pixel_assessment.md`](../assesment/03_dual_platform_ui_pixel_assessment.md)), ditemukan bahwa fungsionalitas pemutaran video YouTube aktif telah bekerja 100% pada Android dan iOS. Namun, terdapat 2 (dua) inkonsistensi visual dan layout glitch kritis pada layar detail pemutar video di platform iOS (iPhone 16 Pro):

1. **Badge Status Progres Terjepit Vertikal (Layout Glitch 1):**  
   Pada kartu detail materi [`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt), teks status `"Belum Selesai"` tertekan oleh baris checkbox sebelah kiri dan terbungkus per huruf secara vertikal (*letter-by-letter vertical stacking*).
2. **Formulir Dialog Pelaporan Tumpang Tindih (Layout Glitch 2):**  
   Pada modal [`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt), input teks catatan (`OutlinedTextField`) menimpa radio button opsi ke-4 (*"Lainnya"*) dan menutupi label catatan opsional karena ketiadaan kontainer scrollable di dalam batas dialog Skiko iOS.

---

## 2. Tujuan & Sasaran (Objectives)

- **Konsistensi Visual Antar-Platform:** Memastikan seluruh komponen pada *Detail View Video Player* dan *Modal Dialog* tampil konsisten, rapi, dan tidak mengalami tabrakan/glitch di berbagai ukuran layar dan font engine (Roboto di Android & SF Pro di iOS).
- **Eliminasi Regresi Teks Vertikal:** Memastikan badge status progres selalu tampil horizontal dalam 1 baris (`maxLines = 1`, `softWrap = false`).
- **Respon Scroll Dialog Adaptif:** Menjamin formulir pelaporan link rusak memiliki scroll internal yang fleksibel pada semua resolusi viewport dialog.

---

## 3. Ruang Lingkup & Kebutuhan Fungsional (Functional Requirements)

### ✅ Kebutuhan Fungsional (FR):

- **FR-3.1 (Responsive Checklist & Badge Bar):**
  - Kontainer checklist manual harus mengalokasikan ruang proporsional: teks `"Tandai Selesai (Manual)"` diberi bobot fleksibel (`Modifier.weight(1f)`), dan teks badge `"Belum Selesai"` / `"Selesai"` di sisi kanan dijamin tidak mengalami *line-wrap* vertikal.
  - Teks yang terlalu panjang pada layar sempit dipotong dengan elipsis (`TextOverflow.Ellipsis`).
- **FR-3.2 (Scrollable & Responsive Report Dialog):**
  - Konten modal `AlertDialog` pada `ReportBrokenVideoDialog` wajib dibungkus dalam `Column` dengan modifier `verticalScroll(rememberScrollState())`.
  - Jarak (*spacing*) antar-elemen (alasan radio button, label catatan, dan textfield) harus konsisten dan tidak boleh bertabrakan/overlapping pada resolusi tinggi maupun rendah.
- **FR-3.3 (Dual-Platform Visual Parity):**
  - Menguji dan memvalidasi tampilan di Android Emulator (Pixel 9 Pro) dan iOS Simulator (iPhone 16 Pro) agar memenuhi standar token warna, padding, dan tipografi Design System OpenCampus.

---

## 4. User Flow & Wireframe Perbaikan

### 4.1. Detail View Checklist Bar (State: Belum Selesai)

```
[ Layout Normal (Android & iOS) ]
+-------------------------------------------------------------------+
|  [ ]  Tandai Selesai (Manual)                 [ Belum Selesai ]   |
+-------------------------------------------------------------------+
```

### 4.2. Modal Dialog Pelaporan Link Rusak

```
+-------------------------------------------------------------------+
| Laporkan Link Rusak                                               |
| Bantu kurator kami memperbarui materi belajar ini.                |
|                                                                   |
| Pilih Alasan Masalah:                                             |
|  (o) Video Dihapus / Private                                      |
|  ( ) Video Tidak Sesuai Topik                                     |
|  ( ) Audio / Visual Rusak                                         |
|  ( ) Lainnya                                                      |
|                                                                   |
| Catatan Tambahan (Opsional):                                      |
| +---------------------------------------------------------------+ |
| | Contoh: Video bermasalah di menit ke 05:20...                 | |
| +---------------------------------------------------------------+ |
|                                                                   |
|                      [ Batal ]      [ Kirim Laporan ]             |
+-------------------------------------------------------------------+
```

---

## 5. Non-Functional Requirements & Acceptance Criteria

### 🎯 Acceptance Criteria (AC):

1. **AC-1 (Badge Horizontal):** Pada iOS Simulator (iPhone 16 Pro) dan Android Emulator, badge status progres materi berstatus `"Belum Selesai"` ditampilkan utuh secara horizontal dalam 1 baris di dalam container pill tanpa pemenggalan vertikal.
2. **AC-2 (No Dialog Overlap):** Pada iOS dan Android, seluruh 4 opsi radio button terlihat jelas, label *"Catatan Tambahan (Opsional):"* tidak tertutup, dan kotak input teks berada tepat di bawah label tanpa menabrak radio button *"Lainnya"*.
3. **AC-3 (Build & Packaging Green):** Kedua target platform lolos kompilasi tanpa regresi:
   - `./gradlew test` $\to$ 26/26 lulus.
   - `./gradlew composeApp:lintDebug` $\to$ Exit 0.
   - `./gradlew assembleDebug` $\to$ Exit 0.
   - `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` $\to$ Exit 0.
