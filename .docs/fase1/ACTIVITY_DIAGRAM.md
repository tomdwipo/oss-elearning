# Activity Diagram — OpenCampus Mobile (MVP v0.1)

Dokumen ini memetakan alur aktivitas (*Activity Diagram*) fungsional sederhana untuk **OpenCampus Mobile** dalam format diagram ASCII berdasarkan [USE_CASE.md](USE_CASE.md) dan [PRD.md](../fase0/PRD.md).

---

## 1. Alur Navigasi Kurikulum & Memilih Materi (UC-01, UC-02, UC-03)

```text
  ( Start )
      |
      v
[ Buka Aplikasi OpenCampus ]
      |
      v
[ Tampilkan Tab Semester & Progress Bar ]
      |
      v
[ Pengguna Memilih Semester (1 - 8) ]
      |
      v
[ Sistem Memuat & Menampilkan Daftar Mata Kuliah ]
      |
      v
[ Pengguna Memilih Mata Kuliah ]
      |
      v
[ Sistem Menampilkan Silabus Topik (Pertemuan 1 - 16) ]
      |
      v
[ Pengguna Memilih Topik Pertemuan ]
      |
      v
   ( End: Masuk ke Halaman Video Pembelajaran )
```

---

## 2. Alur Video Player & Pelacakan Progres (UC-04, UC-05, UC-07, UC-08, UC-09)

```text
               ( Start: Buka Topik Materi )
                            |
                            v
        [ Sistem Memuat YouTube Player Tertanam ]
                            |
                            v
              [ Video Mulai Diputar ] <-------------------------+
                            |                                   |
              +-------------+-------------+                     |
              |                           |                     |
              v                           v                     |
    [ Kontrol Playback ]        [ Buka Tautan YouTube ]         |
    (Speed 0.75-2x / Scrub)      (Atribusi Kreator)             |
              |                           |                     |
              +-------------+-------------+                     |
                            |                                   |
                            v                                   |
             < Durasi Tontonan >= 85%? >                        |
                  /                   \                         |
              [ Ya ]                [ Tidak ]                   |
                |                      |                        |
                v                      v                        |
        [ Sistem Otomatis       [ Masih Menonton /              |
         Centang Selesai ]       Centang Manual? ]              |
                |                    /          \               |
                |             [ Centang ]     [ Lanjut ] -------+
                |                  |
                +--------+---------+
                         |
                         v
        [ Simpan Status Selesai ke Penyimpanan Lokal ]
                         |
                         v
        [ Perbarui Persentase Progress Semester ]
                         |
                         v
                      ( End )
```

---

## 3. Alur Pelaporan Link / Video Rusak (UC-10)

```text
                  ( Start )
                      |
                      v
      [ Video Error / Private / Dihapus ]
                      |
                      v
    [ Pengguna Tekan Tombol "Laporkan Link Rusak" ]
                      |
                      v
        [ Pilih Kategori / Alasan Laporan ]
                      |
                      v
      [ Sistem Mencatat & Mengirim Data Laporan ]
                      |
                      v
      [ Tampilkan Notifikasi Umpan Balik Sukses ]
                      |
                      v
                   ( End )
```
