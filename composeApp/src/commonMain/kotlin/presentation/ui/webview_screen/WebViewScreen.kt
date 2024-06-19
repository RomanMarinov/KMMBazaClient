package presentation.ui.webview_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.util.toKermitSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_back
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import org.jetbrains.compose.resources.vectorResource

@Composable
fun WebViewScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController,
    address: String?,
    videoUrl: String?
) {

    val decodedUrl = UrlEncoderUtil.decode(videoUrl ?: "")
   // val decodedAddress = UrlEncoderUtil.decode(address ?: "")

    Logger.d { "webview =" + decodedUrl }

    val webViewState = remember { WebViewState(WebContent.Url(decodedUrl)) }
    //val webViewAddress = remember { WebViewState(WebContent.Url(decodedAddress)) }
    val webViewNavigator = rememberWebViewNavigator()
    val webViewJsBridge = remember { WebViewJsBridge() }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(Color.Black),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_back),
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier
                    .size(60.dp)
                    .padding(start = 16.dp, top = 30.dp, end = 16.dp)
                    .clickable {
                        navHostController.popBackStack()
                    }
            )


            Text(
                text = address ?: "",
                color = Color.White,
                modifier = Modifier
                    .padding(top = 30.dp, end = 16.dp)
            )
        }

        val loadingState = webViewState.loadingState
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = { loadingState.progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }


        webViewState.webSettings.apply {
            isJavaScriptEnabled = true
            androidWebSettings.apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                isAlgorithmicDarkeningAllowed = true
                safeBrowsingEnabled = true

                Logger.d { "4444 WebViewScreen webViewState.isLoading=" + webViewState.isLoading }

                Logger.d { "4444 WebViewScreen KLogSeverity.Error=" + KLogSeverity.Error.toKermitSeverity() }
                Logger.d { "4444 WebViewScreen KLogSeverity.Assert=" + KLogSeverity.Assert.toKermitSeverity() }
                Logger.d { "4444 WebViewScreen KLogSeverity.Info=" + KLogSeverity.Info.toKermitSeverity() }
            }
        }

        WebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize(),
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
