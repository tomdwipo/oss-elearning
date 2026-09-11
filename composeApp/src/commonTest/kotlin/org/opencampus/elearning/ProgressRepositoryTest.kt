package org.opencampus.elearning

import com.russhwolf.settings.MapSettings
import org.opencampus.elearning.data.LocalProgressRepository
import org.opencampus.elearning.telemetry.DefaultAnalyticsService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProgressRepositoryTest {

    @Test
    fun testEmptyStorageReturnsEmptySet() {
        val settings = MapSettings()
        val repository = LocalProgressRepository(settings)
        assertTrue(repository.getCompletedTopicIds().isEmpty())
        assertFalse(repository.isTopicCompleted("top_01"))
    }

    @Test
    fun testSetAndToggleTopicCompletion() {
        val settings = MapSettings()
        val analytics = DefaultAnalyticsService()
        val repository = LocalProgressRepository(settings, analytics)

        // Set completed
        repository.setTopicCompleted("t101_01", true)
        assertTrue(repository.isTopicCompleted("t101_01"))
        assertEquals(setOf("t101_01"), repository.getCompletedTopicIds())

        // Toggle to false
        val newState = repository.toggleTopicCompletion("t101_01")
        assertFalse(newState)
        assertFalse(repository.isTopicCompleted("t101_01"))
        assertTrue(repository.getCompletedTopicIds().isEmpty())

        // Toggle back to true
        val toggledAgain = repository.toggleTopicCompletion("t101_01")
        assertTrue(toggledAgain)
        assertTrue(repository.isTopicCompleted("t101_01"))
    }

    @Test
    fun testClearAll() {
        val settings = MapSettings()
        val repository = LocalProgressRepository(settings)
        repository.setTopicCompleted("t101_01", true)
        repository.setTopicCompleted("t101_02", true)
        assertEquals(2, repository.getCompletedTopicIds().size)

        repository.clearAll()
        assertTrue(repository.getCompletedTopicIds().isEmpty())
    }
}
