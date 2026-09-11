# TRD 01: Navigasi Kurikulum & Silabus (Modul 1)

**Nomor Modul:** 01  
**Fitur Utama:** Navigasi Kurikulum, Semester, Silabus, dan Local Progress Tracking  
**Target Platform:** Kotlin Multiplatform (Android & iOS) via Compose Multiplatform  
**Dokumen PRD Terkait:** [PRD 01: Navigasi Kurikulum](../prd/01_prd.md)  
**Dokumen Master Desain:** [Design System UI Kit Free](../design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)  
**Arsip Evidence:** [Evidence 01: Log & Verification](../evidence/01/README.md)  

---

## 1. Arsitektur & Tech Stack

- **Framework:** Kotlin Multiplatform (KMP) & Compose Multiplatform (CMP)
- **Target Platform:** Android (`androidMain`) & iOS (`iosMain`)
- **Shared Codebase:** `composeApp/src/commonMain/kotlin`
- **Serialization:** `kotlinx.serialization` (JSON)
- **State Management & ViewModel:** Jetpack Compose / Lifecycle ViewModel Multiplatform (`androidx.lifecycle.viewmodel.compose`)
- **Local Persistence:** Multiplatform Settings / KV Storage (`com.russhwolf:multiplatform-settings`)
- **Design Tokens:** Design System & UI Kit Free (`Corporate/Purple` `#9D3FE7`, `Corporate/DarkPurple` `#602093`, `Informing/Approval` `#00B998`, Poppins typography)

```text
+-------------------------------------------------------------------------+
|                           PRESENTATION LAYER                            |
|  - SemesterHomeScreen (PillTabRow, HeroProgressCard, CourseCard, Search)|
|  - CourseSyllabusScreen (SyllabusHeader, TopicItem with Checkbox)       |
|  - SemesterViewModel (StateFlow<SemesterUiState>, UI Events)            |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                              DOMAIN LAYER                               |
|  - Data Models: CurriculumData, Semester, Course, Topic, ProgressSummary|
|  - Business Rules: Progress Calculation, Trace ID Generator             |
+-------------------------------------------------------------------------+
                                    |
                                    v
+-------------------------------------------------------------------------+
|                               DATA LAYER                                |
|  - CurriculumRepository: Static JSON Parser (curriculum_ti.json)        |
|  - ProgressRepository: Multiplatform Settings KV Store                  |
+-------------------------------------------------------------------------+
```

---

## 2. Sequence Diagram (Propagasi Trace ID & Client Events)

Setiap interaksi pengguna menghasilkan atau mewarisi `trace_id` dengan format:  
`trc_<action>_<timestamp>_<random_hex>` (contoh: `trc_sem_open_1725541000_a1b2c3`).

### 2.1. Alur Inisialisasi Beranda Semester & Pemuatan Awal
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
 |                     |                     | 4. loadCurriculumData(trace_id)                           |
 |                     |                     |------------------->|                                      |
 |                     |                     |                    | 5. Read curriculum_ti.json           |
 |                     |                     |                    |--+                                   |
 |                     |                     |                    |<-+                                   |
 |                     |                     |                    |                                      |
 |                     |                     |                    | 6. Return CurriculumData             |
 |                     |                     |<-------------------|                                      |
 |                     |                     |                                                           |
 |                     |                     | 7. loadAllProgress(trace_id)                              |
 |                     |                     |-------------------------------------->|                   |
 |                     |                     |                                       | 8. Read KV Store  |
 |                     |                     |                                       |--+                |
 |                     |                     |                                       |<-+                |
 |                     |                     | 9. Return Map<String, Boolean>        |                   |
 |                     |                     |<--------------------------------------|                   |
 |                     |                     |                                                           |
 |                     |                     | 10. Compute Semester 1 Progress                           |
 |                     |                     |--+                                                        |
 |                     |                     |<-+                                                        |
 |                     |                     |                                                           |
 |                     |                     | 11. emit(SemesterLoadedState)                             |
 |                     |                     |---------------------------------------------------------->|
 |                     | 12. Render UI       |                     (Record: sem_viewed)                  |
 |                     |<--------------------|                                                           |
```

### 2.2. Alur Toggle Checkbox Progres Silabus
```text
User            UI (Syllabus)         Controller/Bloc       ProgressStorage        CurriculumRepo     Analytics
 |                    |                      |                     |                     |                |
 | 1. Tap Checkbox    |                      |                     |                     |                |
 |    Topik 03        |                      |                     |                     |                |
 |------------------->|                      |                     |                     |                |
 |                    | 2. toggleProgress()  |                     |                     |                |
 |                    |--------------------->|                     |                     |                |
 |                    |                      | 3. Generate Trace ID: `trc_chk_toggle_xxx`|                |
 |                    |                      |--+                  |                     |                |
 |                    |                      |<-+                  |                     |                |
 |                    |                      |                     |                     |                |
 |                    |                      | 4. setTopicCompleted(topicId, true)       |                |
 |                    |                      |-------------------->|                     |                |
 |                    |                      |                     | 5. Commit KV Store  |                |
 |                    |                      |                     |--+                  |                |
 |                    |                      |                     |<-+                  |                |
 |                    |                      | 6. Ack Success      |                     |                |
 |                    |                      |<--------------------|                     |                |
 |                    |                      |                                           |                |
 |                    |                      | 7. Recalculate Course & Semester Progress |                |
 |                    |                      |--+                                        |                |
 |                    |                      |<-+                                        |                |
 |                    |                      |                                           |                |
 |                    | 8. Update Checkbox & | 9. trackEvent("topic_checkbox_toggled",   |                |
 |                    |    Progress UI State |    payload: {topic_id, completed, trace}) |                |
 |                    |<---------------------|----------------------------------------------------------->|
```

---

## 3. State Machine & MVI Lifecycle Diagram

```text
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
           |     | (Semester 1..8 & Course List)|                                    |
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

## 4. Ringkasan Task & Acceptance Criteria (AC)

| Task ID | Issue GitHub | Judul Task | Source Set / Layer | Milestone | Dependensi |
|---|---|---|---|---|---|
| **TASK-00** | [#1](https://github.com/tomdwipo/oss-elearning/issues/1) | Inisialisasi scaffolding project Kotlin Multiplatform & CMP | Root / `composeApp` | MVP v0.1 | - |
| **TASK-01** | [#2](https://github.com/tomdwipo/oss-elearning/issues/2) | Data models `@Serializable`, static JSON kurikulum, dan progress storage | `commonMain` (Data/Domain) | MVP v0.1 | [#1](https://github.com/tomdwipo/oss-elearning/issues/1) |
| **TASK-02** | [#3](https://github.com/tomdwipo/oss-elearning/issues/3) | SemesterHomeScreen dengan horizontal pill tabs dan daftar kartu MK | `commonMain` (CMP UI) | MVP v0.1 | [#2](https://github.com/tomdwipo/oss-elearning/issues/2) |
| **TASK-03** | [#4](https://github.com/tomdwipo/oss-elearning/issues/4) | HeroProgressCard dengan maskot Cookies dan progress bar dinamis | `commonMain` (CMP UI) | MVP v0.1 | [#2](https://github.com/tomdwipo/oss-elearning/issues/2) |
| **TASK-04** | [#5](https://github.com/tomdwipo/oss-elearning/issues/5) | CourseSyllabusScreen dengan daftar 16 topik silabus dan checkbox manual | `commonMain` (CMP UI) | MVP v0.1 | [#2](https://github.com/tomdwipo/oss-elearning/issues/2) |
| **TASK-05** | [#6](https://github.com/tomdwipo/oss-elearning/issues/6) | Unit tests di `commonTest`, negative control (Gate 3b), dan preview validation | `commonTest` (Testing) | MVP v0.1 | [#1](https://github.com/tomdwipo/oss-elearning/issues/1)..[#5](https://github.com/tomdwipo/oss-elearning/issues/5) |

---

## 5. Kontrak Data Model (Kotlin)

```kotlin
@Serializable
data class Topic(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val creatorChannel: String,
    val youtubeVideoId: String
)

@Serializable
data class Course(
    val id: String,
    val name: String,
    val code: String,
    val estimatedHours: Float,
    val topics: List<Topic>
)

@Serializable
data class Semester(
    val semesterNumber: Int,
    val name: String,
    val courses: List<Course>
)

@Serializable
data class CurriculumData(
    val major: String,
    val semesters: List<Semester>
)

data class ProgressSummary(
    val completedCount: Int,
    val totalCount: Int,
    val percentage: Int
) {
    companion object {
        fun calculate(completed: Int, total: Int): ProgressSummary {
            val pct = if (total == 0) 0 else ((completed.toFloat() / total) * 100).toInt().coerceIn(0, 100)
            return ProgressSummary(completed, total, pct)
        }
    }
}
```
