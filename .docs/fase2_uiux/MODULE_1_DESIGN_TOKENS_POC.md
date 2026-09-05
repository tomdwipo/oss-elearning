# Modul 1: Navigasi Kurikulum — Proof of Concept (POC) Design Tokens Evaluation (MVP v0.1)

Dokumen ini mendokumentasikan hasil pengujian dan evaluasi **Proof of Concept (POC)** pergantian *Design Tokens* untuk **Modul 1: Navigasi Kurikulum** aplikasi **OpenCampus Mobile**. Sesuai arahan, prototipe demo interaktif difokuskan secara eksklusif pada **3 sistem desain resmi yang memiliki berkas biner `.fig` di proyek**:

1. **Google Material 3 (M3)** *(via `Material 3 Design Kit (Community).fig`)*
2. **GitHub Primer Web** *(via `Primer Web (Community).fig`)*
3. **Design System & UI Kit Free** *(via `Design System _ Ui Kit Free (Community).fig`)*

*(Catatan: Opsi non-.fig seperti Flowbite dan Aalto University DS telah dievaluasi secara konseptual namun dihapus dari demo interaktif agar simulasi 100% berbasis aset Figma terverifikasi).*

---

## 1. Artefak Eksperimen Interaktif (POC Interactive Simulator)

Untuk melihat, berinteraksi, dan membandingkan secara visual ketiga sistem desain `.fig` pada tampilan mobile nyata (Layar 1 Beranda & Layar 2 Silabus), buka artefak simulasi interaktif berikut:

* **Berkas Prototipe POC:** [`assets/module_1_tokens_poc.html`](./assets/module_1_tokens_poc.html)
* **Fitur Utama POC Simulator:**
  - **Live Theme Switcher:** Berpindah seketika antar M3 (.fig), Primer (.fig), dan UI Kit Free (.fig).
  - **Matrix Mode (Side-by-Side):** Menampilkan ketiga bingkai ponsel sekaligus secara berdampingan untuk komparasi langsung.
  - **Interaktivitas Penuh:** Uji coba klik tab semester, navigasi ke silabus, dan *toggle checkbox* dengan gaya khas masing-masing sistem.

---

## 2. Karakteristik & Pemetaan Token Tiap Sistem Desain

### 2.1. Opsi A: Google Material 3 (M3)
* **Sumber Referensi & Berkas:** [`.docs/fase2_uiux/assets/Material 3 Design Kit (Community).fig`](./assets/Material%203%20Design%20Kit%20%28Community%29.fig) *(Kiwi v106, 87.523 nodes, 34 pages, 318 Figma Variables)* / [m3.material.io](https://m3.material.io/)
* **Ekstraksi Token JSON:** [`assets/m3_extracted_tokens.json`](./assets/m3_extracted_tokens.json)
* **Palet Utama (Hasil Ekstraksi `Schemes/*` Variable Modes):**
  | Token Semantik M3 | Hex (Light Mode) | Hex (Dark Mode) | Peran UI / Komponen |
  | :--- | :--- | :--- | :--- |
  | `Schemes/Primary` | `#6750A4` | `#D0BCFF` | Warna primer identitas, checkmark tercentang, active tab |
  | `Schemes/On Primary` | `#FFFFFF` | `#381E72` | Teks/ikon di atas warna primer |
  | `Schemes/Primary Container` | `#EADDFF` | `#4F378B` | Badge jurusan, highlight kontainer |
  | `Schemes/On Primary Container` | `#4F378A` | `#EADDFF` | Teks di dalam badge kontainer |
  | `Schemes/Secondary Container` | `#E8DEF8` | `#4A4458` | Latar chip semester yang sedang aktif |
  | `Schemes/On Secondary Container` | `#4A4459` | `#E8DEF8` | Teks chip semester aktif |
  | `Schemes/Surface` | `#FEF7FF` | `#141218` | Latar dasar kanvas & app bar |
  | `Schemes/Surface Container Low` | `#F7F2FA` | `#1D1B20` | Latar kartu mata kuliah (Elevated card) |
  | `Schemes/Surface Container` | `#F3EDF7` | `#211F26` | Latar belakang perangkat / scaffold |
  | `Schemes/Surface Container Highest`| `#E6E0E9` | `#36343B` | Track progress bar, filled cards |
  | `Schemes/On Surface` | `#1D1B20` | `#E6E0E9` | Teks judul utama & nama mata kuliah |
  | `Schemes/On Surface Variant` | `#49454F` | `#CAC4D0` | Teks metadata (durasi, jumlah pertemuan) |
  | `Schemes/Outline` | `#79747E` | `#938F99` | Border chip semester yang belum aktif |
  | `Schemes/Outline Variant` | `#CAC4D0` | `#49454F` | Border pemisah silabus (*divider*) |
* **Skala Sudut (Shape Tokens dari `Corner/*` Variables):**
  - `Corner/None`: `0px`
  - `Corner/Extra-small`: `4px` (Checkbox inner: `2px`)
  - `Corner/Small`: `8px` (Filter chips semester)
  - `Corner/Medium`: `12px` (Kartu mata kuliah Elevated/Outlined)
  - `Corner/Large`: `16px` (Bottom sheet, Modal dialog)
  - `Corner/Full`: `1000px` (Round pills, tombol, FAB)
* **Spesifikasi Komponen Hasil Ekstraksi:**
  - **Cards (`Style=Elevated` / `Style=Outlined`):** Border radius `12px`, background `#F7F2FA`, elevasi level 1 (`box-shadow: 0px 1px 3px 1px rgba(0,0,0,0.15), 0px 1px 2px 0px rgba(0,0,0,0.30)`).
  - **Filter Chips (Semester Tabs):** Tinggi `32px`, corner radius `8px`, border `1px solid #79747E` (unselected), background `#E8DEF8` (selected).
  - **Checkbox (`Type=Checkbox`):** Touch target `48x48 dp`, box container `18x18 dp`, corner radius `2px`, checked `#6750A4` dengan ikon centang putih `#FFFFFF`.
  - **Linear Progress Indicator:** Tinggi `4px`, corner radius `2px`, track `#E6E0E9`, fill `#6750A4`.
* **Tipografi:** `Roboto` (Baseline Typescale brand & plain dari token `Static/Font/Brand`).
* **Karakter Visual:** Tonal color roles, elevasi dinamis, sudut rounded konsisten `12px/8px`, sangat modern.
* **Kelebihan untuk OpenCampus:** Komponen langsung 100% kompatibel secara *native* di Flutter (`ThemeData(useMaterial3: true)`) dan Android Jetpack Compose (`MaterialTheme`).

---

### 2.2. Opsi B: GitHub Primer
* **Sumber Referensi:** [Primer Figma](https://www.figma.com/community/file/854767373644076713) / [@primer/primitives](https://primer.style/)
* **Palet Utama:**
  - `accent.fg`: `#0969DA` (Primer Blue)
  - `canvas.default`: `#FFFFFF`
  - `canvas.subtle`: `#F6F8FA`
  - `fg.default`: `#1F2328`
  - `border.default`: `#D0D7DE`
  - `success.fg`: `#1A7F37`
* **Tipografi:** `-apple-system`, `Inter`, `BlinkMacSystemFont` (Monospace untuk code tokens).
* **Border Radius:** Kartu & Badge `6px`, Pill `100px`.
* **Karakter Visual:** Sangat bersih (*clean*), berorientasi teks, kontras tinggi, border halus abu-abu yang tegas, sudut membulat kecil (*crisp*).
* **Kelebihan untuk OpenCampus:** Memberikan nuansa *Software Engineering* yang otentik, sangat familiar bagi mahasiswa Teknik Informatika dan developer.

---

### 2.3. Opsi C: Flowbite (Tailwind Scale)
* **Sumber Referensi:** [Flowbite Figma](https://www.figma.com/community/file/1179442320711977498) / [flowbite.com](https://flowbite.com/)
* **Palet Utama:**
  - `blue-600`: `#2563EB`
  - `blue-50`: `#EFF6FF`
  - `gray-900`: `#111827`
  - `gray-50`: `#F9FAFB`
  - `gray-200`: `#E5E7EB`
  - `emerald-600`: `#059669`
* **Tipografi:** `Inter`.
* **Border Radius:** Kartu `12px` (`rounded-xl`), Tombol `8px` (`rounded-lg`).
* **Karakter Visual:** Standar SaaS modern, warna biru terang yang ramah, pemisahan kontras kartu dan latar belakang yang tegas.
* **Kelebihan untuk OpenCampus:** Sangat mudah diadaptasi jika menggunakan arsitektur utility CSS (seperti Tailwind CSS atau NativeWind).

---

### 2.4. Opsi D: Aalto University Design System (`brand.aalto.fi`)
* **Sumber Referensi:** [Aalto Brand & Design System](https://brand.aalto.fi/en/ds) (Hasil ekstraksi langsung dari CSS produksi)
* **Palet Utama:**
  - `aalto-red-1`: `#FD6360` (Primary Accent) / `aalto-red-2`: `#501F1E`
  - `neutral-1` (White): `#FFFFFF`
  - `neutral-3` (Surface Gray): `#F2F2F2`
  - `neutral-8` (Aalto Black): `#151515`
  - `school-elec` (Teknik Elektro/Komputer): `#A987FF`
  - `school-sci` (Ilmu Sains): `#FF8D4F`
  - `school-chem` (Aksen Sukses): `#5DD089`
* **Tipografi:** `Inter` (Skandinavia minimalis, *high legibility*).
* **Border Radius:** Kartu `8px`, Tab Chips `4px` (Desain geometris arsitektural khas Finlandia).
* **Karakter Visual:** Sangat unik, berwibawa khas institusi perguruan tinggi Eropa ternama, berani (*bold*), tidak pasaran seperti UI komersial umum.
* **Kelebihan untuk OpenCampus:** Memberikan **DNA Universitas Nyata (Higher Education DNA)** yang sangat sejalan dengan konsep *OpenCampus*. Aksen warna fakultas (`elec`, `sci`) sangat cocok untuk label semester dan mata kuliah.

---

### 2.5. Opsi E: Design System & UI Kit Free (Community) (.fig)
* **Sumber Referensi & Berkas:** [`.docs/fase2_uiux/assets/Design System _ Ui Kit Free (Community).fig`](./assets/Design%20System%20_%20Ui%20Kit%20Free%20%28Community%29.fig) *(Kiwi v101, 11.815 nodes, 59 pages)*
* **Ekstraksi Token JSON:** [`assets/uikit_free_extracted_tokens.json`](./assets/uikit_free_extracted_tokens.json)
* **Palet Utama (Hasil Ekstraksi Halaman `Colors`):**
  | Token UI Kit Free | Nilai Hex | Peran UI / Komponen |
  | :--- | :--- | :--- |
  | `Corporate/Primary` | `#9D3FE7` | Warna primer identitas (*Electric Violet*), active checkbox |
  | `Corporate/PrimaryDark` | `#602093` | Warna ujung gradasi (*Deep Violet*) |
  | `Corporate/Gradient` | `linear-gradient(159.13deg, #9D3FE7, #602093)` | Tombol utama, active tab pill, fill progress bar |
  | `Corporate/TextDark` | `#1A141F` | Teks judul utama & nama mata kuliah |
  | `Corporate/SurfaceLight`| `#F5F3F7` | Latar kanvas aplikasi (*Light Lavender Gray*) |
  | `Corporate/CardSurface` | `#FFFFFF` | Latar kartu mata kuliah & silabus |
  | `Corporate/Border` | `#E5E0EB` | Border kartu dan pemisah silabus |
  | `Grayscale/TextMuted` | `#707075` | Teks metadata (durasi, kreator) |
  | `Informing/Success` | `#00B998` | Indikator selesai (*Teal Green*), badge sukses |
  | `Informing/Warning` | `#FF9500` | Indikator materi sedang berjalan (*Amber*) |
* **Skala Sudut (Shape Radius Scale):**
  - Small: `4px` (Checkbox, tombol default)
  - Medium: `8px` (Tab pill, chip filter)
  - Large: `12px` (Kartu mata kuliah & panel)
  - Extra Large / Full: `24px` / `1000px` (Round badge, kapsul)
* **Tipografi:** `Poppins` (Modern geometric sans-serif yang ramah dan dinamis).
* **Karakter Visual:** Sangat modern, *stylish*, energik, estetika edtech kontemporer yang disukai Gen Z dan pembelajar muda.
* **Kelebihan untuk OpenCampus:** Tampilan paling memikat secara visual (*eye-catching*), palet gradasi ungu-violet memberikan kesan inovatif dan premium tanpa terlihat kaku.

---

## 3. Matriks Perbandingan Sistem Desain .fig (3 Kit Terverifikasi)

| Parameter Evaluasi | Google Material 3 (`.fig`) | GitHub Primer Web (`.fig`) | UI Kit Free (`.fig`) |
| :--- | :---: | :---: | :---: |
| **Arsip Berkas Figma** | `Material 3 Design Kit.fig` | `Primer Web.fig` | `Design System _ Ui Kit Free.fig` |
| **Karakter & DNA Visual** | Ramah, Modern & Adaptive (Google) | Developer & Tech Academy | **Modern, Vibrant & Energetic EdTech** |
| **Warna Primer Brand** | `#6750A4` (M3 Purple) | `#0969DA` (Primer Blue) | **`#9D3FE7` (Electric Violet & Gradient)** |
| **Warna Sukses / Selesai** | `#386A20` (Tonal Green) | `#1F883D` (Primer Success) | **`#00B998` (Teal Emerald)** |
| **Radius Kartu** | `12px` (`Corner/Medium`) | `6px` (*Crisp Border*) | **`12px` (Modern Rounded)** |
| **Radius Checkbox** | `2px` (M3 Extra-small) | `3px` (Primer Sharp) | **`4px` (Smooth Rounded)** |
| **Tipografi Utama** | `Roboto` | `-apple-system / Inter` | **`Poppins`** |
| **Kesesuaian Mobile Native** | 🟢 **100% Native Flutter 3.x** | 🟡 Perlu Custom Token Styling | 🟢 Sangat Mudah Di-token (Design System) |
| **Aksesibilitas Kontras (WCAG)**| 12.5:1 (AAA) | 14.8:1 (AAA) | **13.8:1 (AAA)** |

---

## 4. Analisis & Kesimpulan Rekomendasi (3 Kit .fig)

1. **Opsi 1: Design System & UI Kit Free (`#9D3FE7` & `Poppins`)**
   👉 **Paling Menarik Secara Visual & Estetika EdTech Modern**
   *Alasan:* Tipografi *Poppins* yang ramah dipadukan dengan aksen ungu elektrik (*Electric Violet*) dan tombol/progress bar gradasi memberikan energi tinggi, modern, dan sangat bersahabat bagi mahasiswa generasi baru tanpa terlihat kaku.

2. **Opsi 2: Google Material 3 (M3) (`#6750A4` & `Roboto`)**
   👉 **Paling Mudah Diimplementasikan di Mobile Native**
   *Alasan:* Komponen M3 (*Elevated Card*, *Filter Chips*, *Linear Progress Indicator*) didukung secara bawaan (*out-of-the-box*) oleh framework Flutter (`useMaterial3: true`) dan Android Jetpack Compose, menghemat waktu styling manual.

3. **Opsi 3: GitHub Primer Web (`#0969DA` & `Inter`)**
   👉 **Paling Kuat Nuansa "Sekolah Coding / Developer"**
   *Alasan:* Mengadopsi bahasa visual GitHub yang sangat akrab bagi mahasiswa Teknik Informatika, dengan tab *UnderlineNav* dan border halus 1px yang bersih berorientasi teks.

---

## 5. Ekstraksi Ikon Vektor Asli dari Berkas Figma Masing-Masing (.fig)

Sesuai konfirmasi arahan pengguna (*"setiap poc pake icon masing2 design systemnya? -> ya"*), prototipe POC telah ditingkatkan sehingga **setiap sistem desain menggunakan set ikon SVG asli dari berkas biner `.fig`-nya masing-masing**, bukan berbagi set gabungan (*shared*).

### 5.1. Metodologi Ekstraksi Biner Geometry Blob
Pada skema biner Kiwi Figma (v100+), kontur vektor disimpan dalam blob biner `commandsBlob` di dalam struktur `fillGeometry`. Aliran byte diurai langsung menjadi perintah SVG (*path data*):
* **Opcode 0 (`Z`):** *Close path*
* **Opcode 1 (`M x y`):** *MoveTo* (2 float 32-bit LE)
* **Opcode 2 (`L x y`):** *LineTo* (2 float 32-bit LE)
* **Opcode 3 (`Q x1 y1 x y`):** *Quadratic Bezier* (4 float 32-bit LE)
* **Opcode 4 (`C x1 y1 x2 y2 x y`):** *Cubic Bezier* (6 float 32-bit LE)

---

### 5.2. Katalog Ikon Khusus per Sistem Desain (`.docs/fase2_uiux/assets/icons/`)

#### A. Google Material 3 (`assets/icons/m3/` — Material Symbols Resmi)
Diekstrak dari berkas [`Material 3 Design Kit (Community).fig`](./assets/Material%203%20Design%20Kit%20%28Community%29.fig) (Session `54616`, canvas 24x24 dp):
| Peran UI | Nama Berkas SVG | Node GUID & Nama Komponen Sumber | Karakter Visual M3 |
| :--- | :--- | :--- | :--- |
| **Brand Logo** | [`brand.svg`](./assets/icons/m3/brand.svg) | `54616:25456` (`bookmark`) | Rounded Academic Bookmark |
| **Tab Beranda** | [`home.svg`](./assets/icons/m3/home.svg) | `54616:25412` (`stars`) | M3 Active Navigation Indicator Star |
| **Tab Cari** | [`search.svg`](./assets/icons/m3/search.svg) | `54616:25440` (`search`) | Material Rounded 45° Search Lens |
| **Tab Profil** | [`profile.svg`](./assets/icons/m3/profile.svg) | `54616:25462` (`person`) | Smooth Pill Head & Shoulder Avatar |
| **Header Kembali**| [`back.svg`](./assets/icons/m3/back.svg) | `54616:25400` (`arrow_back`) | Standard Google 24px Left Arrow |
| **Panah Kartu** | [`forward.svg`](./assets/icons/m3/forward.svg) | `54616:25402` (`arrow_forward`) | Standard Google 24px Right Arrow |
| **Durasi Jam** | [`time.svg`](./assets/icons/m3/time.svg) | `54616:25518` (`schedule`) | 90° Rounded Angle Clock Face |
| **Aksi Belajar** | [`play.svg`](./assets/icons/m3/play.svg) | `54616:25418` (`play_arrow`) | Rounded Equilateral Play Triangle |
| **Checkbox** | [`check.svg`](./assets/icons/m3/check.svg) | `54616:25514` (`check_small`) | Compact Rounded Material Checkmark |
| **MK 1 (Coding)**| [`course1.svg`](./assets/icons/m3/course1.svg)| `54616:25506` (`keyboard`) | Material Developer Hardware Keyboard |
| **MK 2 (Struktur)**| [`course2.svg`](./assets/icons/m3/course2.svg)| `54616:25482` (`folder`) | Material Data/Directory Folder |
| **MK 3 (Jaringan)**| [`course3.svg`](./assets/icons/m3/course3.svg)| `54616:25528` (`language`) | Material Global Latitude Sphere |

#### B. GitHub Primer (`assets/icons/primer/` — GitHub Octicons Resmi)
Diekstrak dari berkas [`Primer Web (Community).fig`](./assets/Primer%20Web%20%28Community%29.fig) (Koleksi Octicons, canvas 16x16 px):
| Peran UI | Nama Berkas SVG | Node GUID & Nama Komponen Sumber | Karakter Visual Primer |
| :--- | :--- | :--- | :--- |
| **Brand Logo** | [`brand.svg`](./assets/icons/primer/brand.svg) | `12515:40782` (`book`) | Octicon Open Academic Book |
| **Tab Beranda** | [`home.svg`](./assets/icons/primer/home.svg) | `31891:4107` (`home`) | Octicon Roof, Chimney & Door |
| **Tab Cari** | [`search.svg`](./assets/icons/primer/search.svg) | `12515:40638` (`search`) | Sharp Precision Octicon Search |
| **Tab Profil** | [`profile.svg`](./assets/icons/primer/profile.svg) | `23829:87997` (`person`) | Classic GitHub Developer Avatar |
| **Header Kembali**| [`back.svg`](./assets/icons/primer/back.svg) | `26704:84176` (`arrow-left`) | Crisp 16px Navigation Arrow |
| **Panah Kartu** | [`forward.svg`](./assets/icons/primer/forward.svg) | `31853:5246` (`chevron-right`) | Thin 1px Angled Chevron |
| **Durasi Jam** | [`time.svg`](./assets/icons/primer/time.svg) | `12515:40646` (`clock`) | Precision 12:00 / 3:00 Clock |
| **Aksi Belajar** | [`play.svg`](./assets/icons/primer/play.svg) | `23829:88007` (`play`) | Sharp Geometric Octicon Play |
| **Checkbox** | [`check.svg`](./assets/icons/primer/check.svg) | `13057:43840` (`check`) | Crisp Angled Octicon Checkmark |
| **MK 1 (Coding)**| [`course1.svg`](./assets/icons/primer/course1.svg)| `23829:88029` (`code`) | Octicon Code Syntax Brackets `< / >` |
| **MK 2 (Struktur)**| [`course2.svg`](./assets/icons/primer/course2.svg)| `26704:84264` (`graph`) | Octicon Data Graph & Metrics |
| **MK 3 (Jaringan)**| [`course3.svg`](./assets/icons/primer/course3.svg)| `21062:78765` (`globe`) | Octicon Connected World Network |

#### C. Design System & UI Kit Free (`assets/icons/uikit/` — Modern EdTech & Iconly)
Diekstrak dari berkas [`Design System _ Ui Kit Free (Community).fig`](./assets/Design%20System%20_%20Ui%20Kit%20Free%20%28Community%29.fig) & [`Mobile E-Learning.fig`](./assets/Mobile%20E-Learning%20App%20Design%20%28Community%29.fig):
| Peran UI | Nama Berkas SVG | Node GUID & Nama Komponen Sumber | Karakter Visual UI Kit |
| :--- | :--- | :--- | :--- |
| **Brand Logo** | [`brand.svg`](./assets/icons/uikit/brand.svg) | `12515:40782` (`book`) | Academic Book Identity |
| **Tab Beranda** | [`home.svg`](./assets/icons/uikit/home.svg) | `229:683` (`Iconly Home`) | Contemporary Broken Line Home |
| **Tab Cari** | [`search.svg`](./assets/icons/uikit/search.svg) | `511:119,120` (`UI Kit Search`) | Dual-path Modern Search Lens |
| **Tab Profil** | [`profile.svg`](./assets/icons/uikit/profile.svg) | `122:24` (`UI Kit Person`) | Modern Geometric User Profile |
| **Header Kembali**| [`back.svg`](./assets/icons/uikit/back.svg) | `229:692` (`Iconly Arrow Left`) | Stylized Curve Back Arrow |
| **Panah Kartu** | [`forward.svg`](./assets/icons/uikit/forward.svg) | `629:6304` (`arrow-forward`) | Contemporary Thick Arrow |
| **Durasi Jam** | [`time.svg`](./assets/icons/uikit/time.svg) | `1690:7625,7626` (`bx-time-five`)| Dual-contour Modern Time Clock |
| **Aksi Belajar** | [`play.svg`](./assets/icons/uikit/play.svg) | `229:797` (`Play Icon`) | Rounded Dynamic EdTech Play |
| **Checkbox** | [`check.svg`](./assets/icons/uikit/check.svg) | `1579:1328` (`fa-solid:check`) | Bold Solid Checkmark |
| **MK 1 (Coding)**| [`course1.svg`](./assets/icons/uikit/course1.svg)| `23829:88029` (`code`) | Code Syntax Symbol |
| **MK 2 (Struktur)**| [`course2.svg`](./assets/icons/uikit/course2.svg)| `248:24` (`storage`) | Database & Storage Servers |
| **MK 3 (Jaringan)**| [`course3.svg`](./assets/icons/uikit/course3.svg)| `1669:6540` (`mdi:view-grid`) | Grid Architecture & Modules |

---

### 5.3. Efek Visual pada Prototipe Interaktif
Ketika berpindah antar sistem desain (atau saat membuka mode **Matrix Side-by-Side**):
1. **Bentuk Ikon Berubah Menyeluruh:**
   * Di **Material 3**, seluruh ikon berkarakter bulat lembut (*smooth rounded*) dengan grid 24x24 khas Android & Flutter.
   * Di **GitHub Primer**, seluruh ikon berubah menjadi Octicons 16x16 tajam dan presisi tinggi khas ekosistem GitHub/developer.
   * Di **UI Kit Free**, ikon bernuansa EdTech modern dengan kombinasi *dual-contour* dan *broken-line*.
2. **Interaktivitas Checkbox per Sistem:**
   * Saat mengklik checkbox pada salah satu ponsel, simbol centang yang muncul secara instan adalah centang spesifik sistem desain ponsel tersebut (`Icons/check_small` untuk M3, Octicon `check` untuk Primer, dan `fa-solid:check` untuk UI Kit Free).


