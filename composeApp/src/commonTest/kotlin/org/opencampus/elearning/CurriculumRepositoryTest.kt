package org.opencampus.elearning

import org.opencampus.elearning.data.LocalCurriculumRepository
import org.opencampus.elearning.domain.model.ProgressStatus
import org.opencampus.elearning.domain.model.ProgressSummary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CurriculumRepositoryTest {

    private val repository = LocalCurriculumRepository()

    @Test
    fun testCurriculumRootLoadsAllSemesters() {
        val root = repository.getCurriculum()
        assertEquals("Teknik Informatika", root.major)
        assertEquals(8, root.semesters.size)
    }

    @Test
    fun testSemesterOneHasRequiredCourses() {
        val semesterOne = repository.getSemester(1)
        assertNotNull(semesterOne)
        assertEquals(4, semesterOne.courses.size)

        val algoCourse = repository.getCourse("cs101")
        assertNotNull(algoCourse)
        assertEquals("Algoritma & Pemrograman Dasar", algoCourse.title)
        assertEquals(16, algoCourse.topics.size)
        assertEquals("Web Programming UNPAS", algoCourse.topics[0].channel)
    }

    @Test
    fun testProgressSummaryCalculation() {
        // Zero completed
        val zeroProgress = ProgressSummary.calculate(0, 16)
        assertEquals(0, zeroProgress.progressPercentage)
        assertEquals(ProgressStatus.NOT_STARTED, zeroProgress.status)

        // 4 of 16 completed -> 25%
        val partialProgress = ProgressSummary.calculate(4, 16)
        assertEquals(25, partialProgress.progressPercentage)
        assertEquals(ProgressStatus.IN_PROGRESS, partialProgress.status)

        // 16 of 16 completed -> 100%
        val fullProgress = ProgressSummary.calculate(16, 16)
        assertEquals(100, fullProgress.progressPercentage)
        assertEquals(ProgressStatus.COMPLETED, fullProgress.status)

        // Empty topics edge case
        val emptyProgress = ProgressSummary.calculate(0, 0)
        assertEquals(0, emptyProgress.progressPercentage)
        assertEquals(ProgressStatus.NOT_STARTED, emptyProgress.status)
    }

    @Test
    fun testCalculateSemesterAndCourseProgress() {
        val completedIds = setOf("t101_01", "t101_02", "t101_03", "t101_04")

        val courseProgress = repository.calculateCourseProgress("cs101", completedIds)
        assertEquals(4, courseProgress.completedTopicsCount)
        assertEquals(16, courseProgress.totalTopicsCount)
        assertEquals(25, courseProgress.progressPercentage)

        val semesterProgress = repository.calculateSemesterProgress(1, completedIds)
        assertEquals(4, semesterProgress.completedTopicsCount)
        assertEquals(64, semesterProgress.totalTopicsCount)
        assertEquals(6, semesterProgress.progressPercentage) // 4 / 64 = 6.25% -> 6%
    }
}
