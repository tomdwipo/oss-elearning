# Open Source E-Learning Platform (OSS E-Learning)

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

Platform Learning Management System (LMS) modern, open source, dan modular yang dirancang untuk kemudahan self-hosting, fleksibilitas kurikulum, pembelajaran interaktif, asesmen/kuis, serta sertifikasi otomatis.

---

## 🎯 Visi & Tujuan
- **100% Open Source & Self-Hostable**: Dapat dijalankan di server lokal, VPS murah, maupun cloud provider dengan Docker Compose.
- **Modern & Developer Friendly**: Menggunakan tech stack modern yang bersih, performant, dan mudah dikustomisasi.
- **Lengkap & Siap Pakai**: Mendukung video streaming, markdown lesson, kuis otomatis, forum diskusi, dan sertifikat ber-QR code.

---

## 🚀 Fitur Utama

- 👥 **Role-Based Access Control (RBAC)**: Super Admin, Instructor, Student, dan Public Viewer.
- 📚 **Course & Curriculum Builder**: Struktur hierarki (Course -> Modul -> Bab -> Materi/Lesson).
- 🎬 **Multi-Format Content**: Video (YouTube, Vimeo, S3/MinIO), Markdown text, slide PDF, dan coding snippet.
- 📝 **Interactive Quizzes & Assignments**: Pilihan ganda, esai, kuis interaktif dengan penilaian otomatis.
- 💬 **Discussion & Community**: Forum tanya jawab terintegrasi di setiap materi belajar.
- 🎓 **Automated Certificate Generation**: Sertifikat kelulusan otomatis dalam format PDF dengan QR Code verifikasi keaslian.
- 📊 **Analytics Dashboard**: Tracking progress belajar siswa dan statistik penyelesaian untuk instruktur.

---

## 🛠️ Rekomendasi Tech Stack

- **Frontend**: Next.js (App Router), Tailwind CSS, shadcn/ui
- **Backend / API**: Node.js (NestJS / Hono / Express) atau Go / Python FastAPI
- **Database**: PostgreSQL dengan Prisma ORM / Drizzle ORM
- **Cache & Message Queue**: Redis, BullMQ
- **Object Storage**: S3-Compatible (MinIO untuk self-hosted / Cloudflare R2 / AWS S3)
- **Deployment**: Docker, Docker Compose

---

## 🗺️ Roadmap & Project Plan
Detail fase pengembangan, struktur database, dan arsitektur dapat dilihat di [PLAN.md](PLAN.md).

---

## 🤝 Kontribusi
Kami sangat menyambut kontribusi dari komunitas! Silakan baca [CONTRIBUTING.md](CONTRIBUTING.md) untuk panduan memulai.

---

## 📄 Lisensi
Didistribusikan di bawah Lisensi MIT. Lihat [LICENSE](LICENSE) untuk informasi lebih lanjut.
