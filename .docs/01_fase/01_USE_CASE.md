# Use Case Diagram — OpenCampus Mobile (MVP v0.1)

Dokumen ini memetakan diagram *Use Case* fungsional untuk **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../00_fase/01_PRD.md).

---

## 1. Diagram Use Case (ASCII)

```text
========================================================================================
                              OPENCAMPUS MOBILE (MVP v0.1)
========================================================================================

      +---+
     /     \
     | o o |
     \  -  /
      +-+-+
        |
     ---+---        --[ 1. Navigasi Kurikulum ]----------------------------------------
        |           |
       / \          +---> (UC-01) Memilih Tab Semester (1 s.d. 8)
      /   \         |
                    +---> (UC-02) Melihat Daftar Mata Kuliah
  Pengguna /        |
  Pembelajar        +---> (UC-03) Melihat Silabus Topik (Pertemuan 1 s.d. 16)
  Mandiri           |
                    |
                    --[ 2. Video Player Bebas Distraksi ]------------------------------
                    |
                    +---> (UC-04) Memutar Video Pembelajaran Tertanam
                    |          |
                    |          +---<<include>>---> (UC-05) Mengatur Playback & Speed (0.75x-2x)
                    |          +---<<include>>---> (UC-06) Membuka Tautan Asli YouTube
                    |          |
                    |          +---<<extend>>----> (UC-07) Auto-Centang Progres (>= 85%)
                    |          +---<<extend>>----> (UC-10) Melaporkan Video / Link Rusak
                    |
                    --[ 3. Pelacakan Progres Belajar ]---------------------------------
                    |
                    +---> (UC-08) Menandai / Membatalkan Centang Manual
                    |
                    +---> (UC-09) Melihat Ringkasan & Persentase Selesai per Semester
                    -------------------------------------------------------------------

========================================================================================
```

---

## 2. Deskripsi Use Case & Referensi Desain

| Kode | Nama Use Case | Aktor | Deskripsi Singkat | Referensi Figma / Node ID | Ref |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **UC-01** | Memilih Tab Semester | Pengguna [1] | Berpindah antar semester (Semester 1 s.d. 8). | Tab Semester Pills (Node `1842:24850`) | [2] |
| **UC-02** | Melihat Daftar Mata Kuliah | Pengguna [1] | Melihat kartu mata kuliah pada semester terpilih beserta estimasi pertemuan. | Course Card (Node `1842:24900`) | [3] |
| **UC-03** | Melihat Silabus Topik | Pengguna [1] | Melihat daftar 16 pertemuan, judul materi, durasi video, dan nama channel kreator. | Syllabus List Item (Node `1842:24920`) | [4] |
| **UC-04** | Memutar Video Pembelajaran | Pengguna [1] | Memutar video YouTube tertanam tanpa rekomendasi/komentar distraksi. | Video Player Frame (Node `1842:25100`) | [5] |
| **UC-05** | Mengatur Playback | Pengguna [1] | Mengontrol Play/Pause, scrubbing durasi, fullscreen, dan kecepatan putar (0.75x–2x). | Player Controls (Node `1842:25120`) | [6] |
| **UC-06** | Membuka Video di YouTube | Pengguna [1] | Menuju link video asli YouTube untuk atribusi kreator (TOS compliance). | Button Secondary Outline (Node `1842:24720`) | [7] |
| **UC-07** | Melacak Progres Otomatis | Pengguna / Sistem [1] | Menandai materi selesai otomatis saat tontonan mencapai minimal 85%. | Linear Progress Indicator (Node `1842:25040`) | [8] |
| **UC-08** | Menandai Centang Manual | Pengguna [1] | Mencentang atau membatalkan status selesai pada topik secara mandiri. | Checkbox Component (Node `1842:25010`) | [9] |
| **UC-09** | Melihat Ringkasan Progres | Pengguna [1] | Memantau persentase dan jumlah materi selesai di header semester dengan maskot. | Hero Progress Card & Mascot Cookies (Node `1842:25160`) | [10] |
| **UC-10** | Melaporkan Link Rusak | Pengguna [1] | Mengirim laporan ketika video YouTube berstatus private/dihapus/tidak dapat diputar. | Error Fallback Banner & Action Button (Node `1842:24750`) | [11] |

---

## 3. Tabel Sitasi & Dasar Rujukan

| Ref | Dokumen Sumber | Bab / Bagian Spesifik | Kutipan Aturan Faktual / Dasar Desain |
| :--- | :--- | :--- | :--- |
| **[1]** | [01_PRD.md](../00_fase/01_PRD.md) | §1 Problem Statement & Solusi | Aktor utama adalah pembelajar mandiri yang membutuhkan kurasi perkuliahan terstruktur. |
| **[2]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 1 (FR-1.1) | Pengguna dapat memilih tab Semester 1 hingga 8. |
| **[3]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 1 (FR-1.2) | Tampil daftar kartu mata kuliah (Judul, estimasi pertemuan, status progres). |
| **[4]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 1 (FR-1.3) | Daftar pertemuan urut (Topik 1 s.d. 16) dengan judul, durasi, channel, dan checkbox. |
| **[5]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 2 (FR-2.1) | Video diputar langsung via YouTube Player embedded tanpa rekomendasi sampingan & komentar. |
| **[6]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 2 (FR-2.2) | Kontrol player: Play/Pause, scrubbing durasi, fullscreen, kecepatan 0.75x–2x. |
| **[7]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 2 (FR-2.3) | Tombol tautan "Tonton di YouTube" untuk memenuhi YouTube API Terms of Service. |
| **[8]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 3 (FR-3.1) | Pertemuan otomatis tercentang jika video ditonton ≥ 85%. |
| **[9]** | [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 3 (FR-3.2) | Pengguna dapat mencentang atau membatalkan centang manual via checkbox. |
| **[10]**| [01_PRD.md](../00_fase/01_PRD.md) | §4 Modul 3 (FR-3.3) | Header semester menampilkan progres: X dari Y materi selesai (Z%). |
| **[11]**| [01_PRD.md](../00_fase/01_PRD.md) | §5.2 Penanganan Error Utama | Pesan "Video materi sedang diperbarui" dan tombol "Laporkan Link" saat video rusak/private. |
