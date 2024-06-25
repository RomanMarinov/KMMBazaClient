package presentation.ui.webview_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.setting.PlatformWebSettings
import com.multiplatform.webview.util.toKermitSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import net.baza.bazanetclientapp.R
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import util.ColorCustomResources
import util.ProgressBarHelper

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebViewPlatform(videoUrl: String?) {

    // ссылка из домофона рабочая
    val videoUrl1 = "https://sputnikdvr1.baza.net/a8afbbde-981b-492f-8b4c-e1af5edd5b2b/embed.html?dvr=true&token=NzZhOGM1YmU1YmY5N2MyZWUwZWFkN2FkYzI5YjA4MjBlMjA5NDdkOC4xNzE5NjQ2NzAx"
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AndroidView(factory = {
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.allowFileAccess = true
                    settings.domStorageEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            android.R.color.black
                        )
                    ) // Устанавливаем черный фон

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            Log.d("WebViewScreen", "Loading: $url")
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            Log.d("WebViewScreen", "Finished loading: $url")
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            Log.e("WebViewScreen", "Error: ${error?.description}")
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        private var mCustomView: View? = null
                        private var mCustomViewCallback: CustomViewCallback? = null
                        private var mOriginalOrientation = 0
                        private var mOriginalSystemUiVisibility = 0
                        override fun getDefaultVideoPoster(): Bitmap {
                            val drawable =
                                ContextCompat.getDrawable(context, R.drawable.ic_webview_logo)
                            val bitmap = Bitmap.createBitmap(
                                drawable?.intrinsicWidth ?: 0,
                                drawable?.intrinsicHeight ?: 0,
                                Bitmap.Config.ARGB_8888
                            )
                            val canvas = Canvas(bitmap)
                            drawable?.setBounds(0, 0, canvas.width, canvas.height)
                            drawable?.draw(canvas)
                            return bitmap
                        }

                        override fun onHideCustomView() {
                            (activity.window.decorView as FrameLayout).removeView(mCustomView)
                            mCustomView = null
                            activity.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
                            activity.requestedOrientation = mOriginalOrientation
                            mCustomViewCallback!!.onCustomViewHidden()
                            mCustomViewCallback = null
                        }

                        override fun onShowCustomView(
                            paramView: View,
                            paramCustomViewCallback: CustomViewCallback
                        ) {
                            if (mCustomView != null) {
                                onHideCustomView()
                                return
                            }
                            mCustomView = paramView
                            mOriginalSystemUiVisibility = activity.window.decorView.systemUiVisibility
                            mOriginalOrientation = activity.requestedOrientation
                            mCustomViewCallback = paramCustomViewCallback
                            (activity.window.decorView as FrameLayout).addView(
                                mCustomView,
                                FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT
                                )
                            )
                            activity.window.decorView.systemUiVisibility = (
                                    View.SYSTEM_UI_FLAG_FULLSCREEN
                                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    )
                        }

                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            Log.d(
                                "WebViewScreen", "${consoleMessage?.message()} -- From line " +
                                        "${consoleMessage?.lineNumber()} of " +
                                        "${consoleMessage?.sourceId()}"
                            )
                            return true
                        }
                    }
                }
            }, update = { webView ->
                webView.loadUrl(videoUrl1)
            })
        }

        if (isLoading) {
            Log.d("4444", " заходит епта")
            ProgressBarHelper.Start(
                trackColor = Color.White,
                mainColor = ColorCustomResources.colorBazaMainBlue
            )
        }
    }
}

@Composable
fun IncomingCallWebView(
    videoUrl: String?
) {
    // ссылка из домофона рабочая
    val videoUrl1 =
        "https://sputnikdvr1.baza.net/a8afbbde-981b-492f-8b4c-e1af5edd5b2b/embed.html?dvr=true&token=NzZhOGM1YmU1YmY5N2MyZWUwZWFkN2FkYzI5YjA4MjBlMjA5NDdkOC4xNzE5NjQ2NzAx"
    val decodedUrl = UrlEncoderUtil.decode(videoUrl1)
    // val decodedAddress = UrlEncoderUtil.decode(address ?: "")

    val webViewState = remember { WebViewState(WebContent.Url(decodedUrl)) }
    //val webViewAddress = remember { WebViewState(WebContent.Url(decodedAddress)) }
    val webViewNavigator = rememberWebViewNavigator()
    val webViewJsBridge = remember { WebViewJsBridge() }

    Column(
        modifier = Modifier
//            .fillMaxSize()
            .height(220.dp)
            .background(Color.Black)
    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 30.dp)
//                .background(Color.Black),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                imageVector = vectorResource(Res.drawable.ic_back),
//                contentDescription = "back",
//                tint = Color.White,
//                modifier = Modifier
//                    .size(60.dp)
//                    .padding(start = 16.dp, top = 30.dp, end = 16.dp)
//                    .clickable {
//
//                    }
//            )
//
//
//            Text(
//                text = address ?: "",
//                color = Color.White,
//                modifier = Modifier
//                    .padding(top = 30.dp, end = 16.dp)
//            )
//        }

        val loadingState = webViewState.loadingState
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = { loadingState.progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }


        webViewState.webSettings.apply {
            isJavaScriptEnabled = true

            PlatformWebSettings.AndroidWebSettings().apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                isAlgorithmicDarkeningAllowed = true
                safeBrowsingEnabled = true
                Logger.d { "4444 WebViewScreen android webViewState.isLoading=" + webViewState.isLoading }
                Logger.d { "4444 WebViewScreen android KLogSeverity.Error=" + com.multiplatform.webview.util.KLogSeverity.Error.toKermitSeverity() }
                Logger.d { "4444 WebViewScreen android KLogSeverity.Assert=" + com.multiplatform.webview.util.KLogSeverity.Assert.toKermitSeverity() }
                Logger.d { "4444 WebViewScreen android KLogSeverity.Info=" + com.multiplatform.webview.util.KLogSeverity.Info.toKermitSeverity() }
            }
            PlatformWebSettings.IOSWebSettings().apply {


                Logger.d { "4444 WebViewScreen ios webViewState.isLoading=" + webViewState.isLoading }
                Logger.d { "4444 WebViewScreen ios KLogSeverity.Error=" + com.multiplatform.webview.util.KLogSeverity.Error.toKermitSeverity() }
                Logger.d { "4444 WebViewScreen ios KLogSeverity.Assert=" + com.multiplatform.webview.util.KLogSeverity.Assert.toKermitSeverity() }
                Logger.d { "4444 WebViewScreen ios KLogSeverity.Info=" + com.multiplatform.webview.util.KLogSeverity.Info.toKermitSeverity() }
            }
        }

        WebView(
            state = webViewState,
            modifier = Modifier.height(220.dp),
            navigator = webViewNavigator,
            webViewJsBridge = webViewJsBridge,
            onCreated = {
                // Вызовется, когда WebView будет создан

            },
            onDispose = {
                // Вызовется при удалении WebView
            }
        )
    }
}
