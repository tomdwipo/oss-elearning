package org.opencampus.elearning

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.opencampus.elearning.telemetry.DefaultAnalyticsService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TraceIdAndTelemetryTest {

    private val analytics = DefaultAnalyticsService()

    @Test
    fun testTraceIdFormatMatchesSpecification() {
        val traceId = analytics.generateTraceId("sem_open")
        val regex = Regex("^trc_[a-z_]+_[0-9]+_[0-9a-fA-F]+$")
        assertTrue(regex.matches(traceId), "Trace ID '$traceId' should match pattern 'trc_<action>_<timestamp>_<hex>'")
    }

    @Test
    fun testAllFourClientEventsLoggedWithTraceId() {
        // 1. app_opened
        val traceAppInit = analytics.generateTraceId("app_init")
        analytics.logEvent(
            "app_opened",
            traceAppInit,
            buildJsonObject {
                put("app_version", "0.1.0")
                put("platform", "kmp_cmp")
                put("initial_semester", 1)
            }
        )

        // 2. semester_switched
        val traceSemSwitch = analytics.generateTraceId("sem_switch")
        analytics.logEvent(
            "semester_switched",
            traceSemSwitch,
            buildJsonObject {
                put("previous_semester", 1)
                put("selected_semester", 2)
                put("total_courses", 4)
            }
        )

        // 3. course_opened
        val traceCourseOpen = analytics.generateTraceId("course_open")
        analytics.logEvent(
            "course_opened",
            traceCourseOpen,
            buildJsonObject {
                put("course_id", "cs101")
                put("course_title", "Algoritma & Pemrograman Dasar")
                put("semester", 1)
                put("completed_topics_count", 4)
                put("total_topics_count", 16)
                put("progress_percentage", 25)
            }
        )

        // 4. topic_selected
        val traceTopicSel = analytics.generateTraceId("topic_sel")
        analytics.logEvent(
            "topic_selected",
            traceTopicSel,
            buildJsonObject {
                put("topic_id", "t101_01")
                put("course_id", "cs101")
                put("meeting_number", 1)
                put("video_id", "dQw4w9WgXcQ")
                put("channel_name", "Web Programming UNPAS")
                put("duration_minutes", 18)
            }
        )

        val recorded = analytics.getRecordedEvents()
        assertEquals(4, recorded.size)
        assertEquals("app_opened", recorded[0].event_name)
        assertEquals("semester_switched", recorded[1].event_name)
        assertEquals("course_opened", recorded[2].event_name)
        assertEquals("topic_selected", recorded[3].event_name)

        recorded.forEach { event ->
            assertTrue(event.trace_id.startsWith("trc_"))
            assertTrue(event.timestamp > 0)
        }
    }
}
