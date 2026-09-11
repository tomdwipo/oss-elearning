package org.opencampus.elearning.data

import com.russhwolf.settings.Settings
import org.opencampus.elearning.telemetry.AnalyticsService
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

interface ProgressRepository {
    fun getCompletedTopicIds(): Set<String>
    fun isTopicCompleted(topicId: String): Boolean
    fun setTopicCompleted(topicId: String, isCompleted: Boolean)
    fun toggleTopicCompletion(topicId: String): Boolean
    fun clearAll()
}

class LocalProgressRepository(
    private val settings: Settings = Settings(),
    private val analyticsService: AnalyticsService? = null
) : ProgressRepository {

    private val KEY_COMPLETED_TOPICS = "completed_topics_set"

    override fun getCompletedTopicIds(): Set<String> {
        return try {
            val raw = settings.getString(KEY_COMPLETED_TOPICS, "")
            if (raw.isEmpty()) emptySet() else raw.split(",").filter { it.isNotBlank() }.toSet()
        } catch (e: Exception) {
            // Skenario E5: Corrupted Local Progress Storage / Disk Error
            val traceId = analyticsService?.generateTraceId("storage_err") ?: "trc_storage_err_fallback"
            analyticsService?.logEvent(
                "storage_read_error",
                traceId,
                buildJsonObject {
                    put("error_message", e.message ?: "Unknown storage read exception")
                    put("fallback", "empty_guest_state")
                }
            )
            emptySet()
        }
    }

    override fun isTopicCompleted(topicId: String): Boolean {
        return getCompletedTopicIds().contains(topicId)
    }

    override fun setTopicCompleted(topicId: String, isCompleted: Boolean) {
        val current = getCompletedTopicIds().toMutableSet()
        if (isCompleted) {
            current.add(topicId)
        } else {
            current.remove(topicId)
        }
        try {
            settings.putString(KEY_COMPLETED_TOPICS, current.joinToString(","))
        } catch (e: Exception) {
            val traceId = analyticsService?.generateTraceId("storage_write_err") ?: "trc_storage_write_err"
            analyticsService?.logEvent(
                "storage_write_error",
                traceId,
                buildJsonObject {
                    put("topic_id", topicId)
                    put("error", e.message ?: "Write failed")
                }
            )
        }
    }

    override fun toggleTopicCompletion(topicId: String): Boolean {
        val currentlyCompleted = isTopicCompleted(topicId)
        val targetState = !currentlyCompleted
        setTopicCompleted(topicId, targetState)
        return targetState
    }

    override fun clearAll() {
        settings.remove(KEY_COMPLETED_TOPICS)
    }
}
