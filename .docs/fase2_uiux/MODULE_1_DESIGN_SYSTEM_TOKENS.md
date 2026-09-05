# Modul 1: Navigasi Kurikulum — UI Design System & Tokens (GitHub Primer Web v106)

Dokumen ini menetapkan spesifikasi standar **Design System & Design Tokens** untuk **Modul 1: Navigasi Kurikulum** pada aplikasi **OpenCampus Mobile** berdasarkan integrasi berkas desain resmi **GitHub Primer Web** (`Primer Web (Community).fig`), [PRD.md](../fase0/PRD.md), [USE_CASE.md](../fase1/USE_CASE.md), [MODULE_1_USER_FLOW_WIREFLOW.md](./MODULE_1_USER_FLOW_WIREFLOW.md), dan [MODULE_1_LOFI_WIREFRAME.md](./MODULE_1_LOFI_WIREFRAME.md).

---

## 1. Prinsip Desain (Design Principles)

1. **Academic & Software Engineering DNA:** Mengadopsi bahasa visual GitHub Primer yang sudah sangat akrab bagi mahasiswa Teknik Informatika dan pembelajar mandiri coding.
2. **Distraction-Free & Content-First:** Desain monokromatik bersih dengan aksen biru Primer (`#0969DA`) dan hijau keberhasilan (`#1A7F37`), memastikan fokus penuh pada materi silabus.
3. **Ergonomic Touch Targets:** Komponen interaktif (UnderlineNav tabs, kartu mata kuliah, dan Primer Checkbox) memenuhi target sentuh minimum **48 x 48 dp**.
4. **Accessible Contrast (WCAG 2.1 AA):** Rasio kontras teks utama terhadap latar belakang melebihi 14:1 (Lulus kualifikasi WCAG AAA).

---

## 2. Sumber Resmi & Penelusuran Figma (Traceability)

Spesifikasi token dan komponen di bawah ini diekstraksi langsung dari berkas resmi Figma:
* **Source Binary Archive:** [`.docs/fase2_uiux/assets/Primer Web (Community).fig`](./assets/Primer%20Web%20%28Community%29.fig) *(v106, 57.436 nodes, 2.009 variables, 68 pages)*
* **Decoder Pipeline:** [`.agents/skills/fig-decode/`](../../.agents/skills/fig-decode/)
* **Figma Community URL:** [Primer Web Design System](https://www.figma.com/community/file/854767373644076713)

---

## 3. Token Warna Resmi Primer (Color Tokens)

### 3.1. Palet Semantik Utama (Light & Dark Theme)

Berdasarkan ekstraksi 2.009 *Figma Variables* pada koleksi `base`, `bgColor`, `fgColor`, dan `borderColor`:

| Token Name (Primer Variable) | Hex (Light Mode) | Hex (Dark Mode) | Deskripsi & Penerapan Komponen |
| :--- | :--- | :--- | :--- |
| `bgColor/default` | `#FFFFFF` | `#0D1117` | Latar utama kartu mata kuliah dan layar silabus. |
| `bgColor/muted` | `#F6F8FA` | `#161B22` | Latar kanvas aplikasi (*Scaffold Background*), track progress bar. |
| `bgColor/inset` | `#F6F8FA` | `#010409` | Latar belakang kotak pencarian (*Search Input*). |
| `bgColor/accent-emphasis` | `#0969DA` | `#1F6FEB` | Warna aksen aktif (Tab terpilih, tombol primer, link). |
| `bgColor/accent-muted` | `#DDF4FF` | `rgba(56, 139, 253, 0.15)` | Latar badge jurusan, highlight baris yang sedang aktif. |
| `fgColor/default` | `#1F2328` | `#E6EDF3` | Warna teks judul mata kuliah, nama topik pertemuan. |
| `fgColor/muted` | `#656D76` | `#848D97` | Warna teks metadata (durasi video, nama channel kreator, label tab). |
| `fgColor/accent` | `#0969DA` | `#2F81F7` | Warna teks tautan dan status aktif. |
| `fgColor/onEmphasis` | `#FFFFFF` | `#FFFFFF` | Warna teks di atas tombol / tab primer. |
| `borderColor/default` | `#D0D7DE` | `#30363D` | Garis tepi kartu mata kuliah, garis pemisah silabus (*Divider*). |
| `borderColor/muted` | `#D8DEE4` | `#21262D` | Garis pemisah internal komponen. |
| `borderColor/accent-emphasis`| `#0969DA` | `#1F6FEB` | Garis batas elemen saat mendapatkan fokus navigasi. |

### 3.2. Token Status & Progres (Semantic Progress Tokens)

| Token Name | Hex (Light) | Hex (Dark) | Penggunaan |
| :--- | :--- | :--- | :--- |
| `success/emphasis` | `#1F883D` | `#238636` | Latar checkbox saat tercentang `[✔]`. |
| `success/fg` | `#1A7F37` | `#3FB950` | Progress bar saat tuntas 100%, badge "Selesai". |
| `success/muted` | `#DAFBE1` | `rgba(46, 160, 67, 0.15)` | Latar baris silabus materi yang sudah tuntas. |
| `attention/fg` | `#9A6700` | `#D29922` | Indikator progres sedang berjalan. |
| `danger/fg` | `#CF222E` | `#F85149` | Indikator video rusak / gagal muat. |

---

## 4. Token Tipografi (Typography Tokens)

Mengikuti `fontStack/sansSerif` dan `fontStack/monospace` resmi Primer:
* **Font Family Utama (Body & UI):** `-apple-system`, `BlinkMacSystemFont`, `"Segoe UI"`, `"Noto Sans"`, `Inter`, `Helvetica`, `Arial`, `sans-serif`
* **Font Family Kode & Angka Pertemuan:** `'SFMono-Regular'`, `Consolas`, `'Liberation Mono'`, `Menlo`, `'JetBrains Mono'`, `monospace`

| Token Name | Font Size | Line Height | Weight | Penerapan Komponen |
| :--- | :--- | :--- | :--- | :--- |
| `text/title/large` | `20px (1.25rem)` | `28px` | `700 (Bold)` | Header Aplikasi OpenCampus & Judul Silabus MK. |
| `text/title/medium` | `16px (1.0rem)` | `24px` | `600 (SemiBold)`| Judul Mata Kuliah pada Card. |
| `text/body/medium` | `14px (0.875rem)`| `20px` | `600 (SemiBold)`| Judul Topik Pertemuan Silabus. |
| `text/body/small` | `12px (0.75rem)` | `16px` | `400 (Regular)` | Metadata durasi video (`⏱ 18m`), nama kreator YouTube. |
| `text/caption` | `11px (0.6875rem)`| `14px` | `600 (SemiBold)`| Label Badge Counter (`CounterLabel`), persentase progres. |

---

## 5. Token Spasi, Radius & Elevasi

### 5.1. Skala Spasi (Grid 4px / 8px)
* `space/xsmall`: `4px` (Gap teks dengan ikon kecil)
* `space/small`: `8px` (Padding horizontal badge, gap antar baris meta)
* `space/medium`: `16px` (Margin tepi layar mobile, padding dalam kartu)
* `space/large`: `24px` (Jarak vertikal antar section)
* `space/xlarge`: `32px` (Margin bawah header utama)

### 5.2. Border Radius
* `borderRadius/small`: `3px` (Kotak Checkbox Primer)
* `borderRadius/default`: `6px` (Kartu Mata Kuliah, Tombol Aksi, Input Field)
* `borderRadius/medium`: `8px` (Container Hero Summary Card)
* `borderRadius/full`: `100px` (Badge Counter, Pill Tab UnderlineNav, Progress Bar Track)

### 5.3. Elevasi & Bayangan (Shadow Tokens)
```css
/* Primer Resting Card */
box-shadow: 0 1px 3px rgba(31, 35, 40, 0.12), 0 1px 2px rgba(31, 35, 40, 0.08);

/* Primer Hover / Floating Card */
box-shadow: 0 3px 6px rgba(31, 35, 40, 0.15), 0 2px 4px rgba(31, 35, 40, 0.12);
```

---

## 6. Pemetaan Komponen Primer untuk Modul 1 (Component Specs)

| Komponen Modul 1 | Komponen Primer Asal | Figma Node GUID | Penerapan di OpenCampus |
| :--- | :--- | :--- | :--- |
| **Tab Semester 1–8** | `UnderlineNav` | `18843:67449` | Tab horizontal dengan *underline indicator* aktif `#0969DA` dan badge counter semester. |
| **Progress Bar** | `ProgressBar` | `16443:62464` | Indikator progres ketercapaian semester dan silabus (track `#F6F8FA`, fill `#1A7F37` / `#0969DA`). |
| **Checkbox Selesai** | `Checkbox` | `15341:46321` | Checkbox `3px radius` dengan centang putih saat aktif (FR-3.2, UC-08). |
| **Counter Badge** | `CounterLabel` | `18959:64970` | Badge pill jumlah pertemuan (`16 Pertemuan`, `4 Selesai`). |
| **Baris Pertemuan Silabus**| `ActionListItem/Default` | `6:25028` | Baris topik pertemuan 1–16 lengkap dengan leading visual dan trailing play action. |
| **Top App Bar** | `Header` | `1378:19` | Navigasi atas dengan tombol kembali `< Back`. |

---

## 7. Format JSON Design Tokens (W3C DTCG Format)

```json
{
  "color": {
    "bgColor": {
      "default": { "value": "#FFFFFF", "type": "color" },
      "muted": { "value": "#F6F8FA", "type": "color" },
      "accentEmphasis": { "value": "#0969DA", "type": "color" },
      "accentMuted": { "value": "#DDF4FF", "type": "color" }
    },
    "fgColor": {
      "default": { "value": "#1F2328", "type": "color" },
      "muted": { "value": "#656D76", "type": "color" },
      "accent": { "value": "#0969DA", "type": "color" }
    },
    "borderColor": {
      "default": { "value": "#D0D7DE", "type": "color" },
      "muted": { "value": "#D8DEE4", "type": "color" }
    },
    "success": {
      "fg": { "value": "#1A7F37", "type": "color" },
      "emphasis": { "value": "#1F883D", "type": "color" },
      "muted": { "value": "#DAFBE1", "type": "color" }
    }
  },
  "borderRadius": {
    "small": { "value": "3px", "type": "borderRadius" },
    "default": { "value": "6px", "type": "borderRadius" },
    "full": { "value": "100px", "type": "borderRadius" }
  },
  "typography": {
    "fontFamily": { "value": "-apple-system, BlinkMacSystemFont, Segoe UI, Noto Sans, Inter, sans-serif", "type": "fontFamily" },
    "fontFamilyMono": { "value": "SFMono-Regular, Consolas, Menlo, monospace", "type": "fontFamily" }
  }
}
```
