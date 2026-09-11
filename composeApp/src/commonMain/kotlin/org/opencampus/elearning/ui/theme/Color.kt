package org.opencampus.elearning.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object OpenCampusColors {
    // 3.1. Palet Brand Utama & Gradient
    val CorporatePurple = Color(0xFF9D3FE7)
    val CorporateDarkPurple = Color(0xFF602093)
    val Blue = Color(0xFF00ACE5)
    val CorporateGradient = Brush.linearGradient(
        listOf(CorporatePurple, CorporateDarkPurple)
    )

    // 3.2. Token Semantik Status & Progres
    val InformingApproval = Color(0xFF00B998)
    val InformingAttention = Color(0xFFFF9500)
    val InformingError = Color(0xFFD51A52)

    // 3.3. Palet Grayscale & Latar Belakang
    val GrayscaleBlack = Color(0xFF1A141F)
    val GrayscaleHintText = Color(0xFF4B3A5A)
    val GrayscaleBorder = Color(0xFFABA7AF)
    val GrayscaleSpacer = Color(0xFFD9D1E0)
    val GrayscaleSpacerLight = Color(0xFFE5E0EB)
    val GrayscaleBgLightGrey = Color(0xFFF5F3F7)
    val GrayscaleWhite = Color(0xFFFFFFFF)
}
