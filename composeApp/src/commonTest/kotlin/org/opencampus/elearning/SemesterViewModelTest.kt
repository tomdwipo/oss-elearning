package org.opencampus.elearning

import com.russhwolf.settings.MapSettings
import org.opencampus.elearning.data.LocalCurriculumRepository
import org.opencampus.elearning.data.LocalProgressRepository
import org.opencampus.elearning.domain.model.Topic
import org.opencampus.elearning.telemetry.DefaultAnalyticsService
import org.opencampus.elearning.ui.ScreenDestination
import org.opencampus.elearning.ui.SemesterViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SemesterViewModelTest {

    private fun createViewModel(): Triple<SemesterViewModel, LocalProgressRepository, DefaultAnalyticsService> {
        val settings = MapSettings()
        val analytics = DefaultAnalyticsService()
        val progressRepo = LocalProgressRepository(settings, analytics)
        val curriculumRepo = LocalCurriculumRepository()
        val vm = SemesterViewModel(curriculumRepo, progressRepo, analytics)
        return Triple(vm, progressRepo, analytics)
    }

    @Test
    fun testInitialStateAndAppOpenedEvent() {
        val (vm, _, analytics) = createViewModel()
        val state = vm.uiState.value

        assertEquals(1, state.selectedSemester)
        assertEquals(ScreenDestination.Home, state.currentDestination)
        assertTrue(state.completedTopicIds.isEmpty())

        val events = analytics.getRecordedEvents()
        assertTrue(events.any { it.event_name == "app_opened" })
    }

    @Test
    fun testSwitchingSemesterEmitsTelemetry() {
        val (vm, _, analytics) = createViewModel()

        vm.onSemesterTabSelected(2)
        assertEquals(2, vm.uiState.value.selectedSemester)

        val switchEvent = analytics.getRecordedEvents().find { it.event_name == "semester_switched" }
        assertTrue(switchEvent != null)
        assertTrue(switchEvent.trace_id.startsWith("trc_sem_switch_"))
    }

    @Test
    fun testSelectingCourseAndBackNavigation() {
        val (vm, _, analytics) = createViewModel()

        vm.onCourseSelected("cs101")
        val state = vm.uiState.value
        assertEquals("cs101", state.currentCourseId)
        assertTrue(state.currentDestination is ScreenDestination.Syllabus)

        val courseEvent = analytics.getRecordedEvents().find { it.event_name == "course_opened" }
        assertTrue(courseEvent != null)
        assertTrue(courseEvent.trace_id.startsWith("trc_course_open_"))

        // Navigate back
        val handled = vm.navigateBack()
        assertTrue(handled)
        assertEquals(ScreenDestination.Home, vm.uiState.value.currentDestination)
    }

    @Test
    fun testTopicSelectionAndManualCheckboxToggle() {
        val (vm, _, analytics) = createViewModel()

        val sampleTopic = Topic(
            id = "t101_01",
            no = 1,
            title = "Konsep Dasar Logika",
            duration = "18m",
            channel = "Web Programming UNPAS",
            videoId = "vid_01"
        )

        vm.onTopicSelected(sampleTopic)
        val topicEvent = analytics.getRecordedEvents().find { it.event_name == "topic_selected" }
        assertTrue(topicEvent != null)
        assertTrue(topicEvent.trace_id.startsWith("trc_topic_sel_"))

        // Toggle checkbox
        vm.toggleTopicCheckbox("t101_01")
        assertTrue(vm.uiState.value.completedTopicIds.contains("t101_01"))

        val videoCompEvent = analytics.getRecordedEvents().find { it.event_name == "video_completed" }
        assertTrue(videoCompEvent != null)
        assertTrue(videoCompEvent.trace_id.startsWith("trc_video_comp_"))
    }

    @Test
    fun testSearchQueryFiltering() {
        val (vm, _, _) = createViewModel()
        vm.onSemesterTabSelected(1)

        val allCourses = vm.getFilteredCourses()
        assertEquals(4, allCourses.size)

        vm.updateSearchQuery("algoritma")
        val filtered = vm.getFilteredCourses()
        assertEquals(1, filtered.size)
        assertEquals("cs101", filtered[0].id)
    }
}
