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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.domain.model.Topic
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun TopicItem(
    topic: Topic,
    isCompleted: Boolean,
    isNextUp: Boolean,
    onTopicClick: () -> Unit,
    onToggleCheckbox: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerBorder = if (isNextUp) {
        Modifier.border(1.5.dp, OpenCampusColors.CorporatePurple, RoundedCornerShape(8.dp))
    } else {
        Modifier.border(1.dp, OpenCampusColors.GrayscaleSpacerLight, RoundedCornerShape(8.dp))
    }

    val containerBg = if (isNextUp) {
        OpenCampusColors.CorporatePurple.copy(alpha = 0.04f)
    } else {
        OpenCampusColors.GrayscaleWhite
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(containerBg)
            .then(containerBorder)
            .clickable(onClick = onTopicClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Meeting number box
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isCompleted) OpenCampusColors.InformingApproval.copy(alpha = 0.12f)
                        else OpenCampusColors.GrayscaleBgLightGrey
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = topic.no.toString().padStart(2, '0'),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) OpenCampusColors.InformingApproval else OpenCampusColors.GrayscaleBlack
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Topic metadata
            Column(modifier = Modifier.weight(1f)) {
                if (isNextUp) {
                    Text(
                        text = "BERIKUTNYA (NEXT UP)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OpenCampusColors.CorporatePurple
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = topic.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OpenCampusColors.GrayscaleBlack
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⏱ ${topic.duration}",
                        fontSize = 12.sp,
                        color = OpenCampusColors.GrayscaleHintText
                    )
                    Text(
                        text = " • ",
                        fontSize = 12.sp,
                        color = OpenCampusColors.GrayscaleBorder
                    )
                    Text(
                        text = topic.channel,
                        fontSize = 12.sp,
                        color = OpenCampusColors.GrayscaleHintText
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Checkbox 20x20 dp
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onToggleCheckbox),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isCompleted) OpenCampusColors.InformingApproval
                            else OpenCampusColors.GrayscaleWhite
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (isCompleted) OpenCampusColors.InformingApproval else OpenCampusColors.GrayscaleBorder,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Text(
                            text = "✔",
                            color = OpenCampusColors.GrayscaleWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
