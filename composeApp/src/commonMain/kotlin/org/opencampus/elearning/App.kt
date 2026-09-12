package org.opencampus.elearning

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import org.opencampus.elearning.ui.CourseSyllabusScreen
import org.opencampus.elearning.ui.ScreenDestination
import org.opencampus.elearning.ui.SemesterHomeScreen
import org.opencampus.elearning.ui.SemesterViewModel
import org.opencampus.elearning.ui.VideoPlayerScreen
import org.opencampus.elearning.ui.theme.OpenCampusTheme

@Composable
fun App(
    viewModel: SemesterViewModel = remember { SemesterViewModel() }
) {
    OpenCampusTheme {
        val uiState by viewModel.uiState.collectAsState()

        when (val dest = uiState.currentDestination) {
            is ScreenDestination.Home -> {
                SemesterHomeScreen(viewModel = viewModel)
            }
            is ScreenDestination.Syllabus -> {
                CourseSyllabusScreen(viewModel = viewModel)
            }
            is ScreenDestination.VideoPlayer -> {
                VideoPlayerScreen(
                    courseId = dest.courseId,
                    topicId = dest.topicId,
                    onNavigateBack = { viewModel.navigateBack() }
                )
            }
        }
    }
}
