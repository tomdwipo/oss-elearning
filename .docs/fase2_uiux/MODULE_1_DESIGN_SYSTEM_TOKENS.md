# Modul 1: Navigasi Kurikulum — UI Design System & Tokens (MVP v0.1)

Dokumen ini menetapkan spesifikasi standar **Design System & Design Tokens** untuk **Modul 1: Navigasi Kurikulum** pada aplikasi **OpenCampus Mobile** berdasarkan spesifikasi pada [PRD.md](../fase0/PRD.md), [USE_CASE.md](../fase1/USE_CASE.md), [MODULE_1_USER_FLOW_WIREFLOW.md](./MODULE_1_USER_FLOW_WIREFLOW.md), dan [MODULE_1_LOFI_WIREFRAME.md](./MODULE_1_LOFI_WIREFRAME.md).

---

## 1. Prinsip Desain (Design Principles)

1. **Distraction-Free & Minimalist:** Fokus penuh pada struktur pembelajaran tanpa elemen visual berlebihan atau ornamen dekoratif non-fungsional.
2. **High Information Hierarchy:** Memudahkan pembelajar mengenali posisi semester, mata kuliah, dan status ketercapaian secara sekilas.
3. **Ergonomic Touch Targets:** Semua elemen interaktif (Tab Semester, Kartu Mata Kuliah, Checkbox) memiliki target sentuh minimal **48 x 48 dp** untuk kenyamanan penggunaan satu tangan.
4. **Accessible Contrast (WCAG 2.1 AA):** Rasio kontras teks terhadap latar belakang minimal 4.5:1 untuk teks standar dan 3:1 untuk elemen grafis interaktif.

---

## 2. Token Warna (Color Tokens)

### 2.1 Palet Utama (Light & Dark Theme)

Berdasarkan Styleguide resmi Figma (`Color` `229:52`):

| Token Name | Hex (Light) | Hex (Dark) | Deskripsi & Penggunaan |
| :--- | :--- | :--- | :--- |
| `color.brand.primary` | `#6D57FC` | `#B0A4FD` | Warna utama identitas OpenCampus (Variable 01 - Header, Tab Aktif, Tombol Primer). |
| `color.brand.primary-hover` | `#5040B8` | `#6D57FC` | State saat elemen primer ditekan / di-*hover*. |
| `color.brand.primary-deep` | `#261E58` | `#130F26` | Warna aksen gelap (Variable 02 - Banner background, Dark Header). |
| `color.brand.primary-surface`| `#E8E4FF` | `#1E2028` | Latar belakang komponen terpilih (Variable 04 - *Pill Tab*, Highlight Card). |
| `color.brand.accent` | `#B0A4FD` | `#5040B8` | Warna aksen sekunder / pastel (Variable 05 - Badge, Border highlight). |
| `color.bg.base` | `#F8F7FF` | `#0C0A1C` | Warna kanvas latar belakang seluruh layar (*Scaffold Background*). |
| `color.bg.surface` | `#FFFFFF` | `#1F2028` | Warna kartu mata kuliah, kartu ringkasan progres, dan modal. |
| `color.bg.surface-subtle` | `#F2F4F6` | `#292526` | Latar belakang track progress bar, badge non-aktif. |
| `color.text.primary` | `#0C0A1C` | `#F8F7FF` | Warna teks judul, nama mata kuliah, topik silabus (Variable 03 - High Contrast). |
| `color.text.secondary` | `#4B5563` | `#B0A4FD` | Warna teks metadata (durasi, nama kreator, jumlah pertemuan). |
| `color.text.tertiary` | `#9CA3AF` | `#767677` | Warna teks placeholder, status bar, caption pelengkap. |
| `color.border.default` | `#DFDAFE` | `#261E58` | Garis batas kartu, pemisah item silabus (*Divider*). |
| `color.border.focused` | `#6D57FC` | `#B0A4FD` | Garis batas elemen saat mendapatkan fokus navigasi. |

### 2.2 Token Status & Semantik (Semantic Tokens)

| Token Name | Hex (Light) | Hex (Dark) | Penggunaan |
| :--- | :--- | :--- | :--- |
| `color.success.base` | `#0E9F6E` | `#31C48D` | Centang topik selesai, progress bar 100%. |
| `color.success.surface` | `#DEF7EC` | `#064E3B` | Latar belakang indikator status selesai / badge kelulusan topik. |
| `color.warning.base` | `#FF9500` | `#FFA502` | Indikator progres berjalan, bookmark badge. |
| `color.error.base` | `#FF4423` | `#FF6348` | Indikator video rusak, status gagal muat. |

---

## 3. Token Tipografi (Typography Tokens)

* **Font Family Utama (Body & UI):** `Inter`, `-apple-system`, `BlinkMacSystemFont`, `sans-serif`
* **Font Family Display & Heading:** `Urbanist`, `Inter`, `sans-serif`
* **Font Family Monospace / Kode:** `JetBrains Mono`, `Fira Code`, `monospace`

| Token Name | Font Family | Font Size | Line Height | Weight | Penggunaan |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `font.heading.xl` | `Urbanist` | `24px (1.5rem)` | `32px` | `700 (Bold)` | Judul Utama Halaman Beranda & Silabus MK. |
| `font.heading.lg` | `Urbanist` | `18px (1.125rem)`| `26px` | `600 (SemiBold)`| Judul Mata Kuliah pada Card, Judul Section. |
| `font.heading.md` | `Inter` | `16px (1.0rem)` | `24px` | `600 (SemiBold)`| Judul Topik Pertemuan Silabus. |
| `font.body.md` | `Inter` | `14px (0.875rem)`| `20px` | `400 (Regular)` | Ringkasan progres, deskripsi teks umum. |
| `font.body.sm` | `Inter` | `12px (0.75rem)` | `16px` | `400 (Regular)` | Metadata durasi video, nama channel kreator. |
| `font.label.sm` | `Inter` | `12px (0.75rem)` | `16px` | `600 (SemiBold)`| Label Tab Semester, Badge Jurusan, Tag Progres. |

---

## 4. Token Spasi, Radius & Elevasi (Spacing & Elevation Tokens)

### 4.1 Spacing Scale (Sistem Grid 4px / 8px)

| Token Name | Nilai | Penggunaan Khusus |
| :--- | :--- | :--- |
| `spacing.2xs` | `4px` | Jarak antar ikon dan teks kecil, gap badge. |
| `spacing.xs` | `8px` | Jarak antar baris teks metadata, padding vertikal tab. |
| `spacing.sm` | `12px` | Gap antar elemen dalam kartu, padding horizontal tab. |
| `spacing.md` | `16px` | Margin tepi layar standar mobile, padding dalam kartu MK. |
| `spacing.lg` | `20px` | Jarak vertikal antar section / modul. |
| `spacing.xl` | `24px` | Jarak antar grup kartu mata kuliah. |
| `spacing.2xl` | `32px` | Margin bawah header utama. |

### 4.2 Border Radius

| Token Name | Nilai | Penerapan Komponen |
| :--- | :--- | :--- |
| `radius.sm` | `6px` | Checkbox, badge status materi. |
| `radius.md` | `10px` | Tab Pill Semester, Tombol Aksi Sekunder. |
| `radius.lg` | `14px` | Kartu Mata Kuliah (*Course Card*), Kartu Progress Header. |
| `radius.full` | `9999px` | Progress Bar Track & Fill, Avatar / Round Badge. |

### 4.3 Elevasi & Bayangan (Elevation / Shadows)

```text
elevation.none   : none
elevation.card   : 0 1px 3px 0 rgba(0, 0, 0, 0.08), 0 1px 2px -1px rgba(0, 0, 0, 0.04)
elevation.hover  : 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.05)
```

---

## 5. Spesifikasi Komponen & Status (Component Specs & States)

### 5.1 Tab Pemilih Semester (`SemesterTab`)

* **Ukuran:** Tinggi `40dp`, Minimal Lebar `72dp`, Padding `8dp 16dp`.
* **State Mapping:**
  * **Default (Inactive):** Latar `color.bg.surface`, Teks `color.text.secondary`, Border `color.border.default`.
  * **Selected (Active):** Latar `color.brand.primary`, Teks `#FFFFFF`, Border `color.brand.primary`, Radius `radius.md`.
  * **Pressed:** Opacity `0.85`.

```text
[ Inactive ]              [ Active / Selected ]
+------------------+      +------------------+
|     Sem 2        |      |     Sem 1*       |  <-- Primary Violet Background (#6D57FC)
+------------------+      +------------------+
```

### 5.2 Kartu Mata Kuliah (`CourseCard`)

* **Ukuran:** Lebar `100% (Responsive)`, Padding `16dp`, Border `1px solid color.border.default`, Radius `radius.lg`.
* **State Mapping:**
  * **Normal:** Latar `color.bg.surface`, Bayangan `elevation.card`.
  * **Pressed / Active:** Latar `color.brand.primary-surface`, Border `color.brand.primary`.

```text
+-------------------------------------------------------------+
| [ICON]  Algoritma & Pemrograman Dasar                       |
|         16 Pertemuan • Est. ~6 Jam                          |
|         Progres: 4 / 16 Selesai (25%)                       |
|         [========--------------------------------]      [>] |
+-------------------------------------------------------------+
```

### 5.3 Item Silabus Topik (`SyllabusTopicItem`)

* **Ukuran:** Tinggi Adaptif (Min `56dp` untuk area sentuh), Padding `12dp 16dp`, Border Bawah `1px solid color.border.default`.
* **Sub-komponen:**
  * **Checkbox (`InteractiveCheckbox`):**
    * Ukuran `22 x 22 dp` dengan area sentuh virtual `48 x 48 dp`.
    * **Unchecked `[ ]`:** Border `2px solid color.border.default`, Latar Transparan.
    * **Checked `[X]`:** Latar `color.success.base`, Ikon Centang Putih, Border `color.success.base`.
  * **Judul Pertemuan:** `font.heading.md`, `color.text.primary`. Jika *checked*, warna teks bertransisi halus ke `color.text.secondary`.
  * **Metadata Teks:** `font.body.sm`, `color.text.secondary` (Ikon `⏱` + Durasi, Ikon `👤` + Nama Kreator).

```text
+-------------------------------------------------------------+
| [X]  01. Konsep Dasar Algoritma & Flowchart                 |
|      ⏱ 18 Menit  •  👤 Web Programming UNPAS            [>] |
+-------------------------------------------------------------+
```

### 5.4 Komponen Progress Bar (`LinearProgressBar`)

* **Ukuran:** Tinggi `8dp`, Radius `radius.full`.
* **Track:** Latar `color.bg.surface-subtle`.
* **Indicator (Fill):**
  * Progres $1\% - 99\%$: Latar `color.brand.primary`.
  * Progres $100\%$: Latar `color.success.base`.
  * Animasi Transisi Nilai: `duration: 250ms`, `curve: easeInOut`.

---

## 6. Format Token JSON (Design Tokens Schema)

```json
{
  "color": {
    "brand": {
      "primary": { "value": "#6D57FC", "type": "color" },
      "primary_deep": { "value": "#261E58", "type": "color" },
      "primary_surface": { "value": "#E8E4FF", "type": "color" },
      "accent": { "value": "#B0A4FD", "type": "color" }
    },
    "bg": {
      "base": { "value": "#F8F7FF", "type": "color" },
      "surface": { "value": "#FFFFFF", "type": "color" }
    },
    "text": {
      "primary": { "value": "#0C0A1C", "type": "color" },
      "secondary": { "value": "#4B5563", "type": "color" }
    },
    "semantic": {
      "success": { "value": "#0E9F6E", "type": "color" },
      "warning": { "value": "#FF9500", "type": "color" },
      "error": { "value": "#FF4423", "type": "color" }
    }
  },
  "spacing": {
    "xs": { "value": "8px", "type": "spacing" },
    "sm": { "value": "12px", "type": "spacing" },
    "md": { "value": "16px", "type": "spacing" },
    "lg": { "value": "20px", "type": "spacing" },
    "xl": { "value": "24px", "type": "spacing" }
  },
  "radius": {
    "sm": { "value": "6px", "type": "borderRadius" },
    "md": { "value": "10px", "type": "borderRadius" },
    "lg": { "value": "14px", "type": "borderRadius" },
    "full": { "value": "9999px", "type": "borderRadius" }
  },
  "typography": {
    "fontFamilyHeading": { "value": "Urbanist, sans-serif", "type": "fontFamily" },
    "fontFamilyBody": { "value": "Inter, sans-serif", "type": "fontFamily" },
    "heading_xl": { "fontSize": "24px", "lineHeight": "32px", "fontWeight": "700" },
    "heading_lg": { "fontSize": "18px", "lineHeight": "26px", "fontWeight": "600" },
    "heading_md": { "fontSize": "16px", "lineHeight": "24px", "fontWeight": "600" },
    "body_md": { "fontSize": "14px", "lineHeight": "20px", "fontWeight": "400" },
    "body_sm": { "fontSize": "12px", "lineHeight": "16px", "fontWeight": "400" }
  }
}
```

---

## 7. Referensi Desain & Mapping Node Figma (Figma Traceability)

Komponen dan token di atas dirujuk dan diinspeksi dari aset desain komunitas:
* **Source Figma File:** `Mobile E-Learning App Design (Community).fig` (Format v101, 375 x 812 viewport)
* **Decoder Pipeline:** `.agents/skills/fig-decode/`

### 7.1 Key Frames & Node Mapping

| Komponen / Screen | Figma Frame Name | Figma Node GUID | Ukuran Frame | Aset Render Resmi (Export) |
| :--- | :--- | :--- | :--- | :--- |
| **Layar Beranda (Category Home)** | `High-Fidelity ( Category Home )` | `229:773` | `375 x 812` | [`figma_category_home_render.png`](./assets/figma_category_home_render.png) |
| **Layar Kategori (Course List)** | `High-Fidelity ( Category )` | `224:311` | `375 x 812` | [`figma_category_list_render.png`](./assets/figma_category_list_render.png) |
| **Layar Detail & Silabus** | `High-Fidelity ( Detail )` | `229:1189` | `375 x 812` | [`figma_detail_course_render.png`](./assets/figma_detail_course_render.png) |
| **Layar Onboarding** | `High-Fidelity ( Onboarding )` | `224:279` | `375 x 812` | [`figma_onboarding_render.png`](./assets/figma_onboarding_render.png) |
| **Styleguide - Colors** | `Color` | `229:52` / `224:172` | - | Ekstrak Hex 01–05 |
| **Styleguide - Typography** | `Typography` | `229:102` / `224:173` | - | `Urbanist` + `Inter` |

### 7.2 Link Sumber & File Aset
* **Figma Cloud URL:** [Figma Community Design](https://www.figma.com/design/3UzTSK8vlS6ICGgi14brey/Mobile-E-Learning-App-Design--Community-)
* **Local Binary Archive:** [`.docs/fase2_uiux/assets/Mobile E-Learning App Design (Community).fig`](./assets/Mobile%20E-Learning%20App%20Design%20%28Community%29.fig)
* **Interactive HTML Prototype (Semester Tabs):** [`hifi_semester_tabs_preview.html`](./assets/hifi_semester_tabs_preview.html)
