# Modul 1: Navigasi Kurikulum — State Diagram (+ State Metrics & Drop-offs)

Dokumen ini mendefinisikan rancangan **State Machine Diagram** untuk **Modul 1: Navigasi Kurikulum** (UC-01, UC-02, UC-03, UC-08, UC-09) pada aplikasi **OpenCampus Mobile**, dilengkapi dengan identifikasi **State Metrics (SLA & Target Konversi)** serta titik-titik potensi **Drop-off Pengguna**.

---

## 1. State Diagram (ASCII)

```text
========================================================================================================
                            STATE MACHINE: MODUL 1 (NAVIGASI KURIKULUM)
========================================================================================================

                 +-----------------------------+
                 |       [ APP_LAUNCHED ]      |
                 +-----------------------------+
                                |
                                | (Evt: app_opened)
                                v
                 +-----------------------------+     (Error / Timeout)      +--------------------+
                 |    LOADING_SEMESTER_DATA    | -------------------------> | ERROR_STATE_HOME   |
                 +-----------------------------+                            +--------------------+
                                |                                                     | (Retry)
                                | (Success: Data Loaded) <----------------------------+
                                v
           +---> +-----------------------------+ <-----------------------------------+
           |     |     HOME_SEMESTER_VIEW      |                                     |
           |     | (Semester 1..8 & Course List)|                                     |
           |     +-----------------------------+                                     |
           |              |           |                                              |
(Sem Switched)            |           | (Tap Course Card / Evt: course_opened)       |
           |              |           v                                              |
           |     (Tap Tab |    +-----------------------------+                       |
           |      Sem 1..8|    |    LOADING_SYLLABUS_DATA    |                       |
           |     Evt:     |    +-----------------------------+                       |
           |     sem_swtc)|           |                                              |
           |              |           | (Success: Silabus 1..16 Loaded)              |
           |              |           v                                              |
           +--------------+    +-----------------------------+                       |
                               |    COURSE_SYLLABUS_VIEW     |                       |
                               |  (16 Pertemuan Silabus UI)  |                       |
                               +-----------------------------+                       |
                                      |          |    |                              |
                (Tap Checkbox Manual) |          |    +--- (Tap Back Button) --------+
                                      v          |
                       +--------------------+    |
                       | UPDATING_PROGRESS  |    |
                       +--------------------+    |
                                      |          | (Tap Topik Pertemuan / Evt: topic_selected)
                                      +----------+
                                                 |
                                                 v
                               +-----------------------------------+
                               |     TRANSITION_TO_MODULE_2        |
                               |   (Handoff: Video Player View)    |
                               +-----------------------------------+
```

---

## 2. Definisi State & Transisi

| State | Deskripsi | Input Trigger / Transisi | Next State |
| :--- | :--- | :--- | :--- |
| **`APP_LAUNCHED`** | Aplikasi baru dibuka oleh pengguna. | Lifecycle startup selesai | `LOADING_SEMESTER_DATA` |
| **`LOADING_SEMESTER_DATA`** | Sistem membaca kurasi JSON & data progres lokal. | Data berhasil dimuat | `HOME_SEMESTER_VIEW` |
| **`HOME_SEMESTER_VIEW`** | Menampilkan tab semester (1–8), progress bar, dan daftar kartu MK. | 1. Tap tab semester<br>2. Tap kartu mata kuliah | 1. `HOME_SEMESTER_VIEW`<br>2. `LOADING_SYLLABUS_DATA` |
| **`LOADING_SYLLABUS_DATA`** | Memuat 16 topik pertemuan & status checklist mata kuliah terpilih. | Data silabus siap | `COURSE_SYLLABUS_VIEW` |
| **`COURSE_SYLLABUS_VIEW`** | Menampilkan silabus 16 topik, metadata video, & checkbox penyelesaian. | 1. Tap checkbox manual<br>2. Tap topik pertemuan<br>3. Tap tombol kembali | 1. `UPDATING_PROGRESS`<br>2. `TRANSITION_TO_MODULE_2`<br>3. `HOME_SEMESTER_VIEW` |
| **`UPDATING_PROGRESS`** | Menyimpan status checklist topik ke SQLite / local storage. | Selesai menulis ke storage | `COURSE_SYLLABUS_VIEW` |
| **`TRANSITION_TO_MODULE_2`**| Navigasi keluar dari Modul 1 menuju Pemutar Video (Modul 2). | Event `topic_selected` terkirim | Modul 2 Player State |

---

## 3. State Metrics & Target Konversi (Telemetry)

Berdasarkan sasaran pada [PRD.md](../../../00_fase/01_PRD.md), metrik performansi dan konversi tiap state diatur sebagai berikut:

```text
+-----------------------+      SLA: < 300ms      +-----------------------+
|  LOADING_SEMESTER     | ---------------------> |   HOME_SEMESTER_VIEW  |
+-----------------------+     Target: > 99%      +-----------------------+
                                                             |
                                                             | Conversion Target: >= 75%
                                                             | (PRD Activation >= 60%)
                                                             v
+-----------------------+      SLA: < 150ms      +-----------------------+
|   COURSE_SYLLABUS     | <--------------------- |    LOADING_SYLLABUS   |
+-----------------------+     Target: > 99%      +-----------------------+
            |
            | Conversion Target: >= 80% (Pilih Pertemuan)
            v
+-----------------------+
| TRANSITION_TO_MODUL_2 |
+-----------------------+
```

### **Tabel State Metrics:**

| State Transition | Metric / KPI | SLA / Target Nilai | Telemetry Event |
| :--- | :--- | :--- | :--- |
| `APP_LAUNCHED` ➔ `HOME_SEMESTER_VIEW` | Cold Start to Interactive | `< 500 ms` | `app_opened` |
| `HOME_SEMESTER_VIEW` (Switch Tab) | Semester Switch Latency | `< 100 ms` | `semester_switched` |
| `HOME_SEMESTER_VIEW` ➔ `SYLLABUS_VIEW` | Course Select to Syllabus Render | `< 200 ms` | `course_opened` |
| `SYLLABUS_VIEW` ➔ `UPDATING_PROGRESS` | Local Progress Write Latency | `< 50 ms` | `progress_toggled` |
| `SYLLABUS_VIEW` ➔ `TRANSITION_TO_MODULE_2` | Syllabus to Player Navigation | `< 200 ms` | `topic_selected` |

---

## 4. Analisis Titik Drop-off & Strategi Mitigasi

```text
========================================================================================================
                                     TITIK DROP-OFF PADA MODUL 1
========================================================================================================

 [ Layar 1: Beranda ] -------------------> [ Layar 2: Silabus ] -----------------> [ Modul 2: Player ]
           |                                         |
           v [ DROP-OFF D1 ]                         v [ DROP-OFF D2 ]
   * Pengguna keluar tanpa                   * Pengguna membuka silabus
     memilih mata kuliah                       namun tidak memilih video
   * Rasio Maks: <= 25%                      * Rasio Maks: <= 20%
```

### **Rincian Titik Drop-off & Mitigasi:**

#### **1. Drop-off D1: Semester / Course Abandonment**
* **Gejala:** Pengguna berada di `HOME_SEMESTER_VIEW`, mengganti-ganti tab semester namun menutup aplikasi tanpa membuka mata kuliah (`course_opened` = 0).
* **Penyebab Utama:** Terlalu banyak pilihan atau informasi kartu MK kurang menarik/informatif.
* **Solusi Mitigasi UX:**
  * Berikan indikator visual mata kuliah yang direkomendasikan untuk dimulai pertama kali (misal: badge *"Mulai dari Sini"* pada Semester 1).
  * Tampilkan durasi total singkat dan progres bar yang memotivasi.

#### **2. Drop-off D2: Syllabus Exploration Drop-off**
* **Gejala:** Pengguna masuk ke `COURSE_SYLLABUS_VIEW` tetapi tidak melakukan *tap* pada pertemuan 1 s/d 16 (`topic_selected` = 0).
* **Penyebab Utama:** Keraguan pengguna terkait durasi materi atau tidak tahu video mana yang harus ditonton selanjutnya.
* **Solusi Mitigasi UX:**
  * Sorot (*highlight*) secara otomatis pertemuan aktif berikutnya (*Next Up*).
  * Tampilkan durasi estimasi yang ringkas (misal: `⏱ 18 Menit`) agar tidak intimidatif bagi pembelajar mandiri.

#### **3. Drop-off D3: Loading Stall / Error Drop-off**
* **Gejala:** Kegagalan atau *freeze* saat membaca data lokal/JSON kurikulum (`LOADING_SEMESTER_DATA` > 2000 ms).
* **Solusi Mitigasi Teknis:**
  * Gunakan data kurikulum lokal yang di-bundle bersama aplikasi (*zero network latency* untuk MVP).
  * Lakukan *asynchronous reading* dengan fallback state yang aman.
