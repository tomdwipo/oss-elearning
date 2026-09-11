package org.opencampus.elearning.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.opencampus.elearning.data.CurriculumRepository
import org.opencampus.elearning.data.LocalCurriculumRepository
import org.opencampus.elearning.data.LocalProgressRepository
import org.opencampus.elearning.data.ProgressRepository
import org.opencampus.elearning.domain.model.Course
import org.opencampus.elearning.domain.model.ProgressSummary
import org.opencampus.elearning.domain.model.Semester
import org.opencampus.elearning.domain.model.Topic
import org.opencampus.elearning.telemetry.AnalyticsService
import org.opencampus.elearning.telemetry.DefaultAnalyticsService

sealed interface ScreenDestination {
    object Home : ScreenDestination
    data class Syllabus(val courseId: String) : ScreenDestination
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val selectedSemester: Int = 1,
    val currentDestination: ScreenDestination = ScreenDestination.Home,
    val currentCourseId: String = "cs101",
    val searchQuery: String = "",
    val completedTopicIds: Set<String> = emptySet(),
    val errorMessage: String? = null
)

class SemesterViewModel(
    val curriculumRepository: CurriculumRepository = LocalCurriculumRepository(),
    val progressRepository: ProgressRepository = LocalProgressRepository(),
    val analyticsService: AnalyticsService = DefaultAnalyticsService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        val initTraceId = analyticsService.generateTraceId("app_init")
        val savedCompleted = progressRepository.getCompletedTopicIds()
        _uiState.update { it.copy(completedTopicIds = savedCompleted) }

        analyticsService.logEvent(
            "app_opened",
            initTraceId,
            buildJsonObject {
                put("app_version", "0.1.0")
                put("platform", "kmp_cmp")
                put("initial_semester", 1)
            }
        )
    }

    fun getSemesters(): List<Semester> = curriculumRepository.getSemesters()

    fun getActiveSemester(): Semester? = curriculumRepository.getSemester(_uiState.value.selectedSemester)

    fun getActiveCourse(): Course? = curriculumRepository.getCourse(_uiState.value.currentCourseId)

    fun getFilteredCourses(): List<Course> {
        val sem = getActiveSemester() ?: return emptyList()
        val query = _uiState.value.searchQuery.trim().lowercase()
        return if (query.isEmpty()) {
            sem.courses
        } else {
            sem.courses.filter { it.title.lowercase().contains(query) }
        }
    }

    fun getSemesterProgress(
        semesterNumber: Int,
        completedTopicIds: Set<String> = _uiState.value.completedTopicIds
    ): ProgressSummary {
        return curriculumRepository.calculateSemesterProgress(semesterNumber, completedTopicIds)
    }

    fun getCourseProgress(
        courseId: String,
        completedTopicIds: Set<String> = _uiState.value.completedTopicIds
    ): ProgressSummary {
        return curriculumRepository.calculateCourseProgress(courseId, completedTopicIds)
    }

    fun onSemesterTabSelected(newSemester: Int) {
        val prevSemester = _uiState.value.selectedSemester
        if (prevSemester == newSemester) return

        val traceId = analyticsService.generateTraceId("sem_switch")
        val semesterCourses = curriculumRepository.getSemester(newSemester)?.courses ?: emptyList()

        _uiState.update { it.copy(selectedSemester = newSemester) }

        analyticsService.logEvent(
            "semester_switched",
            traceId,
            buildJsonObject {
                put("previous_semester", prevSemester)
                put("selected_semester", newSemester)
                put("total_courses", semesterCourses.size)
            }
        )
    }

    fun onCourseSelected(courseId: String) {
        val course = curriculumRepository.getCourse(courseId) ?: return
        val traceId = analyticsService.generateTraceId("course_open")
        val progress = getCourseProgress(courseId)

        _uiState.update {
            it.copy(
                currentCourseId = courseId,
                currentDestination = ScreenDestination.Syllabus(courseId)
            )
        }

        analyticsService.logEvent(
            "course_opened",
            traceId,
            buildJsonObject {
                put("course_id", courseId)
                put("course_title", course.title)
                put("semester", _uiState.value.selectedSemester)
                put("completed_topics_count", progress.completedTopicsCount)
                put("total_topics_count", progress.totalTopicsCount)
                put("progress_percentage", progress.progressPercentage)
            }
        )
    }

    fun onTopicSelected(topic: Topic) {
        val traceId = analyticsService.generateTraceId("topic_sel")
        analyticsService.logEvent(
            "topic_selected",
            traceId,
            buildJsonObject {
                put("topic_id", topic.id)
                put("course_id", _uiState.value.currentCourseId)
                put("meeting_number", topic.no)
                put("video_id", topic.videoId)
                put("channel_name", topic.channel)
                put("duration_minutes", topic.duration)
            }
        )
    }

    fun toggleTopicCheckbox(topicId: String) {
        val newState = progressRepository.toggleTopicCompletion(topicId)
        val updatedSet = progressRepository.getCompletedTopicIds()
        _uiState.update { it.copy(completedTopicIds = updatedSet) }

        val traceId = analyticsService.generateTraceId("video_comp")
        analyticsService.logEvent(
            "video_completed",
            traceId,
            buildJsonObject {
                put("video_id", topicId)
                put("is_manual", true)
                put("is_completed", newState)
            }
        )
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun navigateBack(): Boolean {
        return if (_uiState.value.currentDestination !is ScreenDestination.Home) {
            _uiState.update { it.copy(currentDestination = ScreenDestination.Home) }
            true
        } else {
            false
        }
    }
}
