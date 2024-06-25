package presentation.ui.webview_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.setting.PlatformWebSettings
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.util.toKermitSeverity
import com.multiplatform.webview.web.IOSWebView
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WKNavigationDelegate
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import io.kamel.core.utils.URL
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.CoreImage.CIColor.Companion.blackColor
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.WebKit.WKAudiovisualMediaTypeAll
import platform.WebKit.WKAudiovisualMediaTypeNone
import platform.WebKit.WKUIDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.WebKit.javaScriptEnabled
import platform.darwin.NSObject

import platform.UIKit.*
import platform.WebKit.*


import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSError
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL.Companion.URLWithString
import platform.UIKit.UIApplication
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.WebKit.*


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewPlatform(videoUrl: String?) {
    Logger.d("4444 WebViewPlatform ios загружен")
//    // ссылка из домофона рабочая
//    val videoUrl1 =
//        "https://sputnikdvr1.baza.net/a8afbbde-981b-492f-8b4c-e1af5edd5b2b/embed.html?dvr=true&token=NzZhOGM1YmU1YmY5N2MyZWUwZWFkN2FkYzI5YjA4MjBlMjA5NDdkOC4xNzE5NjQ2NzAx"
//    val decodedUrl = UrlEncoderUtil.decode(videoUrl1)
//    // val decodedAddress = UrlEncoderUtil.decode(address ?: "")
//
//    val webViewState = remember { WebViewState(WebContent.Url(decodedUrl)) }
//    //val webViewAddress = remember { WebViewState(WebContent.Url(decodedAddress)) }
//    val webViewNavigator = rememberWebViewNavigator()
//    val webViewJsBridge = remember { WebViewJsBridge() }
//
//    Column(
//        modifier = Modifier
////            .fillMaxSize()
//            .height(220.dp)
//            .background(Color.Black)
//    ) {
//
//        val loadingState = webViewState.loadingState
//        if (loadingState is LoadingState.Loading) {
//            LinearProgressIndicator(
//                progress = { loadingState.progress },
//                modifier = Modifier.fillMaxWidth(),
//            )
//        }
//
//        webViewState.webSettings.apply {
//            isJavaScriptEnabled = true
//            PlatformWebSettings.IOSWebSettings().apply {
//                Logger.d { "4444 WebViewScreen ios webViewState.isLoading=" + webViewState.isLoading }
//                Logger.d { "4444 WebViewScreen ios KLogSeverity.Error=" + com.multiplatform.webview.util.KLogSeverity.Error.toKermitSeverity() }
//                Logger.d { "4444 WebViewScreen ios KLogSeverity.Assert=" + com.multiplatform.webview.util.KLogSeverity.Assert.toKermitSeverity() }
//                Logger.d { "4444 WebViewScreen ios KLogSeverity.Info=" + com.multiplatform.webview.util.KLogSeverity.Info.toKermitSeverity() }
//            }
//        }
//
//        WebView(
//            state = webViewState,
//            modifier = Modifier.height(220.dp),
//            navigator = webViewNavigator,
//            webViewJsBridge = webViewJsBridge,
//            onCreated = {
//                // Вызовется, когда WebView будет создан
//
//            },
//            onDispose = {
//                // Вызовется при удалении WebView
//            }
//        )
//    }

    //    // ссылка из домофона рабочая
    val videoUrl1 =
        "https://sputnikdvr1.baza.net/a8afbbde-981b-492f-8b4c-e1af5edd5b2b/embed.html?dvr=true&token=NzZhOGM1YmU1YmY5N2MyZWUwZWFkN2FkYzI5YjA4MjBlMjA5NDdkOC4xNzE5NjQ2NzAx"

//    UIKitView(
//        modifier = Modifier.fillMaxSize().height(220.dp),
//        factory = {
//            val configuration = WKWebViewConfiguration().apply {
//                allowsInlineMediaPlayback = true
//                mediaTypesRequiringUserActionForPlayback = 0u // Разрешаем автоматическое воспроизведение медиа
//            }
//
//            WKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
//                backgroundColor = UIColor.blackColor // Устанавливаем черный фон
//                loadRequest(NSURLRequest(NSURL(string = videoUrl1)))
//            }
//        },
//        update = { view ->
//            view.loadRequest(NSURLRequest(NSURL(string = videoUrl1)))
//        },
//        interactive = true
//    )


    // работает но фул скрин включается при простом нажатии на экран
    // так же постоянно пауза от любого касания
///////////////////////////////////////////
//    val url = remember { videoUrl1 }
//    var webView = remember { WKWebView() }
//
//    UIKitView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//           // val container = UIView()
//            webView.apply {
//                WKWebViewConfiguration().apply {
//                    backgroundColor = UIColor.blackColor // Устанавливаем черный фон
//                    preferences.javaScriptEnabled()
////                    //  Этот параметр управляет тем, разрешено ли воспроизведение медиаконтента
////                    //  (например, видео) встроенно внутри WKWebView, без перехода на полноэкранный режим.
////                    allowsInlineMediaPlayback = true
////                    //  Этот параметр отвечает за возможность воспроизведения медиаконтента в режиме "картинка в картинке" (Picture in Picture),
////                    //  что позволяет пользователю продолжать просмотр контента в небольшом всплывающем окне, в то время как он работает с другими приложениями.
////                    allowsPictureInPictureMediaPlayback = true
////                    // При установке этого параметра в 0u (который представляет беззнаковое целое число), это отключает требования
////                    // к пользовательскому вмешательству для воспроизведения медиаконтента в WKWebView. Это означает, что медиаконтент будет
////                    // воспроизводиться автоматически без необходимости взаимодействия со стороны пользователя.
////                    mediaTypesRequiringUserActionForPlayback = 0u
//                }
//                // webView = WKWebView(frame, configuration)
//                loadRequest(request = NSURLRequest(NSURL(string = url)))
//            }
//            // В этой строке кода происходит добавление WKWebView в UIView (контейнер).
//            // С использованием метода addSubview, WKWebView становится подпредставлением UIView,
//            // что позволяет отобразить WKWebView внутри этого контейнера, т.е. на экране устройства.
////            container.addSubview(webView)
////            container
//        },
//       // interactive = true,
//        // задает логику изменения размеров встроенных UIKit-представлений при изменении размеров
//        // view: UIView - это представление, которое изменяет размер.
//        // rect: CValue<CGRect> - это новый прямоугольник, представляющий новые размеры.
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            CATransaction.begin() // Начало изменения анимации.
//            // Устанавливается значение, указывающее, что анимация изменения рамки должна быть отключена.
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            //  Устанавливается новый размер рамки для представления.
//            view.layer.setFrame(rect)
//            // Устанавливается новый размер рамки для WKWebView.
//            webView.setFrame(rect)
//            // Окончание изменения анимации.
//            CATransaction.commit()
//        }
//    )
/////////////////////////////////////////////
    // еще один вариант
//    val url = remember { videoUrl1 }
//    var webView = remember {
//        val configuration = WKWebViewConfiguration().apply {
//            allowsInlineMediaPlayback = true
//            allowsPictureInPictureMediaPlayback = true
//            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
//            preferences.javaScriptEnabled = true
//        }
//        WKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
//            backgroundColor = UIColor.blackColor
//        }
//    }
//
//    UIKitView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            val container = UIView()
//            container.addSubview(webView)
//            webView.loadRequest(NSURLRequest(NSURL(string = url)))
//            container
//        },
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            CATransaction.begin()
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            view.setFrame(rect)
//            webView.setFrame(rect)
//            CATransaction.commit()
//        }
//    )

    ///////////////////////////////////////

    // еще один с фул
//    val url = remember { videoUrl1 ?: "" }
//    val fullScreenController = remember { FullscreenWebViewController() }
//    val webView = remember {
//        val configuration = WKWebViewConfiguration().apply {
//            allowsInlineMediaPlayback = true
//            allowsPictureInPictureMediaPlayback = true
//            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
//            preferences.javaScriptEnabled = true
//        }
//        WKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
//            backgroundColor = UIColor.blackColor
//            UIDelegate = object : NSObject(), WKUIDelegateProtocol {
//                override fun webView(
//                    webView: WKWebView,
//                    createWebViewWithConfiguration: WKWebViewConfiguration,
//                    forNavigationAction: WKNavigationAction,
//                    windowFeatures: WKWindowFeatures
//                ): WKWebView {
//                    fullScreenController.webView = WKWebView(frame = CGRectZero.readValue(), configuration = createWebViewWithConfiguration)
//                    fullScreenController.showFrom(currentViewController())
//                    return fullScreenController.webView
//                }
//
//                override fun webViewDidClose(webView: WKWebView) {
//                    fullScreenController.hide()
//                }
//            }
//        }
//    }
//
//    UIKitView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            val container = UIView()
//            container.addSubview(webView)
//            webView.loadRequest(NSURLRequest(NSURL(string = url)))
//            container
//        },
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            CATransaction.begin()
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            view.setFrame(rect)
//            webView.setFrame(rect)
//            CATransaction.commit()
//        }
//    )
    ///////////////////////////

//    val url = remember { videoUrl1 }
//    val webView = remember {
//        val configuration = WKWebViewConfiguration().apply {
//            // разрешено ли воспроизведение медиаконтента встроенно внутри WKWebView, без перехода на полноэкранный режим.
//            allowsInlineMediaPlayback = true
////            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
//            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeAll
//            preferences.javaScriptEnabled = true
//        }
//        WKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
//            backgroundColor = UIColor.blackColor
//        }
//    }
//
//    UIKitView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            webView.apply {
////            val container = UIView()
////            container.addSubview(webView)
//               WKWebViewConfiguration().apply {
//                    backgroundColor = UIColor.blackColor
//                }
//                loadRequest(NSURLRequest(NSURL(string = url)))
//            }
//            webView
////            container
//        },
//        update = {
////            webView.loadRequest(NSURLRequest(NSURL(string = videoUrl1)))
//        },
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            //CATransaction.begin()
//           // CATransaction.setValue(false, kCATransactionDisableActions)
////            view.frame = rect.
////            webView.frame = rect.readValue()
//            //CATransaction.commit()
//        }
//    )

//
//    val decodedUrl = UrlEncoderUtil.decode(videoUrl1)
//    // val decodedAddress = UrlEncoderUtil.decode(address ?: "")
//
//    val webViewState = remember { WebViewState(WebContent.Url(decodedUrl)) }
//    //val webViewAddress = remember { WebViewState(WebContent.Url(decodedAddress)) }
//    val webViewNavigator = rememberWebViewNavigator()
//    val webViewJsBridge = remember { WebViewJsBridge() }
//
//    DisposableEffect(Unit) {
//
//            PlatformWebSettings.IOSWebSettings().apply {
//                backgroundColor = Color.Black
//                webViewState.webSettings.apply {
//                    isJavaScriptEnabled = true
//            }
//        }
//        onDispose { }
//    }
//
//    Column(
//        modifier = Modifier
////            .fillMaxSize()
//            .height(220.dp)
//            .background(Color.Black)
//    ) {
//
////        val loadingState = webViewState.loadingState
////        if (loadingState is LoadingState.Loading) {
////            LinearProgressIndicator(
////                progress = { loadingState.progress },
////                modifier = Modifier.fillMaxWidth(),
////            )
////        }
////        webViewState.webSettings.apply {
////            isJavaScriptEnabled = true
////            PlatformWebSettings.IOSWebSettings().apply {
////                Logger.d { "4444 WebViewScreen ios webViewState.isLoading=" + webViewState.isLoading }
////                Logger.d { "4444 WebViewScreen ios KLogSeverity.Error=" + com.multiplatform.webview.util.KLogSeverity.Error.toKermitSeverity() }
////                Logger.d { "4444 WebViewScreen ios KLogSeverity.Assert=" + com.multiplatform.webview.util.KLogSeverity.Assert.toKermitSeverity() }
////                Logger.d { "4444 WebViewScreen ios KLogSeverity.Info=" + com.multiplatform.webview.util.KLogSeverity.Info.toKermitSeverity() }
////            }
////        }
//
//
////        IOSWebView(
////            state = webViewState,
////            modifier = Modifier.height(220.dp),
////            captureBackPresses = true,
////            navigator = webViewNavigator,
////            webViewJsBridge = webViewJsBridge,
////            onCreated = {
////
////            },
////            onDispose = {
////
////            }
////        )
//
////        IOSWebView(
////            state = webViewState,
////            modifier = Modifier.height(220.dp),
////            navigator = webViewNavigator,
////            webViewJsBridge = webViewJsBridge,
////            onCreated = {
////                // Вызовется, когда WebView будет создан
////
////            },
////            onDispose = {
////                // Вызовется при удалении WebView
////            }
////        )
//
////        WebView(
////            state = webViewState,
////            modifier = Modifier.height(220.dp),
////            navigator = webViewNavigator,
////            webViewJsBridge = webViewJsBridge,
////            onCreated = {
////                // Вызовется, когда WebView будет создан
////
////            },
////            onDispose = {
////                // Вызовется при удалении WebView
////            }
////        )
//    }


//    val state = rememberWebViewState(url = videoUrl1)
////    DisposableEffect(Unit) {
////        state.webSettings.apply {
////            logSeverity = KLogSeverity.Debug
////            customUserAgentString =
////                "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1) AppleWebKit/625.20 (KHTML, like Gecko) Version/14.3.43 Safari/625.20"
////        }
////
////        onDispose { }
////    }
////    val navigator = rememberWebViewNavigator()
////    var textFieldValue by remember(state.lastLoadedUrl) {
////        mutableStateOf(state.lastLoadedUrl)
////    }
//    val loadingState = state.loadingState
//    if (loadingState is LoadingState.Loading) {
//        LinearProgressIndicator(
//            progress = { loadingState.progress },
//            modifier = Modifier.fillMaxWidth(),
//        )
//    }
//
//    WebView(
//        state = state,
//        modifier = Modifier.fillMaxSize(),
//       // navigator = navigator,
//    )


    ////////////////////////////
    // фул после качалки


    val url = remember { videoUrl }
    val webView = remember {
        val configuration = WKWebViewConfiguration().apply {
            allowsInlineMediaPlayback = true

            allowsPictureInPictureMediaPlayback = true
            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
            preferences.javaScriptEnabled = true
            preferences.elementFullscreenEnabled = true
        }
        WKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
            backgroundColor = UIColor.blackColor
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val container = UIView().apply {
                translatesAutoresizingMaskIntoConstraints = false
                // Ignore safe area insets

                insetsLayoutMarginsFromSafeArea = false
                layoutMargins = UIEdgeInsetsZero.readValue()
            }
            webView.apply {
//                WKWebViewConfiguration().apply {
//                    allowsInlineMediaPlayback = true
//                    allowsAirPlayForMediaPlayback = true
//                    allowsPictureInPictureMediaPlayback = true
//                }
                loadRequest(request = NSURLRequest(NSURL(string = url ?: "")))
            }
            container.addSubview(webView)
            container
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.setFrame(rect)
            webView.setFrame(rect)
            CATransaction.commit()
        }
    )


    /////////////////////////

//
//    // Fullscreen controller
//    LaunchedEffect(Unit) {
//        val customWebController = UIViewController().apply {
//            view = UIView().apply {
//                addSubview(webView)
//            }
//            modalPresentationStyle = UIModalPresentationFullScreen
//        }
//        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
//        rootViewController?.presentViewController(customWebController, animated = true, completion = null)
//    }

//    //////////////////////////////////////
//
//    // фул после качалки 2
//    val url = remember { videoUrl1 }
//    var webView = remember {
//        val configuration = WKWebViewConfiguration().apply {
//            allowsInlineMediaPlayback = true
//            allowsPictureInPictureMediaPlayback = true
//            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
//            preferences.javaScriptEnabled = true
//            preferences.elementFullscreenEnabled = true
//        }
//        FullScreenWKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
//            backgroundColor = UIColor.blackColor
//        }
//    }
//
//    UIKitView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            val container = UIView()
//            container.addSubview(webView)
//            webView.loadRequest(NSURLRequest(NSURL(string = url)))
//            container
//        },
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            CATransaction.begin()
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            view.setFrame(rect)
//            webView.setFrame(rect)
//            CATransaction.commit()
//        }
//    )

    //////////////////////////////////////

//    // фул после качалки 3
//    val url = remember { videoUrl1 }
//    var webView = remember {
//        WKWebViewConfiguration().apply {
//            allowsInlineMediaPlayback = true
//            allowsPictureInPictureMediaPlayback = true
//            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
//            preferences.javaScriptEnabled = true
//            preferences.elementFullscreenEnabled = true
//        }
////        FullScreenWKWebView(frame = CGRectZero.readValue(), configuration = configuration).apply {
////            backgroundColor = UIColor.blackColor
////        }
//    }
//
//    UIKitView(
//        modifier = Modifier.fillMaxSize(),
//        factory = {
//            val container = UIView()
//            container.addSubview(webView)
//            webView.loadRequest(NSURLRequest(NSURL(string = url)))
//            container
//        },
//        onResize = { view: UIView, rect: CValue<CGRect> ->
//            CATransaction.begin()
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            view.setFrame(rect)
//            webView.setFrame(rect)
//            CATransaction.commit()
//        }
//    )
}


@OptIn(ExperimentalForeignApi::class)
class FullScreenWKWebView @OptIn(ExperimentalForeignApi::class) constructor(
    frame: CValue<CGRect>,
    configuration: WKWebViewConfiguration
) : WKWebView(frame, configuration) {
    @ExperimentalForeignApi
    override fun safeAreaInsets(): CValue<UIEdgeInsets> {
        return UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)
    }
}

//fun currentViewController(): UIViewController {
//    // Метод для получения текущего контроллера представления
//    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
//    var currentViewController = rootViewController
//    while (currentViewController?.presentedViewController != null) {
//        currentViewController = currentViewController.presentedViewController
//    }
//    return currentViewController!!
//}
//
//class FullscreenWebViewController : UIViewController(null, null) {
//    lateinit var webView: WKWebView
//
//    @OptIn(ExperimentalForeignApi::class)
//    override fun viewDidLoad() {
//        super.viewDidLoad()
//        view.backgroundColor = UIColor.blackColor
//        view.addSubview(webView)
//        webView.setFrame(view.bounds)
//        webView.autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
//    }
//
//    fun showFrom(parentViewController: UIViewController) {
//        parentViewController.presentViewController(this, animated = true, completion = null)
//    }
//
//    fun hide() {
//        dismissViewControllerAnimated(true, completion = null)
//    }
//}