# Modul 1: Navigasi Kurikulum — Sequence Diagram (+ Trace ID & Client Events)

Dokumen ini mendefinisikan rancangan **Sequence Diagram** teknis untuk alur sistem pada **Modul 1: Navigasi Kurikulum** (UC-01, UC-02, UC-03) aplikasi **OpenCampus Mobile**, mencakup propagasi **Trace ID** untuk observabilitas end-to-end serta pencatatan **Client Events** (analitik & telemetri).

---

## 1. Komponen & Aktor Sistem

| Komponen | Layer | Tanggung Jawab |
| :--- | :--- | :--- |
| **User** | Aktor | Pengguna / Pembelajar mandiri yang berinteraksi dengan layar mobile. |
| **UI (View / Screen)** | Presentation Layer | Widget antarmuka (`HomeScreen`, `SemesterView`, `SyllabusScreen`). |
| **Controller / State Mgr** | State / BLoC / ViewModel | Mengelola state UI, alur navigasi, dan inisiasi request data. |
| **Curriculum Repository** | Domain / Data Layer | Menyediakan data statis kurikulum (JSON lokal / cache kurasi silabus). |
| **Progress Storage** | Local Storage (SQLite / KV) | Menyimpan & membaca status penyelesaian materi per topik/semester. |
| **Analytics Service** | Observability / Telemetry | Mengirim/mencatat event telemetri (`Mixpanel`, `PostHog`, dsb.). |

---

## 2. Standar Trace ID & Konvensi Header Telemetri

Setiap interaksi pengguna menghasilkan atau mewarisi `trace_id` dengan format W3C / UUID v4:
- **Format Trace ID:** `trc_<action>_<timestamp>_<random_hex>` (contoh: `trc_sem_open_1725541000_a1b2c3`) atau W3C `traceparent: 00-{trace_id}-{span_id}-01`.
- **Propagasi:** `trace_id` diteruskan dari Presentation Layer ke State Manager, Repository, Local Storage, dan disertakan di setiap payload Client Event.

---

## 3. Sequence Diagram (ASCII)

### 3.1. Alur 1: Inisialisasi Beranda Semester & Pemuatan Awal Aplikasi

```text
User           UI (Home/Semester)     Controller/Bloc      CurriculumRepo     ProgressStorage     AnalyticsService
 |                     |                     |                    |                  |                   |
 | 1. Buka Aplikasi    |                     |                    |                  |                   |
 |-------------------->|                     |                    |                  |                   |
 |                     | 2. init()           |                    |                  |                   |
 |                     |-------------------->|                                                           |
 |                     |                     | 3. Generate Trace ID: `trc_app_init_xxx`                  |
 |                     |                     |--+                                                        |
 |                     |                     |  |                                                        |
 |                     |                     |<-+                                                        |
 |                     |                     |                                                           |
 |                     |                     | 4. Log Event: `app_opened` (trace_id: trc_app_init_xxx)   |
 |                     |                     |---------------------------------------------------------->|
 |                     |                     |                                                           |
 |                     |                     | 5. getCurriculum(semester: 1)                             |
 |                     |                     |------------------->|                                      |
 |                     |                     |                    | 6. Baca data kurasi (JSON)           |
 |                     |                     |                    |--+                                   |
 |                     |                     |                    |<-+                                   |
 |                     |                     | 7. Return List<Course> (Sem 1)                            |
 |                     |                     |<-------------------|                                      |
 |                     |                     |                                                           |
 |                     |                     | 8. getCompletedTopicIds(semester: 1)                      |
 |                     |                     |-------------------------------------->|                   |
 |                     |                     | 9. Return Set<TopicId>                |                   |
 |                     |                     |<--------------------------------------|                   |
 |                     |                     |                                                           |
 |                     |                     | 10. Hitung Agregasi Progres Smt 1 (45%, 7/16 Topik)       |
 |                     |                     |--+                                                        |
 |                     |                     |<-+                                                        |
 |                     |                     |                                                           |
 |                     | 11. Emit State:     |                                                           |
 |                     |     SemesterLoaded( |                                                           |
 |                     |       courses,      |                                                           |
 |                     |       progress)     |                                                           |
 |                     |<--------------------|                                                           |
 | 12. Render Beranda  |                     |                                                           |
 |     (Sem 1 Aktif)   |                     |                                                           |
 |<--------------------|                     |                                                           |
 |                     |                     | 13. Log Event: `semester_viewed`                          |
 |                     |                     |     { semester: 1, trace_id: `trc_app_init_xxx` }         |
 |                     |                     |---------------------------------------------------------->|
```

---

### 3.2. Alur 2: Penggantian Tab Semester (UC-01)

```text
User           UI (SemesterView)      Controller/Bloc      CurriculumRepo     ProgressStorage     AnalyticsService
 |                     |                     |                    |                  |                   |
 | 1. Tap Tab "Sem 2"  |                     |                    |                  |                   |
 |-------------------->|                     |                    |                  |                   |
 |                     | 2. onSemesterTabSelected(sem: 2)         |                  |                   |
 |                     |-------------------->|                                                           |
 |                     |                     | 3. Generate Trace ID: `trc_sem_switch_xxx`                |
 |                     |                     |--+                                                        |
 |                     |                     |  |                                                        |
 |                     |                     |<-+                                                        |
 |                     |                     |                                                           |
 |                     |                     | 4. Log Event: `semester_switched`                         |
 |                     |                     |    { from: 1, to: 2, trace_id: `trc_sem_switch_xxx` }     |
 |                     |                     |---------------------------------------------------------->|
 |                     |                     |                                                           |
 |                     |                     | 5. getCoursesBySemester(sem: 2)                           |
 |                     |                     |------------------->|                                      |
 |                     |                     | 6. Return List<Course> (Sem 2)                            |
 |                     |                     |<-------------------|                                      |
 |                     |                     |                                                           |
 |                     |                     | 7. getCompletedTopicIds(semester: 2)                      |
 |                     |                     |-------------------------------------->|                   |
 |                     |                     | 8. Return Set<TopicId>                |                   |
 |                     |                     |<--------------------------------------|                   |
 |                     |                     |                                                           |
 |                     |                     | 9. Kalkulasi Progres Semester 2                           |
 |                     |                     |--+                                                        |
 |                     |                     |<-+                                                        |
 |                     |                     |                                                           |
 |                     | 10. Emit State:     |                                                           |
 |                     |     SemesterChanged(|                                                           |
 |                     |       sem: 2,       |                                                           |
 |                     |       courses,      |                                                           |
 |                     |       progress)     |                                                           |
 |                     |<--------------------|                                                           |
 | 11. Update UI:      |                     |                                                           |
 |     Render Kartu MK |                     |                                                           |
 |     Semester 2      |                     |                                                           |
 |<--------------------|                     |                                                           |
```

---

### 3.3. Alur 3: Membuka Silabus Mata Kuliah (UC-02, UC-03)

```text
User           UI (SemesterView)       UI (SyllabusScreen)     Controller/Bloc      LocalData/Repo      AnalyticsService
 |                     |                       |                      |                   |                    |
 | 1. Tap Kartu MK     |                       |                      |                   |                    |
 |    "Algoritma"      |                       |                      |                   |                    |
 |-------------------->|                       |                      |                   |                    |
 |                     | 2. navigateToSyllabus |                      |                   |                    |
 |                     |    (courseId: cs101)  |                      |                   |                    |
 |                     |---------------------->|                      |                   |                    |
 |                     |                       | 3. loadSyllabus()    |                   |                    |
 |                     |                       |--------------------->|                                        |
 |                     |                       |                      | 4. Generate Trace ID:                  |
 |                     |                       |                      |    `trc_course_open_xxx`               |
 |                     |                       |                      |--+                                     |
 |                     |                       |                      |<-+                                     |
 |                     |                       |                      |                                        |
 |                     |                       |                      | 5. Log Event: `course_opened`          |
 |                     |                       |                      |    { course_id: "cs101",               |
 |                     |                       |                      |      trace_id: `trc_course_open_xxx` } |
 |                     |                       |                      |--------------------------------------->|
 |                     |                       |                      |                                        |
 |                     |                       |                      | 6. getCourseSyllabus(courseId: "cs101")|
 |                     |                       |                      |------------------>|                    |
 |                     |                       |                      | 7. Return 16 Topik + Status Selesai    |
 |                     |                       |                      |<------------------|                    |
 |                     |                       |                      |                                        |
 |                     |                       | 8. Emit State:       |                                        |
 |                     |                       |    SyllabusLoaded(   |                                        |
 |                     |                       |      topics: 1..16,  |                                        |
 |                     |                       |      progress: 25%   |                                        |
 |                     |                       |    )                 |                                        |
 |                     |                       |<---------------------|                                        |
 | 9. Tampilkan List   |                       |                      |                                        |
 |    16 Topik Silabus |<----------------------|                      |                                        |
```

---

### 3.4. Alur 4: Memilih Topik Pertemuan (Navigasi ke Modul 2 Video Player)

```text
User           UI (SyllabusScreen)    Controller/Bloc     AnalyticsService      UI (PlayerScreen - Modul 2)
 |                     |                     |                    |                         |
 | 1. Tap Topik 01     |                     |                    |                         |
 |    "Pengantar Algo" |                     |                    |                         |
 |-------------------->|                     |                    |                         |
 |                     | 2. onTopicSelected( |                    |                         |
 |                     |      topicId: t01,  |                    |                         |
 |                     |      videoId: v01)  |                    |                         |
 |                     |-------------------->|                    |                         |
 |                     |                     | 3. Generate Trace ID:                        |
 |                     |                     |    `trc_topic_sel_xxx`                       |
 |                     |                     |--+                 |                         |
 |                     |                     |<-+                 |                         |
 |                     |                     |                    |                         |
 |                     |                     | 4. Log Event: `topic_selected`               |
 |                     |                     |    { topic_id: "t01", course_id: "cs101",    |
 |                     |                     |      video_id: "v01",                        |
 |                     |                     |      trace_id: `trc_topic_sel_xxx` }         |
 |                     |                     |------------------->|                         |
 |                     |                     |                                              |
 |                     | 5. Navigasi ke      |                                              |
 |                     |    Video Player     |                                              |
 |                     |    (oper trace_id)  |                                              |
 |                     |------------------------------------------------------------------->|
 |                     |                     |                    |                         |
 | 6. Tampilkan Pemutar|                     |                    |                         |
 |    Video Modul 2    |<-------------------------------------------------------------------|
```

---

## 4. Spesifikasi Payload Client Events

Berikut adalah kamus payload client events yang dihasilkan pada alur Modul 1:

### 1. `app_opened`
```json
{
  "event_name": "app_opened",
  "timestamp": 1725541000123,
  "trace_id": "trc_app_init_8f9a2b",
  "payload": {
    "app_version": "0.1.0",
    "platform": "android",
    "initial_semester": 1
  }
}
```

### 2. `semester_switched`
```json
{
  "event_name": "semester_switched",
  "timestamp": 1725541015456,
  "trace_id": "trc_sem_switch_3c4d5e",
  "payload": {
    "previous_semester": 1,
    "selected_semester": 2,
    "total_courses": 5
  }
}
```

### 3. `course_opened`
```json
{
  "event_name": "course_opened",
  "timestamp": 1725541030789,
  "trace_id": "trc_course_open_7e8f9a",
  "payload": {
    "course_id": "cs101",
    "course_title": "Algoritma & Pemrograman",
    "semester": 1,
    "completed_topics_count": 4,
    "total_topics_count": 16,
    "progress_percentage": 25
  }
}
```

### 4. `topic_selected`
```json
{
  "event_name": "topic_selected",
  "timestamp": 1725541045012,
  "trace_id": "trc_topic_sel_1a2b3c",
  "payload": {
    "topic_id": "top_cs101_01",
    "course_id": "cs101",
    "meeting_number": 1,
    "video_id": "dQw4w9WgXcQ",
    "channel_name": "Web Programming UNPAS",
    "duration_minutes": 18
  }
}
```

---

## 5. Skenario Non-Happy Path (E1..E5 Wajib)

```text
========================================================================================================
                          5 SKENARIO NON-HAPPY PATH: MODUL 1 (NAVIGASI)
========================================================================================================

1. E1: Network Stall / Offline Kurikulum
   [User Tap MK] --> [Repo Timeout (5s)] --> [UI Tampilkan Inline Error Banner (Retry CTA)]
   (State UI form/tab tetap dipertahankan, tidak me-reset posisi scroll)

2. E2: YouTube Video Takedown / Private Error (Code 100/101/150)
   [User Tap Topik] --> [Player Mengembalikan Error 150]
   --> [UI Tampilkan State: "Video materi sedang diperbarui"]
   --> [Tombol CTA: "Laporkan Link Rusak" (UC-10)]

3. E3: OS Process Death / Low Memory Killer (LMK)
   [User Membuka Silabus MK] --> [App di-minimize & OS Kill Process]
   --> [User Buka Kembali App] --> [ViewModel Restore State dari SavedState/Storage]
   --> [Kembali Langsung ke Silabus MK yang Terakhir Dibuka]

4. E4: User Cancellation / Back Press saat Transisi Loading
   [User Tap Topik] --> [Loading State Aktif] --> [User Menekan Tombol Back]
   --> [Controller Membatalkan Coroutine Job Pemutaran]
   --> [UI Tetap Aman di Halaman Silabus tanpa Glitch]

5. E5: Corrupted Local Progress Storage / SQLite Disk Error
   [Baca Progress Lokal] --> [Exception: Database Corrupt]
   --> [Fallback: Inisialisasi Ulang State Guest Kosong]
   --> [Kirim Event Telemetri: `storage_read_error` { trace_id }]
```

---

## 6. Tabel Sitasi & Dasar Rujukan

| Ref | Dokumen Sumber | Bab / Bagian Spesifik | Alur Logis / Kontrak yang Diverifikasi |
| :--- | :--- | :--- | :--- |
| **[1]** | [01_PRD.md](../../../00_fase/01_PRD.md) | §4 Modul 1 (FR-1.1 s.d. FR-1.3) | Alur inisialisasi tab semester, kartu mata kuliah, dan silabus 16 pertemuan. |
| **[2]** | [01_PRD.md](../../../00_fase/01_PRD.md) | §5.1 Analytics Sederhana | Spesifikasi 4 event analitik: `app_opened`, `semester_switched`, `course_opened`, `topic_selected`. |
| **[3]** | [01_PRD.md](../../../00_fase/01_PRD.md) | §5.2 Penanganan Error Utama | Alur penanganan E2 (Video error code / private fallback banner & pelaporan). |
| **[4]** | [01_USE_CASE.md](../../../01_fase/01_USE_CASE.md) | UC-01, UC-02, UC-03 | Interaksi aktor terhadap Presentation Layer dan state controller. |
| **[5]** | [DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md](../../../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md) | Token Komponen | Figma Node `1842:24850` (Pill Tabs), Node `1842:24900` (Course Cards), Node `1842:25160` (Mascot). |

