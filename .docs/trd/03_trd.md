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
**Status Dokumen:** Lengkap (Tahap 2 — Menunggu Human Review Gate 2)  

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

---

## [PARALEL] FASE 2: DESAIN ANTARMUKA (UI/UX)

### 2.1. User Flow & Wireflow
```text
[CourseSyllabusScreen]
         │
         │ (Tap Topik dengan Video ID Aktif)
         ▼
[VideoPlayerScreen] ─── (Loading Iframe) ───> [Live YouTube Media Stream]
         │                                              │
         ├──────────────────────────────────────────────┤
         │                                              │
         ▼                                              ▼
[Scrubbing & Speed Bar]                    [Auto-Complete Checkbox]
(0.75x, 1x, 1.25x, 1.5x, 2x)               (Otomatis saat >= 85%)
         │                                              │
         ├──────────────────────────────────────────────┤
         │                                              │
         ▼                                              ▼
[↗ Tonton di YouTube]                      [Fallback Banner (E2)]
(External YouTube App/Browser)             ("Materi Sedang Diperbarui")
                                                        │
                                                        ▼
                                           [ReportBrokenVideoDialog]
```

- **Entry Point:** Dari `CourseSyllabusScreen` ketika pengguna memilih salah satu item pertemuan (misalnya Pertemuan 1: *Konsep Dasar Logika & Flowchart*).
- **Back-Stack Policy:** Menekan ikon kembali (`ArrowBack`) membatalkan pemutaran dan mengembalikan user ke `CourseSyllabusScreen` tanpa me-reset state progres yang telah tersimpan.
- **Percabangan:**
  - Jika video aktif: YouTube Iframe merender video player penuh dengan poster thumbnail asli.
  - Jika video terhapus/privat: Fallback banner muncul menggantikan kontainer gelap, dengan opsi membuka dialog pelaporan link.

### 2.2. Wireframe Layout (375x812 dp, 9:19.5 Target Mobile)
```text
┌─────────────────────────────────────────┐ ◄── Top Bar (Back Arrow + Judul Topik)
│ ◄  Pertemuan 01: Konsep Dasar Logika    │
├─────────────────────────────────────────┤ ◄── Video Player Container (Aspect 16:9)
│                                         │
│      ▶ [ LIVE YOUTUBE VIDEO STREAM ]    │     - Background: #1E1E2E
│         (Thumbnail / Active Player)     │     - Height: 211 dp (375 * 9/16)
│                                         │     - Controls: Play/Pause, Fullscreen
├─────────────────────────────────────────┤
│ [●─────────────── 08:24 / 18:00 ───────]│ ◄── Scrubbing Bar & Waktu Tonton
├─────────────────────────────────────────┤
│ [0.75x]  [ 1x* ]  [1.25x]  [1.5x]  [2x] │ ◄── Playback Speed Horizontal Selector
├─────────────────────────────────────────┤
│ [☑] Tandai Selesai (Auto-complete 85%)  │ ◄── Status Checkbox Interaktif
├─────────────────────────────────────────┤
│ 👤 Web Programming UNPAS                │ ◄── Atribusi Resmi Kreator & Saluran
│ ↗ Tonton di YouTube (Atribusi Resmi)    │ ◄── Tombol Link Eksternal
├─────────────────────────────────────────┤
│ 🚩 Laporkan Tautan Rusak               │ ◄── Aksi Pelaporan Tautan
└─────────────────────────────────────────┘
```

### 2.3. UI Design System & Tokens
Sesuai dengan `DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md`, token visual yang digunakan adalah:
- **Warna Utama (Brand Colors):**
  - `Corporate/Purple`: `#9D3FE7` (Aksen kontrol, slider thumb, active speed pill)
  - `Corporate/DarkPurple`: `#602093` (Border active, dark state)
  - `Informing/Approval`: `#00B998` (Status topik selesai / checklist auto-complete)
  - `Informing/Error`: `#D32F2F` (Status video rusak & tombol pelaporan link)
  - `Grayscale/Surface`: `#1E1E2E` (Warna latar pemutar video)
  - `Grayscale/TextPrimary`: `#1F1F1F` (Teks utama)
  - `Grayscale/White`: `#FFFFFF` (Latar belakang screen dan kartu)
- **Tipografi:** Font family `Poppins` (SemiBold 18sp untuk judul topik, Medium 14sp untuk speed controls, Regular 12sp untuk atribusi).
- **Corner Radius:** 12dp untuk container video player, 8dp untuk speed pills, 16dp untuk bottom modal / alert dialog.

### 2.4. Design Handoff & 6 Visual States Matrix

| Komponen | Default | Focused | Loading | Error | Empty | Disabled |
|---|---|---|---|---|---|---|
| **Video Player** | Menampilkan thumbnail poster YouTube resmi | Outline ungu 2dp (`#9D3FE7`) | Spinner ungu berputar di atas latar `#1E1E2E` | Banner peringatan merah *"Video materi sedang diperbarui"* | N/A (VideoId valid selalu ada) | Kontainer redup dengan pesan offline |
| **Speed Pill Bar** | Pill abu-abu, teks gelap 14sp | Pill ungu muda, border ungu | N/A | N/A | N/A | Alpha 0.5f saat player buffering |
| **Progres Checkbox** | Kotak kosong border abu-abu | Border ungu 2dp | Animasi transisi centang | N/A | N/A | N/A |
| **Tombol Tonton di YT** | Tombol sekunder border ungu | Background ungu muda 10% | Spinner kecil di tombol | Disable jika offline | N/A | Alpha 0.4f saat tidak ada intent handler |
| **Tombol Laporkan** | Teks merah dengan ikon bendera | Teks merah bergaris bawah | Spinner pada dialog submit | Dialog galat pengiriman | N/A | Disabled jika input alasan belum dipilih |

---

## [PARALEL] FASE 3: PERILAKU SISTEM & TELEMETRI

### 3.1. Sequence Diagram (+ Trace ID & Client Events)

```text
User            VideoPlayerScreen        VideoPlayerViewModel        PlatformVideoPlayer         CurriculumRepository      TelemetryService
 │                     │                          │                           │                           │                        │
 │ 1. Buka Layar       │                          │                           │                           │                        │
 │────────────────────>│                          │                           │                           │                        │
 │                     │ 2. init(courseId, topic) │                           │                           │                        │
 │                     │─────────────────────────>│                           │                           │                        │
 │                     │                          │ 3. Gen Trace ID           │                           │                        │
 │                     │                          │    trc_vid_load_01_a9f2   │                           │                        │
 │                     │                          │──┐                        │                           │                        │
 │                     │                          │<─┘                        │                           │                        │
 │                     │                          │ 4. getCourseTopic(...)    │                           │                        │
 │                     │                          │──────────────────────────────────────────────────────>│                        │
 │                     │                          │ 5. Return Topic (videoId) │                           │                        │
 │                     │                          │<──────────────────────────────────────────────────────│                        │
 │                     │                          │ 6. Emit UiState(videoId)  │                           │                        │
 │                     │<─────────────────────────│                           │                           │                        │
 │                     │                          │ 7. trackEvent("video_screen_opened", trace_id)        │                        │
 │                     │                          │───────────────────────────────────────────────────────────────────────────────>│
 │                     │ 8. Inisialisasi WebView  │                           │                           │                        │
 │                     │    dengan videoId riil   │                           │                           │                        │
 │                     │─────────────────────────────────────────────────────>│                           │                        │
 │                     │                          │                           │ 9. Load YouTube Iframe API│                        │
 │                     │                          │                           │    YT.Player(videoId)     │                        │
 │                     │                          │                           │──┐                        │                        │
 │                     │                          │                           │<─┘                        │                        │
 │                     │                          │                           │ 10. onPlayerReady Callback│                        │
 │                     │                          │                           │──────────────────────────>│                        │
 │                     │                          │ 11. trackEvent("video_started", trace_id)                                      │
 │                     │                          │───────────────────────────────────────────────────────────────────────────────>│
 │                     │                          │                           │                                                    │
 │                     │                          │                           │ 12. onTimeUpdate(sec=920, dur=1080) [85.1%]        │
 │                     │                          │<──────────────────────────│                                                    │
 │                     │                          │ 13. Evaluasi Ambang 85%   │                                                    │
 │                     │                          │     isCompleted = true    │                                                    │
 │                     │                          │──┐                        │                                                    │
 │                     │                          │<─┘                        │                                                    │
 │                     │                          │ 14. setTopicCompleted(...)│                                                    │
 │                     │                          │──────────────────────────────────────────────────────>│                        │
 │                     │                          │ 15. Emit State (isDone)   │                           │                        │
 │                     │<─────────────────────────│                           │                           │                        │
 │                     │ 16. Update Checkbox [X]  │                           │                           │                        │
 │                     │                          │ 17. trackEvent("video_auto_completed", trace_id)                               │
 │                     │                          │───────────────────────────────────────────────────────────────────────────────>│
```

#### 5 Non-Happy Path Scenarios Wajib (E1..E5)
1. **E1 (Network Stall / Offline):**
   - *Kondisi:* Sambungan internet terputus saat memutar video YouTube aktif.
   - *Penanganan:* WebView menampilkan pesan kesalahan koneksi bawaan YouTube atau overlay lokal *"Koneksi terputus. Menunggu jaringan..."* tanpa me-reset state progres yang sudah tercatat.
2. **E2 (YouTube Video Error / Private / Removed):**
   - *Kondisi:* Video diubah statusnya menjadi privat atau dihapus oleh pemiliknya (menghasilkan kode galat JavaScript `100`, `101`, atau `150`).
   - *Penanganan:* Bridge menangkap kode galat tersebut melalui callback `onError`, mengubah `isError = true`, merender banner informatif *"Video materi sedang diperbarui"*, dan menampilkan tombol CTA *"Laporkan Link"* ke `ReportBrokenVideoDialog`.
3. **E3 (OS Process Death / Low Memory Killer):**
   - *Kondisi:* Sistem operasi menghentikan proses aplikasi saat video sedang di-pause di latar belakang.
   - *Penanganan:* Progres belajar disimpan secara atomik ke Multiplatform Settings KV Store saat `onPause` / `onTimeUpdate`, sehingga ketika user membuka kembali topik, status centang dan waktu tonton terakhir dapat dipulihkan secara instan.
4. **E4 (User Cancellation saat Loading):**
   - *Kondisi:* Pengguna menekan tombol kembali (*Back Gesture*) saat WebView masih mengunduh skrip YouTube Iframe API.
   - *Penanganan:* Lifecycle Compose membatalkan coroutine scope `VideoPlayerViewModel` secara bersih, menghancurkan instance WebView (`destroy()`), serta melepaskan seluruh listener JavaScript tanpa kebocoran memori.
5. **E5 (Corrupted Local Storage / Migration):**
   - *Kondisi:* Data KV store corrupt atau versi skema JSON kurikulum berubah.
   - *Penanganan:* Parser JSON menerapkan fallback `ignoreUnknownKeys = true` dan `isLenient = true`. Jika deserialisasi gagal total, aplikasi kembali ke state default dengan log telemetri error `storage_corrupted_fallback`.

---

### 3.2. State Diagram (+ Latency & Drop-offs)

```text
               ┌───────────────────────┐
               │    INIT_SYLLABUS      │
               └───────────┬───────────┘
                           │ (Pilih Pertemuan: Latensi < 100ms)
                           ▼
               ┌───────────────────────┐
               │   LOADING_METADATA    │
               └───────────┬───────────┘
                           │ (Parse JSON & Validasi 11-char ID)
                           ▼
               ┌───────────────────────┐
               │   LOADING_IFRAME      │ ◄── [Titik Kritis Drop-off #1: Latensi Jaringan > 3s]
               └─────┬───────────┬─────┘
  (Error Code 100/150│           │ (onPlayerReady: Latensi ~1.2s)
   atau ID Invalid)  │           ▼
                     │ ┌───────────────────────┐
                     │ │     VIDEO_PLAYING     │ ◄── [Titik Kritis Drop-off #2: Durasi Video Terlalu Panjang]
                     │ └─────┬───────────▲─────┘
                     │       │           │
                     │ (Pause│           │ (Resume)
                     │   Tap)▼           │
                     │ ┌───────────────────────┐
                     │ │     VIDEO_PAUSED      │
                     │ └─────┬─────────────────┘
                     │       │ (Waktu Tonton >= 85%)
                     │       ▼
                     │ ┌───────────────────────┐
                     │ │    AUTO_COMPLETED     │ ──> Simpan ke Local Storage (Latensi < 15ms)
                     │ └───────────────────────┘
                     ▼
       ┌───────────────────────────────┐
       │       VIDEO_ERROR_STATE       │ ◄── Fallback Banner Tampil
       └───────────────┬───────────────┘
                       │ (Tap Laporkan Link)
                       ▼
       ┌───────────────────────────────┐
       │      REPORT_DIALOG_OPEN       │
       └───────────────────────────────┘
```

---

## FASE 4: STRUKTUR DATA & KODE (Clean Architecture)

### 4.1. Class Diagram (ASCII Art)

```text
┌─────────────────────────────────────────────────────────────────────────────┐
│ PRESENTATION LAYER (composeApp/commonMain/ui)                               │
├─────────────────────────────────────────────────────────────────────────────┤
│ + VideoPlayerScreen(courseId: String, topicId: String)                     │
│ + PlatformVideoPlayer(videoId: String, onTimeUpdate, onError, onReady)      │
│ + ReportBrokenVideoDialog(onDismiss, onSubmit)                             │
│ + VideoPlayerViewModel(curriculumRepo, progressRepo, reportRepo)            │
│   - uiState: StateFlow<VideoPlayerUiState>                                  │
│   + onPlayPauseToggle()                                                     │
│   + onSpeedSelected(speed: PlaybackSpeed)                                   │
│   + onProgressEvaluated(currentTime: Float, duration: Float)                │
│   + submitBrokenReport(reason: ReportReason, desc: String)                  │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ depends on
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ DOMAIN LAYER (composeApp/commonMain/domain)                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│ + interface CurriculumRepository                                            │
│ + interface ProgressRepository                                              │
│ + interface VideoReportRepository                                           │
│ + data class VideoPlayerUiState(topic, videoId, isPlaying, isCompleted, ...)│
│ + enum class PlaybackSpeed(val multiplier: Float)                           │
│ + enum class ReportReason                                                   │
│ + object VideoIdValidator { fun isValidYouTubeId(id: String): Boolean }     │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │ implemented by
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│ DATA LAYER (composeApp/commonMain/data)                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│ + class LocalCurriculumRepository(jsonProvider: () -> String)               │
│ + object CurriculumDataSource { val RAW_JSON: String }                      │
│ + class SettingsProgressRepository(settings: Settings)                      │
│ + class LocalVideoReportRepository(settings: Settings)                      │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 4.2. Data Storage, Schema & 85% Idempotency Rule
- **Regex Validasi YouTube ID:** `^[a-zA-Z0-9_-]{11}$`
- **Idempotency Rule Auto-Centang ($\ge 85\%$):**
  - Perhitungan: `val progressRatio = currentTimeSeconds / totalDurationSeconds`
  - Ambang batas: `progressRatio >= 0.85f`
  - Aturan: Pemanggilan penyimpanan `progressRepository.setTopicCompleted(topicId, true)` hanya dilakukan sekali (*idempotent*). Apabila topic sudah berstatus `isCompleted == true` di memori atau persistent storage, callback `onTimeUpdate` tidak akan mengeksekusi operasi tulis berulang (*no-op*), menjaga performa disk I/O dan konsumsi daya baterai.

---

## FASE 5: ARSITEKTUR FISIK RUNTIME MOBILE

### 5.1. Component Diagram (KMP Runtime Ecosystem: Android & iOS Bridge)

```text
┌─────────────────────────────────────────────────────────────────────────────┐
│ Kotlin Multiplatform Shared Core (commonMain)                              │
│                                                                             │
│  ┌─────────────────────────┐          ┌──────────────────────────────────┐  │
│  │ VideoPlayerViewModel    │─────────>│ CurriculumRepository             │  │
│  │ (StateFlow & Business)  │          │ (curriculum_it_semesters.json)   │  │
│  └────────────┬────────────┘          └──────────────────────────────────┘  │
│               │                                                             │
│               ▼                                                             │
│  ┌─────────────────────────┐                                                │
│  │ expect Composable       │                                                │
│  │ PlatformVideoPlayer(...)│                                                │
│  └────────────┬────────────┘                                                │
└───────────────┼─────────────────────────────────────────────────────────────┘
                │
       ┌────────┴──────────────────────────┐
       │ (Platform Actual Implementation)  │
       ▼                                   ▼
┌───────────────────────────────┐ ┌───────────────────────────────────────────┐
│ androidMain                   │ │ iosMain                                   │
│                               │ │                                           │
│ ┌───────────────────────────┐ │ │ ┌───────────────────────────────────────┐ │
│ │ actual Composable         │ │ │ │ actual Composable                     │ │
│ │ PlatformVideoPlayer       │ │ │ │ PlatformVideoPlayer                   │ │
│ │ ┌───────────────────────┐ │ │ │ ┌─────────────────────────────────────┐ │ │
│ │ │ AndroidView           │ │ │ │ │ UIKitView                           │ │ │
│ │ │  └── android.webkit.  │ │ │ │ │  └── WebKit.WKWebView               │ │ │
│ │ │      WebView          │ │ │ │ │      (WKScriptMessageHandler Bridge)│ │ │
│ │ └───────────────────────┘ │ │ │ └─────────────────────────────────────┘ │ │
│ └───────────────────────────┘ │ │ └───────────────────────────────────────┘ │
└───────────────────────────────┘ └───────────────────────────────────────────┘
```

---

## 6. Testing Requirements & 7 Local Verification Gates

- **Gate 1: Linter & Formatter:**
  Perintah: `./gradlew composeApp:lintDebug`  
  Target: Exit code 0, nol error lint baru.
- **Gate 2: Data & API Contracts:**
  Perintah: `./gradlew test --tests "*CurriculumVideoContractTest*"`  
  Target: Seluruh 16 topik aktif `cs101` dan topik terpilih Semester 1 tervalidasi memenuhi pola regex 11-karakter YouTube ID.
- **Gate 3a: Unit Tests (Coverage $\ge 80\%$):**
  Perintah: `./gradlew test`  
  Target: Seluruh unit test di `commonTest` (termasuk validasi parsing kurikulum, auto-complete, dan report repository) lolos 100%.
- **Gate 3b: Negative Control Wajib:**
  Prosedur: Sengaja ubah salah satu video ID di `CurriculumDataSource.kt` menjadi ID mock (`t101_01_v`) → Jalankan `./gradlew test` → Verifikasi kegagalan test (**RED**, exit code 1) pada `CurriculumVideoContractTest` → Kembalikan ke ID riil (`jGyYuQf-GeE`) → Jalankan kembali test → Verifikasi kelolosan (**GREEN**, exit code 0).  
  Target: Dokumentasikan rasio pembuktian `N/N → (N-1)/N → N/N`.
- **Gate 4: Component & Viewport Verification:**
  Memvalidasi antarmuka pemutar video pada resolusi standar mobile 375x812 dp di kedua sistem operasi (Android Pixel 9 Pro dan iOS Simulator iPhone 16 Pro).
- **Gate 5: Performance & Build Artifacts:**
  - Android: `./gradlew assembleDebug` menghasilkan binary `composeApp-debug.apk`.
  - iOS: `./gradlew composeApp:linkDebugFrameworkIosSimulatorArm64` menghasilkan bundle `ComposeApp.framework`.
- **Gate 6: Security & Zero Secret Hygiene:**
  Verifikasi `git diff` memastikan tidak ada secret API key YouTube, token auth rahasia, atau kredensial backend hardcoded.

---

## 7. Standar Pengarsipan Evidence (Dual-Platform: Android & iOS)

Struktur direktori evidence di `.docs/evidence/03/`:
```text
.docs/evidence/03/
├── README.md               # Ringkasan 7 gerbang, tabel verifikasi Android vs iOS, rasio Gate 3b
├── console-evidence.txt    # Log eksekusi build & test (Android APK + iOS Framework)
├── android/
│   ├── demo.mp4            # Video walkthrough Android (Pixel_9_Pro via android CLI / screenrecord)
│   └── screenshots/        # Tangkapan layar Android (01_player_active.png, 02_fullscreen.png, dst.)
└── ios/
    ├── demo.mp4            # Video walkthrough iOS (iPhone 16 Pro via xcrun simctl)
    └── screenshots/        # Tangkapan layar iOS (01_player_active.png, 02_fullscreen.png, dst.)
```
Serta memperbarui berkas rekaman video lama di `.docs/evidence/02/demo.mp4` dengan video pemutaran nyata tanpa layar hitam.
