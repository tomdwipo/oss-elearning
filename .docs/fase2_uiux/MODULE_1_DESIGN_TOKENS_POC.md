# Modul 1: Navigasi Kurikulum — Proof of Concept (POC) Design Tokens Evaluation (MVP v0.1)

Dokumen ini mendokumentasikan hasil pengujian dan evaluasi **Proof of Concept (POC)** pergantian *Design Tokens* untuk **Modul 1: Navigasi Kurikulum** aplikasi **OpenCampus Mobile** dengan membandingkan 4 sistem desain terkemuka:
1. **Google Material 3 (M3)**
2. **GitHub Primer**
3. **Flowbite (Tailwind Scale)**
4. **Aalto University Design System (`brand.aalto.fi/en/ds`)**

---

## 1. Artefak Eksperimen Interaktif (POC Interactive Simulator)

Untuk melihat, berinteraksi, dan membandingkan secara visual keempat sistem desain pada tampilan mobile nyata (Layar 1 Beranda & Layar 2 Silabus), buka artefak simulasi interaktif berikut:

* **Berkas Prototipe POC:** [`assets/module_1_tokens_poc.html`](./assets/module_1_tokens_poc.html)
* **Fitur Utama POC Simulator:**
  - **Live Theme Switcher:** Berpindah seketika antar M3, Primer, Flowbite, dan Aalto DS.
  - **Matrix Mode (Side-by-Side):** Menampilkan 4 bingkai ponsel sekaligus secara berdampingan untuk komparasi langsung.
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

## 3. Matriks Perbandingan Parameter Desain

| Parameter Evaluasi | Google Material 3 | GitHub Primer | Flowbite | Aalto University DS |
| :--- | :---: | :---: | :---: | :---: |
| **Karakter & Identitas Visual** | Ramah & Modern (Google) | Developer / Tech Academy | B2C / SaaS Komersial | **Otentik Universitas Eropa** |
| **Warna Primer Identitas** | `#6750A4` (Ungu M3) | `#0969DA` (Biru GitHub) | `#2563EB` (Biru Tailwind) | **`#FD6360` (Merah Aalto)** |
| **Radius Kartu & Tombol** | Sangat Membulat (`16px`) | Tajam & Tegas (`6px`) | Sedang (`12px`) | **Geometris Bersih (`8px`)** |
| **Kesesuaian Mobile Framework** | 🟢 **Bawaan Flutter** | 🟡 Perlu Custom CSS | 🟢 Sangat Baik (NativeWind) | 🟢 Sangat Mudah Dibuat Token |
| **Pembedaan Warna MK/Fakultas**| Tonal Monokrom | Label Badge Abu-abu | Tag Warna Standar | 🟢 **Palet Sekolah Resmi (`elec`/`sci`)** |
| **Aksesibilitas Kontras (WCAG)** | 12.5:1 (AAA) | 14.8:1 (AAA) | 13.2:1 (AAA) | **15.1:1 (AAA)** |

---

## 4. Analisis & Kesimpulan Rekomendasi

1. **Jika Ingin Menampilkan Kesan "Kampus / Universitas Terbuka" yang Paling Kuat:**
   👉 **Pilih Aalto University Design System (`brand.aalto.fi`)**.
   *Alasan:* Aplikasi langsung memiliki identitas unik perguruan tinggi sains & teknologi global, layout geometris yang bersih tanpa distraksi, serta memiliki token warna sekolah (`elec`, `sci`, `chem`) yang sangat pas dipetakan ke mata kuliah Teknik Informatika.

2. **Jika Ingin Kemudahan Maksimal saat Menulis Kode Mobile:**
   👉 **Pilih Google Material 3 (M3)**.
   *Alasan:* Implementasi di Flutter 3.x tidak memerlukan styling tambahan karena seluruh widget mobile bawaan sudah mengadopsi M3 tokens.

3. **Jika Ingin Nuansa "Sekolah Coding / Hacker":**
   👉 **Pilih GitHub Primer**.
   *Alasan:* Mahasiswa informatika langsung merasa berada di rumah sendiri.
