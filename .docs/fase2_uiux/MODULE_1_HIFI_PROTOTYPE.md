# Modul 1: Navigasi Kurikulum — High-Fidelity (Hi-Fi) Prototype Specification (MVP v0.1)

Dokumen ini mendefinisikan spesifikasi standar teknis, arsitektur layar, panduan interaksi, dan matriks *state* untuk **High-Fidelity (Hi-Fi) Prototype** pada **Modul 1: Navigasi Kurikulum** (UC-01, UC-02, UC-03) aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../fase0/PRD.md), [USE_CASE.md](../fase1/USE_CASE.md), [MODULE_1_DESIGN_SYSTEM_TOKENS.md](./MODULE_1_DESIGN_SYSTEM_TOKENS.md), [MODULE_1_USER_FLOW_WIREFLOW.md](./MODULE_1_USER_FLOW_WIREFLOW.md), dan [MODULE_1_LOFI_WIREFRAME.md](./MODULE_1_LOFI_WIREFRAME.md).

---

## 1. Ringkasan & Tujuan Prototipe (Executive Summary)

Tujuan utama pembuatan **Hi-Fi Prototype** pada fase ini adalah:
1. **Validasi Interaksi & Alur Navigasi:** Membuktikan secara interaktif bahwa pembelajar dapat dengan mudah berpindah antar Semester 1–8 (FR-1.1), memilih mata kuliah (FR-1.2), dan menelusuri 16 pertemuan silabus (FR-1.3).
2. **Kesesuaian Standar Desain (Design Tokens Fidelity):** Mengimplementasikan warna resmi (`#6D57FC`), tipografi display (`Urbanist`) dan body (`Inter`), radius (`10px` s.d. `16px`), serta elevasi kartu dari *Styleguide* Figma.
3. **Uji Coba Fungsionalitas Mandiri (Self-Contained Sandbox):** Menyediakan lingkungan simulasi interaktif lengkap dengan penyimpanan lokal (*LocalStorage*), perubahan tema (*Light/Dark*), dan konsol telemetri *real-time* untuk kebutuhan **Usability Testing (UT)**.

---

## 2. Berkas Artefak Prototipe Interaktif

Prototipe interaktif telah dikembangkan dalam bentuk aplikasi web berbasis mobile responsif (standar viewport **375 x 812 dp**) dan dapat langsung dijalankan pada browser modern tanpa konfigurasi server tambahan:

* **Tautan Berkas Prototipe:** [`assets/hifi_module_1_prototype.html`](./assets/hifi_module_1_prototype.html)
* **Spesifikasi Viewport:** 375 x 812 dp (Rasio aspek 9:19.5, iPhone 14 / Android Modern).
* **Fitur Prototipe:**
  - Simulasi bingkai ponsel fisik lengkap dengan *dynamic notch*, *status bar*, dan *home indicator*.
  - Pergantian dinamis 8 Tab Semester (Semester 1 sampai 8) dengan silabus jurusan Teknik Informatika.
  - Kartu mata kuliah dinamis beserta kalkulasi *mini progress bar* secara instan.
  - Halaman silabus 16 pertemuan lengkap dengan atribusi kanal YouTube resmi (Web Programming UNPAS, Kelas Terbuka, Programmer Zaman Now).
  - *Checkbox* status selesai interaktif yang terhubung langsung ke kalkulator progres mata kuliah dan progres global semester.
  - *Handoff preview* ke Layar Video Player Modul 2 via *bottom sheet*.
  - *Companion Panel* Usability Testing (Stopwatch/Timer ToT, selector skenario task, pencatat hasil pengujian, dan *live stream* telemetri `trace_id`).

---

## 3. Spesifikasi Layar & Matriks Status (Screen Architecture & State Matrix)

### 3.1. Layar 1: Beranda Semester (Home Screen)

Menyajikan navigasi utama per semester, ringkasan capaian belajar pengguna, serta katalog mata kuliah kurikulum Teknik Informatika.

```text
+-------------------------------------------------------------+
| 09:41                                              [📶 🔋]  |  <-- Status Bar (Waktu & Indikator Sinyal)
+-------------------------------------------------------------+
| 🎓 OpenCampus                    [ Teknik Informatika (IF) ]|  <-- Brand Header & Badge Jurusan
|                                                             |
| [ 🔍 Cari mata kuliah atau topik...                      ]  |  <-- Dynamic Instant Search Box
|                                                             |
| +---------------------------------------------------------+ |
| | [=== Accent Bar ======================================] | |
| | Progres Semester 1                                  25% | |  <-- Dynamic Progress Summary Card
| | [========-------------------------------------------]   | |      (Sinkron ke LocalStorage)
| | 4 dari 16 Topik Selesai                  ● Sedang Berjalan|
| +---------------------------------------------------------+ |
|                                                             |
| PILIH SEMESTER                                  8 Semester  |  <-- Section Header
| +-------+  +-------+  +-------+  +-------+  +-------+       |
| | Sem 1*|  | Sem 2 |  | Sem 3 |  | Sem 4 |  | Sem 5 |  ...> |  <-- Horizontal Scrollable Tabs
| +-------+  +-------+  +-------+  +-------+  +-------+       |
|                                                             |
| MATA KULIAH SEMESTER 1                        4 Mata Kuliah |
|                                                             |
| +---------------------------------------------------------+ |
| | [💻] Algoritma & Pemrograman Dasar                      | |
| |      16 Pertemuan • Est. ~6.5 Jam                       | |  <-- Interactive Course Card
| |      [========----------------------------] 25%     [>] | |      (Klik untuk buka Layar 2)
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |
| | [📐] Matematika Diskrit                                 | |
| |      16 Pertemuan • Est. ~5.5 Jam                       | |
| |      [------------------------------------] 0%      [>] | |
| +---------------------------------------------------------+ |
+-------------------------------------------------------------+
| [🏠 Beranda]            [🔍 Cari]               [👤 Profil] |  <-- Bottom Navigation Bar
+-------------------------------------------------------------+
```

#### Matriks Status Komponen Layar 1:

| Komponen | Status (State) | Tampilan Visual | Perilaku Interaktif |
| :--- | :--- | :--- | :--- |
| **Tab Semester** | Default (Inactive) | Latar putih `#FFFFFF`, Border `#DFDAFE`, Teks `#4B5563` | Transisi hover naik 1px, kursor pointer. |
| | Active (Selected) | Latar `#6D57FC`, Teks `#FFFFFF`, Titik putih indikator, Bayangan ungu | Mengganti daftar mata kuliah & menghitung ulang progres semester. |
| **Course Card** | Normal | Latar `#FFFFFF`, Radius `16px`, Bayangan halus | Elevasi default `var(--elevation-card)`. |
| | Hover / Pressed | Latar aksen `#E8E4FF`, Border `#B0A4FD`, Elevasi naik | Navigasi slide ke Layar Silabus Mata Kuliah. |
| | 100% Selesai | Progress fill hijau `#0E9F6E`, Badge "Tuntas" | Memperbarui badge status semester. |
| **Search Input** | Focused | Border berganti ke `#6D57FC`, Bayangan fokus halus | Memfilter kartu mata kuliah secara instan (*live filter*). |

---

### 3.2. Layar 2: Silabus Mata Kuliah (Course Syllabus Screen)

Menampilkan hirarki 16 pertemuan terstruktur secara urut dengan durasi, atribusi kreator YouTube, dan tombol *checkbox* status selesai.

```text
+-------------------------------------------------------------+
| [< Back]        Algoritma & Pemrograman Dasar           [ℹ️] |  <-- Top App Bar dengan Navigasi Kembali
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
| [ Semua (16)* ]     [ Belum Selesai ]     [ Sudah Selesai ] |  <-- Filter Chips Bar
|                                                             |
| +---------------------------------------------------------+ |
| | [✔]  01. Konsep Dasar Logika & Flowchart                | |  <-- Topic Row (Selesai)
| |          ⏱️ 18 Menit  •  👤 Web Programming UNPAS    [▶] | |      (Teks tercoret halus)
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |
| | [ ]  05. Fungsi & Modularitas Kode                      | |  <-- Topic Row (Belum Selesai)
| |          ⏱️ 28 Menit  •  👤 Kelas Terbuka            [▶] | |      (Checkbox siap centang)
| +---------------------------------------------------------+ |
+-------------------------------------------------------------+
```

#### Matriks Status Komponen Layar 2:

| Komponen | Status (State) | Tampilan Visual | Perilaku Interaktif |
| :--- | :--- | :--- | :--- |
| **Tombol Back** | Default | Kotak radius `12px` dengan ikon panah kiri resmi Figma | Klik mengembalikan pengguna ke Beranda Semester & menyinkronkan data. |
| **Interactive Checkbox** | Unchecked `[ ]` | Border `2px solid #DFDAFE`, latar transparan | Target sentuh ergonomis (36x36dp virtual area). |
| | Checked `[✔]` | Latar hijau `#0E9F6E`, ikon centang putih, skala animasi 1.08x | Menambah progres, mencoret judul materi secara halus, menyimpan ke storage. |
| **Topic Row Body** | Click / Tap | Area baris di luar checkbox mendapatkan hover highlight | Membuka *Bottom Sheet* Pemutar Video Modul 2. |
| **Filter Chips** | Active Filter | Background `#6D57FC`, teks putih | Memfilter hanya topik yang sesuai kategori centang. |

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
   * Layar beranda bergeser `-30%` ke kiri dengan opacity `0.4`, sementara layar silabus meluncur masuk dari kanan (`100%` ke `0%`).
2. **Kalkulasi Progress Bar Fill:**
   * Durasi: `400ms`
   * Kurva: `cubic-bezier(0.34, 1.56, 0.64, 1)` (sedikit efek lentur / elastic settling).
   * Warna bar bertransisi otomatis dari `#6D57FC` ke `#0E9F6E` saat mencapai 100%.
3. **Checkbox Toggle:**
   * Durasi: `200ms`
   * Efek: Skala mekar `1.08x` disertai munculnya ikon centang dari `scale(0.5)` ke `scale(1.0)`.

---

## 5. Kepatuhan Aksesibilitas (WCAG 2.1 AA)

| Parameter | Standar WCAG | Implementasi Prototipe | Hasil Audit |
| :--- | :--- | :--- | :--- |
| **Kontras Teks Utama** | Min. 4.5:1 | `#0C0A1C` di atas `#F8F7FF` = **15.8:1** | ✅ Lulus (AAA) |
| **Kontras Tab Terpilih** | Min. 4.5:1 | `#FFFFFF` di atas `#6D57FC` = **5.2:1** | ✅ Lulus (AA) |
| **Kontras Checkbox Centang**| Min. 3.0:1 | `#0E9F6E` di atas `#FFFFFF` = **3.4:1** | ✅ Lulus (AA) |
| **Ukuran Target Sentuh** | Min. 44 x 44 dp | Checkbox: 36x36 dp + padding sentuh = **48 x 48 dp**<br>Tab: **72 x 44 dp**<br>Course Card: **335 x 80 dp** | ✅ Lulus Ergonomi |

---

## 6. Penelusuran Aset & Mapping Node Figma (Traceability)

| Komponen Prototipe | Node Figma Asal | File Aset Vektor / Render |
| :--- | :--- | :--- |
| **Layar Beranda** | `High-Fidelity ( Category Home )` (`229:773`) | [`figma_category_home_render.png`](./assets/figma_category_home_render.png) |
| **Layar Silabus & Detail** | `High-Fidelity ( Detail )` (`229:1189`) | [`figma_detail_course_render.png`](./assets/figma_detail_course_render.png) |
| **Kartu Baris Pertemuan** | `CourseEpisodeRow` (`229:1213`) | Implementasi HTML/CSS fleksibel |
| **Tombol Panah Kembali** | `Iconly/Broken/Arrow - Left` (`229:691`) | [`arrow_left.svg`](./assets/icons/arrow_left.svg) |
| **Indikator Masuk Chevron** | `Iconly/Broken/Arrow - Right` (`229:1152`) | [`arrow_right.svg`](./assets/icons/arrow_right.svg) |
| **Ikon Durasi & Waktu** | `Iconly/Regular/Broken/Time Circle` (`229:1381`) | [`time_circle.svg`](./assets/icons/time_circle.svg) |
| **Ikon Profil & Kreator** | `Iconly/Regular/Broken/Profile` (`229:690`) | [`profile.svg`](./assets/icons/profile.svg) |

---

## 7. Panduan Menjalankan & Menguji Prototipe

1. Buka berkas [`assets/hifi_module_1_prototype.html`](./assets/hifi_module_1_prototype.html) langsung di peramban web (Google Chrome, Safari, Firefox, atau Edge).
2. Di sebelah kiri, gunakan layar ponsel interaktif untuk bernavigasi selayaknya menggunakan aplikasi *native*.
3. Di sebelah kanan, gunakan **Usability Testing Console** untuk mengaktifkan skenario pengujian, menjalankan stopwatch perhitungan waktu tugas (*Time on Task*), mencatat status keberhasilan, dan mengamati emisi event telemetri W3C *Trace ID* secara seketika.
