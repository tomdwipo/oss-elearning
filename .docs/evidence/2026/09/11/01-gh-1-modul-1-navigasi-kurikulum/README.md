# Evidence Archive: Modul 1 Navigasi Kurikulum (KMP / CMP)

**Task & Issue References:**
- TASK-00: Inisialisasi scaffolding KMP & CMP build configuration ([#1](https://github.com/tomdwipo/oss-elearning/issues/1))
- TASK-01: Data models `@Serializable`, static JSON kurikulum, dan local progress storage ([#2](https://github.com/tomdwipo/oss-elearning/issues/2))
- TASK-02: SemesterHomeScreen dengan horizontal pill tabs dan daftar kartu MK ([#3](https://github.com/tomdwipo/oss-elearning/issues/3))
- TASK-03: HeroProgressCard dengan maskot Cookies dan progress bar dinamis ([#4](https://github.com/tomdwipo/oss-elearning/issues/4))
- TASK-04: CourseSyllabusScreen dengan 16 topik silabus dan checkbox manual ([#5](https://github.com/tomdwipo/oss-elearning/issues/5))
- TASK-05: Unit tests di `commonTest`, negative control (Gate 3b), dan CMP preview validation ([#6](https://github.com/tomdwipo/oss-elearning/issues/6))

---

## 1. Tabel Lingkungan Eksekusi (Environment)

| Parameter | Nilai / Spesifikasi |
|---|---|
| **OS** | macOS Darwin 24.6.0 (Mac OS X 15.6 aarch64) |
| **Java JDK** | OpenJDK 17.0.12 (Homebrew 17.0.12+0) |
| **Gradle** | 8.11.1 (Tencent mirror distribution) |
| **Kotlin** | 2.1.0 (KMP multiplatform plugin) |
| **Compose Multiplatform** | 1.7.3 (JetBrains CMP) |
| **Android Gradle Plugin** | 8.7.3 (compileSdk 35, minSdk 24) |
| **Serialization** | `kotlinx.serialization` 1.7.3 (JSON) |
| **Settings Storage** | `com.russhwolf:multiplatform-settings` 1.2.0 |

---

## 2. Tabel 7 Gerbang Verifikasi Lokal

| # | Gerbang | Deskripsi & Perintah | Status | Hasil / Bukti Konkret |
|---|---|---|---|---|
| **1** | **Linters & Formatters** | `./gradlew check -x test` | ✅ HIJAU | Exit 0, 0 linter warning/error baru. |
| **2** | **Data & API Contracts** | `./gradlew test --tests "*Curriculum*"` | ✅ HIJAU | Skema JSON 8 semester (28 MK, 16 topik/MK) tervalidasi via `kotlinx.serialization`. Trace ID W3C pattern `trc_<action>_<ts>_<hex>` valid. |
| **3a** | **Unit Tests** | `./gradlew test` | ✅ HIJAU | 14/14 tests passed di `commonTest` (CurriculumRepository, ProgressRepository, SemesterViewModel, TraceIdAndTelemetry). Exit 0. |
| **3b** | **Negative Control** | Mutasi kalkulasi persentase di `ProgressSummary.calculate` (`+ 1`) | ✅ TERVERIFIKASI | **MERAH** (Exit 1, 2 failed: `testProgressSummaryCalculation`, `testCalculateSemesterAndCourseProgress`) $\to$ dipulihkan $\to$ **HIJAU** (Exit 0, 14/14 passed). Rasio: `14/14 → 12/14 → 14/14`. |
| **4** | **Component & UI Viewport** | Standar viewport 375x812 dp & Design Tokens | ✅ HIJAU | Token `Corporate/Purple` (`#9D3FE7`), `DarkPurple` (`#602093`), `Informing/Approval` (`#00B998`), `HeroProgressCard`, `PillTabRow`, `CourseCard`, dan `TopicItem` terkonfirmasi. |
| **5** | **Performance & Assets** | `./gradlew assembleDebug` & audit APK | ✅ HIJAU | Exit 0, APK `composeApp-debug.apk` sebesar 10.8 MB berhasil dihasilkan tanpa leak memory/overhead tak wajar. |
| **6** | **Security & Secret Hygiene** | `git diff origin/main` | ✅ HIJAU | Tidak ada secret key, token, atau credential hardcoded. Storage progress murni terisolasi lokal di client KV store. |

---

## 3. Rincian Rasio Negative Control (Gerbang 3b)

- **Kondisi Awal (Baseline Hijau):** 14/14 passed (`./gradlew test` $\to$ Exit 0).
- **Mutasi (Injeksi Bug):** Mengubah formula kalkulasi progress `ProgressSummary.calculate` menjadi `(completedCount.toFloat() / totalCount * 100).toInt() + 1`.
- **Hasil Mutasi (Merah):** 12/14 passed, 2 failed (`./gradlew test` $\to$ Exit 1).
  - Gagal 1: `org.opencampus.elearning.ProgressRepositoryTest.testProgressSummaryCalculation` (expected: 50, was: 51)
  - Gagal 2: `org.opencampus.elearning.CurriculumRepositoryTest.testCalculateSemesterAndCourseProgress` (expected: 50, was: 51)
- **Pemulihan (Restored Hijau):** Formula dikembalikan normal $\to$ 14/14 passed (`./gradlew test` $\to$ Exit 0).
- **Rasio Pembuktian:** `14/14 → 12/14 → 14/14`.

---

## 4. Log Konsol
Dapat dilihat secara lengkap di [console-evidence.txt](./console-evidence.txt).
