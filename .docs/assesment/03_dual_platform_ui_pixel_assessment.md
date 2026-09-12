# Dual-Platform UI & Pixel-by-Pixel Assessment (Android vs iOS)

> **Audit Target:** [`composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt) & [`composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)  
> **Evidence Sources:** [`.docs/evidence/03/ios/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/ios/demo.mp4), [`.docs/evidence/03/android/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/android/demo.mp4), and Screenshots in [`.docs/evidence/03/`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/README.md)  
> **Test Environments:** Android Emulator Pixel 9 Pro (API 35, 1280x2856) & iOS Simulator iPhone 16 Pro (iOS 18.5, 1206x2622)  
> **Assessment Date:** 2026-09-12  
> **Auditor Tooling:** Python PIL & NumPy Matrix Analysis, `video-to-image` MCP Frame Extractor  

---

## 1. Executive Summary

Evaluasi perbandingan tampilan antarmuka (UI) dan fungsionalitas lintas platform (*dual-platform verification*) dilakukan terhadap artefak rekaman video demo dan tangkapan layar eksekusi nyata pada modul video kurikulum aktif.

**Temuan Kunci:**
1. **Fungsi Inti Pemutar Video (Live Playback):** **100% Berhasil di Kedua Platform.** Komponen [`PlatformVideoPlayer`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt) berhasil memutar video YouTube terkurasi secara live tanpa kendala layar hitam (*black screen*) baik pada Android (`AndroidView` WebView) maupun iOS (`UIKitView` WKWebView).
2. **Kesesuaian Tampilan Visual (Pixel-by-Pixel & Layout):** **Ditemukan Perbedaan & Glitch Khusus pada iOS.** Meskipun tema warna dan struktur navigasi sejalan dengan Design System, pengujian kuantitatif piksel dan visual membuktikan adanya 2 (dua) regresi layout pada iOS:
   - **Glitch 1:** Badge status *"Belum Selesai"* terjepit dan ter-render vertikal satu per satu huruf (*letter-by-letter wrap*).
   - **Glitch 2:** Form modal dialog *"Laporkan Link Rusak"* tumpang tindih (*overlapping*), di mana input field menimpa radio button *"Lainnya"* dan menyembunyikan label catatan tambahan.

---

## 2. Quantitative Pixel-by-Pixel Comparative Analysis

Analisis piksel dilakukan dengan menormalkan tangkapan layar kedua platform ke resolusi standar kanvas $1080 \times 2400$ piksel, kemudian menghitung *Mean Absolute Error* (MAE), *Mean Squared Error* (MSE), *Peak Signal-to-Noise Ratio* (PSNR), dan persentase piksel berbeda dengan ambang batas toleransi $\Delta > 15/255$ (untuk mengeliminasi noise rendering sub-piksel antialiasing).

### 2.1. Tabel Hasil Uji Komparasi Kuantitatif

| No | Layar / Skenario Pengujian | Android Source | iOS Source | Resolusi Asli Android | Resolusi Asli iOS | MAE (0–255) | MSE | PSNR (dB) | Piksel Berbeda $(\Delta > 15)$ |
|---|---|---|---|---|---|---|---|---|---|
| **1** | **Home Screen** | [`01_home_screen.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/android/screenshots/01_home_screen.png) | [`01_home_screen.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/ios/screenshots/01_home_screen.png) | $1280 \times 2856$ ($0.448$) | $1206 \times 2622$ ($0.460$) | **89.37** | 20,116.32 | 5.10 dB | **45.37%** (1.175.981 px) |
| **2** | **Course Syllabus Screen** | [`02_course_detail.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/android/screenshots/02_course_detail.png) | [`02_course_syllabus.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/ios/screenshots/02_course_syllabus.png) | $1280 \times 2856$ ($0.448$) | $1206 \times 2622$ ($0.460$) | **91.17** | 20,639.30 | 4.98 dB | **61.10%** (1.583.803 px) |
| **3** | **Video Player Active** | [`01_player_active.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/android/screenshots/01_player_active.png) | [`03_video_player_active.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/ios/screenshots/03_video_player_active.png) | $1280 \times 2856$ ($0.448$) | $1206 \times 2622$ ($0.460$) | **136.60** | 28,680.63 | 3.55 dB | **76.18%** (1.974.480 px) |
| **4** | **Report Broken Link Dialog** | [`03_report_dialog.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/android/screenshots/03_report_dialog.png) | [`04_report_dialog.png`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/ios/screenshots/04_report_dialog.png) | $1280 \times 2856$ ($0.448$) | $1206 \times 2622$ ($0.460$) | **72.18** | 10,842.93 | 7.78 dB | **57.61%** (1.493.301 px) |

### 2.2. Dekomposisi Perbedaan Berdasarkan Zona Wilayah Layar

```
+-------------------------------------------------------+
|  Zona 1: Status Bar (Atas 8%) -> 98.6% - 99.8% diff   | (Dynamic Island vs Punch Hole)
+-------------------------------------------------------+
|                                                       |
|  Zona 2: Content Body (Tengah 84%)                    |
|  - Home & Syllabus : 35.1% - 53.8% diff               | (Font Metric & Card Y-Offset)
|  - Video Player    : 71.8% diff                       | (Topik Video & Badge Glitch)
|  - Report Dialog   : 49.6% diff                       | (Form Overlapping)
|                                                       |
+-------------------------------------------------------+
|  Zona 3: Navigation/Insets (Bawah 8%) -> 99.7% diff   | (Home Indicator vs Gesture Bar)
+-------------------------------------------------------+
```

1. **Zona Status Bar (Top 8% Layar):**
   - Perbedaan piksel mencapai **98.6% – 99.8%**.
   - Disebabkan perbedaan komponen OS native: Android menampilkan lubang kamera kecil di tengah dengan jam `10:46`, sedangkan iOS menampilkan **Dynamic Island** (kapsul hitam lebar di tengah) dengan jam `12:21` dan susunan ikon seluler/baterai Apple.
2. **Zona Navigasi Bawah (Bottom 8% Layar):**
   - Perbedaan piksel mencapai **99.7% – 99.9%**.
   - Disebabkan Android menggunakan bar navigasi gestur tipis, sedangkan iOS menampilkan **iOS Home Indicator Bar** (garis kapsul abu-abu tebal) dengan safe area inset bawah.
3. **Zona Konten Utama (Middle 84% Layar):**
   - **Font Engine:** Android menggunakan *Roboto* (Google Skia rasterizer), sedangkan iOS menggunakan *San Francisco / SF Pro* (Compose Skiko + CoreText).
   - **Aspect Ratio:** Layar Pixel 9 Pro berasio $\approx 20:9$ (lebih ramping vertikal) dibandingkan iPhone 16 Pro $\approx 19.5:9$, menghasilkan pergeseran vertikal (*Y-offset*) pada batas bawah kartu.

---

## 3. Qualitative Assessment & Video Walkthrough Analysis

### 3.1. Skenario Rekaman Video Walkthrough
- **Android Walkthrough ([`android/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/android/demo.mp4)):**
  - **Materi Uji:** Pertemuan 01: *Konsep Dasar Logika & Flowchart* (Mata kuliah CS101, Channel: *Web Programming UNPAS*, Video ID `jGyYuQf-GeE`).
  - **Alur Interaksi:** Membuka player $\to$ Live streaming aktif $\to$ Mengubah kecepatan $1.25\times$ $\to$ Checklist progres manual $\to$ Membuka dialog pelaporan $\to$ Memilih alasan masalah.
- **iOS Walkthrough ([`ios/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/03/ios/demo.mp4)):**
  - **Materi Uji:** Pertemuan 05: *Kombinatorika (Permutasi & Kombinasi)* (Mata kuliah CS102, Channel: *Zero Tutorial*, Video ID `Gq3sKvf-4h8`).
  - **Alur Interaksi:** Membuka silabus $\to$ Menavigasi ke pertemuan 5 $\to$ Live streaming video pembelajaran matematika aktif berjalan mulus $\to$ Mengakses kontrol status.

---

## 4. Root Cause Analysis (RCA) Glitch UI di iOS

### 4.1. Glitch 1: Badge Status *"Belum Selesai"* Terjepit Vertikal

```
Ekspektasi (Android):      [ Tandai Selesai (Manual) ]    [ Belum Selesai ]
                                                                
Realita di iOS:            [ Tandai Selesai (Manual) ]    | B |
                                                          | e |
                                                          | l |
                                                          | u |
                                                          | m |
                                                          | ...
```

* **Lokasi Berkas:** [`composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L336-L400`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L336-L400)
* **Penyebab Teknis:**
  1. Kontainer `Row` luar menggunakan `Arrangement.SpaceBetween`.
  2. `Row` sebelah kiri yang membungkus ikon checkbox dan teks `"Tandai Selesai (Manual)"` **tidak memiliki modifier `Modifier.weight(1f)`**.
  3. Font SF Pro di iOS memiliki *tracking* dan *bounding width* yang lebih lebar daripada Roboto di Android. Akibatnya, `Row` kiri memakan hampir seluruh lebar kartu, menyisakan ruang horizontal yang sangat sempit (< 20dp) bagi `Box` badge di sebelah kanan.
  4. String 13-karakter `"Belum Selesai"` yang tidak memiliki batasan `maxLines = 1` / `softWrap = false` dipaksa ter-wrap per-karakter ke bawah (*letter-by-letter vertical stacking*).

### 4.2. Glitch 2: Form Dialog Pelaporan Tumpang Tindih (*Overlapping*)

```
Ekspektasi (Android):                 Realita di iOS:
+---------------------------------+  +---------------------------------+
| Laporkan Link Rusak             |  | Laporkan Link Rusak             |
|                                 |  |                                 |
| Pilih Alasan Masalah:           |  | Pilih Alasan Masalah:           |
| (o) Video Dihapus / Private     |  | (o) Video Dihapus / Private     |
| ( ) Video Tidak Sesuai Topik    |  | ( ) Video Tidak Sesuai Topik    |
| ( ) Audio / Visual Rusak        |  | ( ) Audio / Visual Rusak        |
| ( ) Lainnya                     |  | ( ) +-------------------------+ |
|                                 |  |     | [OutlinedTextField]     | |  <-- MENABRAK OPSI "LAINNYA"
| Catatan Tambahan (Opsional):    |  |     +-------------------------+ |
| +-----------------------------+ |  |                                 |
| | Contoh: Menit 05:20...      | |  | [Batal]         [Kirim Laporan] |
| +-----------------------------+ |  +---------------------------------+
|                                 |
| [Batal]         [Kirim Laporan] |
+---------------------------------+
```

* **Lokasi Berkas:** [`composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt#L68-L126`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt#L68-L126)
* **Penyebab Teknis:**
  1. `AlertDialog` Material3 pada Compose Skiko (iOS target) menerapkan batasan viewport dialog yang ketat tanpa otomatis menambahkan scrolling.
  2. `Column` di dalam slot `text` dialog **tidak memiliki `Modifier.verticalScroll(rememberScrollState())`**.
  3. Akumulasi tinggi elemen (Title + 4 baris Radio Button dengan touch target 48dp + Label Catatan + OutlinedTextField 3 baris) melebihi batas `maxHeight` dialog content di iOS.
  4. Karena tidak scrollable, layout engine memaksakan `OutlinedTextField` naik menutupi baris radio button *"Lainnya"* dan menenggelamkan label teks catatan.

---

## 5. Actionable Fix Plan (Rencana Perbaikan Kode)

### 5.1. Patch [`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)
Menambahkan `Modifier.weight(1f)` pada baris checklist kiri dan mengunci teks badge agar tidak pernah terbungkus vertikal:

```kotlin
// SEBELUM:
Row(
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(OpenCampusColors.GrayscaleBgLightGrey)
        .clickable { viewModel.toggleManualCompletion() }
        .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ...
        Text(text = "Tandai Selesai (Manual)", ...)
    }
    Box(...) {
        Text(text = if (uiState.isCompleted) "Selesai" else "Belum Selesai", ...)
    }
}

// SESUDAH (PERBAIKAN):
Row(
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(OpenCampusColors.GrayscaleBgLightGrey)
        .clickable { viewModel.toggleManualCompletion() }
        .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Row(
        modifier = Modifier.weight(1f).padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ...
        Text(
            text = "Tandai Selesai (Manual)",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OpenCampusColors.GrayscaleBlack,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(...)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = if (uiState.isCompleted) "Selesai" else "Belum Selesai",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false
        )
    }
}
```

### 5.2. Patch [`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)
Menambahkan `Modifier.verticalScroll(rememberScrollState())` dan padding yang proporsional pada container dialog:

```kotlin
// SEBELUM:
text = {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Pilih Alasan Masalah:", ...)
        ...
        OutlinedTextField(...)
    }
}

// SESUDAH (PERBAIKAN):
text = {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Pilih Alasan Masalah:", ...)
        ...
        OutlinedTextField(...)
    }
}
```

---

## 6. Verification & Quality Gates

Setelah mengimplementasikan perbaikan di atas, lakukan verifikasi ulang melalui 7 Gerbang Verifikasi:
1. **Lint:** `./gradlew composeApp:lintDebug` $\to$ Exit 0.
2. **Contract & Unit Tests:** `./gradlew test` $\to$ 26/26 tests passed (Exit 0).
3. **Packaging Dual-Platform:** `./gradlew assembleDebug` & `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` $\to$ Exit 0.
4. **Visual Re-verification:** Uji ulang tangkapan layar iOS pada iPhone 16 Pro simulator untuk memastikan badge *"Belum Selesai"* tampil horizontal dan form dialog tidak lagi mengalami tabrakan visual (*overlapping*).
