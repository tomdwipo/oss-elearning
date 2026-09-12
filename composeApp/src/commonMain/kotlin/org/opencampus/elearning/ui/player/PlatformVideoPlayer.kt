package org.opencampus.elearning.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformVideoPlayer(
    videoId: String,
    playbackSpeed: Float,
    isPlaying: Boolean,
    onProgressUpdate: (currentSec: Float, totalSec: Float) -> Unit,
    onError: (errorCode: Int, message: String) -> Unit,
    modifier: Modifier = Modifier
)
