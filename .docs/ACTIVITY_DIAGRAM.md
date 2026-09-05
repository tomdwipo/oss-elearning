# Activity Diagram — OpenCampus Mobile (MVP v0.1)

Dokumen ini memetakan alur aktivitas (*Activity Diagram*) sederhana berdasarkan Use Case pada [USE_CASE.md](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/USE_CASE.md) dan [PRD.md](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/PRD.md).

---

## 1. Alur Utama: Navigasi Kurikulum & Memilih Materi (UC-01, UC-02, UC-03)

Diagram aktivitas saat pengguna membuka aplikasi, memilih semester, mata kuliah, hingga topik materi pembelajaran.

```mermaid
stateDiagram-v2
    [*] --> BukaAplikasi: Buka OpenCampus Mobile
    BukaAplikasi --> TampilSemester: Sistem menampilkan Tab Semester & Ringkasan Progres
    TampilSemester --> PilihSemester: Pengguna memilih Semester (1-8)
    PilihSemester --> TampilMataKuliah: Sistem memuat daftar Mata Kuliah
    TampilMataKuliah --> PilihMataKuliah: Pengguna memilih Mata Kuliah
    PilihMataKuliah --> TampilSilabus: Sistem menampilkan Daftar Pertemuan (Topik 1-16)
    TampilSilabus --> PilihTopik: Pengguna memilih Topik Materi
    PilihTopik --> [*]: Masuk ke Halaman Video Pembelajaran
```

---

## 2. Alur Video Player & Pelacakan Progres (UC-04, UC-05, UC-07, UC-08, UC-09)

Diagram aktivitas pemutaran video tertanam, kontrol playback, serta penandaan progres belajar secara otomatis ($\ge 85\%$) maupun manual.

```mermaid
stateDiagram-v2
    [*] --> MemuatVideo: Sistem memuat YouTube Player Tertanam
    
    state VideoPlayer {
        MemuatVideo --> PutarVideo: Video diputar
        PutarVideo --> KontrolPlayback: Pengguna mengatur kecepatan (0.75x-2x) / Scrubbing
        PutarVideo --> BukaYouTube: Klik "Tonton di YouTube" (Opsional / Atribusi)
    }

    PutarVideo --> CekDurasi: Sistem memantau progress tontonan
    
    state CekDurasi <<choice>>
    CekDurasi --> AutoSelesai: Durasi tontonan >= 85%
    CekDurasi --> Menonton: Durasi tontonan < 85%
    
    AutoSelesai --> UpdateProgres: Sistem otomatis mencentang topik selesai
    
    Menonton --> ChecklistManual: Pengguna mencentang manual checkbox (Opsional)
    ChecklistManual --> UpdateProgres: Simpan status selesai ke penyimpanan lokal
    
    UpdateProgres --> UpdateHeader: Sistem memperbarui persentase progres Semester
    UpdateHeader --> [*]
```

---

## 3. Alur Pelaporan Link / Video Rusak (UC-10)

Diagram aktivitas ketika video YouTube tidak dapat diputar (private / dihapus / error).

```mermaid
stateDiagram-v2
    [*] --> VideoError: Video tidak bisa diputar / error
    VideoError --> KlikLaporkan: Pengguna menekan tombol "Laporkan Link Rusak"
    KlikLaporkan --> Konfirmasi: Pengguna memilih alasan laporan
    Konfirmasi --> KirimLaporan: Sistem mencatat & mengirim laporan link bermasalah
    KirimLaporan --> TampilNotifikasi: Sistem menampilkan notifikasi terima kasih
    TampilNotifikasi --> [*]
```
