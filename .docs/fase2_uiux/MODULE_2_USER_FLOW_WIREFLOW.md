# Modul 2: Video Player & Pelacakan Progres — User Flow & Wireflow (MVP v0.1)

Dokumen ini mendefinisikan rancangan **User Flow** dan **Wireflow Sederhana** untuk **Modul 2: Video Player Bebas Distraksi & Pelacakan Progres** (UC-04, UC-05, UC-06, UC-07, UC-08, UC-09, UC-10) pada aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../fase0/PRD.md), [USE_CASE.md](../fase1/USE_CASE.md), dan [ACTIVITY_DIAGRAM.md](../fase1/ACTIVITY_DIAGRAM.md).

---

## 1. User Flow (Alur Pengguna)

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

---

## 2. Wireflow (Player & Modal Pelaporan)

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

---

## 3. Rincian Komponen & Interaksi Layar

### **Layar Pemutar Video Pembelajaran ([UC-04](../fase1/USE_CASE.md), [UC-05](../fase1/USE_CASE.md), [UC-06](../fase1/USE_CASE.md), [UC-07](../fase1/USE_CASE.md), [UC-08](../fase1/USE_CASE.md))**
* **Tujuan:** Ruang belajar fokus tanpa rekomendasi video luar, distraksi komentar, atau feeds lain.
* **Elemen UI & Fitur:**
  * **Tombol Kembali (`< Back`):** Kembali ke halaman silabus mata kuliah.
  * **Embedded YouTube Player:** Memutar video materi secara mandiri.
  * **Playback Controls:** Pilihan cepat kecepatan putar (*0.75x, 1x, 1.25x, 1.5x, 2x*), scrubbing slider, dan tombol fullscreen.
  * **Informasi Materi:** Judul pertemuan, nama kreator/channel, dan durasi video.
  * **Checkbox Status Selesai:**
    * Otomatis tercentang saat durasi tontonan mencapai $\ge 85\%$.
    * Bisa di-toggle secara manual oleh pengguna.
    * Status disimpan langsung ke *local storage*.
  * **Tautan Atribusi Eksternal (`Tonton di YouTube`):** Membuka aplikasi YouTube / browser eksternal untuk kepatuhan lisensi YouTube TOS.
  * **Tombol Laporkan Link Rusak:** Membuka modal pelaporan jika video bermasalah.

### **Modal Laporkan Link Rusak ([UC-10](../fase1/USE_CASE.md))**
* **Tujuan:** Memberikan mekanisme bagi pembelajar untuk melaporkan video yang private, dihapus, atau rusak agar tim kurator dapat memperbarui data URL tanpa rilis ulang aplikasi.
* **Elemen UI:**
  * Pilihan kategori kerusakan (*Checkbox/Radio button*).
  * Tombol *Batal* & *Kirim Laporan*.
