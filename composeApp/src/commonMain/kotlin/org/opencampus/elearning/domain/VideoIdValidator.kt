package org.opencampus.elearning.domain

object VideoIdValidator {
    private val YOUTUBE_ID_REGEX = Regex("^[a-zA-Z0-9_-]{11}$")

    fun isValidYouTubeId(videoId: String): Boolean {
        return YOUTUBE_ID_REGEX.matches(videoId)
    }
}
