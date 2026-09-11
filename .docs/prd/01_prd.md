# PRD 01: Navigasi Kurikulum & Silabus (Modul 1)

**Nomor Modul:** 01  
**Fitur Utama:** Navigasi Kurikulum Berbasis Semester & Silabus Topik  
**Target Rilis:** MVP v0.1  
**Platform:** Mobile (Android & iOS — Compose Multiplatform)  
**Dokumen Induk PRD:** [PRD 00 (Global MVP)](./00_prd.md)  
**Dokumen Teknis Terkait:** [TRD 01](../trd/01_trd.md)  
**Bukti Verifikasi:** [Evidence 01](../evidence/01/README.md)  

---

## 1. Problem Statement & Tujuan Modul 1

- **Problem:** Pembelajar mandiri sering kesulitan mengetahui materi mana yang harus dipelajari terlebih dahulu dalam kurikulum Ilmu Komputer/Informatika. Kurasi materi yang tersebar tanpa urutan semester membingungkan dan memicu rasa kewalahan (*curation fatigue*).
- **Tujuan:** Menyajikan roadmap perkuliahan terstruktur 8 semester dari program studi Teknik Informatika, dengan navigasi cepat per semester, daftar mata kuliah, dan silabus terurut (16 pertemuan per mata kuliah) beserta indikator progres belajar.

---

## 2. Ruang Lingkup & Kebutuhan Fungsional (Functional Requirements)

### ✅ Kebutuhan Fungsional (FR):
- **FR-1.1:** Pengguna dapat berpindah antar tab Semester 1 hingga Semester 8 melalui horizontal pill bar.
- **FR-1.2:** Di setiap semester yang aktif, sistem menampilkan daftar kartu mata kuliah (Nama MK, jumlah pertemuan, estimasi durasi, dan ringkasan progres).
- **FR-1.3:** Saat kartu mata kuliah ditekan, pengguna diarahkan ke layar Silabus Mata Kuliah yang menampilkan daftar 16 topik pertemuan terurut.
- **FR-1.4:** Pengguna dapat menandai (centang manual) topik yang sudah selesai dipelajari, yang secara reaktif memperbarui persentase progres pada kartu mata kuliah dan progres semester.
- **FR-1.5:** Terdapat filter pencarian instan pada beranda semester untuk menyaring mata kuliah berdasarkan kata kunci judul MK.

---

## 3. Use Case & Activity Diagram

### 3.1. Pemetaan Use Case
- **(UC-01) Memilih Tab Semester (1 s.d. 8):**
  - **Aktor:** Pengguna / Pembelajar Mandiri.
  - **Pre-kondisi:** Aplikasi terbuka di Beranda Semester (`SemesterHomeScreen`).
  - **Alur Utama:** Pengguna menekan salah satu tab semester (1–8). Sistem memuat dan menampilkan daftar mata kuliah sesuai semester tersebut, serta memperbarui ringkasan progres semester di Hero Card.
- **(UC-02) Melihat Daftar Mata Kuliah:**
  - **Aktor:** Pengguna.
  - **Pre-kondisi:** Semester telah dipilih.
  - **Alur Utama:** Sistem merender daftar kartu mata kuliah lengkap dengan judul, icon, durasi estimasi, dan progress bar.
- **(UC-03) Melihat Silabus Topik Pertemuan:**
  - **Aktor:** Pengguna.
  - **Pre-kondisi:** Pengguna memilih salah satu kartu mata kuliah.
  - **Alur Utama:** Sistem membuka layar `CourseSyllabusScreen` yang menyajikan 16 pertemuan topik, nama kreator/channel, durasi video, dan status penyelesaian.

### 3.2. Activity Diagram Navigasi Modul 1

```text
  ( Start )
      |
      v
[ Buka Aplikasi OpenCampus ]
      |
      v
[ Tampilkan Tab Semester & Hero Progress Card ]
      |
      v
[ Pengguna Memilih Semester (1 - 8) ]
      |
      v
[ Sistem Memuat & Menampilkan Daftar Mata Kuliah ]
      |
      v
[ Pengguna Memilih Salah Satu Mata Kuliah ]
      |
      v
[ Sistem Menampilkan Silabus Topik (Pertemuan 1 - 16) ]
      |
      v
[ Pengguna Memilih Topik Pertemuan ]
      |
      v
   ( End: Masuk ke Layar Pemutar Video — Modul 2 )
```

---

## 4. User Flow & Wireflow

### 4.1. User Flow
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

### 4.2. Wireflow Antar Layar
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
                                                ( Masuk ke Modul 2: Video Player )
```

---

## 5. Lo-Fi Wireframe & Anatomi Komponen UI

```text
+-------------------------------------------------------------+
| 09:41                                              [📶 🔋]  |  <-- Status Bar
+-------------------------------------------------------------+
| [LOGO] OpenCampus                     [ Jurusan: IF ]       |  <-- App Bar / Header
|                                                             |
| +---------------------------------------------------------+ |
| | Progres Semester 1                                      | |  <-- Hero Progress Card
| | [==================-------------------------] 45%       | |      (Cookies Mascot + Dynamic Bar)
| | 7 dari 16 Topik Selesai                                 | |
| +---------------------------------------------------------+ |
|                                                             |
| PILIH SEMESTER:                                             |  <-- Horizontal Scroll Pill Tab Row
| +-------+  +-------+  +-------+  +-------+  +-------+       |      (1 s.d. 8)
| | Sem 1*|  | Sem 2 |  | Sem 3 |  | Sem 4 |  | Sem 5 |  ...> |
| +-------+  +-------+  +-------+  +-------+  +-------+       |
|                                                             |
| DAFTAR MATA KULIAH (Semester 1)                             |  <-- Section Title
|                                                             |
| +---------------------------------------------------------+ |  <-- Course Card Item
| | [ICON] Algoritma & Pemrograman Dasar                    | |
| |        16 Pertemuan • Durasi Est: ~6 Jam                | |
| |        Progres: 4 / 16 Selesai (25%)                    | |
| |        [========----------------------------]           | |
| |                                                     [>] | |
| +---------------------------------------------------------+ |
+-------------------------------------------------------------+
```

---

## 6. Design System Tokens & Standar Visual

Desain mengacu langsung pada master [Design System](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md):
- **Primary Color:** `Corporate/Purple` (`#9D3FE7`)
- **Dark Primary:** `Corporate/DarkPurple` (`#602093`)
- **Success / Completed:** `Informing/Approval` (`#00B998`)
- **Background / Card Surface:** `#FFFFFF` (Light), `#F8F9FA` (Neutral Background), `#1E1E2E` (Dark Mode Surface)
- **Typography:** Poppins (SemiBold untuk heading, Regular untuk body, Medium untuk badges)
- **Corner Radius:** 16dp (Hero Card & Course Card), 24dp (Pill Tabs), 8dp (Checkbox container)

---

## 7. Aset Prototipe Interaktif & Pengujian Usability

Aset prototipe HTML interaktif dapat dibuka langsung di browser:
1. **Full Flow Modul 1 Prototype:** [hifi_module_1_prototype.html](./prototypes/hifi_module_1_prototype.html)
2. **Pill Tabs & Course Card Interaction:** [hifi_semester_tabs_preview.html](./prototypes/hifi_semester_tabs_preview.html)
3. **Design Tokens Color & Typography POC:** [module_1_tokens_poc.html](./prototypes/module_1_tokens_poc.html)

**Hasil Usability Review:**
- Rasio kepuasan navigasi tab: 100% peserta berhasil memilih semester dan melihat pembaruan daftar mata kuliah.
- Transisi navigasi antara `SemesterHomeScreen` dan `CourseSyllabusScreen` responsif di bawah 100ms.
