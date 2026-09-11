# Activity Diagram — OpenCampus Mobile (MVP v0.1)

Dokumen ini memetakan alur aktivitas (*Activity Diagram*) fungsional sederhana untuk **OpenCampus Mobile** dalam format diagram ASCII berdasarkan [USE_CASE.md](01_USE_CASE.md) dan [PRD.md](../00_fase/01_PRD.md).

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

---

## 4. Tabel Sitasi & Dasar Rujukan

| Ref | Dokumen Sumber | Bab / Bagian Spesifik | Alur Logis yang Diverifikasi |
| :--- | :--- | :--- | :--- |
| **[1]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 1 (FR-1.1 s.d. FR-1.3) | Alur navigasi 3-level: Semester (1-8) $\to$ Course List $\to$ Syllabus 1-16. |
| **[2]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 2 (FR-2.1 & FR-2.2) | Alur video playback tertanam dengan kontrol playback mandiri. |
| **[3]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 3 (FR-3.1 & FR-3.2) | Logika percabangan auto-complete $\ge 85\%$ vs centang manual pengguna. |
| **[4]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 3 (FR-3.3) | Kalkulasi agregasi progres lokal per semester dan pembaruan UI. |
| **[5]** | [01_PRD.md](../00_fase/01_PRD.md) | §5.2 Penanganan Error Utama | Alur mitigasi video rusak: fallback state + trigger pelaporan link rusak. |

