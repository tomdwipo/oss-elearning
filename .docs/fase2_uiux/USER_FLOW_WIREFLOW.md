# User Flow & Wireflow (Simple) — OpenCampus Mobile (MVP v0.1)

Dokumen ini mendefinisikan rancangan **User Flow** dan **Wireflow Sederhana** untuk **Fase 2: Desain Antarmuka (UI/UX)** pada aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../fase0/PRD.md), [USE_CASE.md](../fase1/USE_CASE.md), dan [ACTIVITY_DIAGRAM.md](../fase1/ACTIVITY_DIAGRAM.md).

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
              |     [ Muat Mata Kuliah Sesuai Semester ]                |
              |            |                                            |
              |            +--------------------------------------------+
              |
              v (Pilih/Tap Kartu Mata Kuliah)
  [ Layar 2: Silabus Mata Kuliah ] <----------------------------+
              |                                                 |
              +---> [ Tap Checkbox Selesai (Manual) ]           |
              |            |                                    |
              |            v                                    |
              |     [ Simpan Status ke Penyimpanan Lokal ]      |
              |            |                                    |
              |            +------------------------------------+
              |
              | (Kembali ke Beranda)
              +---------------------------------------------------------> [ Layar 1 ]
              |
              v (Pilih/Tap Topik Pertemuan)
  [ Layar 3: Video Player Pembelajaran ]
              |
              +---> [ Putar Video >= 85% ] ---> [ Otomatis Centang Selesai ]
              |
              +---> [ Tap "Laporkan Link" ] ---> [ Modal: Laporan Link Rusak ]
              |                                          |
              |                                  (Kirim Laporan)
              |                                          |
              | <----------------------------------------+
              |
              v (Kembali ke Silabus)
  [ Layar 2: Silabus Mata Kuliah ]
```

---

## 2. Wireflow (Representasi Layar & Alur Transisi)

```text
========================================================================================================================
[ LAYAR 1: BERANDA SEMESTER ]               [ LAYAR 2: SILABUS MATA KULIAH ]            [ LAYAR 3: VIDEO LEARNING ]
========================================================================================================================

+-----------------------------------+       +-----------------------------------+       +-----------------------------------+
| OpenCampus            Teknik Info |       | [<] Algoritma & Pemrograman       |       | [<] Pertemuan 01                  |
+-----------------------------------+       +-----------------------------------+       +-----------------------------------+
| Progress Smt 1: [====----] 45%    |       | Progres: 4 / 16 Selesai (25%)     |       | +-------------------------------+ |
| 7 dari 16 Topik Selesai           |       +-----------------------------------+       | |        [ > PLAY VIDEO ]       | |
+-----------------------------------+       | DAFTAR TOPIK:                     |       | |  (YouTube Embedded Player)    | |
| [Sem 1*] [Sem 2] [Sem 3] [Sem 4]  |       |                                   |       | +-------------------------------+ |
| [Sem 5]  [Sem 6] [Sem 7] [Sem 8]  |       | [X] 01. Pengantar Algoritma       |       | Kontrol: [0.75x] [1x*] [1.5x] [2x]|
+-----------------------------------+       |     - 18 Menit • Web Programming  |       +-----------------------------------+
| DAFTAR MATA KULIAH (Sem 1):       |       |                                   |       | Pengantar Logika & Algoritma      |
|                                   |       | [X] 02. Variabel & Tipe Data      |       | Channel: Web Programming UNPAS    |
| +-------------------------------+ |       |     - 25 Menit • Web Programming  |       | Durasi: 18 Menit                  |
| | Algoritma & Pemrograman       | | ===>  |                                   | ===>  +-----------------------------------+
| | 16 Pertemuan • Progres: 25%   | | (Tap) | [ ] 03. Percabangan (If-Else)     | (Tap) | [X] Tandai Selesai                |
| +-------------------------------+ |       |     - 30 Menit • Kelas Terbuka    |       +-----------------------------------+
|                                   |       |                                   |       | [↗] Tonton di YouTube (Atribusi)  |
| +-------------------------------+ |       | [ ] 04. Perulangan (Loops)        |       | [!] Laporkan Link Rusak           |
| | Matematika Diskrit            | |       |     - 22 Menit • Kelas Terbuka    |       +-----------------------------------+
| | 16 Pertemuan • Progres: 0%    | |       +-----------------------------------+       | Deskripsi singkat materi...       |
| +-------------------------------+ |                                                   +-----------------------------------+
+-----------------------------------+                                                                     |
                                                                                                          v (Jika link error)
                                                                                        +-----------------------------------+
                                                                                        | MODAL LAPORKAN LINK               |
                                                                                        | - [ ] Video Dihapus / Private     |
                                                                                        | - [ ] Video Tidak Relevan         |
                                                                                        | [ Batal ]        [ Kirim Laporan ]|
                                                                                        +-----------------------------------+
```

---

## 3. Rincian Komponen & Interaksi Layar

### **Layar 1 — Beranda Semester**
* **Tujuan:** Memberikan ringkasan progres belajar semester aktif dan pintu masuk navigasi kurikulum per semester.
* **Elemen UI:**
  * Header judul aplikasi & nama jurusan percontohan (*Teknik Informatika*).
  * *Semester Progress Bar*: menampilkan total topik yang sudah diselesaikan vs total topik semester aktif.
  * *Semester Tabs (1–8)*: berpindah semester dengan satu sentuhan.
  * *Course Cards*: daftar kartu mata kuliah pada semester yang dipilih (memuat judul, total pertemuan, dan progres penyelesaian mata kuliah).

### **Layar 2 — Silabus Mata Kuliah**
* **Tujuan:** Menampilkan daftar urutan 16 topik pertemuan terstruktur dari mata kuliah yang dipilih.
* **Elemen UI:**
  * Tombol navigasi kembali (`< Back`).
  * Header nama mata kuliah dan ringkasan persentase penyelesaian mata kuliah.
  * List topik pertemuan (1 s/d 16) yang mencakup:
    * *Checkbox*: menandai selesai/belum secara instan tanpa harus membuka video.
    * Judul materi pertemuan.
    * Durasi estimasi video.
    * Nama channel kreator pembuat video.

### **Layar 3 — Pemutar Video Pembelajaran**
* **Tujuan:** Ruang belajar fokus tanpa rekomendasi video luar, distraksi komentar, atau feeds lain.
* **Elemen UI:**
  * *Embedded YouTube Player* dengan kontrol esensial (Play/Pause, Seekbar, Fullscreen).
  * Selector kecepatan putar cepat (*Playback Speed*: 0.75x, 1x, 1.25x, 1.5x, 2x).
  * Info materi (Judul, nama channel kreator, deskripsi materi).
  * *Checkbox Progress*: tercentang otomatis jika tontonan $\ge 85\%$ atau dapat diubah secara manual.
  * Tautan eksternal *"Tonton di YouTube"* untuk memenuhi kepatuhan lisensi atribusi YouTube TOS.
  * Tombol aksi *"Laporkan Link Rusak"* untuk membuka modal dialog pelaporan jika video terhapus atau private.
