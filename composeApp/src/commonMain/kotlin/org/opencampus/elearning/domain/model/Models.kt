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
