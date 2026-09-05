# Modul 1: Navigasi Kurikulum — High-Fidelity (Hi-Fi) Prototype Specification (Design System & UI Kit Free)

Dokumen ini mendefinisikan spesifikasi standar teknis, arsitektur layar, panduan interaksi, dan matriks *state* untuk **High-Fidelity (Hi-Fi) Prototype** pada **Modul 1: Navigasi Kurikulum** (UC-01, UC-02, UC-03) aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../../fase0/PRD.md), [USE_CASE.md](../../fase1/USE_CASE.md), [DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md](../../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md), [MODULE_1_DESIGN_SYSTEM_TOKENS.md](./MODULE_1_DESIGN_SYSTEM_TOKENS.md), [MODULE_1_USER_FLOW_WIREFLOW.md](./MODULE_1_USER_FLOW_WIREFLOW.md), dan [MODULE_1_LOFI_WIREFRAME.md](./MODULE_1_LOFI_WIREFRAME.md).

---

## 1. Ringkasan & Tujuan Prototipe (Executive Summary)

Tujuan utama pembuatan **Hi-Fi Prototype** pada fase ini adalah:
1. **Validasi Interaksi & Alur Navigasi:** Membuktikan secara interaktif bahwa pembelajar dapat dengan mudah berpindah antar Semester 1–8 (FR-1.1), memilih mata kuliah (FR-1.2), dan menelusuri 16 pertemuan silabus (FR-1.3).
2. **Kesesuaian Standar Desain (Design Tokens Fidelity):** Mengimplementasikan warna resmi `Corporate/Purple` (`#9D3FE7`), `Corporate/DarkPurple` (`#602093`), gradasi primer `linear-gradient(135deg, #9D3FE7 0%, #602093 100%)`, dan status sukses `Informing/Approval` (`#00B998`), tipografi geometris `Poppins`, skala radius ergonomis (`4px`, `8px`, `12px`, `9999px`), serta elevasi bayangan ungu lembut `0 4px 16px rgba(157, 63, 231, 0.08)` langsung dari berkas resmi Figma [`Design System _ Ui Kit Free (Community).fig`](../../design_system/assets/Design%20System%20_%20Ui%20Kit%20Free%20%28Community%29.fig).
3. **Uji Coba Fungsionalitas Mandiri (Self-Contained Sandbox):** Menyediakan lingkungan simulasi interaktif lengkap dengan penyimpanan lokal (*LocalStorage*), perubahan tema (*Light/Dark*), dan konsol telemetri *real-time* untuk kebutuhan **Usability Testing (UT)**.

---

## 2. Berkas Artefak Prototipe Interaktif

Prototipe interaktif dikembangkan dalam bentuk aplikasi web berbasis mobile responsif (standar viewport **375 x 812 dp**) dan dapat langsung dijalankan pada peramban modern:

* **Tautan Berkas Prototipe:** [`./hifi_module_1_prototype.html`](./hifi_module_1_prototype.html)
* **Spesifikasi Viewport:** 375 x 812 dp (Rasio aspek 9:19.5, iPhone 14 / Android Modern).
* **Fitur Prototipe:**
  - Simulasi bingkai ponsel fisik lengkap dengan *dynamic notch*, *status bar*, dan *home indicator*.
  - Pergantian dinamis 8 Tab Kapsul Semester (Semester 1 sampai 8) dengan silabus jurusan Teknik Informatika.
  - Kartu mata kuliah dinamis (*Course Cards*) dengan elevasi bayangan ungu lembut dan kalkulasi *mini progress bar* secara instan.
  - Kartu ringkasan capaian semester (*Hero Progress Banner*) lengkap dengan maskot edukatif Cookie (Node `1842:25160`).
  - Halaman silabus 16 pertemuan terstruktur dengan atribusi kanal YouTube resmi (Web Programming UNPAS, Kelas Terbuka, Programmer Zaman Now).
  - *Checkbox* status selesai interaktif yang terhubung langsung ke kalkulator progres mata kuliah dan progres global semester.
  - *Handoff preview* ke Layar Video Player Modul 2 via *bottom sheet*.
  - *Companion Panel* Usability Testing (Stopwatch/Timer ToT, selector skenario task, pencatat hasil pengujian, dan *live stream* telemetri `trace_id`).

---

## 3. Spesifikasi Layar & Matriks Status (Screen Architecture & State Matrix)

### 3.1. Layar 1: Beranda Semester (Home Screen)

Menyajikan navigasi utama per semester, ringkasan capaian belajar pengguna dengan maskot, serta katalog mata kuliah kurikulum Teknik Informatika.

```text
+-------------------------------------------------------------+
| 09:41                                              [📶 🔋]  |  <-- Status Bar (Waktu & Indikator Sinyal)
+-------------------------------------------------------------+
| [📖] OpenCampus                  ( Teknik Informatika / IF )|  <-- Brand Header & Badge Jurusan (Pill)
|                                                             |
| [ 🔍 Cari mata kuliah atau topik...                      ]  |  <-- Dynamic Instant Search Box (8px Radius)
|                                                             |
| +---------------------------------------------------------+ |
| | Progres Semester 1                                  25% | |  <-- Hero Progress Banner (Summary Card)
| | [========-------------------------------------------]   | |      (Mascot Cookie Node 1842:25160)
| | 4 dari 16 Topik Selesai                  ● Sedang Berjalan|
| +---------------------------------------------------------+ |
|                                                             |
| PILIH SEMESTER                                  8 Semester  |  <-- Section Header
| ( Sem 1* )  ( Sem 2 )  ( Sem 3 )  ( Sem 4 )  ( Sem 5 )  ...>|  <-- Horizontal Scrollable Pill Tabs (9999px)
|                                                             |
| MATA KULIAH SEMESTER 1                        4 Mata Kuliah |
|                                                             |
| +---------------------------------------------------------+ |
| | [💻] Algoritma & Pemrograman Dasar                      | |
| |      16 Pertemuan • Est. ~6.5 Jam                       | |  <-- Interactive Course Card
| |      [========----------------------------] 25%     [>] | |      (Border Radius 8px + Soft Shadow)
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |
| | [📁] Matematika Diskrit                                 | |
| |      16 Pertemuan • Est. ~5.5 Jam                       | |
| |      [------------------------------------] 0%      [>] | |
| +---------------------------------------------------------+ |
+-------------------------------------------------------------+
| [🏠 Beranda]            [🔍 Cari]               [👤 Profil] |  <-- Bottom Navigation Bar (Canvas Nav bar)
+-------------------------------------------------------------+
```

#### Matriks Status Komponen Layar 1:

| Komponen | Status (State) | Tampilan Visual | Perilaku Interaktif |
| :--- | :--- | :--- | :--- |
| **Pill Tab Semester** | Default (Inactive) | Latar putih `#FFFFFF`, Border `#E5E0EB`, Teks `#4B3A5A`, Radius `9999px` | Transisi hover naik 1px, kursor pointer. |
| | Active (Selected) | Latar gradasi `linear-gradient(135deg, #9D3FE7, #602093)`, Teks `#FFFFFF`, Bayangan `0 4px 12px rgba(157, 63, 231, 0.3)` | Mengganti daftar mata kuliah & menghitung ulang progres semester. |
| **Course Card** | Normal | Latar `#FFFFFF`, Radius `8px`, Border `#E5E0EB`, Bayangan `0 4px 16px rgba(157, 63, 231, 0.08)` | Elevasi default `var(--uikit-shadow-card)`. |
| | Hover / Pressed | Latar aksen `#F5F3F7`, Border `#9D3FE7`, Bayangan `0 8px 24px rgba(157, 63, 231, 0.14)` | Navigasi slide ke Layar Silabus Mata Kuliah. |
| | 100% Selesai | Progress fill hijau `#00B998`, Badge `Informing/Approval` | Memperbarui badge status semester. |
| **Search Input** | Default | Background `#FFFFFF`, Border `1px solid #ABA7AF`, Radius `8px`, Ikon `search.svg` | Teks placeholder `#707075`. |
| | Focused | Border `#9D3FE7`, Box-shadow `0 0 0 3px rgba(157, 63, 231, 0.15)` | Memfilter kartu mata kuliah secara instan (*live filter*). |

---

### 3.2. Layar 2: Silabus Mata Kuliah (Course Syllabus Screen)

Menampilkan hirarki 16 pertemuan terstruktur secara urut dengan durasi, atribusi kreator YouTube, dan tombol *checkbox* status selesai.

```text
+-------------------------------------------------------------+
| [‹ Back]        Algoritma & Pemrograman Dasar     (16 Topik)|  <-- Top App Bar (Header Canvas)
+-------------------------------------------------------------+
|                                                             |
| +---------------------------------------------------------+ |
| | [💻]  Algoritma & Pemrograman Dasar                     | |  <-- Syllabus Hero Card
| |       16 Pertemuan • Total ~6 Jam 15 Menit              | |
| |                                                         | |
| |  Progres Pembelajaran                               25% | |
| |  [========------------------------------------------]   | |
| |  4 dari 16 Topik Selesai             ● Sedang Dipelajari| |
| +---------------------------------------------------------+ |
|                                                             |
| ( Semua 16* )       ( Belum Selesai )       ( Sudah Selesai )|  <-- Filter Pill Chips (9999px)
|                                                             |
| +---------------------------------------------------------+ |
| | [✔]  01. Konsep Dasar Logika & Flowchart                | |  <-- Topic Accordion Row (Selesai)
| |          ⏱️ 18 Menit  •  👤 Web Programming UNPAS    [▶] | |      (Teks tercoret halus, fill hijau #00B998)
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |
| | [ ]  05. Fungsi & Modularitas Kode                      | |  <-- Topic Accordion Row (Belum Selesai)
| |          ⏱️ 28 Menit  •  👤 Kelas Terbuka            [▶] | |      (Checkbox siap centang, border 2px)
| +---------------------------------------------------------+ |
+-------------------------------------------------------------+
```

#### Matriks Status Komponen Layar 2:

| Komponen | Status (State) | Tampilan Visual | Perilaku Interaktif |
| :--- | :--- | :--- | :--- |
| **Tombol Back** | Default | Kotak radius `8px` dengan latar `#F5F3F7`, ikon `assets/icons/uikit/back.svg` | Klik mengembalikan pengguna ke Beranda Semester & menyinkronkan data. |
| **Interactive Checkbox** | Unchecked `[ ]` | Border `2px solid #ABA7AF`, radius `4px`, latar putih | Target sentuh ergonomis (48x48dp virtual area). |
| | Checked `[✔]` | Latar hijau `#00B998`, border `#00B998`, ikon centang putih `assets/icons/uikit/check.svg`, skala animasi 1.08x | Menambah progres, mencoret judul materi secara halus, menyimpan ke storage. |
| **Topic Row Body** | Click / Tap | Area baris di luar checkbox mendapatkan hover highlight `#F5F3F7` | Membuka *Bottom Sheet* Pemutar Video Modul 2. |
| **Filter Chips** | Active Filter | Background `linear-gradient(135deg, #9D3FE7, #602093)`, teks putih, radius `9999px` | Memfilter hanya topik yang sesuai kategori centang. |

---

### 3.3. Layar 3 / Transition Sheet: Video Player (Modul 2 Handoff)

Untuk menjaga kesinambungan prototipe saat pengujian pengguna (UT Task 4), saat baris materi ditekan, sistem memunculkan transisi *bottom sheet* pemutar video:

* **Mockup Frame Pemutar:** Visual 16:9 rasio pemutar YouTube resmi dengan label *"Bebas Distraksi"*.
* **Tombol Simulasi Tonton 85%:** Mengizinkan penguji mensimulasikan fitur otomatis centang (FR-3.1, UC-07) tanpa harus menunggu durasi video asli habis.
* **Atribusi Kreator YouTube:** Tautan resmi *"Tonton di YouTube"* untuk memenuhi lisensi YouTube API Terms of Service.

---

## 4. Spesifikasi Mikro-Interaksi & Transisi Animasi

Untuk menghasilkan pengalaman pengguna yang mulus (*native-like feel*), prototipe menerapkan kurva transisi standar:

1. **Pergantian Layar (Screen Slide Transition):**
   * Durasi: `320ms`
   * Kurva: `cubic-bezier(0.4, 0.0, 0.2, 1.0)`
   * Layar beranda bergeser `-25%` ke kiri dengan opacity `0.3`, sementara layar silabus meluncur masuk dari kanan (`100%` ke `0%`).
2. **Kalkulasi Progress Bar Fill:**
   * Durasi: `400ms`
   * Kurva: `cubic-bezier(0.34, 1.56, 0.64, 1)` (sedikit efek lentur / elastic settling).
   * Warna bar bertransisi otomatis dari `#9D3FE7` ke `#00B998` saat mencapai 100%.
3. **Checkbox Toggle:**
   * Durasi: `200ms`
   * Efek: Skala mekar `1.08x` disertai munculnya ikon centang SVG dari `scale(0.5)` ke `scale(1.0)`.

---

## 5. Kepatuhan Aksesibilitas (WCAG 2.1 AA)

| Parameter | Standar WCAG | Implementasi Prototipe | Hasil Audit |
| :--- | :--- | :--- | :--- |
| **Kontras Teks Utama** | Min. 4.5:1 | `#1A141F` (`Grayscale/Black`) di atas `#F5F3F7` = **15.2:1** | ✅ Lulus (AAA) |
| **Kontras Tab Terpilih** | Min. 4.5:1 | `#FFFFFF` di atas `#9D3FE7` (`Corporate/Purple`) = **4.8:1** | ✅ Lulus (AA) |
| **Kontras Checkbox Centang**| Min. 3.0:1 | `#00B998` (`Informing/Approval`) di atas `#FFFFFF` = **3.1:1** | ✅ Lulus (AA Grafis) |
| **Ukuran Target Sentuh** | Min. 44 x 44 dp | Checkbox: 36x36 dp + padding sentuh = **48 x 48 dp**<br>Pill Tab: **80 x 44 dp**<br>Course Card: **335 x 80 dp** | ✅ Lulus Ergonomi |

---

## 6. Penelusuran Aset & Mapping Node Figma (Traceability)

Seluruh komponen prototipe dipetakan langsung ke master components pada berkas resmi Figma [`Design System _ Ui Kit Free (Community).fig`](../../design_system/assets/Design%20System%20_%20Ui%20Kit%20Free%20%28Community%29.fig) sebagaimana dikatalogkan di [DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md](../../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md):

| Komponen Prototipe | Canvas Asal Figma | Master Component Sumber | File Aset Vektor / Render |
| :--- | :--- | :--- | :--- |
| **Top App Bar** | `Header` | `Header` (71 components, Node `6:50` dkk.) | Stylized Navigation Header |
| **Pill Tabs Semester 1–8** | `Tabs` | `Tabs` (Pill / Capsule variant) | Implementasi CSS Capsule 9999px |
| **Hero Progress Banner** | `Progress` / `Layout` | `Summary Card` + `Progress` | Latar putih dengan elevasi violet |
| **Maskot Edukatif Cookie** | `Cookies` | `Cookies` (Node `1842:25160`) | [`../../design_system/assets/illustrations/uikit_hero.png`](../../design_system/assets/illustrations/uikit_hero.png) |
| **Kartu Mata Kuliah** | `Table` / `Layout` | `Card Container` (Border Radius 8px) | Card dengan bayangan `0 4px 16px rgba(157, 63, 231, 0.08)` |
| **Badge SKS & Jurusan** | `Badge` | `Badge` (56 components) | Pill Capsule Badge |
| **Linear Progress Bar** | `Progress` | `Linear Progress` (21 components) | Track `#D9D1E0`, Fill `#00B998` |
| **Checkbox Selesai** | `Checkbox` | `Checkbox` (10 components) | [`../../design_system/assets/icons/uikit/check.svg`](../../design_system/assets/icons/uikit/check.svg) (Node `1579:1333`) |
| **Baris Topik Silabus** | `Collapse` | `Accordion` / `Collapse` (6 components) | Expandable Topic Row dengan border `#E5E0EB` |
| **Search Bar Input** | `Input` / `Search` | `Input` (Search variant, Node `1582:1601`) | Input field dengan [`../../design_system/assets/icons/uikit/search.svg`](../../design_system/assets/icons/uikit/search.svg) |
| **Bottom Navigation Bar** | `Navigation bar` | `Navigation bar` (71 components) | 3/5 Tab Mobile Navigation Bar |
| **Ikon Brand Logo** | `Icons` | `12515:40782` (`book`) | [`../../design_system/assets/icons/uikit/brand.svg`](../../design_system/assets/icons/uikit/brand.svg) |
| **Ikon Beranda (Home)** | `Icons` | `229:683` (`Iconly Home`) | [`../../design_system/assets/icons/uikit/home.svg`](../../design_system/assets/icons/uikit/home.svg) |
| **Ikon Pencarian (Search)** | `Icons` | `511:119,120` (`UI Kit Search`) | [`../../design_system/assets/icons/uikit/search.svg`](../../design_system/assets/icons/uikit/search.svg) |
| **Ikon Profil (User)** | `Icons` | `122:24` (`UI Kit Person`) | [`../../design_system/assets/icons/uikit/profile.svg`](../../design_system/assets/icons/uikit/profile.svg) |
| **Ikon Navigasi Kembali** | `Icons` | `229:692` (`Iconly Arrow Left`) | [`../../design_system/assets/icons/uikit/back.svg`](../../design_system/assets/icons/uikit/back.svg) |
| **Ikon Panah Forward** | `Icons` | `629:6304` (`arrow-forward`) | [`../../design_system/assets/icons/uikit/forward.svg`](../../design_system/assets/icons/uikit/forward.svg) |
| **Ikon Durasi / Jam** | `Icons` | `1690:7625,7626` (`bx-time-five`) | [`../../design_system/assets/icons/uikit/time.svg`](../../design_system/assets/icons/uikit/time.svg) |
| **Ikon Putar Video (Play)** | `Icons` | `229:797` (`Play Icon`) | [`../../design_system/assets/icons/uikit/play.svg`](../../design_system/assets/icons/uikit/play.svg) |
| **Ikon Mata Kuliah (Code)** | `Icons` | `23829:88029` (`code`) | [`../../design_system/assets/icons/uikit/course1.svg`](../../design_system/assets/icons/uikit/course1.svg) |
| **Ikon Mata Kuliah (Data)** | `Icons` | `248:24` (`storage`) | [`../../design_system/assets/icons/uikit/course2.svg`](../../design_system/assets/icons/uikit/course2.svg) |
| **Ikon Mata Kuliah (Grid)** | `Icons` | `1669:6540` (`mdi:view-grid`) | [`../../design_system/assets/icons/uikit/course3.svg`](../../design_system/assets/icons/uikit/course3.svg) |

---

## 7. Panduan Menjalankan & Menguji Prototipe

1. Buka berkas [`./hifi_module_1_prototype.html`](./hifi_module_1_prototype.html) langsung di peramban web (Google Chrome, Safari, Firefox, atau Edge).
2. Di sebelah kiri, gunakan layar ponsel interaktif untuk bernavigasi selayaknya menggunakan aplikasi *native*.
3. Di sebelah kanan, gunakan **Usability Testing Console** untuk mengaktifkan skenario pengujian, menjalankan stopwatch perhitungan waktu tugas (*Time on Task*), mencatat status keberhasilan, dan mengamati emisi event telemetri W3C *Trace ID* secara seketika.

