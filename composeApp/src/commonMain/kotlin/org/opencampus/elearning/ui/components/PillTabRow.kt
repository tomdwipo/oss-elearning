package org.opencampus.elearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun PillTabRow(
    selectedSemester: Int,
    onSemesterSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    totalSemesters: Int = 8
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (sem in 1..totalSemesters) {
            val isSelected = sem == selectedSemester
            val backgroundModifier = if (isSelected) {
                Modifier.background(OpenCampusColors.CorporateGradient, shape = RoundedCornerShape(9999.dp))
            } else {
                Modifier
                    .background(OpenCampusColors.GrayscaleWhite, shape = RoundedCornerShape(9999.dp))
                    .border(1.dp, OpenCampusColors.GrayscaleSpacerLight, shape = RoundedCornerShape(9999.dp))
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .then(backgroundModifier)
                    .clickable { onSemesterSelected(sem) }
                    .height(44.dp)
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sem $sem",
                    color = if (isSelected) OpenCampusColors.GrayscaleWhite else OpenCampusColors.GrayscaleHintText,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}
