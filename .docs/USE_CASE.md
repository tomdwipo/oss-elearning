# Use Case Diagram — OpenCampus Mobile (MVP v0.1)

Dokumen ini memetakan diagram *Use Case* fungsional untuk **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/PRD.md).

---

## 1. Diagram Use Case (Mermaid)

```mermaid
flowchart LR
    %% Actor Definition
    User["👤 Pengguna / Pembelajar Mandiri"]

    subgraph SystemBoundary ["📱 OpenCampus Mobile System (MVP v0.1)"]
        %% Modul 1: Navigasi Kurikulum
        UC1(["UC-01: Memilih Tab Semester (1-8)"])
        UC2(["UC-02: Melihat Daftar Mata Kuliah"])
        UC3(["UC-03: Melihat Silabus Topik & Durasi"])

        %% Modul 2: Video Player Bebas Distraksi
        UC4(["UC-04: Memutar Video Pembelajaran"])
        UC5(["UC-05: Mengatur Playback & Kecepatan (0.75x-2x)"])
        UC6(["UC-06: Membuka Video Asli di YouTube"])

        %% Modul 3: Pelacakan Progres
        UC7(["UC-07: Melacak Progres Otomatis (≥ 85%)"])
        UC8(["UC-08: Menandai/Membatalkan Centang Manual"])
        UC9(["UC-09: Melihat Ringkasan Progres Semester"])

        %% Modul 4: Error Handling
        UC10(["UC-10: Melaporkan Video Rusak / Link Mati"])
    end

    %% Relationships
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9
    User --> UC10

    %% Includes & Extensions
    UC4 -.->|"<<include>>"| UC5
    UC4 -.->|"<<include>>"| UC6
    UC4 -.->|"<<extend>>"| UC7
    UC4 -.->|"<<extend>>"| UC10
```

---

## 2. Deskripsi Use Case

| Kode | Nama Use Case | Aktor | Deskripsi Singkat |
| :--- | :--- | :--- | :--- |
| **UC-01** | Memilih Tab Semester | Pengguna | Berpindah antar semester (Semester 1 s.d. 8). |
| **UC-02** | Melihat Daftar Mata Kuliah | Pengguna | Melihat daftar mata kuliah pada semester terpilih beserta estimasi pertemuan. |
| **UC-03** | Melihat Silabus Topik | Pengguna | Melihat daftar 16 pertemuan, judul materi, durasi video, dan nama channel kreator. |
| **UC-04** | Memutar Video Pembelajaran | Pengguna | Memutar video YouTube tertanam tanpa rekomendasi/komentar distraksi. |
| **UC-05** | Mengatur Playback | Pengguna | Mengontrol Play/Pause, scrubbing durasi, fullscreen, dan kecepatan putar (0.75x–2x). |
| **UC-06** | Membuka Video di YouTube | Pengguna | Menuju link video asli YouTube untuk atribusi kreator. |
| **UC-07** | Melacak Progres Otomatis | Pengguna / Sistem | Menandai materi selesai otomatis saat tontonan mencapai minimal 85%. |
| **UC-08** | Menandai Centang Manual | Pengguna | Mencentang atau membatalkan status selesai pada topik secara mandiri. |
| **UC-09** | Melihat Ringkasan Progres | Pengguna | Memantau persentase dan jumlah materi selesai di header semester. |
| **UC-10** | Melaporkan Link Rusak | Pengguna | Mengirim laporan ketika video YouTube berstatus private/dihapus/tidak dapat diputar. |
