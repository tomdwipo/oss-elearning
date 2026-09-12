# PRD 03: Comprehensive Detail View Audit & Dual-Platform UI Parity (Android & iOS)

**Nomor Modul / PRD:** 03  
**Fitur Utama:** Audit Menyeluruh Detail View & Rekayasa Presisi UI Lintas Platform (Android & iOS)  
**Target Rilis:** MVP v0.2.1  
**Platform:** Mobile (Android & iOS — Compose Multiplatform 1.7.3 / Kotlin 2.1.0)  
**Dokumen Asesmen Rujukan:** [Asesmen Dual-Platform UI & Piksel (`.docs/assesment/03_dual_platform_ui_pixel_assessment.md`)](../assesment/03_dual_platform_ui_pixel_assessment.md)  
**Dokumen Desain Sistem:** [Design System UIKit (`.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md`)](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Dokumen Induk PRD:** [PRD 00 (Global MVP)](./00_prd.md), [PRD 01 (Navigasi Kurikulum)](./01_prd.md), & [PRD 02 (Video Player)](./02_prd.md)  

---

## 1. Executive Summary & Latar Belakang

Berdasarkan hasil audit mendalam dan asesmen piksel dual-platform ([`03_dual_platform_ui_pixel_assessment.md`](../assesment/03_dual_platform_ui_pixel_assessment.md)) terhadap artefak video dan tangkapan layar eksekusi nyata pada Google Pixel 9 Pro (Android 15) dan Apple iPhone 16 Pro (iOS 18.5), fungsionalitas pemutaran video aktif telah terbukti berjalan 100% lancar (*zero black screen*).

Namun, audit mendalam pada komponen **Detail View** ([`CourseSyllabusScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/CourseSyllabusScreen.kt) dan [`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)) serta dialog pendukung ([`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)) mengidentifikasi adanya perbedaan perilaku rendering Skiko (iOS) vs Skia Android yang memicu 2 (dua) *layout glitch* kritis.

Dokumen PRD ini memformalkan audit detail view, spesifikasi perbaikan antarmuka responsif, standar ukuran target sentuh (*touch target*), dan matriks penerimaan kualitas (*acceptance criteria*).

---

## 2. Matriks Audit Detail View Komponen (Comprehensive Component Audit)

| Komponen / Layar | Elemen UI | Status Android (Pixel 9 Pro) | Status iOS (iPhone 16 Pro) | Tingkat Keparahan (Severity) | Tindakan Korektif |
|---|---|---|---|---|---|
| **Course Detail View** ([`CourseSyllabusScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/CourseSyllabusScreen.kt)) | Top Navigation Bar & Judul | ✅ Normal (Judul `16.sp` + Badge Topik) | ✅ Normal (Terpotong elipsis rapi jika panjang) | Rendah (P3) | Pertahankan batasan `weight(1f)` dan `maxLines = 1`. |
| **Course Detail View** ([`CourseSyllabusScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/CourseSyllabusScreen.kt)) | Hero Course Summary Card | ✅ Normal (Estimasi jam, progres bar hijau) | ✅ Normal (Elevasi 2.dp, padding 16.dp seragam) | Rendah (P3) | Validasi warna progress `#27AE60` & purple `#9D3FE7`. |
| **Topic Item List** ([`TopicItem.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/TopicItem.kt)) | Indikator `isNextUp` & Checkbox | ✅ Normal (Border 1.5.dp ungu & checkbox 28.dp) | ✅ Normal (Interaktif dan responsive) | Rendah (P3) | Pertahankan ukuran touch target. |
| **Video Player Detail View** ([`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)) | Header & Video Container 16:9 | ✅ Normal (Aspect ratio 16:9 stabil) | ✅ Normal (Live stream WKWebView termuat rapi) | Rendah (P3) | Pertahankan handling fallback galat. |
| **Video Player Detail View** ([`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)) | Baris Kecepatan (*Speed Selector*) | ✅ Normal (5 opsi speed `0.75x` s.d. `2x`) | ✅ Normal (Pill buttons ter-render rapi) | Rendah (P3) | Pertahankan border aktif `#602093`. |
| **Video Player Detail View** ([`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)) | **Baris Checklist Progres & Badge** | ✅ Normal (Teks horizontal `"Selesai"`) | ❌ **GLITCH:** Teks `"Belum Selesai"` terjepit vertikal per huruf (*letter-by-letter wrap*) | 🔴 **Tinggi (P1)** | Tambahkan `Modifier.weight(1f)` pada label kiri dan kunci `maxLines = 1`, `softWrap = false` pada badge kanan. |
| **Modal Dialog Pelaporan** ([`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)) | **Form Input Masalah & Catatan** | ✅ Normal (4 radio buttons + label + textfield rapi) | ❌ **GLITCH:** Input `OutlinedTextField` menabrak radio button ke-4 (*"Lainnya"*) dan menutupi label catatan | 🔴 **Tinggi (P1)** | Pasang `Modifier.verticalScroll(rememberScrollState())` pada container dialog dan sesuaikan batasan layout Skiko. |
| **Platform Bridge** ([`PlatformVideoPlayer`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt)) | Iframe Embed Lifecycle | ✅ Normal (`AndroidView` WebView) | ✅ Normal (`UIKitView` WKWebView) | Rendah (P3) | Pertahankan optimasi JS script handler. |

---

## 3. Temuan Kuantitatif & Akar Masalah (Root Cause Analysis)

### 3.1. Glitch Layout 1: Badge Status *"Belum Selesai"* Mengalami Pembungkusan Karakter Vertikal
- **Lokasi:** [`VideoPlayerScreen.kt` Baris 336–400](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L336-L400).
- **Akar Masalah:**
  - Baris sebelah kiri `Row` (*"Tandai Selesai (Manual)"*) tidak diberi pembatas lebar fleksibel (`weight(1f)`).
  - Pada iOS (Skiko), mesin rendering tipografi menggunakan font *San Francisco* yang memiliki *letter-spacing* bawaan lebih lebar daripada *Roboto* di Android.
  - Akibatnya, `Row` kiri memakan $\approx 92\%$ lebar kontainer kartu, menyisakan ruang $< 20\text{ dp}$ bagi `Box` badge di sebelah kanan. Teks `"Belum Selesai"` (13 karakter) yang tidak dibatasi barisnya terdorong ke bawah huruf demi huruf.

### 3.2. Glitch Layout 2: Overlapping Input Form pada Dialog Pelaporan
- **Lokasi:** [`ReportBrokenVideoDialog.kt` Baris 68–126](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt#L68-L126).
- **Akar Masalah:**
  - `AlertDialog` Material3 pada platform iOS (Compose UIKit Layer) memiliki batas tinggi kanvas tetap (*fixed height boundary*).
  - Slot `text` diisi oleh `Column` yang memuat judul, 4 baris radio button, spacer, label catatan, dan `OutlinedTextField` 3 baris tanpa mengaktifkan scrollbar vertikal (`verticalScroll`).
  - Total tinggi konten melebihi batas kanvas dialog iOS, memicu layout engine memaksakan `OutlinedTextField` naik dan menggambar di atas opsi radio button ke-4 (*"Lainnya"*), serta menghilangkan label catatan.

---

## 4. Kebutuhan Fungsional & Spesifikasi Perbaikan (Functional Requirements)

### ✅ FR-1: Responsivitas & Proteksi Layout Badge Status Progres
1. **Bobot Fleksibel Kontainer Kiri:** Elemen `Row` yang berisi ikon centang dan teks `"Tandai Selesai (Manual)"` wajib menggunakan `Modifier.weight(1f).padding(end = 8.dp)`.
2. **Ellipsis pada Layar Sempit:** Teks label checklist wajib menerapkan `maxLines = 1` dan `overflow = TextOverflow.Ellipsis`.
3. **Pencegahan Line-Wrap pada Badge:** Komponen teks di dalam `Box` badge status (`"Selesai"` / `"Belum Selesai"`) wajib menerapkan `maxLines = 1` dan `softWrap = false` serta padding horizontal $8\text{ dp}$.

### ✅ FR-2: Scrollable & Adaptive Layout pada Dialog Pelaporan Link Rusak
1. **Vertical Scroll Integration:** Kontainer `Column` di dalam slot `text` pada `ReportBrokenVideoDialog` wajib mengimplementasikan `Modifier.fillMaxWidth().verticalScroll(rememberScrollState())`.
2. **Preservasi Touch Target & Spacing:** Setiap baris opsi radio button wajib mempertahankan tinggi sentuh minimal $40\text{ dp}$ dengan padding vertikal $4\text{ dp}$.
3. **Pemisahan Jarak Elemen Form:** Jarak antara opsi radio terakhir dengan label catatan tambahan diatur minimal $12\text{ dp}$, dan `OutlinedTextField` memiliki tinggi tetap maksimal 3 baris tanpa tabrakan visual.

### ✅ FR-3: Keselarasan Warna & Tipografi Lintas Platform (Design Parity)
1. Seluruh komponen detail view wajib mematuhi token warna resmi:
   - Primary Brand: `CorporatePurple` (`#9D3FE7`)
   - Accent / Dark Container: `CorporateDarkPurple` (`#602093`)
   - Success / Completion: `InformingApproval` (`#27AE60`)
   - Error / Danger: `InformingError` (`#EB5757`)
   - Surface Background: `GrayscaleBgLightGrey` (`#F8F9FA`)
   - Card Container: `GrayscaleWhite` (`#FFFFFF`)

---

## 5. Spesifikasi Teknis Perubahan Kode (Technical Code Diff)

### 5.1. Perbaikan [`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt)

```kotlin
// --- BEFORE ---
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
        // Checkbox Box (22.dp)
        ...
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Tandai Selesai (Manual)",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = OpenCampusColors.GrayscaleBlack
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(...)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = if (uiState.isCompleted) "Selesai" else "Belum Selesai",
            fontSize = 11.sp,
            ...
        )
    }
}

// --- AFTER (FIXED) ---
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
        // Checkbox Box (22.dp)
        ...
        Spacer(modifier = Modifier.width(10.dp))
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
            color = if (uiState.isCompleted) OpenCampusColors.InformingApproval
            else OpenCampusColors.GrayscaleHintText,
            maxLines = 1,
            softWrap = false
        )
    }
}
```

### 5.2. Perbaikan [`ReportBrokenVideoDialog.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt)

```kotlin
// --- BEFORE ---
text = {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Pilih Alasan Masalah:", ...)
        ReportReason.values().forEach { reason ->
            Row(...) { ... }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Catatan Tambahan (Opsional):", ...)
        OutlinedTextField(...)
    }
}

// --- AFTER (FIXED) ---
text = {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Pilih Alasan Masalah:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OpenCampusColors.GrayscaleBlack,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        ReportReason.values().forEach { reason ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedReason = reason }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (reason == selectedReason),
                    onClick = { selectedReason = reason },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = OpenCampusColors.CorporatePurple,
                        unselectedColor = OpenCampusColors.GrayscaleBorder
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = reason.label,
                    fontSize = 14.sp,
                    color = OpenCampusColors.GrayscaleBlack
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Catatan Tambahan (Opsional):",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = OpenCampusColors.GrayscaleBlack,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = {
                Text(
                    text = "Contoh: Video bermasalah di menit ke 05:20...",
                    fontSize = 12.sp,
                    color = OpenCampusColors.GrayscaleHintText
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            maxLines = 3
        )
    }
}
```

---

## 6. Kriteria Penerimaan Kualitas (Acceptance Criteria)

- **AC-1 (Badge Horizontal Non-Wrap):** Pada pengujian iOS Simulator (iPhone 16 Pro) dan Android Emulator (Pixel 9 Pro), badge status progres pada state `"Belum Selesai"` dan `"Selesai"` ter-render utuh 1 baris secara horizontal tanpa karakter terputus.
- **AC-2 (Form Dialog Bebas Overlap):** Seluruh 4 pilihan radio button, label catatan tambahan, dan kotak input teks pada dialog pelaporan link rusak terlihat terpisah secara hierarki dan dapat digulir jika ruang layar terbatas.
- **AC-3 (Kompilasi & Test Suite Hijau):**
  - `./gradlew test` $\to$ Lolos 26/26 pengujian unit di `commonTest`.
  - `./gradlew composeApp:lintDebug` $\to$ Exit 0 (0 lint error).
  - `./gradlew assembleDebug` $\to$ Sukses menghasilkan `composeApp-debug.apk`.
  - `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` $\to$ Sukses menghasilkan `ComposeApp.framework`.
- **AC-4 (Artefak Evidence Terkini):** Tangkapan layar dan rekaman video demo iOS diperbarui untuk membuktikan perbaikan telah terverifikasi secara visual.
