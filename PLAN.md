# Project Plan & Architecture: OSS E-Learning Platform

Dokumen ini berisi rencana arsitektur, desain entitas database, dan roadmap pengembangan dari nol hingga rilis open-source.

---

## 1. Arsitektur Sistem

```
┌─────────────────────────────────────────────────────────────┐
│                    Web Client (Next.js)                     │
│       (Student App, Instructor Studio, Admin Dashboard)     │
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTPS / REST / GraphQL
┌──────────────────────────────▼──────────────────────────────┐
│                    API Gateway / Backend                    │
│      - Auth & RBAC Service        - Course & CMS Service    │
│      - Quiz & Evaluation Service  - Certificate Generator   │
└──────────────┬───────────────────────────────┬──────────────┘
               │                               │
┌──────────────▼─────────────┐   ┌─────────────▼──────────────┐
│   PostgreSQL (Main DB)     │   │   Object Storage (MinIO)   │
│ (Users, Courses, Progress) │   │ (Videos, PDFs, Assets)     │
└────────────────────────────┘   └────────────────────────────┘
               │
┌──────────────▼─────────────┐
│    Redis (Cache & Queue)   │
│  - Session & Rate Limiting │
│  - Async Tasks (PDF, Email)│
└────────────────────────────┘
```

---

## 2. Skema Data Inti (Data Model)

1. **User & Auth**:
   - `id`, `name`, `email`, `password_hash`, `role` (ADMIN, INSTRUCTOR, STUDENT), `avatar_url`, `created_at`
2. **Course**:
   - `id`, `title`, `slug`, `description`, `thumbnail_url`, `instructor_id`, `is_published`, `difficulty`
3. **Module & Lesson**:
   - `Module`: `id`, `course_id`, `title`, `order_index`
   - `Lesson`: `id`, `module_id`, `title`, `slug`, `content_type` (VIDEO, MARKDOWN, QUIZ), `content_body`, `video_url`, `duration_minutes`, `order_index`
4. **Enrollment & Progress**:
   - `Enrollment`: `id`, `user_id`, `course_id`, `status`, `enrolled_at`, `completed_at`
   - `LessonProgress`: `id`, `user_id`, `lesson_id`, `is_completed`, `completed_at`
5. **Quiz & Submission**:
   - `Quiz`: `id`, `lesson_id`, `passing_score`
   - `Question`: `id`, `quiz_id`, `question_text`, `options_json`, `correct_answer`
   - `QuizSubmission`: `id`, `user_id`, `quiz_id`, `score`, `passed`, `submitted_at`
6. **Certificate**:
   - `Certificate`: `id`, `user_id`, `course_id`, `certificate_code`, `issued_at`, `pdf_url`

---

## 3. Tahapan Pengembangan (Milestones)

### 📌 Milestone 1: Fondasi Proyek & Repository Setup
- [ ] Inisialisasi struktur monorepo/polyrepo (`apps/web`, `apps/api` atau fullstack Next.js).
- [ ] Konfigurasi Docker & `docker-compose.yml` (PostgreSQL, Redis, MinIO).
- [ ] Setup ORM & Migration script awal.
- [ ] Setup Authentication (Email/Password + OAuth) & Middleware RBAC.

### 📌 Milestone 2: Course Management & Student Learning Experience
- [ ] **Instructor Studio**: Form pembuatan kursus, modul, re-order materi (drag & drop), markdown editor & video uploader.
- [ ] **Course Catalog**: Filter kursus berdasarkan kategori/tingkat kesulitan, search bar.
- [ ] **Course Player**: Antarmuka belajar siswa, checklist penyelesaian materi, video streaming player.

### 📌 Milestone 3: Interactivity, Quiz & Discussion
- [ ] Builder kuis (Multiple Choice, Single Choice).
- [ ] Engine evaluasi & auto-scoring kuis bagi siswa.
- [ ] Kolom komentar / forum Q&A bertingkat di setiap halaman materi belajar.

### 📌 Milestone 4: Sertifikasi Otomatis & Gamification
- [ ] Pipeline pembuatan PDF sertifikat otomatis (menggunakan `@react-pdf/renderer` atau Puppeteer).
- [ ] Halaman verifikasi sertifikat publik (`/verify/[certificate_code]`).
- [ ] Statistik belajar, badges pencapaian, dan streak harian.

### 📌 Milestone 5: Open Source Polish & 1-Click Deployment
- [ ] Template `docker-compose.prod.yml` untuk self-hosting 1-command.
- [ ] Dokumentasi lengkap instalasi, arsitektur, dan contributing guide.
- [ ] Release v1.0.0 di GitHub.
