#!/usr/bin/env bash
# verify-evidence.sh — gate mekanis untuk klaim di PRD / Acceptance Criteria / Task OSS.
#
# Memastikan setiap klaim selesai (- [x] atau ✅) membawa tag bukti provenance
# ([RUN], [RUN-cli], [CODE], [ASSUMED]) dan semua file yang dikutip benar-benar ada di disk.
#
# Pakai:
#   bash scripts/verify-evidence.sh --ac <path-doc.md>
#
# Exit 0 = boleh bilang selesai. Exit 1 = belum.

set -uo pipefail

AC=""
FAIL=0

while [ $# -gt 0 ]; do
  case "$1" in
    --ac) AC="${2:-}"; shift 2 ;;
    -h|--help) sed -n '2,15p' "$0"; exit 0 ;;
    *) echo "argumen tidak dikenal: $1" >&2; exit 2 ;;
  esac
done

if [ -z "$AC" ]; then
  echo "FATAL: --ac wajib" >&2
  exit 2
fi
if [ ! -f "$AC" ]; then
  echo "FATAL: Dokumen tidak ditemukan: $AC" >&2
  exit 2
fi

say()  { printf '%s\n' "$*"; }
bad()  { printf '  ❌ %s\n' "$*"; FAIL=1; }
ok()   { printf '  ✅ %s\n' "$*"; }

say "verify-evidence: $AC"
say ""

# ── 1. Tag Provenance pada Klaim Selesai ──────────────────────────────────────
say "1. Tag provenance pada klaim selesai"
CLAIMS=$(grep -nE '^[[:space:]]*-[[:space:]]*(\[[xX]\]|✅)' "$AC" || true)
UNTAGGED=0
if [ -n "$CLAIMS" ]; then
  while IFS= read -r line; do
    [ -z "$line" ] && continue
    if ! printf '%s' "$line" | grep -qE '\[(RUN|RUN-cli|CODE|ASSUMED)\]'; then
      bad "baris tanpa tag provenance: ${line:0:110}"
      UNTAGGED=$((UNTAGGED+1))
    fi
  done <<< "$CLAIMS"
fi
[ "$UNTAGGED" -eq 0 ] && ok "semua klaim selesai bertag provenance"
say ""

# ── 2. Artefak yang Dikutip Ada di Disk ───────────────────────────────────────
say "2. Artefak yang dikutip ada di disk"
PATHS=$(grep -oE '\.docs/[A-Za-z0-9_./-]+\.(png|json|md|html|mp4)' "$AC" | sort -u || true)
MISSING=0
if [ -n "$PATHS" ]; then
  while IFS= read -r p; do
    [ -z "$p" ] && continue
    if [ ! -e "$p" ]; then
      bad "dikutip di dokumen tapi tidak ada di disk: $p"
      MISSING=$((MISSING+1))
    fi
  done <<< "$PATHS"
  [ "$MISSING" -eq 0 ] && ok "$(printf '%s' "$PATHS" | grep -c . ) path terverifikasi ada"
else
  ok "tidak ada path artefak yang dikutip"
fi
say ""

# ── 3. 'deferred' Wajib Membawa Alasan Mesin ─────────────────────────────────
say "3. Setiap 'deferred' membawa alasan"
DEFERRED=$(sed -E 's/"[^"]*"//g' "$AC" | grep -noE 'deferred[^(]' | grep -vE 'deferred\(' || true)
if [ -n "$DEFERRED" ]; then
  while IFS= read -r d; do
    [ -z "$d" ] && continue
    bad "deferred tanpa alasan mesin, harus deferred(alasan): baris $d"
  done <<< "$DEFERRED"
else
  ok "tidak ada deferred polos"
fi
say ""

say "────────────────────────────────────────────"
if [ "$FAIL" -ne 0 ]; then
  say "GATE MERAH — bukti belum lengkap."
  exit 1
fi
say "GATE HIJAU — bukti terverifikasi."
exit 0
