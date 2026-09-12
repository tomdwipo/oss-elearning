package org.opencampus.elearning.data

import com.russhwolf.settings.Settings
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.opencampus.elearning.domain.model.VideoReportPayload

interface VideoReportRepository {
    fun submitReport(payload: VideoReportPayload): Boolean
    fun getReports(): List<VideoReportPayload>
    fun clearReports()
}

class LocalVideoReportRepository(
    private val settings: Settings = Settings()
) : VideoReportRepository {

    private val KEY_REPORTS = "video_reports_json"
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override fun submitReport(payload: VideoReportPayload): Boolean {
        return try {
            val currentReports = getReports().toMutableList()
            currentReports.add(payload)
            val encoded = json.encodeToString(ListSerializer(VideoReportPayload.serializer()), currentReports)
            settings.putString(KEY_REPORTS, encoded)
            true
        } catch (e: Exception) {
            println("[VideoReportRepository] Error submitting report: ${e.message}")
            false
        }
    }

    override fun getReports(): List<VideoReportPayload> {
        val raw = settings.getString(KEY_REPORTS, "")
        if (raw.isBlank()) return emptyList()
        return try {
            json.decodeFromString(ListSerializer(VideoReportPayload.serializer()), raw)
        } catch (e: Exception) {
            println("[VideoReportRepository] Error parsing reports: ${e.message}")
            emptyList()
        }
    }

    override fun clearReports() {
        settings.remove(KEY_REPORTS)
    }
}
