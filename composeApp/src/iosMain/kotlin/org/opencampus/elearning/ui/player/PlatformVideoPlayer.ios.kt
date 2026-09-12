package org.opencampus.elearning.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

private class IosVideoScriptHandler(
    private val onProgress: (Float, Float) -> Unit,
    private val onErr: (Int, String) -> Unit
) : NSObject(), WKScriptMessageHandlerProtocol {
    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        val body = didReceiveScriptMessage.body as? String ?: return
        try {
            // Simple comma-separated message: "progress,current,total" or "error,code,msg"
            val parts = body.split(":")
            if (parts.isNotEmpty()) {
                when (parts[0]) {
                    "progress" -> {
                        if (parts.size >= 3) {
                            val cur = parts[1].toFloatOrNull() ?: 0f
                            val total = parts[2].toFloatOrNull() ?: 0f
                            onProgress(cur, total)
                        }
                    }
                    "error" -> {
                        val code = parts.getOrNull(1)?.toIntOrNull() ?: -1
                        val msg = parts.getOrNull(2) ?: "Player Error"
                        onErr(code, msg)
                    }
                }
            }
        } catch (e: Exception) {
            println("[IosPlatformVideoPlayer] Script error: ${e.message}")
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformVideoPlayer(
    videoId: String,
    playbackSpeed: Float,
    isPlaying: Boolean,
    onProgressUpdate: (currentSec: Float, totalSec: Float) -> Unit,
    onError: (errorCode: Int, message: String) -> Unit,
    modifier: Modifier
) {
    val htmlContent = remember(videoId) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                html, body { width: 100%; height: 100%; margin: 0; padding: 0; background-color: #1E1E2E; overflow: hidden; }
                #player { position: fixed; top: 0; left: 0; width: 100%; height: 100%; }
            </style>
        </head>
        <body>
            <div id="player"></div>
            <script>
                var tag = document.createElement('script');
                tag.src = "https://www.youtube.com/iframe_api";
                var firstScriptTag = document.getElementsByTagName('script')[0];
                firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                var player;
                var progressTimer = null;

                function onYouTubeIframeAPIReady() {
                    player = new YT.Player('player', {
                        videoId: '$videoId',
                        playerVars: {
                            'autoplay': 1,
                            'playsinline': 1,
                            'rel': 0,
                            'modestbranding': 1,
                            'controls': 1,
                            'enablejsapi': 1,
                            'fs': 1,
                            'origin': 'https://localhost'
                        },
                        events: {
                            'onReady': onPlayerReady,
                            'onStateChange': onPlayerStateChange,
                            'onError': onPlayerError
                        }
                    });
                }

                function onPlayerReady(event) {
                    // Ready
                }

                function onPlayerStateChange(event) {
                    if (event.data === YT.PlayerState.PLAYING) {
                        startProgressTimer();
                    } else {
                        stopProgressTimer();
                    }
                }

                function startProgressTimer() {
                    stopProgressTimer();
                    progressTimer = setInterval(function() {
                        if (player && player.getCurrentTime && player.getDuration) {
                            var cur = player.getCurrentTime() || 0;
                            var dur = player.getDuration() || 0;
                            if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.videoBridge) {
                                window.webkit.messageHandlers.videoBridge.postMessage("progress:" + cur + ":" + dur);
                            }
                        }
                    }, 500);
                }

                function stopProgressTimer() {
                    if (progressTimer) {
                        clearInterval(progressTimer);
                        progressTimer = null;
                    }
                }

                function onPlayerError(event) {
                    var code = event.data;
                    var msg = "YouTube Player Error: " + code;
                    if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.videoBridge) {
                        window.webkit.messageHandlers.videoBridge.postMessage("error:" + code + ":" + msg);
                    }
                }

                function setPlaybackSpeed(speed) {
                    if (player && player.setPlaybackRate) {
                        player.setPlaybackRate(speed);
                    }
                }
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    val handler = remember(onProgressUpdate, onError) {
        IosVideoScriptHandler(onProgressUpdate, onError)
    }

    Box(
        modifier = modifier
            .aspectRatio(16f / 9f)
            .background(Color(0xFF1E1E2E))
    ) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                val config = WKWebViewConfiguration().apply {
                    allowsInlineMediaPlayback = true
                    mediaTypesRequiringUserActionForPlayback = 0u
                    userContentController.addScriptMessageHandler(handler, "videoBridge")
                }
                val webView = WKWebView(frame = platform.CoreGraphics.CGRectMake(0.0, 0.0, 0.0, 0.0), configuration = config)
                val baseUrl = NSURL.URLWithString("https://localhost")
                webView.loadHTMLString(htmlContent, baseUrl)
                webView
            },
            update = { webView ->
                webView.evaluateJavaScript("if (typeof setPlaybackSpeed === 'function') { setPlaybackSpeed($playbackSpeed); }", null)
            }
        )
    }
}
