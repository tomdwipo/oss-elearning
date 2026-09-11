package org.opencampus.elearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.domain.model.ProgressStatus
import org.opencampus.elearning.domain.model.ProgressSummary
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun HeroProgressCard(
    semesterNumber: Int,
    progress: ProgressSummary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = OpenCampusColors.GrayscaleWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Progres Semester $semesterNumber",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OpenCampusColors.GrayscaleBlack
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${progress.completedTopicsCount} dari ${progress.totalTopicsCount} Topik Selesai",
                        fontSize = 13.sp,
                        color = OpenCampusColors.GrayscaleHintText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Mascot badge Cookie
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(OpenCampusColors.CorporatePurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🍪", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${progress.progressPercentage}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OpenCampusColors.CorporatePurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Linear Progress Bar
            val animatedFraction = (progress.progressPercentage / 100f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(OpenCampusColors.GrayscaleSpacer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(9999.dp))
                        .background(OpenCampusColors.InformingApproval)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (statusText, statusColor) = when (progress.status) {
                    ProgressStatus.COMPLETED -> "● Selesai" to OpenCampusColors.InformingApproval
                    ProgressStatus.IN_PROGRESS -> "● Sedang Berjalan" to OpenCampusColors.InformingAttention
                    ProgressStatus.NOT_STARTED -> "● Belum Dimulai" to OpenCampusColors.GrayscaleHintText
                }
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor
                )
            }
        }
    }
}
