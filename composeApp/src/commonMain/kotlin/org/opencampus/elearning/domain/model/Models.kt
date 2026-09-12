package org.opencampus.elearning.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CurriculumRoot(
    val major: String,
    val semesters: List<Semester>
)

@Serializable
data class Semester(
    val semesterNumber: Int,
    val title: String,
    val courses: List<Course>
)

@Serializable
data class Course(
    val id: String,
    val title: String,
    val iconType: String = "course1",
    val meetingsCount: Int = 16,
    val estHours: String = "6.0 Jam",
    val topics: List<Topic> = emptyList()
)

@Serializable
data class Topic(
    val id: String,
    val no: Int,
    val title: String,
    val duration: String,
    val channel: String,
    val videoId: String = ""
)

enum class ProgressStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

@Serializable
data class ProgressSummary(
    val completedTopicsCount: Int,
    val totalTopicsCount: Int,
    val progressPercentage: Int,
    val status: ProgressStatus
) {
    companion object {
        fun calculate(completedCount: Int, totalCount: Int): ProgressSummary {
            if (totalCount <= 0) {
                return ProgressSummary(
                    completedTopicsCount = 0,
                    totalTopicsCount = 0,
                    progressPercentage = 0,
                    status = ProgressStatus.NOT_STARTED
                )
            }
            val percentage = ((completedCount.toFloat() / totalCount.toFloat()) * 100f).toInt().coerceIn(0, 100)
            val status = when {
                completedCount == 0 -> ProgressStatus.NOT_STARTED
                completedCount >= totalCount -> ProgressStatus.COMPLETED
                else -> ProgressStatus.IN_PROGRESS
            }
            return ProgressSummary(
                completedTopicsCount = completedCount,
                totalTopicsCount = totalCount,
                progressPercentage = percentage,
                status = status
            )
        }
    }
}

enum class PlaybackSpeed(val speedMultiplier: Float, val label: String) {
    SPEED_0_75X(0.75f, "0.75x"),
    SPEED_1_0X(1.0f, "1x"),
    SPEED_1_25X(1.25f, "1.25x"),
    SPEED_1_5X(1.5f, "1.5x"),
    SPEED_2_0X(2.0f, "2x")
}

enum class ReportReason(val id: String, val label: String) {
    DELETED_OR_PRIVATE("deleted_private", "Video Dihapus / Private"),
    NOT_RELEVANT("not_relevant", "Video Tidak Sesuai Topik"),
    AUDIO_VISUAL_BROKEN("broken_media", "Audio / Visual Rusak"),
    OTHER("other", "Lainnya")
}

@Serializable
data class VideoReportPayload(
    val reportId: String,
    val topicId: String,
    val courseId: String,
    val videoId: String,
    val reason: String,
    val notes: String = "",
    val timestampEpochMs: Long,
    val traceId: String
)

data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val courseId: String = "",
    val topic: Topic? = null,
    val isCompleted: Boolean = false,
    val currentTimeSeconds: Float = 0f,
    val totalDurationSeconds: Float = 0f,
    val playbackSpeed: PlaybackSpeed = PlaybackSpeed.SPEED_1_0X,
    val isPlaying: Boolean = false,
    val isReportDialogOpen: Boolean = false,
    val isReportSubmittedSuccess: Boolean = false
)

