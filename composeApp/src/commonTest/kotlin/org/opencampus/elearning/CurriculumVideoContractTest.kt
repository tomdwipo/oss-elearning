package org.opencampus.elearning

import org.opencampus.elearning.data.LocalCurriculumRepository
import org.opencampus.elearning.domain.VideoIdValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CurriculumVideoContractTest {

    private val repository = LocalCurriculumRepository()

    @Test
    fun testCs101AllSixteenTopicsHaveValidYouTubeVideoIds() {
        val course = repository.getCourse("cs101")
        assertNotNull(course, "Course cs101 should exist")
        assertEquals(16, course.topics.size, "cs101 should have 16 topics")

        course.topics.forEach { topic ->
            assertTrue(
                VideoIdValidator.isValidYouTubeId(topic.videoId),
                "Topic #${topic.no} (${topic.id}) videoId '${topic.videoId}' must match 11-char YouTube format"
            )
            assertTrue(
                topic.videoId.length == 11,
                "Topic #${topic.no} videoId length must be exactly 11 characters"
            )
        }
    }

    @Test
    fun testCuratedTopicsChannelAttributionNotEmpty() {
        val semesterOne = repository.getSemester(1)
        assertNotNull(semesterOne, "Semester 1 should exist")

        val curatedCourseIds = listOf("cs101", "cs102", "cs103", "cs104")
        curatedCourseIds.forEach { courseId ->
            val course = semesterOne.courses.find { it.id == courseId }
            assertNotNull(course, "Course $courseId should exist in Semester 1")

            course.topics.forEach { topic ->
                assertTrue(
                    topic.channel.isNotBlank(),
                    "Topic ${topic.id} channel attribution must not be empty"
                )
                assertTrue(
                    topic.title.isNotBlank(),
                    "Topic ${topic.id} title must not be empty"
                )
            }
        }
    }

    @Test
    fun testCuratedKeyTopicsInCs102Cs103Cs104HaveValidVideoIds() {
        val course102 = repository.getCourse("cs102")
        assertNotNull(course102)
        // Check first 7 topics curated in cs102
        course102.topics.take(7).forEach { topic ->
            assertTrue(
                VideoIdValidator.isValidYouTubeId(topic.videoId),
                "cs102 Topic #${topic.no} videoId '${topic.videoId}' must be valid 11-char YouTube ID"
            )
        }

        val course103 = repository.getCourse("cs103")
        assertNotNull(course103)
        // Check first 7 topics curated in cs103
        course103.topics.take(7).forEach { topic ->
            assertTrue(
                VideoIdValidator.isValidYouTubeId(topic.videoId),
                "cs103 Topic #${topic.no} videoId '${topic.videoId}' must be valid 11-char YouTube ID"
            )
        }

        val course104 = repository.getCourse("cs104")
        assertNotNull(course104)
        // Check first 6 topics curated in cs104
        course104.topics.take(6).forEach { topic ->
            assertTrue(
                VideoIdValidator.isValidYouTubeId(topic.videoId),
                "cs104 Topic #${topic.no} videoId '${topic.videoId}' must be valid 11-char YouTube ID"
            )
        }
    }

    @Test
    fun testVideoIdValidatorNegativeCases() {
        // Assert mock IDs or malformed IDs fail
        assertTrue(!VideoIdValidator.isValidYouTubeId("t101_01_v"))
        assertTrue(!VideoIdValidator.isValidYouTubeId("vid_cs102_01"))
        assertTrue(!VideoIdValidator.isValidYouTubeId(""))
        assertTrue(!VideoIdValidator.isValidYouTubeId("short"))
        assertTrue(!VideoIdValidator.isValidYouTubeId("toolongvideoidentifier123"))
        assertTrue(!VideoIdValidator.isValidYouTubeId("invalid!@#$*"))

        // Assert valid cases pass
        assertTrue(VideoIdValidator.isValidYouTubeId("jGyYuQf-GeE"))
        assertTrue(VideoIdValidator.isValidYouTubeId("1FAnrYu7LCM"))
        assertTrue(VideoIdValidator.isValidYouTubeId("-9IyBehKm4g"))
    }
}
