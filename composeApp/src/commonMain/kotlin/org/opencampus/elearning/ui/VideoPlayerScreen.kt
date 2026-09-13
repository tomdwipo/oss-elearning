package org.opencampus.elearning.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.domain.model.PlaybackSpeed
import org.opencampus.elearning.ui.components.ReportBrokenVideoDialog
import org.opencampus.elearning.ui.player.PlatformVideoPlayer
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun VideoPlayerScreen(
    courseId: String,
    topicId: String,
    onNavigateBack: () -> Unit,
    viewModel: VideoPlayerViewModel = remember(courseId, topicId) {
        VideoPlayerViewModel(courseId = courseId, topicId = topicId)
    },
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    val topic = uiState.topic

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OpenCampusColors.GrayscaleBgLightGrey
    ) { innerPadding ->
        if (topic == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Topik materi tidak ditemukan",
                    color = OpenCampusColors.GrayscaleHintText,
                    fontSize = 14.sp
                )
            }
            return@Scaffold
        }

        val canonicalYoutubeUrl = "https://www.youtube.com/watch?v=${topic.videoId}"

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Top Navigation Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OpenCampusColors.GrayscaleWhite)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(OpenCampusColors.GrayscaleBgLightGrey)
                            .clickable { onNavigateBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "‹",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = OpenCampusColors.CorporatePurple
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pertemuan ${topic.no.toString().padStart(2, '0')}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OpenCampusColors.CorporatePurple
                        )
                        Text(
                            text = topic.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OpenCampusColors.GrayscaleBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // 2. Video Player / Error Fallback Container
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(androidx.compose.ui.graphics.Color(0xFF1E1E2E)),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isError) {
                        // Fallback UI (Skenario E2)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Video materi sedang diperbarui",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OpenCampusColors.GrayscaleWhite,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.errorMessage ?: "Tautan video tidak dapat dimuat atau telah diubah statusnya.",
                                fontSize = 12.sp,
                                color = OpenCampusColors.GrayscaleHintText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.openReportDialog() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = OpenCampusColors.InformingError
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Laporkan Link Rusak",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OpenCampusColors.GrayscaleWhite
                                )
                            }
                        }
                    } else {
                        PlatformVideoPlayer(
                            videoId = topic.videoId,
                            playbackSpeed = uiState.playbackSpeed.speedMultiplier,
                            isPlaying = uiState.isPlaying,
                            onProgressUpdate = { currentSec, totalSec ->
                                viewModel.onProgressUpdate(currentSec, totalSec)
                            },
                            onError = { errorCode, message ->
                                viewModel.onPlayerError(errorCode, message)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // 3. Playback Speed Selector Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(OpenCampusColors.GrayscaleWhite)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kecepatan:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OpenCampusColors.GrayscaleHintText
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        PlaybackSpeed.values().forEach { speed ->
                            val isSelected = (speed == uiState.playbackSpeed)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) OpenCampusColors.CorporatePurple
                                        else OpenCampusColors.GrayscaleBgLightGrey
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) OpenCampusColors.CorporateDarkPurple
                                        else OpenCampusColors.GrayscaleBorder,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable { viewModel.onPlaybackSpeedSelected(speed) }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = speed.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OpenCampusColors.GrayscaleWhite
                                    else OpenCampusColors.GrayscaleBlack
                                )
                            }
                        }
                    }
                }
            }

            // Report Success Banner (if submitted)
            if (uiState.isReportSubmittedSuccess) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = OpenCampusColors.InformingApproval.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "✅ Laporan berhasil dikirim. Terima kasih!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OpenCampusColors.InformingApproval
                            )
                            Text(
                                text = "✕",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OpenCampusColors.InformingApproval,
                                modifier = Modifier.clickable { viewModel.dismissReportSuccessMessage() }
                            )
                        }
                    }
                }
            }

            // 4. Topic Metadata & Progress Details
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = OpenCampusColors.GrayscaleWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = topic.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = OpenCampusColors.GrayscaleBlack
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎬 ${topic.channel}",
                                modifier = Modifier.weight(1f, fill = false),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = OpenCampusColors.CorporateDarkPurple,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "•",
                                fontSize = 12.sp,
                                color = OpenCampusColors.GrayscaleBorder
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "⏱ Estimasi ${topic.duration}",
                                fontSize = 13.sp,
                                color = OpenCampusColors.GrayscaleHintText,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Manual Checklist Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(OpenCampusColors.GrayscaleBgLightGrey)
                                .clickable {
                                    println("[VideoPlayerScreen] Toggle manual completion for topic: $topicId")
                                    viewModel.toggleManualCompletion()
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (uiState.isCompleted) OpenCampusColors.InformingApproval
                                            else OpenCampusColors.GrayscaleWhite
                                        )
                                        .border(
                                            width = 1.5.dp,
                                            color = if (uiState.isCompleted) OpenCampusColors.InformingApproval
                                            else OpenCampusColors.GrayscaleBorder,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (uiState.isCompleted) {
                                        Text(
                                            text = "✔",
                                            color = OpenCampusColors.GrayscaleWhite,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = "Tandai Selesai (Manual)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OpenCampusColors.GrayscaleBlack,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(9999.dp))
                                    .background(
                                        if (uiState.isCompleted) OpenCampusColors.InformingApproval.copy(alpha = 0.12f)
                                        else OpenCampusColors.GrayscaleSpacer
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (uiState.isCompleted) "Selesai" else "Belum Selesai",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.isCompleted) OpenCampusColors.InformingApproval
                                    else OpenCampusColors.GrayscaleHintText,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }
            }

            // 5. External Actions: YouTube Attribution & Report Broken Link
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button "Tonton di YouTube"
                    Button(
                        onClick = {
                            viewModel.onOpenYouTubeExternal(canonicalYoutubeUrl)
                            try {
                                uriHandler.openUri(canonicalYoutubeUrl)
                            } catch (e: Exception) {
                                println("[VideoPlayerScreen] Cannot open URI: ${e.message}")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OpenCampusColors.CorporatePurple
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "↗ Tonton di YouTube (Atribusi Resmi)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OpenCampusColors.GrayscaleWhite
                        )
                    }

                    // Button "Laporkan Link Rusak"
                    OutlinedButton(
                        onClick = { viewModel.openReportDialog() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = OpenCampusColors.InformingError
                        )
                    ) {
                        Text(
                            text = "⚠️ Laporkan Link Rusak / Bermasalah",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = OpenCampusColors.InformingError
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Modal Dialog: Laporkan Link Rusak
    if (uiState.isReportDialogOpen) {
        ReportBrokenVideoDialog(
            onDismissRequest = { viewModel.closeReportDialog() },
            onSubmitReport = { reason, notes ->
                viewModel.submitReport(reason, notes)
            }
        )
    }
}
