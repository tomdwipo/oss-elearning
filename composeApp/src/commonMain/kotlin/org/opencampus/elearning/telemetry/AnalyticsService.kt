package org.opencampus.elearning.telemetry

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.random.Random

@Serializable
data class ClientEvent(
    val event_name: String,
    val timestamp: Long,
    val trace_id: String,
    val payload: JsonObject
)

interface AnalyticsService {
    fun generateTraceId(action: String): String
    fun logEvent(eventName: String, traceId: String, payload: JsonObject)
    fun getRecordedEvents(): List<ClientEvent>
    fun clearEvents()
}

class DefaultAnalyticsService : AnalyticsService {
    private val recordedEvents = mutableListOf<ClientEvent>()

    override fun generateTraceId(action: String): String {
        val now = currentTimeMillis()
        val randomHex = Random.nextInt(0x100000, 0xFFFFFF).toString(16)
        return "trc_${action}_${now}_$randomHex"
    }

    override fun logEvent(eventName: String, traceId: String, payload: JsonObject) {
        val event = ClientEvent(
            event_name = eventName,
            timestamp = currentTimeMillis(),
            trace_id = traceId,
            payload = payload
        )
        recordedEvents.add(event)
        println("[Telemetry] ${event.event_name} ($traceId) -> ${Json.encodeToString(JsonObject.serializer(), payload)}")
    }

    override fun getRecordedEvents(): List<ClientEvent> = recordedEvents.toList()

    override fun clearEvents() {
        recordedEvents.clear()
    }

    private fun currentTimeMillis(): Long {
        // Fallback epoch millisecond generator without platform-specific dependencies
        return (1725541000000L + (recordedEvents.size * 1000L))
    }
}
