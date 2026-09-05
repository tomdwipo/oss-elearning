# Product Requirement Document (PRD) — MVP Edition

**Nama Proyek:** OpenCampus Mobile  
**Target Rilis:** MVP v0.1  
**Platform:** Mobile (Android & iOS)  
**Tujuan MVP:** Memvalidasi apakah pembelajar mandiri lebih konsisten menyelesaikan materi kuliah jika kurasi video YouTube disajikan dalam format semester terstruktur.

---

## 1. Problem Statement & Solusi MVP

- **Problem:** Pembelajar mandiri kelelahan merangkai materi acak di YouTube (*curation fatigue*) dan sering terdistraksi oleh algoritma video rekomendasi, sehingga gagal menguasai materi secara berurutan.
- **Solusi MVP:** Aplikasi mobile yang menyajikan 1 jurusan percontohan (**Teknik Informatika / Ilmu Komputer**) yang dipetakan dari **Semester 1 sampai 8**, dengan video player tertanam tanpa rekomendasi luar, dan tombol penanda progres belajar.

---

## 2. Metrik Validasi MVP

| Metrik | Target | Alasan Pengukuran |
| :--- | :--- | :--- |
| **Aktivasi** | ≥ 60% pengguna baru memutar minimal 1 video materi | Memastikan kurasi kurikulum menarik perhatian user. |
| **Retensi Hari ke-7 (D7)** | ≥ 25% | Mengukur apakah user kembali untuk melanjutkan materi berikutnya. |
| **Completion Rate** | ≥ 30% menyelesaikan minimal 3 topik di Semester 1 | Validasi apakah struktur kurikulum mengurangi *drop-off* belajar. |

---

## 3. Ruang Lingkup MVP (Scope)

### ✅ In-Scope (Hanya yang Dibangun)
1. **Katalog 1 Jurusan:** Pemetaan silabus Semester 1–8 untuk jurusan Teknik Informatika.
2. **Struktur Hirarki 3 Level:**
   - **Halaman Semester:** Tab 1–8.
   - **Halaman Mata Kuliah:** Daftar mata kuliah per semester (misal: Algoritma & Pemrograman, Matematika Diskrit).
   - **Halaman Silabus Topik:** Daftar video pertemuan 1–16.
3. **Player Bebas Distraksi:** Pemutar YouTube resmi bawaan (*embedded*) tanpa kolom komentar, tanpa video rekomendasi sampingan, dan tanpa shorts.
4. **Progress Tracking Sederhana:** Centang manual/otomatis saat video selesai dan progress bar persentase per semester.
5. **Atribusi Kreator:** Nama channel YouTube dan tombol tautan ke video asli (memenuhi YouTube API TOS).

### ❌ Out-of-Scope (Ditunda Pasca-MVP)
- Fitur registrasi/login akun (gunakan penyimpanan lokal/guest state dulu untuk percepat rilis).
- Catatan pribadi, bookmark, dan forum diskusi.
- Pilihan multi-jurusan (fokus ke Informatika dulu).
- Ujian, kuis, atau sertifikat kelulusan.
- Unduh video luring (offline playback).

---

## 4. Spesifikasi Fungsional MVP (Core Features)

### Modul 1: Navigasi Kurikulum
- **FR-1.1:** Pengguna dapat memilih tab Semester 1 hingga 8.
- **FR-1.2:** Di setiap semester, tampil daftar kartu mata kuliah (Judul, estimasi jumlah pertemuan, dan status progres).
- **FR-1.3:** Saat mata kuliah diklik, tampil daftar pertemuan urut (Topik 1 s.d. 16) yang memuat judul materi, durasi video, nama channel kreator, dan status checkbox.

### Modul 2: Video Player
- **FR-2.1:** Video diputar langsung di dalam aplikasi menggunakan YouTube Player SDK / iframe player.
- **FR-2.2:** Kontrol player hanya memuat: Play/Pause, Slider durasi (*scrubbing*), Fullscreen, dan Pengatur kecepatan (0.75x–2x).
- **FR-2.3:** Terdapat tombol tautan "Tonton di YouTube" untuk memenuhi lisensi atribusi YouTube.

### Modul 3: Pelacakan Progres
- **FR-3.1:** Pertemuan otomatis tercentang jika video ditonton ≥ 85%.
- **FR-3.2:** Pengguna dapat mencentang atau membatalkan centang secara manual via ikon checkbox.
- **FR-3.3:** Header semester menampilkan progres: X dari Y materi selesai (Z%).

---

## 5. Tracking & Keandalan MVP

### 5.1 Analytics Sederhana (Mixpanel / PostHog)
Cukup pasang 4 event inti:
- `semester_switched`: `{ semester: Int }`
- `course_opened`: `{ course_id: String }`
- `video_started`: `{ video_id: String, course_id: String }`
- `video_completed`: `{ video_id: String, is_manual: Boolean }`

### 5.2 Penanganan Error Utama
- **Video Rusak / Dihapus Pemilik:** Jika YouTube Player mengembalikan error (video private/deleted), player menampilkan pesan:
  > *"Video materi sedang diperbarui"*
  serta tombol **"Laporkan Link"** agar tim bisa memperbarui URL di database tanpa perlu merilis ulang aplikasi.
