# Modul 1: Navigasi Kurikulum — User Flow & Wireflow (MVP v0.1)

Dokumen ini mendefinisikan rancangan **User Flow** dan **Wireflow Sederhana** untuk **Modul 1: Navigasi Kurikulum** (UC-01, UC-02, UC-03) pada aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../fase0/PRD.md), [USE_CASE.md](../fase1/USE_CASE.md), dan [ACTIVITY_DIAGRAM.md](../fase1/ACTIVITY_DIAGRAM.md).

---

## 1. User Flow (Alur Pengguna)

```text
  ( Start: Buka Aplikasi )
              |
              v
  [ Layar 1: Beranda Semester ] <---------------------------------------+
              |                                                         |
              +---> [ Pilih Tab Semester (1-8) ]                        |
              |            |                                            |
              |            v                                            |
              |     [ Muat & Render Mata Kuliah Sesuai Semester ]       |
              |            |                                            |
              |            +--------------------------------------------+
              |
              v (Pilih/Tap Kartu Mata Kuliah)
  [ Layar 2: Silabus Mata Kuliah ]
              |
              +---> [ Tap Tombol Kembali (< Back) ] -------------------> [ Layar 1 ]
              |
              v (Pilih/Tap Topik Pertemuan 1-16)
  ( End: Masuk ke Modul 2 - Layar Video Pembelajaran )
```

---

## 2. Wireflow (Navigasi Antar Layar)

```text
========================================================================================
[ LAYAR 1: BERANDA SEMESTER ]               [ LAYAR 2: SILABUS MATA KULIAH ]
========================================================================================

+-----------------------------------+       +-----------------------------------+
| OpenCampus            Teknik Info |       | [<] Algoritma & Pemrograman       |
+-----------------------------------+       +-----------------------------------+
| Progress Smt 1: [====----] 45%    |       | Progres: 4 / 16 Selesai (25%)     |
| 7 dari 16 Topik Selesai           |       +-----------------------------------+
+-----------------------------------+       | DAFTAR TOPIK (Pertemuan 1 - 16):  |
| [Sem 1*] [Sem 2] [Sem 3] [Sem 4]  |       |                                   |
| [Sem 5]  [Sem 6] [Sem 7] [Sem 8]  |       | [X] 01. Pengantar Algoritma       |
+-----------------------------------+       |     - 18 Menit • Web Programming  |
| DAFTAR MATA KULIAH (Sem 1):       |       |                                   |
|                                   |       | [X] 02. Variabel & Tipe Data      |
| +-------------------------------+ |       |     - 25 Menit • Web Programming  |
| | Algoritma & Pemrograman       | | ===>  |                                   |
| | 16 Pertemuan • Progres: 25%   | | (Tap) | [ ] 03. Percabangan (If-Else)     |
| +-------------------------------+ |       |     - 30 Menit • Kelas Terbuka    |
|                                   |       |                                   |
| +-------------------------------+ |       | [ ] 04. Perulangan (Loops)        |
| | Matematika Diskrit            | |       |     - 22 Menit • Kelas Terbuka    |
| | 16 Pertemuan • Progres: 0%    | |       +-----------------------------------+
| +-------------------------------+ |                         |
+-----------------------------------+                         v (Tap Topik)
                                                ( Ke Layar Video Player Modul 2 )
```

---

## 3. Rincian Komponen & Interaksi Layar

### **Layar 1 — Beranda Semester ([UC-01](../fase1/USE_CASE.md), [UC-09](../fase1/USE_CASE.md))**
* **Tujuan:** Memberikan ringkasan progres belajar semester aktif dan pintu masuk navigasi kurikulum per semester.
* **Elemen UI:**
  * Header judul aplikasi & nama jurusan percontohan (*Teknik Informatika*).
  * *Semester Progress Bar*: menampilkan total topik yang sudah diselesaikan vs total topik semester aktif.
  * *Semester Tabs (1–8)*: berpindah semester dengan satu sentuhan.
  * *Course Cards*: daftar kartu mata kuliah pada semester yang dipilih (memuat judul, total pertemuan, dan progres penyelesaian mata kuliah).

### **Layar 2 — Silabus Mata Kuliah ([UC-02](../fase1/USE_CASE.md), [UC-03](../fase1/USE_CASE.md))**
* **Tujuan:** Menampilkan daftar urutan 16 topik pertemuan terstruktur dari mata kuliah yang dipilih.
* **Elemen UI:**
  * Tombol navigasi kembali (`< Back`).
  * Header nama mata kuliah dan ringkasan persentase penyelesaian mata kuliah.
  * List topik pertemuan (1 s/d 16) yang mencakup:
    * Judul materi pertemuan.
    * Durasi estimasi video.
    * Nama channel kreator pembuat video.
