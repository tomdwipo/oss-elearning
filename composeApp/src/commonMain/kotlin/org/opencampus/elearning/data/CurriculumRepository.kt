package org.opencampus.elearning.data

import kotlinx.serialization.json.Json
import org.opencampus.elearning.domain.model.Course
import org.opencampus.elearning.domain.model.CurriculumRoot
import org.opencampus.elearning.domain.model.ProgressSummary
import org.opencampus.elearning.domain.model.Semester

interface CurriculumRepository {
    fun getCurriculum(): CurriculumRoot
    fun getSemesters(): List<Semester>
    fun getSemester(semesterNumber: Int): Semester?
    fun getCourse(courseId: String): Course?
    fun calculateSemesterProgress(semesterNumber: Int, completedTopicIds: Set<String>): ProgressSummary
    fun calculateCourseProgress(courseId: String, completedTopicIds: Set<String>): ProgressSummary
}

class LocalCurriculumRepository(
    private val jsonStringProvider: () -> String = { CurriculumDataSource.RAW_JSON }
) : CurriculumRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val root: CurriculumRoot by lazy {
        try {
            json.decodeFromString<CurriculumRoot>(jsonStringProvider())
        } catch (e: Exception) {
            println("[CurriculumRepository] JSON decode exception: ${e.message}")
            CurriculumRoot(major = "Teknik Informatika", semesters = emptyList())
        }
    }

    override fun getCurriculum(): CurriculumRoot = root

    override fun getSemesters(): List<Semester> = root.semesters

    override fun getSemester(semesterNumber: Int): Semester? =
        root.semesters.find { it.semesterNumber == semesterNumber }

    override fun getCourse(courseId: String): Course? {
        for (semester in root.semesters) {
            val course = semester.courses.find { it.id == courseId }
            if (course != null) return course
        }
        return null
    }

    override fun calculateSemesterProgress(semesterNumber: Int, completedTopicIds: Set<String>): ProgressSummary {
        val semester = getSemester(semesterNumber) ?: return ProgressSummary.calculate(0, 0)
        val allTopics = semester.courses.flatMap { it.topics }
        val totalCount = allTopics.size
        val completedCount = allTopics.count { completedTopicIds.contains(it.id) }
        return ProgressSummary.calculate(completedCount, totalCount)
    }

    override fun calculateCourseProgress(courseId: String, completedTopicIds: Set<String>): ProgressSummary {
        val course = getCourse(courseId) ?: return ProgressSummary.calculate(0, 0)
        val totalCount = course.topics.size
        val completedCount = course.topics.count { completedTopicIds.contains(it.id) }
        return ProgressSummary.calculate(completedCount, totalCount)
    }
}
