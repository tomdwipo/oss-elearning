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
    val InformingApproval = Color(0xFF27AE60)
    val InformingAttention = Color(0xFFFF9500)
    val InformingError = Color(0xFFEB5757)

    // 3.3. Palet Grayscale & Latar Belakang
    val GrayscaleBlack = Color(0xFF1A141F)
    val GrayscaleHintText = Color(0xFF8A90A2)
    val GrayscaleBorder = Color(0xFFE2E4E8)
    val GrayscaleSpacer = Color(0xFFF0F1F3)
    val GrayscaleSpacerLight = Color(0xFFF5F3F7)
    val GrayscaleBgLightGrey = Color(0xFFF8F9FA)
    val GrayscaleWhite = Color(0xFFFFFFFF)
}
