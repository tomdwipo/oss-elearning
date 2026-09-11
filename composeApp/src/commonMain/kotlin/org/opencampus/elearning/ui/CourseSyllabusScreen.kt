package org.opencampus.elearning.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.domain.model.Topic
import org.opencampus.elearning.ui.components.TopicItem
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun CourseSyllabusScreen(
    viewModel: SemesterViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val course = viewModel.getActiveCourse()
    val progress = if (course != null) viewModel.getCourseProgress(course.id) else null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OpenCampusColors.GrayscaleBgLightGrey
    ) { innerPadding ->
        if (course == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Mata kuliah tidak ditemukan", color = OpenCampusColors.GrayscaleHintText)
            }
            return@Scaffold
        }

        val firstIncompleteIndex = course.topics.indexOfFirst { !uiState.completedTopicIds.contains(it.id) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Navigation Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OpenCampusColors.GrayscaleWhite)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(OpenCampusColors.GrayscaleBgLightGrey)
                            .clickable { viewModel.navigateBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "‹",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = OpenCampusColors.CorporatePurple
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = course.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OpenCampusColors.GrayscaleBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(OpenCampusColors.CorporatePurple.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${course.topics.size} Topik",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OpenCampusColors.CorporateDarkPurple
                        )
                    }
                }
            }

            // Hero Summary Card for Course
            if (progress != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = OpenCampusColors.GrayscaleWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = course.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = OpenCampusColors.GrayscaleBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Kurikulum Resmi • Est. ~${course.estHours} Durasi Total",
                                fontSize = 13.sp,
                                color = OpenCampusColors.GrayscaleHintText
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${progress.completedTopicsCount} dari ${progress.totalTopicsCount} Pertemuan Selesai",
                                    fontSize = 13.sp,
                                    color = OpenCampusColors.GrayscaleHintText
                                )
                                Text(
                                    text = "${progress.progressPercentage}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (progress.progressPercentage == 100) OpenCampusColors.InformingApproval
                                    else OpenCampusColors.CorporatePurple
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val fraction = (progress.progressPercentage / 100f).coerceIn(0f, 1f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(OpenCampusColors.GrayscaleSpacer)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(9999.dp))
                                        .background(OpenCampusColors.InformingApproval)
                                )
                            }
                        }
                    }
                }
            }

            // Section Title: DAFTAR SILABUS
            item {
                Text(
                    text = "DAFTAR SILABUS 16 PERTEMUAN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OpenCampusColors.GrayscaleHintText,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
                )
            }

            // Topics list
            items(course.topics, key = { it.id }) { topic ->
                val isCompleted = uiState.completedTopicIds.contains(topic.id)
                val isNextUp = (topic.no - 1) == firstIncompleteIndex

                TopicItem(
                    topic = topic,
                    isCompleted = isCompleted,
                    isNextUp = isNextUp,
                    onTopicClick = { viewModel.onTopicSelected(topic) },
                    onToggleCheckbox = { viewModel.toggleTopicCheckbox(topic.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
