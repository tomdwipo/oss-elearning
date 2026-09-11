# PRD 02: Video Player & Pelacakan Progres (Modul 2)

**Nomor Modul:** 02  
**Fitur Utama:** Pemutar Video Pembelajaran Bebas Distraksi & Pelacakan Progres  
**Target Rilis:** MVP v0.1  
**Platform:** Mobile (Android & iOS — Compose Multiplatform)  
**Dokumen Induk PRD:** [PRD 00 (Global MVP)](./00_prd.md)  
**Dokumen Terkait:** [PRD 01 (Navigasi Kurikulum)](./01_prd.md)  

---

## 1. Problem Statement & Tujuan Modul 2

- **Problem:** Menonton materi kuliah di YouTube secara langsung rawan distraksi dari algoritma rekomendasi video, shorts, iklan berulang, dan kolom komentar yang membuyarkan fokus belajar. Selain itu, tidak ada integrasi otomatis penanda progres kurikulum akademik.
- **Tujuan:** Menyediakan antarmuka pemutar video minimalis yang terisolasi dari rekomendasi sampingan, dilengkapi kontrol kecepatan pemutaran, integrasi centang progres otomatis ($\ge 85\%$) maupun manual, serta kepatuhan atribusi YouTube API TOS.

---

## 2. Ruang Lingkup & Kebutuhan Fungsional (Functional Requirements)

### ✅ Kebutuhan Fungsional (FR):
- **FR-2.1:** Video materi diputar langsung di dalam aplikasi menggunakan YouTube Embedded Player SDK / iframe container.
- **FR-2.2:** Kontrol player minimalis hanya mencakup: Play/Pause, Slider durasi (*scrubbing*), Fullscreen, dan Pengatur kecepatan (*0.75x s.d. 2.0x*).
- **FR-2.3:** Terdapat tombol tautan "Tonton di YouTube" untuk memenuhi lisensi atribusi YouTube TOS.
- **FR-3.1:** Sistem secara otomatis menandai topik sebagai "Selesai" jika video telah ditonton $\ge 85\%$ durasinya.
- **FR-3.2:** Pengguna dapat mencentang atau membatalkan centang secara manual di bawah player.
- **FR-3.3:** Status penyelesaian disimpan ke *local storage* dan memperbarui progres mata kuliah serta semester.
- **FR-3.4:** Terdapat modal pelaporan tautan rusak jika video private, dihapus, atau tidak dapat diputar.

---

## 3. Use Case & Activity Diagram

### 3.1. Pemetaan Use Case
- **(UC-04) Memutar Video Pembelajaran Tertanam:** Memuat iframe player resmi YouTube tanpa elemen sidebar/rekomendasi.
- **(UC-05) Mengatur Playback & Speed:** Memilih kecepatan putar (0.75x, 1x, 1.25x, 1.5x, 2x).
- **(UC-06) Membuka Tautan Asli YouTube:** Membuka URL video resmi di aplikasi YouTube / peramban luar untuk atribusi kreator.
- **(UC-07) Auto-Centang Progres (>= 85%):** Listener durasi otomatis memicu event selesai saat ambang batas tercapai.
- **(UC-08) Centang Manual Progres:** Pengguna dapat menekan checkbox secara bebas kapan saja.
- **(UC-10) Melaporkan Video / Link Rusak:** Membuka formulir cepat untuk melaporkan status video ke sistem kurasi.

### 3.2. Activity Diagram Video Player

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
               [ Ya ]               [ Tidak ]                   |
                 |                      |                       |
                 v                      v                       |
         [ Sistem Tandai        [ Pengguna Centang ]            |
          Selesai Otomatis ]      [ Manual (Toggle) ]           |
                 |                      |                       |
                 +----------+-----------+                       |
                            |                                   |
                            v                                   |
           [ Simpan Status ke Local Storage ]                   |
                            |                                   |
                            v                                   |
             [ Update Persentase Progres ]                      |
                            |                                   |
                            v                                   |
             [ Pengguna Keluar Layar Video ] -------------------+
                            |
                            v
             ( End: Kembali ke Layar Silabus )
```

---

## 4. User Flow & Wireflow

### 4.1. User Flow
```text
                   ( Start: Masuk Layar Pemutar Video )
                                    |
                                    v
                 [ Sistem Inisialisasi YouTube Player Iframe ]
                                    |
                    +---------------+---------------+
                    |                               |
                    v (Sukses)                      v (Video Error / Private / Dihapus)
        [ Tampilkan Video & Kontrol ]    [ Tampilkan Pesan Error & Tombol Laporkan ]
                    |                               |
                    |                               v (Tap Laporkan)
                    |                    [ Buka Modal Pelaporan Link ]
                    |                               |
                    |                               v
                    |                    [ Kirim Laporan ke Sistem ]
                    |
                    v
          [ Pengguna Memutar Video ] <---------------------------------------+
                    |                                                        |
         +----------+--------------------+-------------------+               |
         |                               |                   |               |
         v                               v                   v               |
  [ Kontrol Playback ]           [ Buka Tautan Asli ] [ Toggle Checkbox ]    |
  - Play / Pause                 - Buka App YouTube    - Centang Manual      |
  - Slider Durasi (Scrubbing)    - Atribusi Kreator    - Hapus Centang       |
  - Speed (0.75x s/d 2.0x)               |                   |               |
  - Fullscreen Mode                      v                   v               |
         |                       [ Tetap Terbuka ]   [ Simpan ke Storage ]   |
         |                                                   |               |
         +-------------------------------+-------------------+               |
                                         |                                   |
                                         v                                   |
                          < Apakah Durasi Tontonan >= 85%? >                 |
                                   /           \                             |
                           [ Ya ]                [ Tidak ]                   |
                             |                      |                        |
                             v                      v                        |
                     [ Sistem Tandai        [ Masih Menonton? ]              |
                      Selesai Otomatis ]          /       \                  |
                             |               [ Ya ]       [ Keluar ]         |
                             |                 |              |              |
                             |                 +--------------+              |
                             v                                |              |
            [ Simpan Status Centang ke Local Storage ]        |              |
                             |                                |              |
                             v                                |              |
             [ Update Persentase Progres Belajar ]            |              |
                             |                                |              |
                             v                                v              |
            [ Pengguna Tekan Tombol Kembali / Selesai Belajar ]              |
                             |                                               |
                             v                                               |
              ( End: Kembali ke Layar Silabus )                              |
```

### 4.2. Wireflow & Modal Pelaporan
```text
========================================================================================
[ LAYAR UTAMA: VIDEO LEARNING PLAYER ]          [ MODAL DIALOG: LAPORKAN LINK RUSAK ]
========================================================================================

+-----------------------------------+
| [<] Pertemuan 01                  |
+-----------------------------------+
| +-------------------------------+ |
| |                               | |
| |        [ > PLAY VIDEO ]       | |
| |  (YouTube Embedded Player)    | |
| |                               | |
| +-------------------------------+ |
| Kontrol: [0.75x] [1x*] [1.5x] [2x]|
+-----------------------------------+
| Pengantar Logika & Algoritma      |
| Channel: Web Programming UNPAS    |
| Durasi: 18 Menit                  |
+-----------------------------------+
| [X] Tandai Selesai (Manual)       |
+-----------------------------------+
| [↗] Tonton di YouTube (Atribusi)  |
| [!] Laporkan Link Rusak           | ===>  +-----------------------------------+
+-----------------------------------+ (Tap) | MODAL LAPORKAN LINK RUSAK         |
| Deskripsi Singkat Materi:         |       |                                   |
| Pengenalan konsep dasar algoritma |       | Pilih alasan:                     |
| dan logika pemrograman...         |       | - [ ] Video Dihapus / Private     |
+-----------------------------------+       | - [ ] Video Tidak Relevan         |
                                            | - [ ] Audio / Visual Rusak        |
                                            |                                   |
                                            | [ Batal ]        [ Kirim Laporan ]|
                                            +-----------------------------------+
```
