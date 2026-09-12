package org.opencampus.elearning.ui.player

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.opencampus.elearning.ui.theme.OpenCampusColors

class AndroidVideoBridge(
    private val onProgress: (Float, Float) -> Unit,
    private val onErr: (Int, String) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun onProgressUpdate(currentSec: Float, totalSec: Float) {
        mainHandler.post {
            onProgress(currentSec, totalSec)
        }
    }

    @JavascriptInterface
    fun onError(code: Int, message: String) {
        mainHandler.post {
            println("[AndroidPlatformVideoPlayer] onError code=$code, msg=$message")
            onErr(code, message)
        }
    }

    @JavascriptInterface
    fun onStateChange(state: Int) {
        println("[AndroidPlatformVideoPlayer] onStateChange state=$state")
    }
}

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
actual fun PlatformVideoPlayer(
    videoId: String,
    playbackSpeed: Float,
    isPlaying: Boolean,
    onProgressUpdate: (currentSec: Float, totalSec: Float) -> Unit,
    onError: (errorCode: Int, message: String) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val bridge = remember(onProgressUpdate, onError) {
        AndroidVideoBridge(onProgressUpdate, onError)
    }

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
                    if (window.AndroidBridge && window.AndroidBridge.onStateChange) {
                        window.AndroidBridge.onStateChange(1); // Ready
                    }
                }

                function onPlayerStateChange(event) {
                    if (window.AndroidBridge && window.AndroidBridge.onStateChange) {
                        window.AndroidBridge.onStateChange(event.data);
                    }
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
                            if (window.AndroidBridge && window.AndroidBridge.onProgressUpdate) {
                                window.AndroidBridge.onProgressUpdate(cur, dur);
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
                    if (window.AndroidBridge && window.AndroidBridge.onError) {
                        window.AndroidBridge.onError(code, msg);
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

    Box(
        modifier = modifier
            .aspectRatio(16f / 9f)
            .background(androidx.compose.ui.graphics.Color(0xFF1E1E2E))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    setBackgroundColor(Color.parseColor("#1E1E2E"))
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cacheMode = WebSettings.LOAD_DEFAULT
                        userAgentString = "Mozilla/5.0 (Linux; Android 14; Pixel 9 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"
                    }
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()
                    addJavascriptInterface(bridge, "AndroidBridge")
                    loadDataWithBaseURL("https://localhost", htmlContent, "text/html", "UTF-8", null)
                }
            },
            update = { webView ->
                webView.evaluateJavascript("if (typeof setPlaybackSpeed === 'function') { setPlaybackSpeed($playbackSpeed); }", null)
            }
        )
    }
}
