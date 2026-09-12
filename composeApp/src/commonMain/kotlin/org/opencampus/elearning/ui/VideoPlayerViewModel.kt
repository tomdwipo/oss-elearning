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
import org.opencampus.elearning.data.LocalVideoReportRepository
import org.opencampus.elearning.data.ProgressRepository
import org.opencampus.elearning.data.VideoReportRepository
import org.opencampus.elearning.domain.model.PlaybackSpeed
import org.opencampus.elearning.domain.model.ReportReason
import org.opencampus.elearning.domain.model.Topic
import org.opencampus.elearning.domain.model.VideoPlayerUiState
import org.opencampus.elearning.domain.model.VideoReportPayload
import org.opencampus.elearning.telemetry.AnalyticsService
import org.opencampus.elearning.telemetry.DefaultAnalyticsService

class VideoPlayerViewModel(
    val courseId: String,
    val topicId: String,
    val curriculumRepository: CurriculumRepository = LocalCurriculumRepository(),
    val progressRepository: ProgressRepository = LocalProgressRepository(),
    val analyticsService: AnalyticsService = DefaultAnalyticsService(),
    val videoReportRepository: VideoReportRepository = LocalVideoReportRepository()
) : ViewModel() {

    companion object {
        const val AUTO_COMPLETE_THRESHOLD_PERCENT = 85.0f
    }

    private val _uiState = MutableStateFlow(
        VideoPlayerUiState(
            courseId = courseId,
            isLoading = true
        )
    )
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()

    init {
        loadTopicAndInitPlayer()
    }

    private fun loadTopicAndInitPlayer() {
        val course = curriculumRepository.getCourse(courseId)
        val topic = course?.topics?.find { it.id == topicId }
        val isCompleted = progressRepository.isTopicCompleted(topicId)

        val traceId = analyticsService.generateTraceId("vid_play")

        _uiState.update {
            it.copy(
                topic = topic,
                isCompleted = isCompleted,
                isLoading = false
            )
        }

        if (topic != null) {
            analyticsService.logEvent(
                "video_started",
                traceId,
                buildJsonObject {
                    put("video_id", topic.videoId)
                    put("course_id", courseId)
                    put("topic_id", topicId)
                    put("speed", _uiState.value.playbackSpeed.label)
                    put("trace_id", traceId)
                }
            )
        }
    }

    fun onPlaybackSpeedSelected(speed: PlaybackSpeed) {
        _uiState.update { it.copy(playbackSpeed = speed) }
    }

    fun onProgressUpdate(currentTimeSec: Float, totalDurationSec: Float) {
        val percentage = if (totalDurationSec > 0f) {
            (currentTimeSec / totalDurationSec) * 100f
        } else {
            0f
        }

        val wasCompleted = _uiState.value.isCompleted
        val shouldAutoComplete = percentage >= AUTO_COMPLETE_THRESHOLD_PERCENT && !wasCompleted

        _uiState.update {
            it.copy(
                currentTimeSeconds = currentTimeSec,
                totalDurationSeconds = totalDurationSec,
                isCompleted = if (shouldAutoComplete) true else it.isCompleted,
                isPlaying = true
            )
        }

        if (shouldAutoComplete) {
            progressRepository.setTopicCompleted(topicId, true)

            val traceId = analyticsService.generateTraceId("vid_comp")
            analyticsService.logEvent(
                "video_completed",
                traceId,
                buildJsonObject {
                    put("video_id", topicId)
                    put("is_manual", false)
                    put("duration_watched", currentTimeSec.toDouble())
                    put("percentage", percentage.toDouble())
                }
            )
        }
    }

    fun toggleManualCompletion() {
        val newStatus = progressRepository.toggleTopicCompletion(topicId)
        _uiState.update { it.copy(isCompleted = newStatus) }

        val traceId = analyticsService.generateTraceId("vid_comp")
        val curTime = _uiState.value.currentTimeSeconds
        val totalTime = _uiState.value.totalDurationSeconds
        val percentage = if (totalTime > 0f) (curTime / totalTime) * 100f else 0f

        analyticsService.logEvent(
            "video_completed",
            traceId,
            buildJsonObject {
                put("video_id", topicId)
                put("is_manual", true)
                put("duration_watched", curTime.toDouble())
                put("percentage", percentage.toDouble())
            }
        )
    }

    fun onPlayerError(errorCode: Int, message: String) {
        _uiState.update {
            it.copy(
                isError = true,
                errorMessage = message,
                isLoading = false,
                isPlaying = false
            )
        }

        val traceId = analyticsService.generateTraceId("vid_err")
        analyticsService.logEvent(
            "video_error_encountered",
            traceId,
            buildJsonObject {
                put("video_id", _uiState.value.topic?.videoId ?: "")
                put("error_code", errorCode)
                put("error_message", message)
            }
        )
    }

    fun onOpenYouTubeExternal(canonicalUrl: String) {
        val traceId = analyticsService.generateTraceId("yt_open")
        analyticsService.logEvent(
            "external_youtube_opened",
            traceId,
            buildJsonObject {
                put("video_id", _uiState.value.topic?.videoId ?: "")
                put("canonical_url", canonicalUrl)
            }
        )
    }

    fun openReportDialog() {
        _uiState.update { it.copy(isReportDialogOpen = true) }
    }

    fun closeReportDialog() {
        _uiState.update { it.copy(isReportDialogOpen = false) }
    }

    fun submitReport(reason: ReportReason, notes: String = "") {
        val traceId = analyticsService.generateTraceId("rep_send")
        val timestamp = 1725542000000L + (videoReportRepository.getReports().size * 1000L)
        val payload = VideoReportPayload(
            reportId = "rep_${topicId}_${traceId.takeLast(6)}",
            topicId = topicId,
            courseId = courseId,
            videoId = _uiState.value.topic?.videoId ?: "",
            reason = reason.id,
            notes = notes,
            timestampEpochMs = timestamp,
            traceId = traceId
        )

        videoReportRepository.submitReport(payload)

        _uiState.update {
            it.copy(
                isReportDialogOpen = false,
                isReportSubmittedSuccess = true
            )
        }

        analyticsService.logEvent(
            "video_link_reported",
            traceId,
            buildJsonObject {
                put("video_id", payload.videoId)
                put("course_id", payload.courseId)
                put("reason", payload.reason)
                put("notes", payload.notes)
                put("trace_id", payload.traceId)
            }
        )
    }

    fun dismissReportSuccessMessage() {
        _uiState.update { it.copy(isReportSubmittedSuccess = false) }
    }
}
