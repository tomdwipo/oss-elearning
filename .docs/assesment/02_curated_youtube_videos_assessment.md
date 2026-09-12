# Curated YouTube Videos & Embeddability Assessment

> **Audit Target:** [`composeApp/src/commonMain/composeResources/files/curriculum_it_semesters.json`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/composeResources/files/curriculum_it_semesters.json)  
> **Component Target:** [`PlatformVideoPlayer.android.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/androidMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.android.kt) & [`PlatformVideoPlayer.ios.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/iosMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.ios.kt)  
> **Audited Repository:** `oss-elearning` (OpenCampus Mobile)  
> **Assessment Date:** 2026-09-12  
> **Status Verifikasi:** 100% Empirically Tested via YouTube oEmbed API (HTTP 200 OK)  

---

## 1. Executive Summary

Pada saat pengujian visual dan evaluasi rekaman demo aplikasi ([`.docs/evidence/02/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/02/demo.mp4)), ditemukan bahwa area pemutar video ([`PlatformVideoPlayer`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt)) hanya menampilkan kotak persegi panjang gelap berlatar `#1E1E2E` tanpa konten video YouTube yang terputar.

Berdasarkan investigasi berbasis bukti (data-driven), ditemukan akar permasalahan bahwa **seluruh 480 topik kurikulum** pada berkas [`curriculum_it_semesters.json`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/composeResources/files/curriculum_it_semesters.json) masih menggunakan **placeholder / mock string** (misalnya `t101_01_v`, `t101_03_v`, `vid_cs102_16`), bukan **ID video YouTube 11-karakter yang valid**.

Dokumen ini menyajikan:
1. Analisis akar masalah teknis kegagalan render pemutar video.
2. Hasil pencarian, kurasi, dan verifikasi otomatis video perkuliahan/tutorial aktif berbahasa Indonesia dari kreator terpercaya (*Web Programming UNPAS*, *Kelas Terbuka*, *Programmer Zaman Now*, perkuliahan resmi *Informatika ITB*, dll.).
3. Matriks lengkap video aktif yang telah lolos uji *oEmbed API* (status HTTP 200 & hak semat/embed diizinkan).
4. Rekomendasi langkah pembaruan dataset kurikulum dan pengujian ulang.

---

## 2. Root Cause Analysis (Akar Masalah Kegagalan Render Video)

### 2.1. Pemeriksaan Struktur Data Eksisting
Pemeriksaan menyeluruh terhadap dataset kurikulum menghasilkan data statistik berikut:
- **Total Semester:** 8 semester.
- **Total Mata Kuliah:** 30 mata kuliah.
- **Total Topik Silabus:** 480 pertemuan topik (16 pertemuan per mata kuliah).
- **Format Video ID Saat Ini:** 100% menggunakan pola mock seperti `t101_01_v` atau `vid_cs{course_id}_{meeting_no}`.

Contoh entri data lama:
```json
{
  "id": "t101_03",
  "no": 3,
  "title": "Percabangan: If, Else If, Nested If",
  "videoId": "t101_03_v",
  "duration": "30m",
  "channel": "Kelas Terbuka"
}
```

### 2.2. Dampak pada Android WebView & iOS WKWebView
Komponen [`PlatformVideoPlayer`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/player/PlatformVideoPlayer.kt) memuat YouTube Iframe API dengan URL dasar:
```html
<script src="https://www.youtube.com/iframe_api"></script>
...
player = new YT.Player('player', {
    videoId: '$videoId', // Nilai mock: "t101_03_v"
    ...
});
```

Ketika nilai `$videoId` bukan merupakan ID 11-karakter resmi YouTube:
1. Server YouTube Iframe mengembalikan kode galat atau tidak dapat menemukan media stream.
2. Area `#player` gagal melakukan rendering frame pertama maupun thumbnail poster video.
3. Kontainer WebView hanya memperlihatkan warna latar belakang CSS `body { background-color: #1E1E2E; }`.
4. Saat pengguna menekan tombol *"Tonton di YouTube"*, aplikasi membuka URL tidak valid `https://www.youtube.com/watch?v=t101_03_v` yang menghasilkan halaman *"This video isn't available anymore"*.

---

## 3. Metodologi Pengujian & Validasi Video Aktif

Untuk memastikan video yang direkomendasikan **benar-benar aktif**, **publik**, dan **diizinkan untuk disematkan (embeddable)** sesuai YouTube Developer API TOS, dilakukan pengujian otomatis menggunakan endpoint resmi **YouTube oEmbed**:
```http
GET https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v={videoId}&format=json
```

**Kriteria Lolos Uji (Pass Criteria):**
1. **HTTP Status Code = 200 OK**: Video bersifat publik dan tidak dihapus/di-private oleh pemiliknya.
2. **Embed Permission Granted**: Tidak mengembalikan galat `401 Unauthorized` atau `403 Forbidden` (indikator larangan penyematan di luar platform YouTube).
3. **Metadata Matching**: Judul video dan nama kanal sesuai dengan topik akademik silabus Informatika.

---

## 4. Matriks Kurasi Video Aktif Semester 1

### 4.1. Mata Kuliah cs101: Algoritma & Pemrograman Dasar (16 Pertemuan Lengkap)

Seluruh 16 topik berikut telah diverifikasi 100% aktif dan dapat disematkan ke dalam aplikasi:

| No | Topik Silabus | Video ID (11 Karakter) | Kanal Resmi | Judul Video YouTube Aktual | Status oEmbed | Tautan YouTube |
|:---:|---|:---:|---|---|:---:|:---:|
| **01** | Konsep Dasar Logika & Flowchart | `jGyYuQf-GeE` | Web Programming UNPAS | *Apa itu Pemrograman?* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=jGyYuQf-GeE) |
| **02** | Variabel, Tipe Data & Input Output | `1FAnrYu7LCM` | Web Programming UNPAS | *NILAI DAN TIPE DATA PADA JAVASCRIPT* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=1FAnrYu7LCM) |
| **03** | Percabangan: If, Else If, Nested If | `-9IyBehKm4g` | Kelas Terbuka | *Belajar C++ [Dasar] - 14 - if else* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=-9IyBehKm4g) |
| **04** | Perulangan: For, While, Do-While | `ZeqJewFm7zc` | Kelas Terbuka | *Belajar C++ [Dasar] - 21 - For Loop* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=ZeqJewFm7zc) |
| **05** | Fungsi & Modularitas Kode | `iTUO1DWVUv8` | Kelas Terbuka | *Belajar C++ [Dasar] - 26 - Fungsi (Pengenalan)* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=iTUO1DWVUv8) |
| **06** | Rekursi & Call Stack Visualizer | `-JV_YbJR1GY` | Web Programming UNPAS | *FUNCTION pada JAVASCRIPT : REKURSIF* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=-JV_YbJR1GY) |
| **07** | Array 1 Dimensi & Manipulasi Indeks | `8WhUADLI4RQ` | Kelas Terbuka | *Belajar C++ [Dasar] - 42 - Pendahuluan Array* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=8WhUADLI4RQ) |
| **08** | Review & Latihan Logika Pemrograman | `TanFwAcHBqE` | Web Programming UNPAS | *10 Tips Melatih LOGIKA PEMROGRAMAN agar Ngoding makin Sat-Set🧑‍💻🤩* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=TanFwAcHBqE) |
| **09** | String Manipulasi & Substring | `OpodtuA0xyI` | Kelas Terbuka | *Belajar C++ [Dasar] - 53 - Akses Substring* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=OpodtuA0xyI) |
| **10** | Dasar Alokasi Memori & Pointer | `O1kWNj5Ikro` | Kelas Terbuka | *Belajar C++ [Dasar] - 38 - Pointer* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=O1kWNj5Ikro) |
| **11** | Struct & Record Data Bentukan | `ELCI_U4OF5w` | Kelas Terbuka | *Belajar C++ [Dasar] - 56 - Struct* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=ELCI_U4OF5w) |
| **12** | Algoritma Searching: Linear & Binary | `QFC4DXvRu8o` | Kelas Terbuka | *Belajar C++ [Dasar] - 49 - Search Array \| std library* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=QFC4DXvRu8o) |
| **13** | Algoritma Sorting: Bubble & Selection | `G0cml-wvaBc` | Kelas Terbuka | *Belajar C++ [Dasar] - 48 - Sort Array \| std library* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=G0cml-wvaBc) |
| **14** | Analisis Kompleksitas Dasar (Big O) | `XgKfcZctwA8` | Backend Magang | *Big O Notation - Memahami Kompleksitas Algoritma dalam Pemrograman* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=XgKfcZctwA8) |
| **15** | File Handling: Baca Tulis File Eksternal | `rS-mnrY4Djw` | Kelas Terbuka | *Belajar C++ [Dasar] - 64 - Menulis File Eksternal \| ofstream* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=rS-mnrY4Djw) |
| **16** | Proyek Akhir: Mini Aplikasi CLI / Game | `Mmf3SXHifBw` | Web Programming UNPAS | *MEMBUAT GAME SUWIT JAWA DENGAN JAVASCRIPT* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=Mmf3SXHifBw) |

---

### 4.2. Mata Kuliah cs102: Matematika Diskrit (Topik Pilihan Aktif)

| No | Topik Silabus | Video ID | Kanal Resmi | Judul Video YouTube Aktual | Status oEmbed | Tautan YouTube |
|:---:|---|:---:|---|---|:---:|:---:|
| **01** | Logika Proposisi & Tabel Kebenaran | `U5eWAywK1Mo` | Les Kak ir | *Cara Mudah Menentukan Tabel Kebenaran Operasi Logika* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=U5eWAywK1Mo) |
| **02** | Operasi Himpunan | `LrRRt3H4WdY` | Raja MTK | *OPERASI HIMPUNAN \| BELAJAR MATEMATIKA* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=LrRRt3H4WdY) |
| **03** | Relasi dan Fungsi | `sTN31ZFHQ6M` | Ika Qutami (UNAIR) | *Matematika Diskrit: Relasi & Fungsi Part 1* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=sTN31ZFHQ6M) |
| **04** | Induksi Matematika | `R4zgcsAQSh0` | Zero Tutorial | *PAHAM Induksi Matematika dalam 10 MENIT !* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=R4zgcsAQSh0) |
| **05** | Kombinatorika (Permutasi & Kombinasi) | `rskbSgD2eXM` | Zero Tutorial | *PAHAM Permutasi & Kombinasi dalam 15 MENIT !* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=rskbSgD2eXM) |
| **06** | Teori Graf (Dasar & Representasi) | `7x6dc4G-mkY` | Informatika ITB | *Matdis 30: Graf (Bagian 1-01: Apa itu Graf)* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=7x6dc4G-mkY) |
| **07** | Pohon (Tree & Spanning Tree) | `OJ544wy2yVk` | Informatika ITB | *Matdis 38: Pohon (Bagian 1-01: Definisi Pohon)* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=OJ544wy2yVk) |

---

### 4.3. Mata Kuliah cs103: Pengantar Teknologi Informasi (Topik Pilihan Aktif)

| No | Topik Silabus | Video ID | Kanal Resmi | Judul Video YouTube Aktual | Status oEmbed | Tautan YouTube |
|:---:|---|:---:|---|---|:---:|:---:|
| **01** | Pengenalan Komponen Komputer | `rxmEFnky0_w` | Dyah Puteria Wati | *Komponen pada Komputer \| Pengantar Teknologi Informasi* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=rxmEFnky0_w) |
| **02** | Perangkat Keras (Hardware) | `ytodE2dxvn8` | Yafa Asathin | *PENGANTAR TEKNOLOGI INFORMASI (HARDWARE) #2* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=ytodE2dxvn8) |
| **03** | Konsep Sistem Operasi | `rRFZ5hExKI8` | Sekilat Info | *Sistem Operasi Komputer Dijelaskan* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=rRFZ5hExKI8) |
| **04** | Jaringan Komputer & Internet | `_fl7wWbV604` | Kok Bisa? | *Kayak Gimana Bentuk Internet Sebenernya?* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=_fl7wWbV604) |
| **05** | Konsep Basis Data (Database) | `VxQRXBAJ58Q` | Dyah Puteria Wati | *Pengenalan Tentang Konsep Database \| PTI* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=VxQRXBAJ58Q) |
| **06** | Keamanan Siber (Cyber Security) | `lWvR3WEf5vQ` | Fajar Hendyanto | *Cyber Security Secara Singkat Pengantar Teknologi Informasi* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=lWvR3WEf5vQ) |
| **07** | Pengenalan Kecerdasan Buatan (AI) | `UYp32dGr5X8` | Kuliah Informatika | *Mengenal Artificial Intelligence - Kuliah AI #01* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=UYp32dGr5X8) |

---

### 4.4. Mata Kuliah cs104: Organisasi & Arsitektur Komputer (Topik Pilihan Aktif)

| No | Topik Silabus | Video ID | Kanal Resmi | Judul Video YouTube Aktual | Status oEmbed | Tautan YouTube |
|:---:|---|:---:|---|---|:---:|:---:|
| **01** | Pengantar Organisasi Komputer | `zB3pbkrKWtE` | Istas Pratomo | *Kuliah 1 - Arsitektur dan Organisasi Komputer - Introduction* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=zB3pbkrKWtE) |
| **02** | Struktur Internal CPU & ALU | `0jHp0VEz-B8` | Adisty Ismi M. | *Struktur & fungsi internal CPU, ALU, CU & register* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=0jHp0VEz-B8) |
| **03** | Gerbang Logika Digital | `LiqmFy0GRnw` | Guru Digital | *Apa itu gerbang logika ?* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=LiqmFy0GRnw) |
| **04** | Memori Utama & Cache Memory | `hZWx1KmLSYc` | Istas Pratomo | *Arsitektur dan Organisasi Komputer - Memori Cache* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=hZWx1KmLSYc) |
| **05** | Siklus Instruksi Mesin | `ko5tV3hdgCw` | M. Hashfi | *Siklus Instruksi di Dalam Sebuah Komputer* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=ko5tV3hdgCw) |
| **06** | Arsitektur RISC vs CISC | `BwHbWr7wTfk` | Rayhan Dzulna | *Mengenal Cara Kerja Komputer CISC dan RISC* | ✅ 200 OK | [Buka Video](https://www.youtube.com/watch?v=BwHbWr7wTfk) |

---

## 5. Kepatuhan YouTube API TOS & Desain Bebas Distraksi

Implementasi penyematan video pada OpenCampus Mobile telah memenuhi standar kepatuhan YouTube API:
1. **Atribusi Kreator Penuh (FR-2.3):**
   - Nama pembuat konten dan kanal YouTube ditampilkan secara eksplisit pada kartu metadata video ([`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L312-L318)).
   - Tombol *"↗ Tonton di YouTube (Atribusi Resmi)"* mengarahkan pengguna langsung ke video asli di peramban atau aplikasi YouTube resmi ([`VideoPlayerScreen.kt`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/VideoPlayerScreen.kt#L414-L435)).
2. **Penyematan Bebas Distraksi:**
   - Menggunakan parameter konfigurasi resmi:
     ```
     autoplay=1&playsinline=1&rel=0&modestbranding=1&controls=1&enablejsapi=1
     ```
   - Menghindari video rekomendasi pihak ketiga di luar kanal yang bersangkutan (`rel=0`), tidak menampilkan kolom komentar, dan tidak menyajikan algoritma short-form (*Shorts*).
3. **Mekanisme Pelaporan Tautan Rusak (FR-3.4 / §5.2):**
   - Apabila salah satu video di atas suatu saat diubah statusnya menjadi privat atau dihapus oleh pemiliknya (menghasilkan YouTube error code 100/101/150), aplikasi otomatis beralih ke tampilan informatif *"Video materi sedang diperbarui"* disertai dialog pelaporan [`ReportBrokenVideoDialog`](file:///Users/tommy-amarbank/Documents/oss-elearning/composeApp/src/commonMain/kotlin/org/opencampus/elearning/ui/components/ReportBrokenVideoDialog.kt).

---

## 6. Rencana Aksi & Rekomendasi Selanjutnya

1. **Patching `curriculum_it_semesters.json`:**
   Menerapkan 16 video ID terverifikasi pada mata kuliah utama Semester 1 (`cs101`: *Algoritma & Pemrograman Dasar*) agar demo interaktif langsung memutar video riil.
2. **Verifikasi Regresi Otomatis:**
   Menjalankan `./gradlew test` untuk memastikan serialisasi JSON dan perhitungan progres kurikulum tetap 100% hijau.
3. **Perekaman Ulang Demo Video (`demo.mp4`):**
   Merekam kembali interaksi pemutaran video materi YouTube asli di emulator Android untuk menggantikan video demo berlayar hitam sebelumnya di [`.docs/evidence/02/demo.mp4`](file:///Users/tommy-amarbank/Documents/oss-elearning/.docs/evidence/02/demo.mp4).
