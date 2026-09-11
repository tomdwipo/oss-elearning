package org.opencampus.elearning.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.ui.components.CourseCard
import org.opencampus.elearning.ui.components.HeroProgressCard
import org.opencampus.elearning.ui.components.PillTabRow
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun SemesterHomeScreen(
    viewModel: SemesterViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val courses = viewModel.getFilteredCourses()
    val semesterProgress = viewModel.getSemesterProgress(uiState.selectedSemester)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OpenCampusColors.GrayscaleBgLightGrey
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Brand & Faculty Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📖", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OpenCampus",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = OpenCampusColors.CorporatePurple
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .background(OpenCampusColors.CorporatePurple.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Teknik Informatika",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OpenCampusColors.CorporateDarkPurple
                        )
                    }
                }
            }

            // Search Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(OpenCampusColors.GrayscaleWhite)
                        .border(1.dp, OpenCampusColors.GrayscaleBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔍", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        BasicTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = OpenCampusColors.GrayscaleBlack
                            ),
                            cursorBrush = SolidColor(OpenCampusColors.CorporatePurple),
                            decorationBox = { innerTextField ->
                                if (uiState.searchQuery.isEmpty()) {
                                    Text(
                                        text = "Cari mata kuliah atau topik...",
                                        fontSize = 14.sp,
                                        color = OpenCampusColors.GrayscaleHintText.copy(alpha = 0.7f)
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
            }

            // Hero Progress Card with Cookies Mascot
            item {
                HeroProgressCard(
                    semesterNumber = uiState.selectedSemester,
                    progress = semesterProgress
                )
            }

            // Section Header: PILIH SEMESTER
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PILIH SEMESTER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OpenCampusColors.GrayscaleHintText,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "8 Semester",
                        fontSize = 12.sp,
                        color = OpenCampusColors.GrayscaleHintText
                    )
                }
            }

            // Pill Tabs Row
            item {
                PillTabRow(
                    selectedSemester = uiState.selectedSemester,
                    onSemesterSelected = { viewModel.onSemesterTabSelected(it) }
                )
            }

            // Section Header: MATA KULIAH
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MATA KULIAH SEMESTER ${uiState.selectedSemester}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OpenCampusColors.GrayscaleHintText,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${courses.size} Mata Kuliah",
                        fontSize = 12.sp,
                        color = OpenCampusColors.GrayscaleHintText
                    )
                }
            }

            // Courses List
            items(courses, key = { it.id }) { course ->
                val courseProgress = viewModel.getCourseProgress(course.id)
                CourseCard(
                    course = course,
                    progress = courseProgress,
                    onClick = { viewModel.onCourseSelected(course.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
