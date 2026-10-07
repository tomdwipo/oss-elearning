package org.opencampus.elearning

import com.russhwolf.settings.MapSettings
import org.opencampus.elearning.data.CurriculumRepository
import org.opencampus.elearning.data.LocalCurriculumRepository
import org.opencampus.elearning.data.LocalProgressRepository
import org.opencampus.elearning.data.LocalVideoReportRepository
import org.opencampus.elearning.domain.model.PlaybackSpeed
import org.opencampus.elearning.domain.model.ReportReason
import org.opencampus.elearning.ui.ScreenDestination
import org.opencampus.elearning.telemetry.DefaultAnalyticsService
import org.opencampus.elearning.ui.SemesterViewModel
import org.opencampus.elearning.ui.VideoPlayerViewModel
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VideoPlayerViewModelTest {

    private lateinit var settings: MapSettings
    private lateinit var progressRepo: LocalProgressRepository
    private lateinit var reportRepo: LocalVideoReportRepository
    private lateinit var curriculumRepo: CurriculumRepository
    private lateinit var analytics: DefaultAnalyticsService

    private val courseId = "cs101"
    private val topicId = "t101_01"

    @BeforeTest
    fun setUp() {
        settings = MapSettings()
        analytics = DefaultAnalyticsService()
        progressRepo = LocalProgressRepository(settings, analytics)
        reportRepo = LocalVideoReportRepository(settings)
        curriculumRepo = LocalCurriculumRepository()
    }

    private fun createViewModel(): VideoPlayerViewModel {
        return VideoPlayerViewModel(
            courseId = courseId,
            topicId = topicId,
            curriculumRepository = curriculumRepo,
            progressRepository = progressRepo,
            analyticsService = analytics,
            videoReportRepository = reportRepo
        )
    }

    @Test
    fun testInitialStateLoadsTopicAndEmitsVideoStarted() {
        val vm = createViewModel()
        val state = vm.uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertNotNull(state.topic)
        assertEquals(topicId, state.topic?.id)
        assertEquals(PlaybackSpeed.SPEED_1_0X, state.playbackSpeed)
        assertFalse(state.isCompleted)

        val startEvent = analytics.getRecordedEvents().find { it.event_name == "video_started" }
        assertNotNull(startEvent)
        assertTrue(startEvent.trace_id.startsWith("trc_vid_play_"))
    }

    @Test
    fun testAutoCompleteThresholdBoundaryEvaluation() {
        val vm = createViewModel()

        // 1. Below threshold: 84.9% (84.9s of 100s) -> should NOT complete
        vm.onProgressUpdate(84.9f, 100f)
        assertFalse(vm.uiState.value.isCompleted, "84.9% should not trigger completion")
        assertFalse(progressRepo.isTopicCompleted(topicId), "Repository should remain incomplete")

        // 2. At threshold: 85.0% (85.0s of 100s) -> MUST complete
        vm.onProgressUpdate(85.0f, 100f)
        assertTrue(vm.uiState.value.isCompleted, "85.0% must trigger completion")
        assertTrue(progressRepo.isTopicCompleted(topicId), "Repository must be marked complete")

        val completedEvents = analytics.getRecordedEvents().filter { it.event_name == "video_completed" }
        assertEquals(1, completedEvents.size, "Should emit exactly 1 completion event")
        assertTrue(completedEvents.first().trace_id.startsWith("trc_vid_comp_"))

        // 3. Idempotency test: further progress updates (95s of 100s) should not trigger duplicate events
        vm.onProgressUpdate(95.0f, 100f)
        val repeatedEvents = analytics.getRecordedEvents().filter { it.event_name == "video_completed" }
        assertEquals(1, repeatedEvents.size, "Completion logic must be idempotent")
    }

    @Test
    fun testManualToggleCompletionPersistsAndEmitsEvent() {
        val vm = createViewModel()

        assertFalse(vm.uiState.value.isCompleted)
        assertFalse(progressRepo.isTopicCompleted(topicId))

        // Toggle ON
        vm.toggleManualCompletion()
        assertTrue(vm.uiState.value.isCompleted)
        assertTrue(progressRepo.isTopicCompleted(topicId))

        // Toggle OFF
        vm.toggleManualCompletion()
        assertFalse(vm.uiState.value.isCompleted)
        assertFalse(progressRepo.isTopicCompleted(topicId))

        val manualEvents = analytics.getRecordedEvents().filter {
            it.event_name == "video_completed" && it.payload["is_manual"].toString() == "true"
        }
        assertEquals(2, manualEvents.size)

        val toggleEvents = analytics.getRecordedEvents().filter {
            it.event_name == "topic_completion_toggled"
        }
        assertEquals(2, toggleEvents.size)
        assertEquals(courseId, toggleEvents.first().payload["course_id"].toString().replace("\"", ""))
        assertEquals(topicId, toggleEvents.first().payload["topic_id"].toString().replace("\"", ""))
        assertEquals("true", toggleEvents.first().payload["is_completed"].toString())
        assertEquals("manual", toggleEvents.first().payload["source"].toString().replace("\"", ""))
    }

    @Test
    fun testPlayerErrorStateAndTelemetry() {
        val vm = createViewModel()

        vm.onPlayerError(150, "Private video / restricted playback")
        val state = vm.uiState.value

        assertTrue(state.isError)
        assertEquals("Private video / restricted playback", state.errorMessage)
        assertFalse(state.isPlaying)

        val errEvent = analytics.getRecordedEvents().find { it.event_name == "video_error_encountered" }
        assertNotNull(errEvent)
        assertTrue(errEvent.trace_id.startsWith("trc_vid_err_"))
    }

    @Test
    fun testBrokenVideoReportFlowAndPayload() {
        val vm = createViewModel()

        vm.openReportDialog()
        assertTrue(vm.uiState.value.isReportDialogOpen)

        vm.submitReport(
            reason = ReportReason.DELETED_OR_PRIVATE,
            notes = "Video tidak dapat diputar karena disetel ke mode privat oleh pengunggah."
        )

        val state = vm.uiState.value
        assertFalse(state.isReportDialogOpen)
        assertTrue(state.isReportSubmittedSuccess)

        val reports = reportRepo.getReports()
        assertEquals(1, reports.size)
        val report = reports.first()
        assertEquals(topicId, report.topicId)
        assertEquals(courseId, report.courseId)
        assertEquals("deleted_private", report.reason)
        assertEquals("Video tidak dapat diputar karena disetel ke mode privat oleh pengunggah.", report.notes)
        assertTrue(report.traceId.startsWith("trc_rep_send_"))

        val reportEvent = analytics.getRecordedEvents().find { it.event_name == "video_link_reported" }
        assertNotNull(reportEvent)
        assertTrue(reportEvent.trace_id.startsWith("trc_rep_send_"))

        val brokenVideoEvent = analytics.getRecordedEvents().find { it.event_name == "broken_video_reported" }
        assertNotNull(brokenVideoEvent)
        assertTrue(brokenVideoEvent.trace_id.startsWith("trc_broken_rep_"))
        assertEquals(topicId, brokenVideoEvent.payload["topic_id"].toString().replace("\"", ""))
        assertEquals("DELETED_OR_PRIVATE", brokenVideoEvent.payload["reason"].toString().replace("\"", ""))
        assertEquals("true", brokenVideoEvent.payload["has_notes"].toString())

        vm.dismissReportSuccessMessage()
        assertFalse(vm.uiState.value.isReportSubmittedSuccess)
    }

    @Test
    fun testPlaybackSpeedSelection() {
        val vm = createViewModel()

        assertEquals(PlaybackSpeed.SPEED_1_0X, vm.uiState.value.playbackSpeed)

        vm.onPlaybackSpeedSelected(PlaybackSpeed.SPEED_1_5X)
        assertEquals(PlaybackSpeed.SPEED_1_5X, vm.uiState.value.playbackSpeed)
        assertEquals(1.5f, vm.uiState.value.playbackSpeed.speedMultiplier)

        vm.onPlaybackSpeedSelected(PlaybackSpeed.SPEED_2_0X)
        assertEquals(PlaybackSpeed.SPEED_2_0X, vm.uiState.value.playbackSpeed)
        assertEquals(2.0f, vm.uiState.value.playbackSpeed.speedMultiplier)
    }

    @Test
    fun testNavigationToVideoPlayerAndBackInSemesterViewModel() {
        val semesterVm = SemesterViewModel(
            curriculumRepository = curriculumRepo,
            progressRepository = progressRepo,
            analyticsService = analytics
        )

        // Open course cs101
        semesterVm.onCourseSelected("cs101")
        assertEquals(ScreenDestination.Syllabus("cs101"), semesterVm.uiState.value.currentDestination)

        // Select topic
        val topic = curriculumRepo.getCourse("cs101")!!.topics.first()
        semesterVm.onTopicSelected(topic)
        assertEquals(ScreenDestination.VideoPlayer("cs101", topic.id), semesterVm.uiState.value.currentDestination)

        // Navigate back -> should return to Syllabus("cs101")
        val handled1 = semesterVm.navigateBack()
        assertTrue(handled1)
        assertEquals(ScreenDestination.Syllabus("cs101"), semesterVm.uiState.value.currentDestination)

        // Navigate back again -> should return to Home
        val handled2 = semesterVm.navigateBack()
        assertTrue(handled2)
        assertEquals(ScreenDestination.Home, semesterVm.uiState.value.currentDestination)

        // Navigate back at Home -> returns false
        val handled3 = semesterVm.navigateBack()
        assertFalse(handled3)
    }

    @Test
    fun testExternalYouTubeOpenedTelemetry() {
        val vm = createViewModel()
        val url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"

        vm.onOpenYouTubeExternal(url)

        val ytEvent = analytics.getRecordedEvents().find { it.event_name == "external_youtube_opened" }
        assertNotNull(ytEvent)
        assertTrue(ytEvent.trace_id.startsWith("trc_yt_open_"))
    }
}
