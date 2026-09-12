# TRD 03: Integrasi Kurasi Video YouTube Aktif & Verifikasi Pemutaran Dual Platform (Modul 3)

**Nomor Modul:** 03  
**Fitur Utama:** Integrasi Kurasi Video YouTube Aktif (11-Karakter oEmbed 200 OK), Verifikasi Pemutaran Live Dual-Platform (Android & iOS), Kontrak Integritas Dataset Kurikulum, dan Regenerasi Evidence Pemutar Video Bebas Layar Hitam  
**Target Platform:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform (CMP)  
**Dokumen Asesmen Terkait:** [Curated YouTube Videos & Embeddability Assessment](../assesment/02_curated_youtube_videos_assessment.md)  
**Dokumen PRD Terkait:** [PRD 02: Video Player & Pelacakan Progres](../prd/02_prd.md), [PRD 00: Global MVP](../prd/00_prd.md)  
**Dokumen Master Desain:** [Design System UI Kit Free](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Dokumen TRD Sebelumnya:** [TRD 02: Video Player & Pelacakan Progres](./02_trd.md)  
**Target Arsip Evidence:** `.docs/evidence/03/README.md` (dan pembaruan video pemutaran nyata di `.docs/evidence/02/demo.mp4`)  
**Penulis:** Technical Team (Mobile — Tommy Dwi Putranto)  
**Status Dokumen:** Draft Awal (Tahap 1 — Menunggu Human Review Gate 1)  

---

## 1. Executive Summary & Scope

### 1.1. Overview & Arsitektur Ringkas
Pada evaluasi rekaman demo pemutar video Modul 2 ([`.docs/evidence/02/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/02/demo.mp4)), ditemukan bahwa kontainer pemutar video ([`PlatformVideoPlayer`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt)) hanya menampilkan kotak persegi gelap kosong berlatar belakang `#1E1E2E` tanpa memutar stream media YouTube.

Investigasi berbasis data empiris ([`.docs/assesment/02_curated_youtube_videos_assessment.md`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/assesment/02_curated_youtube_videos_assessment.md)) membuktikan bahwa seluruh 480 topik kurikulum pada [`curriculum_it_semesters.json`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/composeResources/files/curriculum_it_semesters.json) dan [`CurriculumDataSource.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/data/CurriculumDataSource.kt) masih menggunakan string placeholder / mock (seperti `t101_01_v`, `vid_cs102_01`) dan bukan **ID video YouTube 11-karakter yang valid**. Ketika dimuat ke dalam YouTube Iframe API v3, server CDN YouTube mengembalikan status galat sehingga WebView tidak merender frame video maupun thumbnail poster.

TRD 03 ini menetapkan spesifikasi teknis untuk:
1. Mengintegrasikan **16 ID video YouTube terverifikasi (100% oEmbed 200 OK & embed allowed)** untuk mata kuliah utama Semester 1 (`cs101`: *Algoritma & Pemrograman Dasar*) serta topik terpilih untuk `cs102`, `cs103`, dan `cs104`.
2. Menyinkronkan dataset kurikulum pada kedua sumber data KMP (`composeResources/files/curriculum_it_semesters.json` dan `CurriculumDataSource.kt`).
3. Menambahkan gerbang validasi kontrak data (`CurriculumVideoContractTest`) di `commonTest` guna mencegah regresi format ID video di masa depan.
4. Memvalidasi pemutaran video aktif, interaksi playback controls, auto-centang progres ($\ge 85\%$), dan pelaporan tautan secara live pada arsitektur dual-platform (Android WebView & iOS WKWebView).
5. Merekam kembali evidence audio-visual (`demo.mp4` & screenshots) pada Android Emulator Pixel 9 Pro dan iOS Simulator iPhone 16 Pro untuk membuktikan pemutaran video nyata tanpa layar hitam.

### 1.2. Current Implementation (Grounded in Code)
- **Sumber Data Terduplikasi:** Dataset kurikulum tersimpan di dua tempat: berkas statis `curriculum_it_semesters.json` (149 KB) dan kelas Kotlin `CurriculumDataSource.kt` (164 KB, berupa string chunk `CHUNKS`).
- **Placeholder Video ID:** 100% dari 480 topik menggunakan format string mock seperti `t101_01_v` atau `vid_cs102_01`.
- **Dampak pada Platform Bridge:**
  - Di Android ([`PlatformVideoPlayer.android.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/androidMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.android.kt)), WebView memuat script Iframe YouTube dengan parameter `videoId = 't101_01_v'`. YouTube API gagal menemukan media stream dan memicu event `onError` atau stall tanpa merender thumbnail.
  - Di iOS ([`PlatformVideoPlayer.ios.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/iosMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.ios.kt)), `WKWebView` mengalami kondisi serupa.
- **Tautan Eksternal:** Tombol *"Tonton di YouTube"* mengarahkan ke `https://www.youtube.com/watch?v=t101_01_v` yang menghasilkan halaman *"This video isn't available anymore"*.
- **Unit Testing Eksisting:** 22 unit test yang ada saat ini hanya memvalidasi parsing struktur JSON dasar dan logika ambang 85%, tanpa memeriksa validitas pola regex 11-karakter pada `videoId`.

### 1.3. Target Implementation
- **Patching Data Kurikulum:** Memperbarui 16 pertemuan lengkap `cs101` dan pertemuan kunci `cs102`, `cs103`, `cs104` dengan ID 11-karakter riil yang lolos uji oEmbed (misal: `jGyYuQf-GeE`, `1FAnrYu7LCM`, `-9IyBehKm4g`, `ZeqJewFm7zc`, `iTUO1DWVUv8`, dst.).
- **Kontrak Pengujian Validitas Format:** Membuat unit test di `commonTest` yang memverifikasi bahwa seluruh topik aktif Semester 1 memiliki `videoId` yang memenuhi pola regex `^[a-zA-Z0-9_-]{11}$`.
- **Verifikasi Live Playback & Auto-Complete:** Memastikan event `onTimeUpdate` dari Iframe API memicu pembaruan `currentTime`, durasi total, dan auto-centang progress ($\ge 85\%$) secara nyata saat video berjalan.
- **Atribusi Kreator Faktual:** Nama channel (*Web Programming UNPAS*, *Kelas Terbuka*, *Programmer Zaman Now*, *Informatika ITB*) dan tautan eksternal membuka video asli di browser/aplikasi YouTube.
- **Dual-Platform Build & Evidence:** Memvalidasi `./gradlew assembleDebug` (Android APK) dan `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` (iOS Framework), serta menghasilkan rekaman video demo interaktif berdurasi penuh di `.docs/evidence/03/` dan pembaruan `.docs/evidence/02/demo.mp4`.

### 1.4. Scope (In Scope vs Out of Scope)
- **In Scope:**
  - Patching video ID dan metadata (channel, durasi, title) pada Semester 1 (`cs101` 16 pertemuan, serta topik terkurasi `cs102`, `cs103`, `cs104`) di `curriculum_it_semesters.json` dan `CurriculumDataSource.kt`.
  - Kontrak unit test validitas YouTube ID di `composeApp/src/commonTest`.
  - Verifikasi live streaming di `PlatformVideoPlayer` Android dan iOS.
  - Verifikasi fungsi fallback dialog saat video diubah ke ID tidak valid (simulasi negative control).
  - Perekaman video walkthrough `demo.mp4` dan tangkapan layar pemutaran aktif pada Android Pixel 9 Pro dan iOS iPhone 16 Pro.
- **Out of Scope:**
  - Kurasi manual untuk seluruh 480 topik Semester 2 s.d. 8 (akan diproses bertahap melalui pipeline otomatisasi kurasi pada rilis v0.2+).
  - Pemutaran video offline / caching video lokal (dilarang oleh Ketentuan Layanan YouTube API).
  - Modifikasi styling dasar UI pemutar video (tetap mempertahankan token desain Modul 2).

---

## FASE 1: KEBUTUHAN & LOGIKA BISNIS

### 1.1. Use Case Diagram (System Boundary & Actors)

```text
┌──────────────────────────────────────────────────────────────────────────────────────────────────┐
│ SYSTEM BOUNDARY: OpenCampus Mobile Ecosystem (oss-elearning)                                     │
│                                                                                                  │
│                     ┌───────────────────────────────────────────────────┐                        │
│                     │ UC-01: Buka Topik Silabus Perkuliahan             │                        │
│                     └─────────────────────────┬─────────────────────────┘                        │
│                                               │                                                  │
│                                               │ <<include>>                                      │
│                                               ▼                                                  │
│                     ┌───────────────────────────────────────────────────┐                        │
│                     │ UC-02: Muat Metadata & Validasi Video ID 11-Char  │                        │
│                     └─────────────────────────┬─────────────────────────┘                        │
│                                               │                                                  │
│                                               │ <<include>>                                      │
│                                               ▼                                                  │
│  ┌──────────────┐   ┌───────────────────────────────────────────────────┐   ┌─────────────────┐  │
│  │              │──>│ UC-03: Render Live Stream Video via Iframe Bridge │<──│                 │  │
│  │  Pembelajar  │   └───────┬───────────────────┬───────────────────┬───┘   │  YouTube CDN /  │  │
│  │   Mandiri    │           │                   │                   │       │  Iframe Service │  │
│  │    (User)    │           │ <<extend>>        │ <<extend>>        │       │                 │  │
│  │              │           ▼ (Duration >= 85%) ▼ (Tap Link)        │       └─────────────────┘  │
│  │              │   ┌───────────────┐   ┌───────────────┐           │                │           │
│  │              │   │ UC-04:        │   │ UC-05:        │           │ <<extend>>     │           │
│  │              │   │ Auto-Centang  │   │ Buka Atribusi │           │ (Error Code    │           │
│  │              │   │ Progres Belajar│  │ Tautan YouTube│           │  100/101/150)  │           │
│  │              │   └───────┬───────┘   └───────────────┘           ▼                ▼           │
│  │              │           │                           ┌─────────────────────────────┐          │
│  │              │           ▼                           │ UC-06: Tampilkan Fallback   │          │
│  │              │   ┌───────────────┐                   │ Banner & Laporkan Link Rusak│          │
│  │              │   │ Local Storage │                   └──────────────┬──────────────┘          │
│  │              │   │ (Settings KV) │                                  │                         │
│  └──────────────┘   └───────────────┘                                  ▼                         │
│                                                         ┌─────────────────────────────┐          │
│                                                         │ Telemetry & Error Logging   │          │
│                                                         └─────────────────────────────┘          │
└──────────────────────────────────────────────────────────────────────────────────────────────────┘
```

**Penjelasan Batasan Sistem & Aktor:**
- **Aktor Utama (Pembelajar Mandiri):** Memilih topik materi perkuliahan, menonton video YouTube aktif tanpa distraksi iklan luar/rekomendasi, mengatur kecepatan, dan memperoleh rekaman progres otomatis.
- **Aktor Sistem (YouTube CDN & Iframe Service):** Menyediakan feed media stream HLS/MP4 terenkripsi, poster thumbnail resmi, dan memancarkan callback JavaScript Iframe API (`onReady`, `onStateChange`, `onError`, `getCurrentTime`).
- **Sistem Batasan (OpenCampus Mobile):** Bertindak sebagai *sandbox* aman yang memuat kurikulum tervalidasi, mengisolasi iframe player, mendeteksi capaian ambang $\ge 85\%$, dan menyimpan progres ke Multiplatform Settings secara offline-first.

---

### 1.2. Activity Diagram (Alur Proses & Swimlanes)

```text
Pembelajar (User)   VideoPlayerScreen (UI)   VideoPlayerViewModel   CurriculumRepository   PlatformVideoPlayer   YouTube Remote CDN
       │                      │                       │                      │                      │                     │
       │ 1. Pilih Pertemuan   │                       │                      │                      │                     │
       │─────────────────────>│                       │                      │                      │                     │
       │                      │ 2. Request Topic Info │                      │                      │                     │
       │                      │──────────────────────>│                      │                      │                     │
       │                      │                       │ 3. getCourse/Topic   │                      │                     │
       │                      │                       │─────────────────────>│                      │                     │
       │                      │                       │ 4. Return Topic Model│                      │                     │
       │                      │                       │<─────────────────────│                      │                     │
       │                      │                       │                      │                      │                     │
       │                      │                       │ 5. Validasi videoId: │                      │                     │
       │                      │                       │    Regex 11-char?    │                      │                     │
       │                      │                       │    [Valid / Mock?]   │                      │                     │
       │                      │                       │──┐                   │                      │                     │
       │                      │                       │  │ Validasi          │                      │                     │
       │                      │                       │<─┘                   │                      │                     │
       │                      │                       │                      │                      │                     │
       │                      │                       ├──────────────────────┼──────────────────────┼─────────────────────┐
       │                      │                       │ [IF videoId Valid]   │                      │                     │
       │                      │ 6. Emit UiState       │                      │                      │                     │
       │                      │    (videoId riil)     │                      │                      │                     │
       │                      │<──────────────────────│                      │                      │                     │
       │                      │                       │                      │                      │                     │
       │                      │ 7. Load Web Iframe    │                      │                      │                     │
       │                      │────────────────────────────────────────────────────────────────────>│                     │
       │                      │                       │                      │                      │ 8. Fetch Stream/DOM │
       │                      │                       │                      │                      │────────────────────>│
       │                      │                       │                      │                      │ 9. Return Stream 200│
       │                      │                       │                      │                      │<────────────────────│
       │                      │                       │                      │                      │                     │
       │                      │                       │                      │ 10. onPlayerReady    │                     │
       │                      │                       │                      │<─────────────────────│                     │
       │                      │ 11. Render Live Player│                      │                      │                     │
       │                      │<────────────────────────────────────────────────────────────────────│                     │
       │                      │                       │                      │                      │                     │
       │ 12. Tonton Video     │                       │                      │                      │                     │
       │─────────────────────>│                       │                      │                      │                     │
       │                      │                       │ 13. onTimeUpdate     │                      │                     │
       │                      │                       │     (sec, dur)       │                      │                     │
       │                      │                       │<────────────────────────────────────────────│                     │
       │                      │                       │                      │                      │                     │
       │                      │                       │ 14. % Watched >= 85%?│                      │                     │
       │                      │                       │──┐                   │                      │                     │
       │                      │                       │  │ Evaluasi Progres  │                      │                     │
       │                      │                       │<─┘                   │                      │                     │
       │                      │ 15. Update Checkbox   │                      │                      │                     │
       │                      │     [X] Selesai       │                      │                      │                     │
       │                      │<──────────────────────│                      │                      │                     │
       │                      │                       │ 16. Save Progress    │                      │                     │
       │                      │                       │─────────────────────>│                      │                     │
       │                      │                       ├──────────────────────┼──────────────────────┼─────────────────────┤
       │                      │                       │ [ELSE IF Error /     │                      │                     │
       │                      │                       │  Code 100/101/150]   │                      │                     │
       │                      │                       │                      │                      │ 17. Return Error    │
       │                      │                       │                      │                      │<────────────────────│
       │                      │                       │ 18. onError Callback │                      │                     │
       │                      │                       │<────────────────────────────────────────────│                     │
       │                      │ 19. Tampilkan Banner  │                      │                      │                     │
       │                      │     Fallback & Tombol │                      │                      │                     │
       │                      │     "Laporkan Link"   │                      │                      │                     │
       │                      │<──────────────────────│                      │                      │                     │
       │ 20. Tap "Laporkan"   │                       │                      │                      │                     │
       │─────────────────────>│                       │                      │                      │                     │
       │                      │ 21. Buka ReportDialog │                      │                      │                     │
       │                      │     (Pilih Alasan)    │                      │                      │                     │
       │                      │<──────────────────────│                      │                      │                     │
       │                      └───────────────────────┴──────────────────────┴──────────────────────┴─────────────────────┘
```

---

### 1.3. Tabel Sitasi & Rujukan PRD, Asesmen, dan Figma

| Nomor Rujukan | Dokumen Sumber | Bagian / Pasal | Deskripsi Kebutuhan Teknis & Implementasi | Figma Node ID |
|---|---|---|---|---|
| **REQ-VID-01** | [Asesmen Kurasi 02](../assesment/02_curated_youtube_videos_assessment.md) | §4.1 (Matriks cs101) | Injeksi 16 ID video YouTube terverifikasi 100% oEmbed 200 OK untuk Algoritma & Pemrograman Dasar (`jGyYuQf-GeE`, `1FAnrYu7LCM`, dll.). | [`4001:1420`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) |
| **REQ-VID-02** | [Asesmen Kurasi 02](../assesment/02_curated_youtube_videos_assessment.md) | §4.2–§4.4 (cs102–104) | Injeksi ID video aktif terverifikasi untuk mata kuliah Semester 1 lainnya (Matematika Diskrit, PTI, Arsitektur Komputer). | [`4001:1425`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) |
| **REQ-VID-03** | [PRD 02: Video Player](../prd/02_prd.md) | §2 (FR-2.1) | Pemutaran video tertanam langsung via YouTube Iframe API v3 tanpa rekomendasi eksternal (`rel=0&modestbranding=1`). | [`5002:2100`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) |
| **REQ-VID-04** | [PRD 02: Video Player](../prd/02_prd.md) | §2 (FR-2.3) | Kepatuhan YouTube TOS: Atribusi resmi nama kanal pembuat dan tombol interaktif *"Tonton di YouTube"*. | [`5002:2150`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) |
| **REQ-VID-05** | [PRD 02: Video Player](../prd/02_prd.md) | §2 (FR-3.1) | Ambang batas auto-centang progres $\ge 85\%$ durasi pemutaran yang disinkronkan ke local persistence. | [`5002:2200`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) |
| **REQ-VID-06** | [PRD 02: Video Player](../prd/02_prd.md) | §2 (FR-3.4) | Fallback UI informatif *"Video materi sedang diperbarui"* disertai dialog pelaporan link rusak jika video tidak tersedia. | [`5002:2300`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) |
| **REQ-VID-07** | [PRD 00: Global MVP](../prd/00_prd.md) | §5 (Arsip Evidence) | Pengarsipan artefak verifikasi dual-platform (Android APK & iOS Framework) dan video walkthrough pemutaran live. | N/A |
