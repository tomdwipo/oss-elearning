# Common Issues Catalog (OpenCampus Mobile)

One-stop index for operational gotchas, architectural patterns, and verified engineering findings in `oss-elearning`.
Before investigating an issue or adding a new gotcha, **scan this catalog** first.

---

## Aturan Penggunaan Katalog (Two-Gate System)

1. **Lookup Gate (Awal Tiap Sesi / Investigasi)**:
   - Cocokkan topik atau pertanyaan dengan subjek dalam tabel di bawah.
   - Jika masalah sudah pernah terjadi dan terdokumentasi, ambil solusi dari katalog ini; hindari mencari ulang dari nol (*avoid rediscovery*).
2. **Save Gate (Sebelum Menyelesaikan Task / Step 7 auto-work)**:
   - Periksa apakah sesi menghasilkan temuan non-obvious atau gotcha baru.
   - Jika ada, tambahkan baris baru pada tabel katalog ini dan buat dokumen detailnya di `.docs/common-issues/<slug>.md`.

---

## Indeks Masalah Umum

| # | Subjek | Gejala / Topik | Catatan & Solusi Singkat | Lokasi Detail |
|---|---|---|---|---|
| **1** | `test-runner` | Exit code 0 padahal test gagal saat pakai pipe | `runner \| tail` mengembalikan exit code `tail`. Simpan output ke file: `cmd > run.log 2>&1; echo "EXIT=$?"`. | [anti-pipe-exit-code.md](./anti-pipe-exit-code.md) |
| **2** | `test-quality` | Ilusi test hijau (test tidak pernah gagal saat kode dirusak) | Wajib jalankan Gerbang 3b (Negative Control). Mutasi kode, buktikan merah, pulihkan, catat rasio `N/N → (N-1)/N → N/N`. | [negative-control-discipline.md](./negative-control-discipline.md) |
| **3** | `figma-assets` | File Figma cloud tidak dapat diakses tanpa token | Gunakan skill `fig-decode` untuk membaca berkas lokal `.fig` (`design_system/assets/...`) via node CLI `kiwi-schema`. | [offline-fig-decode.md](./offline-fig-decode.md) |
| **4** | `youtube-embed` | Player error / video private / YouTube TOS compliance | Pastikan URL fallback "Video sedang diperbarui" dan tombol "Tonton di YouTube" untuk memenuhi atribusi TOS. | [youtube-player-fallback.md](./youtube-player-fallback.md) |
| **5** | `viewport-layout`| Overflow pada layar mobile 375x812 dp | Standar layout mengikuti rasio 9:19.5 (375x812 dp) dengan safe area insets untuk dynamic notch dan home indicator. | [mobile-viewport-standard.md](./mobile-viewport-standard.md) |
| **6** | `kmp-gradle` | JVM 64KB UTF-8 string limit & Gradle 9 incompatible with KMP | Chunk string literal JSON > 64KB dan standarisasi Gradle 8.11.1 + Google Maven mirror. | [kmp-gradle-string-constant-limit.md](./kmp-gradle-string-constant-limit.md) |

