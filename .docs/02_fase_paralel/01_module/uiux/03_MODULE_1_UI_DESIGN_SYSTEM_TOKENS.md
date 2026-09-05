# Modul 1: Navigasi Kurikulum — UI Design System & Tokens (Design System & UI Kit Free)

Dokumen ini menetapkan spesifikasi standar **Design System & Design Tokens** resmi untuk **Modul 1: Navigasi Kurikulum** pada aplikasi **OpenCampus Mobile** berdasarkan berkas desain terpilih **Design System & UI Kit Free (Community)** (`Design System _ Ui Kit Free (Community).fig`), [PRD.md](../../../00_fase/01_PRD.md), [USE_CASE.md](../../../01_fase/01_USE_CASE.md), [01_MODULE_1_USER_FLOW_WIREFLOW.md](./01_MODULE_1_USER_FLOW_WIREFLOW.md), [02_MODULE_1_LOFI_WIREFRAME.md](./02_MODULE_1_LOFI_WIREFRAME.md), dan evaluasi POC [03b_MODULE_1_DESIGN_TOKENS_POC.md](./03b_MODULE_1_DESIGN_TOKENS_POC.md).

---

## 1. Prinsip Desain (Design Principles)

1. **Modern EdTech & Student-Friendly DNA:** Mengadopsi palet warna *Electric Violet* (`#9D3FE7`) dan *Emerald Teal* (`#00B998`) yang modern, dinamis, dan memotivasi mahasiswa dalam menyelesaikan silabus perkuliahan.
2. **Clear Geometric Typography (`Poppins`):** Menggunakan keluarga font Google Sans Geometris *Poppins* dengan kontras hirarki yang tegas antara judul bab, nama mata kuliah, durasi, dan metadata.
3. **8-Point Ergonomic Touch Targets:** Seluruh komponen interaktif (Pill Tabs, Kartu Silabus, Checkbox, dan Tombol Navigasi) mengikuti kelipatan 8px dengan target sentuh minimum **48 x 48 dp**.
4. **Accessible Contrast (WCAG 2.1 AA):** Teks utama (`#1A141F`) di atas kanvas (`#F5F3F7` dan `#FFFFFF`) memiliki rasio kontras 15.2:1 (Lulus kualifikasi WCAG AAA).

---

## 2. Sumber Resmi & Penelusuran Figma (Traceability)

Spesifikasi token dan komponen diekstraksi langsung dari berkas resmi Figma:
* **Source Binary Archive:** [`.docs/design_system/assets/Design System _ Ui Kit Free (Community).fig`](../../../design_system/assets/Design%20System%20_%20Ui%20Kit%20Free%20%28Community%29.fig) *(11.815 nodes, 59 canvases, 1.327 master components)*
* **Complete Token Catalog:** [`.docs/design_system/assets/design_system_uikit_free_complete_tokens.json`](../../../design_system/assets/design_system_uikit_free_complete_tokens.json)
* **Decoder Pipeline:** [`.agents/skills/fig-decode/`](../../../../.agents/skills/fig-decode/)
* **Icon Set:** [`.docs/design_system/assets/icons/uikit/`](../../../design_system/assets/icons/uikit/) (44+ vector icons)
* **Hero Mascot Asset:** [`.docs/design_system/assets/illustrations/uikit_hero.png`](../../../design_system/assets/illustrations/uikit_hero.png)

---

## 3. Token Warna Resmi (Color Tokens)

### 3.1. Palet Brand Utama & Gradient

| Token Name (Figma Style) | Hex Code | Penerapan UI OpenCampus |
| :--- | :--- | :--- |
| `Corporate/Purple` | `#9D3FE7` | Warna primer brand, tombol aksi utama, active pill tab. |
| `Corporate/DarkPurple` | `#602093` | State hover/pressed pada tombol primer, header dark accent. |
| `Corporate/Gradient` | `linear-gradient(135deg, #9D3FE7 0%, #602093 100%)` | Banner hero summary semester, floating CTA button. |
| `Blue` | `#00ACE5` | Warna aksen sekunder untuk tag informasi dan tautan video. |

### 3.2. Token Semantik Status & Progres

| Token Name | Hex Code | Penggunaan |
| :--- | :--- | :--- |
| `Informing/Approval` | `#00B998` | Progress bar 100%, status "Selesai", checkbox pertemuan tercentang `[✔]`. |
| `Informing/Attention` | `#FF9500` | Status "Sedang Berjalan", pengingat jadwal kuis/tugas. |
| `Informing/Error` | `#D51A52` | Error indikator video rusak, validasi formulir. |

### 3.3. Palet Grayscale & Latar Belakang

| Token Name | Hex Code | Deskripsi & Penerapan |
| :--- | :--- | :--- |
| `Grayscale/Black` | `#1A141F` | Teks judul utama mata kuliah, judul topik pertemuan. |
| `Grayscale/Hint_text` | `#4B3A5A` | Teks metadata (durasi video `⏱ 18m`, nama dosen / channel YT). |
| `Grayscale/Border` | `#ABA7AF` | Garis tepi kartu mata kuliah (*Card Outline*), border input. |
| `Grayscale/Spacer` | `#D9D1E0` | Garis pemisah antar seksi, track background progress bar. |
| `Grayscale/Spacer_light`| `#E5E0EB` | Garis pemisah tipis baris pertemuan. |
| `Grayscale/Bg_light_grey`| `#F5F3F7` | Latar belakang aplikasi (*Scaffold Canvas*), tag category chip. |
| `Grayscale/White` | `#FFFFFF` | Latar permukaan kartu silabus, modal dialog, active tab container. |

---

## 4. Token Tipografi (Typography Tokens)

* **Font Family Utama:** `Poppins, -apple-system, BlinkMacSystemFont, sans-serif`
* **Google Fonts CDN:** `https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap`

| Token Name | Font Size | Line Height | Weight | Penerapan Komponen |
| :--- | :--- | :--- | :--- | :--- |
| `Desktop Headlines/H1` | `44px` | `56px` | `500 (Medium)` | Judul Landing / Hero Header Besar. |
| `Desktop Headlines/H2` | `30px` | `36px` | `500 (Medium)` | Header Modul Silabus. |
| `Mob Headlines/Mob H1` | `29px` | `36px` | `600 (SemiBold)`| Header Utama Mobile App. |
| `Desktop Headlines/H3` | `22px` | `28px` | `600 (SemiBold)`| Judul Mata Kuliah pada Kartu Silabus. |
| `Desktop Headlines/H4` | `18px` | `24px` | `700 (Bold)` | Judul Topik Pertemuan Silabus. |
| `Body txt/body_L` | `20px` | `28px` | `400 (Regular)` | Teks pengantar modul. |
| `Body txt/body-M-Regular`| `18px` | `24px` | `400 (Regular)` | Body text standar. |
| `Body txt/body-S` | `16px` | `24px` | `400 (Regular)` | Deskripsi materi perkuliahan. |
| `Body txt/body-XS` | `14px` | `20px` | `400 (Regular)` | Metadata durasi video (`⏱ 18m`), nama channel. |
| `Caption` | `12px` | `16px` | `500 (Medium)` | Label Badge Counter, persentase progres. |
| `Micro` | `10px` | `14px` | `600 (SemiBold)`| Tag status pill mini. |

---

## 5. Token Spasi, Radius & Elevasi

### 5.1. Skala Spasi (8-Point Grid)
* `space/xxs`: `4px` (Jarak mikro antara ikon dan teks label)
* `space/xs`: `8px` (Padding dalam chip, gap baris metadata)
* `space/s`: `12px` (Padding horizontal tombol kecil)
* `space/m`: `16px` (Margin tepi layar mobile, padding dalam kartu silabus)
* `space/l`: `24px` (Jarak vertikal antar section mata kuliah)
* `space/xl`: `32px` (Margin bawah header aplikasi)
* `space/xxl`: `48px` (Target sentuh interaktif minimum)

### 5.2. Border Radius
* `radius/xs`: `2px` (Tag status mikro)
* `radius/s`: `4px` (Kotak Checkbox pertemuan)
* `radius/default`: `8px` (Kartu Mata Kuliah, Input Search, Container Banner)
* `radius/m`: `12px` (Modal Popover, Bottom Sheet)
* `radius/l`: `24px` (Pill Button, Floating Action Button)
* `radius/full`: `9999px` (Badge Counter, Progress Bar Track, Avatar Lingkaran)

### 5.3. Elevasi & Bayangan (Shadow Tokens)
```css
/* Card Resting Elevation */
box-shadow: 0 4px 16px rgba(157, 63, 231, 0.08);

/* Card Active / Hover Elevation */
box-shadow: 0 8px 24px rgba(157, 63, 231, 0.14);

/* Floating CTA / Button Elevation */
box-shadow: 0 4px 12px rgba(157, 63, 231, 0.25);

/* Modal Dialog Elevation */
box-shadow: 0 16px 36px rgba(26, 20, 31, 0.16);
```

---

## 6. Pemetaan Komponen untuk Modul 1 (Component Specs)

| Komponen Modul 1 | Komponen Asal (UI Kit Free) | Canvas Asal | Penerapan di OpenCampus Mobile |
| :--- | :--- | :--- | :--- |
| **Top App Bar** | `Header` (27 varian) | `Header` | Navigasi atas dengan title `OpenCampus` dan icon profil. |
| **Pill Tabs Semester 1–8** | `Tabs` (Pill/Capsule variant) | `Tabs` | Tab selector horizontal dengan kapsul aktif gradient ungu. |
| **Hero Progress Banner** | `Summary Card` + `Progress` | `Progress` / `Layout` | Kartu ringkasan capaian semester dengan mascot Cookie (Node `1842:25160`). |
| **Kartu Mata Kuliah** | `Card Container` + `Badge` | `Data Display` | Kartu silabus dengan border radius 8px, soft violet shadow, dan badge SKS. |
| **Progress Bar** | `Linear Progress` | `Progress` | Indikator progres dengan track `#D9D1E0` dan fill `#00B998`. |
| **Checkbox Selesai** | `Checkbox` (Checked/Unchecked) | `Checkbox` | Checkbox radius 4px dengan fill `#00B998` dan centang putih saat tuntas. |
| **Baris Topik Silabus** | `Accordion` / `Collapse Panel` | `Collapse` | Baris topik pertemuan 1–16 dengan toggle expand dan tombol play video. |
| **Search Bar** | `Input` (Search Variant) | `Input` | Input pencarian mata kuliah dengan ikon kaca pembesar dan background `#FFFFFF`. |
| **Bottom Navigation Bar**| `Navigation bar` (Mobile variant) | `Navigation bar` | Menu bawah 5 tab: Beranda, Silabus, Jadwal, Notifikasi, Akun. |

---

## 7. Format JSON Design Tokens (W3C DTCG Format)

```json
{
  "color": {
    "brand": {
      "primary": { "value": "#9D3FE7", "type": "color" },
      "primaryDark": { "value": "#602093", "type": "color" },
      "blueAccent": { "value": "#00ACE5", "type": "color" }
    },
    "feedback": {
      "success": { "value": "#00B998", "type": "color" },
      "warning": { "value": "#FF9500", "type": "color" },
      "error": { "value": "#D51A52", "type": "color" }
    },
    "neutral": {
      "textPrimary": { "value": "#1A141F", "type": "color" },
      "textMuted": { "value": "#4B3A5A", "type": "color" },
      "border": { "value": "#ABA7AF", "type": "color" },
      "track": { "value": "#D9D1E0", "type": "color" },
      "surface": { "value": "#F5F3F7", "type": "color" },
      "card": { "value": "#FFFFFF", "type": "color" }
    }
  },
  "borderRadius": {
    "checkbox": { "value": "4px", "type": "borderRadius" },
    "card": { "value": "8px", "type": "borderRadius" },
    "modal": { "value": "12px", "type": "borderRadius" },
    "pill": { "value": "9999px", "type": "borderRadius" }
  },
  "typography": {
    "fontFamily": { "value": "Poppins, -apple-system, BlinkMacSystemFont, sans-serif", "type": "fontFamily" },
    "headings": {
      "h1": { "fontSize": "29px", "lineHeight": "36px", "fontWeight": 600 },
      "h2": { "fontSize": "22px", "lineHeight": "28px", "fontWeight": 600 },
      "h3": { "fontSize": "18px", "lineHeight": "24px", "fontWeight": 700 }
    },
    "body": {
      "medium": { "fontSize": "16px", "lineHeight": "24px", "fontWeight": 400 },
      "small": { "fontSize": "14px", "lineHeight": "20px", "fontWeight": 400 },
      "caption": { "fontSize": "12px", "lineHeight": "16px", "fontWeight": 500 }
    }
  },
  "elevation": {
    "card": { "value": "0 4px 16px rgba(157, 63, 231, 0.08)", "type": "boxShadow" },
    "floating": { "value": "0 8px 24px rgba(157, 63, 231, 0.14)", "type": "boxShadow" }
  }
}
```
