package org.opencampus.elearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.domain.model.Course
import org.opencampus.elearning.domain.model.ProgressSummary
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun CourseCard(
    course: Course,
    progress: ProgressSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, OpenCampusColors.GrayscaleSpacerLight, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = OpenCampusColors.GrayscaleWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Course icon box
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(OpenCampusColors.CorporatePurple.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    val iconText = when (course.iconType) {
                        "course1" -> "💻"
                        "course2" -> "📁"
                        "course3" -> "🌐"
                        else -> "📚"
                    }
                    Text(text = iconText, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OpenCampusColors.GrayscaleBlack
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${course.meetingsCount} Pertemuan • Est. ~${course.estHours}",
                        fontSize = 12.sp,
                        color = OpenCampusColors.GrayscaleHintText
                    )
                }

                Text(
                    text = "›",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    color = OpenCampusColors.GrayscaleBorder
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mini progress indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val fraction = (progress.progressPercentage / 100f).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(9999.dp))
                        .background(OpenCampusColors.GrayscaleSpacer)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .height(6.dp)
                            .clip(RoundedCornerShape(9999.dp))
                            .background(
                                if (progress.progressPercentage == 100) OpenCampusColors.InformingApproval
                                else OpenCampusColors.CorporatePurple
                            )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${progress.progressPercentage}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (progress.progressPercentage == 100) OpenCampusColors.InformingApproval
                    else OpenCampusColors.GrayscaleHintText
                )
            }
        }
    }
}
