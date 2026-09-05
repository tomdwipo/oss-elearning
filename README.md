# OpenCampus Mobile 🎓

> **Aplikasi Mobile Pembelajaran Mandiri Terstruktur Berbasis Kurasi Materi Kuliah YouTube**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-green.svg)](#)
[![Status: MVP v0.1](https://img.shields.io/badge/Status-MVP%20v0.1-orange.svg)](PRD.md)

---

## 📌 Gambaran Proyek

**OpenCampus Mobile** dirancang untuk mengatasi fenomena *curation fatigue* dan distraksi algoritma yang kerap dialami oleh pembelajar mandiri (autodidak). Aplikasi ini mengemas video-video perkuliahan berkualitas dari YouTube ke dalam kurikulum semester yang terstruktur dan terurut (Semester 1–8), dilengkapi dengan pemutar video bebas distraksi dan pelacak progres belajar sederhana.

Dokumen kebutuhan produk lengkap dapat dilihat di 📄 **[PRD.md](PRD.md)**.

---

## 🎯 Tujuan & Metrik Validasi MVP

Tujuan utama versi MVP (v0.1) adalah memvalidasi konsistensi belajar mahasiswa/pembelajar mandiri melalui format kurikulum semester terstruktur.

| Metrik | Target | Alasan Pengukuran |
| :--- | :--- | :--- |
| **Aktivasi** | ≥ 60% pengguna baru memutar minimal 1 video materi | Memastikan kurasi kurikulum menarik perhatian user. |
| **Retensi Hari ke-7 (D7)** | ≥ 25% | Mengukur apakah user kembali untuk melanjutkan materi berikutnya. |
| **Completion Rate** | ≥ 30% menyelesaikan minimal 3 topik di Semester 1 | Validasi apakah struktur kurikulum mengurangi *drop-off* belajar. |

---

## 🚀 Fitur Utama (MVP Scope)

- 📚 **Katalog Jurusan Percontohan**: Pemetaan silabus Semester 1–8 jurusan Teknik Informatika / Ilmu Komputer.
- 🗂️ **Navigasi Hierarki 3 Level**:
  1. Halaman Semester (Tab 1–8)
  2. Halaman Mata Kuliah (Daftar mata kuliah per semester)
  3. Halaman Silabus Topik (Daftar video pertemuan 1–16)
- 🎬 **Player Bebas Distraksi**: Pemutar YouTube tersemat (*embedded*) resmi tanpa rekomendasi sampingan, tanpa komentar, dan tanpa shorts.
- ⏱️ **Progress Tracking Sederhana**: Centang progres otomatis (setelah tonton ≥ 85%) atau manual, disertai persentase progres per semester.
- 🏷️ **Atribusi Kreator**: Nama channel kreator dan tautan langsung *"Tonton di YouTube"*.

---

## 🏗️ Roadmap Pengembangan

- [x] Perumusan Product Requirement Document ([PRD.md](PRD.md))
- [ ] Inisialisasi Mobile App Framework (Flutter / React Native)
- [ ] Implementasi Skema & Data Kurikulum Semester 1–8 (JSON / Local Storage)
- [ ] Pembuatan UI Navigasi (Semester Tab, Course List, Topic Syllabus)
- [ ] Integrasi YouTube Embedded Player & Controller
- [ ] Implementasi Local Progress Tracking & Analytics Events
- [ ] Penanganan Fallback Video Error & Pelaporan Link

---

## 🤝 Kontribusi

Kontribusi dari komunitas sangat terbuka! Silakan baca [CONTRIBUTING.md](CONTRIBUTING.md) untuk panduan berpartisipasi.

---

## 📄 Lisensi

Proyek ini berada di bawah lisensi [MIT License](LICENSE).
