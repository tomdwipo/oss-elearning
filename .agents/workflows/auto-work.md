---
description: Jalankan satu tiket atau task open-source end-to-end sampai 'merged' & closed tanpa berhenti minta konfirmasi: putuskan apakah task butuh kerja kode/dokumen di repo ini, rencanakan, kerjakan, verifikasi via 7 gerbang lokal, PR via GitHub CLI, dan transisi status. Pakai saat user memberikan link/nomor issue GitHub, link Jira, atau ID task PRD dan minta dikerjakan otomatis.
---

# auto-work Command (OpenCampus Mobile Edition)

Fully autonomous single-ticket/task pipeline: decide whether a task needs code or documentation work in **this**
repository (`tomdwipo/oss-elearning`), and if it does, carry it end-to-end to "merged & closed" without stopping for confirmation.

## Usage

```
/auto-work {github-issue-number-or-url}
/auto-work {prd-task-id-or-usecase}
/auto-work {jira-link-or-key}
```

`$ARGUMENTS` accepts:
1. **GitHub Issue reference**: `#12`, `12`, or full URL (`https://github.com/tomdwipo/oss-elearning/issues/12`).
   - Parse issue number with `(?:issues/|#)?(\d+)`.
   - Fetch context via GitHub CLI: `gh issue view <number> --json title,body,comments,labels,assignees,milestone`.
2. **PRD / Roadmap Task ID**: `FR-1.1`, `UC-01`, `Modul-1-Navigasi`, or path to task spec in `.docs/00_fase/01_PRD.md`.
   - Parse section and acceptance criteria directly from `.docs/00_fase/01_PRD.md` and related module specs.
3. **Jira reference**: e.g. `OCM-12` or browse URL (`https://.../browse/OCM-12`) if working with Jira issues.
   - Fetch issue via `mcp__atlassian__getJiraIssue` with `fields: ["*all", "comment"]` if Jira MCP is available.

Reject and ask only if neither a GitHub issue, Jira key, nor a PRD task reference is found in the input.

---

## Ground rule 0: WAJIB based on data, BUKAN asumsi

Ini mendahului semua aturan lain di file ini. Kalau bertabrakan dengan "jangan berhenti di tengah jalan",
**aturan ini yang menang** — lebih baik melapor "belum dicek" daripada mengarang hasil supaya laporannya kelihatan lengkap.

**Setiap klaim faktual harus punya perintah yang menghasilkannya, dan keluarannya ditempel.**
Yang dihitung klaim faktual: keadaan runtime/lingkungan, hasil build atau linter, hasil test (berapa test, merah/hijau),
isi response API/JSON, isi file, status issue/PR, apakah komponen/fungsi benar-benar ada di kode.
Kalau lu tidak bisa menunjuk perintah yang lu jalankan buat sebuah kalimat, kalimat itu asumsi —
tandai `[ASSUMED]` atau jangan ditulis sama sekali.

**Tiga cara klaim palsu lolos (belajar dari insiden nyata):**

1. **Template yang sudah lengkap sama alasannya.** Menyalin kalimat template ("gerbang X tidak dijalankan karena Y")
   tanpa benar-benar menjalankan perintah pemeriksaannya. Bentuk output yang rapi **bukan** bukti. Jangan menukar sinyal dengan stempel.
2. **Exit code yang ketutup pipe.** Perintah seperti `npm test | tail -20` atau `flutter test | grep ...` mengembalikan
   exit code **`tail`** atau **`grep`**, bukan runner test! `TEST FAILED` bisa datang bareng `exit 0`.
   Simpan ke file lalu cek exit code runner-nya secara mandiri:
   `npm test > test.log 2>&1; echo "EXIT=$?"` atau `flutter test > test.log 2>&1; echo "EXIT=$?"`.
3. **Artefak basi yang kebaca seperti hasil baru.** File coverage/test report lama di cache tidak terhapus jika task gagal.
   Selalu pastikan hasil lama dibersihkan sebelum menjalankan verifikasi penentu.

### Masih asumsi atau hipotesis? Bikin POC dulu, jangan dilaporkan sebagai kesimpulan

Kalau sebuah pertanyaan tidak bisa dijawab dari kode/config/log yang ada — "apakah YouTube Iframe API support callback ini?",
"apakah local storage persist setelah refresh?", "apakah layout ini overflow di 375x812?", "apakah komponen ini sesuai Design Tokens?" —
**jangan dijawab dari ingatan atau penalaran.** Bikin POC/spike sekecil mungkin, jalankan, lalu laporkan **hasilnya**, bukan dugaannya.

**POC wajib punya kontrol positif.** Dua probe yang dua-duanya gagal **tidak** membuktikan "fitur tidak didukung" —
bisa saja konfigurasi salah atau parameter keliru. Yang mengubahnya jadi bukti adalah **satu kasus yang berhasil**,
beda satu variabel saja dari kasus yang gagal.

**Hasil POC ikut dilaporkan, termasuk yang menjatuhkan dugaan awal lu.** Kalau POC membuktikan dugaan awal keliru,
itu temuan berharga — tulis di laporan PR dan dokumentasikan di `.docs/common-issues/`.

---

## Ground rule: do not stop mid-run

Command ini dirancang untuk berjalan sampai tuntas secara mandiri (*unattended*).
Jangan berhenti meminta konfirmasi pilihan arsitektur, menyetujui plan, atau mengonfirmasi langkah kecil —
setiap cabang keputusan memiliki **default eksplisit**.

Pemberhentian yang sah hanya ada 3:
1. **Ownership / Repo mismatch** — task ini sepenuhnya untuk repositori lain atau out-of-scope untuk mobile app.
2. **Session usage guardrail** — penggunaan kuota/token mendekati ambang batas kritis. Commit + push apa yang aman, lalu checkpoint.
3. **Delegated run awaiting review** — jika task ini didelegasikan oleh task-giver/reviewer, jeda setelah Step 9 sampai review lolos, lalu lanjutkan ke Step 10 sampai Step 12 tuntas.

---

## Step 1 — Tarik Konteks Lengkap (Pull Full Context)

1. **GitHub Issue / PRD Context**:
   - Jika GitHub Issue: jalankan `gh issue view <id> --json title,body,comments,labels,assignees`.
   - Baca seluruh komentar dan update dari kontributor.
   - Jika PRD task: baca spesifikasi terkait di `.docs/00_fase/01_PRD.md`, `.docs/01_fase/`, dan `.docs/02_fase_paralel/`.
2. **Design & UI References (Figma)**:
   - **Jalur Utama (Cloud Figma)**: Jika ada URL Figma, gunakan MCP Figma (`mcp__figma__get_file_nodes`, `mcp__figma__get_image_render`).
   - **Jalur Offline (Local .fig Export)**: Jika file Figma lokal tersedia (`design_system/assets/Design System _ Ui Kit Free (Community).fig`),
     gunakan skill `fig-decode` (`.agents/skills/fig-decode/SKILL.md`) untuk mengekstrak node tree JSON tanpa membuka browser.
   - **Design System Specs**: Cek token warna, tipografi, dan komponen di [`.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md`](../../.docs/design_system/DESIGN_SYSTEM_UIKIT_FREE_COMPONENTS.md)
     dan prototype di [`.docs/02_fase_paralel/01_module/uiux/`](../../.docs/02_fase_paralel/01_module/uiux/).
3. **Asset Manifest Check**:
   - Daftarkan semua icon/ilustrasi yang dibutuhkan layar/fitur tersebut.
   - Cek apakah asset sudah ada di direktori project (misal `assets/icons/`, `design_system/assets/`).
   - Buat daftar asset yang belum ada untuk disiapkan sebelum koding dimulai.

---

## Step 2 — Reality Check: Apakah Task Ini Butuh Kode di Sini?

1. **Reproduksi terhadap kode saat ini**:
   - Telusuri alur kode/arsitektur yang relevan.
   - Konfirmasi apakah bug/gap/kebutuhan fitur benar-benar belum terimplementasi.
2. **Cek mitigasi atau implementasi yang sudah ada**:
   - `git log`, `grep`, cek dokumen `.docs/common-issues/` dan commit terdahulu.
3. **Keputusan**:
   - **Tidak butuh / Sudah selesai / Out-of-scope**:
     - Jangan ubah kode.
     - Tambahkan komentar penjelasan di issue GitHub (`gh issue comment <id> --body "..."`).
     - Tutup issue jika relevan (`gh issue close <id>`).
     - **Berhenti (terminal outcome)** dan laporkan temuan ke user.
   - **Butuh**: Lanjutkan ke langkah berikutnya.
   - **Sebagian sudah selesai**: Lanjutkan dengan cakupan yang dipersempit hanya pada celah yang belum terpenuhi.

---

## Step 3 — Tentukan Target Branch & Buat Branch Kerja

1. **Target Branch**:
   - Default target branch repo ini adalah `main`.
   - Jika ada branch epic/milestone tertentu (misal `epic/module-1-curriculum`), targetkan ke branch tersebut.
2. **Sinkronisasi & Checkout**:
   ```bash
   git fetch origin
   git checkout main
   git pull origin main
   ```
3. **Nama Branch Baru** (mengikuti panduan `CONTRIBUTING.md`):
   - Fitur baru: `feature/{task-id}-{slug}` (contoh: `feature/gh-12-curriculum-tabs`)
   - Perbaikan bug: `fix/{task-id}-{slug}` (contoh: `fix/gh-15-youtube-autoplay`)
   - Dokumentasi: `docs/{task-id}-{slug}`
   ```bash
   git checkout -b feature/{task-id}-{slug}
   ```

---

## Step 4 — Tandai Status "In Progress"

- Pada GitHub Issue: pasang label `in-progress` dan assign ke `tomdwipo`:
  ```bash
  gh issue edit <id> --add-label "in-progress" --add-assignee "@me"
  ```
- Jika menggunakan Jira: ubah transisi issue menjadi **In Progress**.

---

## Step 5 — Plan → Design Breakdown → Implementasi

Jalankan secara sekuensial tanpa berhenti meminta konfirmasi:

1. **Diagnosa & Rencana (Plan-First)**:
   - Pilih opsi yang paling sederhana, paling kuat bukti teknisnya, dan memiliki dependensi paling minim.
   - Tulis diagnosa, data model, dan arsitektur layer (Presentation → State/Controller → Repository → Storage/Analytics).
2. **Design Breakdown**:
   - Rinci langkah implementasi dalam blok BEFORE / AFTER.
   - Tetapkan kontrak data (JSON schema, model class, mock fixtures).
   - Petakan Asset Manifest yang dibutuhkan.
3. **Implementasi Bertahap**:
   - Implementasikan kode secara modular (pisahkan komponen UI, state management, dan repository data).
   - Jangan tumpuk seluruh perubahan dalam 1 file raksasa.

---

## Step 6 — Verifikasi: 7 Gerbang Loop Lokal (Mandatori KMP/CMP)

Repository ini berbasis **Kotlin Multiplatform (KMP)** dan **Compose Multiplatform (CMP)** (Android & iOS).
Urutan gerbang dari yang paling cepat/murah ke yang paling mahal.
Berhenti dan perbaiki di gerbang pertama yang merah sebelum melanjutkan.
**Keluaran tiap gerbang yang dijalankan WAJIB ditempel ke laporan.**

| # | Gerbang | Deskripsi & Perintah (KMP / CMP) | Kapan Wajib |
|---|---|---|---|
| **1** | **Linters & Static Analysis** | Cek linter Gradle & KMP configuration tanpa error/warning baru: `./gradlew check -x test` atau `./gradlew composeApp:lintDebug`. | Tiap perubahan kode |
| **2** | **Data & API Contracts** | Validasi skema kurikulum JSON & serialisasi `@Serializable`, kontrak Trace ID W3C `traceparent` / `trc_<action>_<timestamp>_<hex>`: `./gradlew test --tests "*Curriculum*" --tests "*TraceId*"`. | Perubahan data/API |
| **3a** | **Unit Tests** | Jalankan automated test suite di `commonTest` (Android + Native/JVM): `./gradlew test` (atau `./gradlew composeApp:allTests`). Buktikan semua test hijau. | Selalu |
| **3b** | **Negative Control** | Rusakkan 1 logika yang diuji → buktikan **MERAH** (test gagal) → kembalikan → buktikan **HIJAU**. | **Tiap unit test baru** |
| **4** | **Component & UI Viewport** | Uji rendering Compose Multiplatform pada standar viewport 375x812 dp (9:19.5), verifikasi Light/Dark mode, token desain UI Kit. **Wajib ambil screenshot resolusi tinggi dan rekaman video (.mp4)** untuk setiap interaksi/layar baru. | Perubahan UI/Layout |
| **5** | **Performance & Build Artifacts** | Build binary release/debug untuk memverifikasi packaging: `./gradlew assembleDebug` (audit ukuran bundle APK di `composeApp/build/outputs/apk/debug/` dan beban memori/resource). | Fitur baru / optimasi |
| **6** | **Security & Secret Hygiene** | `git diff origin/main` — pastikan tidak ada API Key YouTube tanpa proteksi, kredensial rahasia, atau hardcoded auth token yang ter-commit. | Selalu |

### Aturan Khusus Gerbang 3b — Negative Control (Anti-Ilusi Test)
Test yang hijau sebelum dan sesudah kode diubah bukan bukti test bekerja.
- Rusakkan 1 kondisi (misal threshold video complete diubah dari `85` ke `101`, atau mutasi formula progress `+ 1`).
- Jalankan test dan tangkap kegagalan (**MERAH**).
- Pulihkan kondisi dan pastikan kembali (**HIJAU**).
- Laporkan rasio: `N/N → (N-1)/N → N/N` beserta **nama test method yang merah**.

### Aturan Khusus Gerbang yang Tidak Dijalankan
Jika suatu gerbang tidak dapat dijalankan (misal gerbang 5 belum memiliki benchmark script):
- **Laporkan apa adanya beserta perintah dan outputnya.**
- DILARANG menyalin template alasan tanpa menjalankan perintah pembuktiannya. Diam terbaca sebagai "sudah dicek".

---

## Step 7 — Harvest Learnings & Arsipkan Bukti

1. **Dokumentasikan Gotchas**:
   - Jika menemukan kendala non-obvious atau keputusan arsitektural penting selama pengerjaan,
     catat ke dalam [`.docs/common-issues/`](../../.docs/common-issues/README.md).
2. **Arsipkan Bukti Verifikasi Lengkap**:
   Setiap task yang mengubah kode/UI membuat folder arsip bukti di `.docs/evidence/{task-id-or-slug}/` (atau `.docs/evidence/YYYY/MM/DD/NN-{task-id}-{slug}/`):
   ```
   .docs/evidence/{task-id-or-number}/
   ├── README.md               # Ringkasan bukti, tabel lingkungan, rasio negative control
   ├── console-evidence.txt    # Log eksekusi test & linter
   ├── demo.mp4                # Video walkthrough eksekusi/interaksi UI nyata (WAJIB jika ada perubahan UI)
   └── screenshots/            # Tangkapan layar PNG viewport 375x812 dp (WAJIB jika ada perubahan UI)
       ├── 01_screen_initial.png
       ├── 02_screen_interaction.png
       └── ...
   ```

---

## Step 8 — Commit & Push

- **Author Identity**: `Tommy Dwi Putranto` (`tommyputranto1@gmail.com`).
- **ATURAN MUTLAK ATRIBUSI AI**:
  - **DILARANG** menambahkan atribut AI ("Generated with Claude", "Co-Authored-By: Claude/Gemini/AI").
  - **Gunakan format commit konvensional oleh LAYER**:
    - `feat(LAYER): deskripsi fitur`
    - `fix(LAYER): deskripsi perbaikan`
    - `docs(LAYER): deskripsi pembaruan dokumentasi`
    - `test(LAYER): penambahan unit test dan kontrol negatif`
- **Referensi Issue yang Netral**:
  - Gunakan `Refs #12` di body commit. Hindari kata penutup otomatis seperti `Fixes #12` atau `Closes #12` jika membutuhkan review terlebih dahulu.
- **Push ke Remote**:
  ```bash
  git push -u origin feature/{task-id}-{slug}
  ```

---

## Step 9 — Buat Pull Request (PR)

Buat PR ke target branch menggunakan GitHub CLI:

```bash
gh pr create --base main --head feature/{task-id}-{slug} \
  --title "feat(LAYER): {judul-fitur} (Refs #{task-id})" \
  --body-file /tmp/pr-description.md
```

**Deskripsi PR WAJIB memuat bukti nyata (Reviewer tidak boleh disuruh menebak):**
1. **Tautan issue** yang dikerjakan (`Refs #...`).
2. **Ringkasan perubahan teknis**.
3. **Tabel 7 Gerbang Verifikasi** lengkap dengan hasil eksekusi perintah nyata (`./gradlew ...`).
4. **Rasio Negative Control** (`N/N → (N-1)/N → N/N`) dan nama unit test yang diuji.
5. **Bukti Visual Tangkapan Layar (Screenshots) Tersemat Langsung**:
   - Gambar WAJIB tampil langsung (*inline rendering*) di web GitHub PR tanpa harus download:
     - Gunakan URL GitHub mentah atau format Markdown gambar:
       `![Nama Layar](https://raw.githubusercontent.com/tomdwipo/oss-elearning/main/.docs/evidence/.../screenshots/nama.png)`
     - Tampilkan tabel galeri screenshot (misal: State Awal, Interaksi/Checklist, Pencarian, Mode Gelap/Terang).
6. **Video Walkthrough Tersemat (Video Recording)**:
   - Sediakan link langsung ke video rekaman alur nyata (`demo.mp4`):
     - `🎬 Video Demo: [Tonton demo.mp4](https://github.com/tomdwipo/oss-elearning/raw/main/.docs/evidence/.../demo.mp4)`
     - Dan/atau sematkan video player tag `<video src="https://github.com/tomdwipo/oss-elearning/raw/main/.docs/evidence/.../demo.mp4" controls width="360"></video>`.
7. **Tautan ke folder arsip bukti** di [`.docs/evidence/`](../../.docs/evidence/).

Update label issue menjadi `in-review`:
```bash
gh issue edit <id> --remove-label "in-progress" --add-label "in-review"
```

---

## Step 10 — Aturan Merge (Merge Policy)

### Kapan Boleh Merge?
1. **Delegated Run** (dijalankan di bawah delegasi/reviewer):
   - Jeda setelah Step 9. Tunggu verdict review (**passed**).
   - Setelah approved, agen langsung menjalankan merge.
2. **Self-Directed Run** (dijalankan langsung oleh pemilik repo):
   - Jika perubahan adalah **docs-only** (`.docs/**` saja): boleh langsung di-merge mandiri.
   - Jika perubahan kode: tunggu instruksi/persetujuan user atau jalankan merge jika user meminta eksekusi end-to-end sampai tuntas.

### Eksekusi Merge:
```bash
gh pr merge <pr-number> --merge --delete-branch
```
Jika `gh pr merge` gagal karena konflik, selesaikan konflik secara lokal, jalankan ulang gerbang verifikasi, commit, dan push kembali.

---

## Step 11 — Konfirmasi Merge Mendarat di Target Branch

Buktikan merge benar-benar mendarat di branch target (`main`):

```bash
git checkout main
git pull origin main
git log --oneline -3 origin/main
```
Pastikan commit SHA PR sudah menjadi leluhur (*ancestor*) dari `main` dan working tree bersih.

---

## Step 12 — Close Out & Pembaruan Dokumentasi

1. **Tutup GitHub Issue**:
   ```bash
   gh issue close <id> --comment "Selesai di-merge ke main via PR #<pr_id> (Commit: <sha>). Hasil verifikasi 7 gerbang dan negative control terlampir di PR."
   ```
2. **Perbarui Roadmap & PRD**:
   - Jika task ini menyelesaikan item di roadmap, beri centang `[x]` pada:
     - `README.md` (§ Roadmap Pengembangan)
     - `.docs/00_fase/01_PRD.md` (§ Acceptance Criteria / Core Features)
   - Commit pembaruan checklist: `docs(LAYER): update roadmap checklist for #{task-id}` dan push ke `main`.
3. **Laporan Akhir ke User**:
   - Sampaikan apa yang berhasil dikerjakan, nomor PR yang ter-merge, bukti pengujian, dan instruksi bagaimana user dapat menguji hasilnya.
