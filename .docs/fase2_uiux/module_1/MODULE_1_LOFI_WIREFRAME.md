# Modul 1: Navigasi Kurikulum — Wireframe (Lo-Fi) (MVP v0.1)

Dokumen ini mendefinisikan rancangan **Wireframe Tingkat Rendah (Low-Fidelity / Lo-Fi)** untuk **Modul 1: Navigasi Kurikulum** (UC-01, UC-02, UC-03) pada aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../../fase0/PRD.md), [USE_CASE.md](../../fase1/USE_CASE.md), dan [MODULE_1_USER_FLOW_WIREFLOW.md](./MODULE_1_USER_FLOW_WIREFLOW.md).

---

## 1. Layar 1: Beranda Semester (Home / Semester Screen)

### **Visual Lo-Fi Wireframe (ASCII):**

```text
+-------------------------------------------------------------+
| 09:41                                              [📶 🔋]  |  <-- Status Bar
+-------------------------------------------------------------+
| [LOGO] OpenCampus                     [ Jurusan: IF ]       |  <-- App Bar / Header
|                                                             |
| +---------------------------------------------------------+ |
| | Progres Semester 1                                      | |  <-- Progress Summary Card
| | [==================-------------------------] 45%       | |      (FR-3.3, UC-09)
| | 7 dari 16 Topik Selesai                                 | |
| +---------------------------------------------------------+ |
|                                                             |
| PILIH SEMESTER:                                             |  <-- Horizontal Scroll Tab Bar
| +-------+  +-------+  +-------+  +-------+  +-------+       |      (FR-1.1, UC-01)
| | Sem 1*|  | Sem 2 |  | Sem 3 |  | Sem 4 |  | Sem 5 |  ...> |
| +-------+  +-------+  +-------+  +-------+  +-------+       |
|                                                             |
| DAFTAR MATA KULIAH (Semester 1)                             |  <-- Section Title
|                                                             |
| +---------------------------------------------------------+ |  <-- Course Card Item 1
| | [ICON] Algoritma & Pemrograman Dasar                    | |      (FR-1.2, UC-02)
| |        16 Pertemuan • Durasi Est: ~6 Jam                | |
| |        Progres: 4 / 16 Selesai (25%)                    | |
| |        [========----------------------------]           | |
| |                                                     [>] | |
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |  <-- Course Card Item 2
| | [ICON] Matematika Diskrit                               | |
| |        16 Pertemuan • Durasi Est: ~5.5 Jam              | |
| |        Progres: 0 / 16 Selesai (0%)                     | |
| |        [------------------------------------]           | |
| |                                                     [>] | |
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |  <-- Course Card Item 3
| | [ICON] Pengantar Teknologi Informasi                    | |
| |        16 Pertemuan • Durasi Est: ~4 Jam                | |
| |        Progres: 3 / 16 Selesai (18%)                    | |
| |        [======------------------------------]           | |
| |                                                     [>] | |
| +---------------------------------------------------------+ |
|                                                             |
+-------------------------------------------------------------+
```

### **Spesifikasi Elemen UI Layar 1:**
1. **Header App Bar:**
   - Nama aplikasi: `OpenCampus`.
   - Badge Program Studi: `Teknik Informatika (IF)`.
2. **Kartu Ringkasan Progres Semester (UC-09):**
   - *Progress bar* visual dengan persentase ketercapaian.
   - Keterangan teks: `X dari Y Topik Selesai`.
3. **Tab Pemilih Semester (UC-01):**
   - Tab tombol Semester 1 sampai 8 (dapat di-scroll secara horizontal jika resolusi layar sempit).
   - Status aktif ditandai secara visual (highlight / asterisk `*`).
4. **Daftar Kartu Mata Kuliah (UC-02):**
   - Icon kategori / mata kuliah.
   - Judul mata kuliah.
   - Total topik pertemuan & estimasi durasi total.
   - Mini progress bar dan persentase penyelesaian spesifik per mata kuliah.
   - Indikator aksi klik `[>]` menuju Layar Silabus.

---

## 2. Layar 2: Silabus Mata Kuliah (Course Syllabus Screen)

### **Visual Lo-Fi Wireframe (ASCII):**

```text
+-------------------------------------------------------------+
| 09:41                                              [📶 🔋]  |  <-- Status Bar
+-------------------------------------------------------------+
| [< Back]  Algoritma & Pemrograman Dasar                     |  <-- Top App Bar
+-------------------------------------------------------------+
|                                                             |
| +---------------------------------------------------------+ |
| | Ringkasan Mata Kuliah                                   | |  <-- Course Summary Header
| | 16 Topik Pertemuan • Estimasi Durasi: 6 Jam 15 Menit    | |
| | Progres: 4 dari 16 Selesai (25%)                        | |
| | [========-------------------------------------------]   | |
| +---------------------------------------------------------+ |
|                                                             |
| SILABUS PEMBELAJARAN:                                       |  <-- Section Title
|                                                             |
| +---------------------------------------------------------+ |  <-- Topic Item 1 (Selesai)
| | [X] 01. Konsep Dasar Algoritma & Flowchart              | |      (FR-1.3, UC-03, UC-08)
| |     ⏱ 18 Menit • 👤 Web Programming UNPAS           [>] | |
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |  <-- Topic Item 2 (Selesai)
| | [X] 02. Variabel, Tipe Data & Operator                  | |
| |     ⏱ 25 Menit • 👤 Web Programming UNPAS           [>] | |
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |  <-- Topic Item 3 (Belum)
| | [ ] 03. Struktur Kendali: Percabangan (If-Else)         | |
| |     ⏱ 30 Menit • 👤 Kelas Terbuka                   [>] | |
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |  <-- Topic Item 4 (Belum)
| | [ ] 04. Struktur Kendali: Perulangan (For, While)       | |
| |     ⏱ 22 Menit • 👤 Kelas Terbuka                   [>] | |
| +---------------------------------------------------------+ |
|                                                             |
| +---------------------------------------------------------+ |  <-- Topic Item 5 (Belum)
| | [ ] 05. Fungsi & Prosedur (Modular Programming)         | |
| |     ⏱ 28 Menit • 👤 Kelas Terbuka                   [>] | |
| +---------------------------------------------------------+ |
|                             ...                             |
| +---------------------------------------------------------+ |  <-- Topic Item 16 (Belum)
| | [ ] 16. Proyek Akhir: Mini Aplikasi CLI                 | |
| |     ⏱ 45 Menit • 👤 Programmer Zaman Now            [>] | |
| +---------------------------------------------------------+ |
|                                                             |
+-------------------------------------------------------------+
```

### **Spesifikasi Elemen UI Layar 2:**
1. **Top App Bar:**
   - Tombol kembali `[< Back]` ke Layar Beranda Semester.
   - Judul mata kuliah yang sedang dibuka.
2. **Course Summary Header Card:**
   - Total pertemuan (16 Pertemuan).
   - Estimasi total waktu tontonan materi.
   - Progress bar penyelesaian topik dalam mata kuliah ini.
3. **Daftar Silabus Topik Pertemuan 1 - 16 (UC-03):**
   - **Checkbox Interaktif `[ ]` / `[X]` (UC-08):** Mengizinkan penandaan status selesai secara instan langsung dari daftar silabus tanpa wajib memutar video.
   - **Nomor & Judul Topik:** Menjelaskan secara ringkas materi pertemuan.
   - **Metadata Pertemuan:**
     - Ikon durasi `⏱` estimasi video YouTube.
     - Ikon kreator `👤` nama kanal YouTube sumber materi (atribusi awal).
   - **Tap Area `[>]`:** Menekan baris topik akan membuka Layar Pemutar Video Modul 2.
