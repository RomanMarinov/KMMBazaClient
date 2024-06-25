package presentation.ui.incoming_call_screen_ios

import PayloadDataCustom
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_video_call_accept
import kmm.composeapp.generated.resources.ic_video_call_decline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import presentation.ui.domofon_screen.model.UnLockState
import presentation.ui.webview_screen.WebViewPlatform
import util.ColorCustomResources
import util.SnackBarHostHelper
import kotlin.math.roundToInt

enum class OpenDoorState {
    DOOR_OPENED, ERROR_OPEN, DEFAULT, DECLINE
}

@Composable
fun IncomingCallScreenIos(
    data: PayloadDataCustom,
    onFinishIncomingCall: () -> Unit,
    viewModel: IncomingCallScreenIosViewModel = koinInject()
) {
//    val context = LocalContext.current
//    val activity = LocalContext.current as Activity
//    val viewModel = koinViewModel<IncomingCallViewModel>()
    val isShowSnackBar = remember { mutableStateOf(OpenDoorState.DEFAULT) }
    val scope = rememberCoroutineScope()

//    // Регистрация приемника широковещательных сообщений
//    val filter = IntentFilter("close_call_activity")
//    LocalBroadcastManager.getInstance(this).registerReceiver(closeReceiver, filter)

    val statusDomofonOpenDoor by viewModel.statusDomofonOpenDoor.collectAsStateWithLifecycle()

    //Log.d("4444", " statusDomofonOpenDoor=" + statusDomofonOpenDoor.name)

//    unlockAndTurnOnTheScreen()

//    val address = intent.getStringExtra("address")
//    val imageUrl = intent.getStringExtra("imageUrl")
//    val videoUrl = intent.getStringExtra("videoUrl")
//    val channelID = intent.getStringExtra("channelID")
//    val uuid = intent.getStringExtra("uuid")

    LaunchedEffect(statusDomofonOpenDoor) {
        if (statusDomofonOpenDoor == UnLockState.OPENED_DOOR) {
            isShowSnackBar.value = OpenDoorState.DOOR_OPENED
        } else if (statusDomofonOpenDoor == UnLockState.ERROR_OPEN) {
            isShowSnackBar.value = OpenDoorState.ERROR_OPEN
        }
    }

    CallScreen(
        address = data.address,
        deviceId = data.uuid,
        videoUrl = data.videoUrl,
        onDeclineCall = {

            Logger.d("44444 --------------------------------------")
            isShowSnackBar.value = OpenDoorState.DECLINE
        },
        viewModel = viewModel
    )

    if (isShowSnackBar.value == OpenDoorState.DOOR_OPENED) {
        SnackBarHostHelper.ShortShortTime(
            message = "Дверь открыта",
            onFinishTime = {
                isShowSnackBar.value = OpenDoorState.DEFAULT
            })
        scope.launch {
            delay(1000L)
            onFinishIncomingCall()
        }
//        stopRingtone(context = context)
  //      executeFinishCurrentActivity(activity = activity)
    }
    if (isShowSnackBar.value == OpenDoorState.ERROR_OPEN) {
        SnackBarHostHelper.ShortShortTime(
            message = "Ошибка открытия двери",
            onFinishTime = {
                isShowSnackBar.value = OpenDoorState.DEFAULT
            })
    }
    if (isShowSnackBar.value == OpenDoorState.DECLINE) {
        SnackBarHostHelper.ShortShortTime(
            message = "Звонок отлонен",
            onFinishTime = {
                isShowSnackBar.value = OpenDoorState.DEFAULT
//                onFinishIncomingCall()
            })
        scope.launch {
            delay(1000L)
            onFinishIncomingCall()
        }
    }

    //LifecycleOwnerIncomingCallActivity()
}


@Composable
fun CallScreen(
    address: String?,
    videoUrl: String?,
    deviceId: String?,
    onDeclineCall: () -> Unit,
    viewModel: IncomingCallScreenIosViewModel
) {
    Logger.d("4444 CallScreen IOS загрузился")

//    val context = LocalContext.current
//    val activity = LocalContext.current as Activity

//    UnlockAndTurnOnTheScreen()
    val scope = rememberCoroutineScope()
    val isFinishCallScreen = remember { mutableStateOf(false) }

    IncomingCallContent( // входящий
        address = address,
        videoUrl = videoUrl,
        //context = context,
        //viewModel = viewModel,
        onAcceptCall = {
            scope.launch {
                deviceId?.let {
                    viewModel.onClickUnLock(deviceId = deviceId)
                }
//                delay(2000L)
//                isFinishCallScreen.value = true
            }
        },
        onDeclineCall = {
            onDeclineCall()
            scope.launch {
                delay(1500L)

                isFinishCallScreen.value = true
            }
        }
    )

    if (isFinishCallScreen.value) {
       // stopRingtone(context = context)
        //executeFinishCurrentActivity(activity = activity)
        isFinishCallScreen.value = false
    }
}

@Composable
fun IncomingCallContent(
    address: String?,
    videoUrl: String?,
   // context: Context,
    modifier: Modifier = Modifier,
    // viewModel: CallScreenViewModel,
    onAcceptCall: (Boolean) -> Unit,
    onDeclineCall: (Boolean) -> Unit
) {

    // viewModel.saveHideNavigationBar(true)

   // Log.d("4444", "IncomingCallContent loaded")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Spacer(modifier = Modifier.weight(1f)) // Добавляет пустое пространство вверху с весом 1f

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 88.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                color = Color.White,
                fontSize = 24.sp,
                text = "Звонок в домофон"
//                text = "Мой дружище"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                color = Color.White,
                fontSize = 24.sp,
                text = address ?: "",
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            //    .padding(16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            WebViewPlatform(
                videoUrl = videoUrl
            )
//            IncomingCallWebView(
//                videoUrl = videoUrl
//            )
        }

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//            //    .padding(16.dp)
//            ,
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.jiraf),
//                modifier = Modifier.fillMaxWidth()
//                //    .size(356.dp, 283.dp)
//                ,
//                contentDescription = null,
//                contentScale = ContentScale.Crop
//            )
//        }

        Column(
            modifier = Modifier
                .fillMaxWidth() // Занимаем всю ширину
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AcceptBand(
                    // viewModel = viewModel,
                    onAcceptCall = {
                        onAcceptCall(it)
                    },
                    //context = context
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                DeclineBand(
                    // viewModel = viewModel,
                    onDeclineCall = {
                        onDeclineCall(it)
                    }
                )
            }
        }
    }
}

@Composable
fun AcceptBand(
    onAcceptCall: (Boolean) -> Unit
) {
    val shimmerColorShades = listOf(
        (ColorCustomResources.colorBazaMainBlue).copy(1f),
        (ColorCustomResources.colorBazaMainBlue).copy(0.0f),
        (ColorCustomResources.colorBazaMainBlue).copy(1f)
    )

    val scope = rememberCoroutineScope()

    val width = 300.dp
    val dragSize = 60.dp

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 4000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(20f, 20f),
        end = Offset(translateAnim, translateAnim)
    )

    var offsetX by remember { mutableStateOf(0f) }
    val maxOffset = with(LocalDensity.current) { (width - dragSize).toPx() }
    val halfOffset = maxOffset / 2
    val progress = derivedStateOf { offsetX / maxOffset }
    val animatableOffset = remember { Animatable(0f) }

    LaunchedEffect(offsetX) {
        animatableOffset.snapTo(offsetX)
    }

    Box(
        modifier = Modifier
            .width(width)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        val targetOffset = if (offsetX > halfOffset) maxOffset else 0f
                        scope.launch {
                            animatableOffset.animateTo(
                                targetValue = targetOffset,
                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                            )
                            offsetX = targetOffset
                        }
                    }
                ) { _, dragAmount ->
                    offsetX = (offsetX + dragAmount).coerceIn(0f, maxOffset)
                }
            }
            .background(brush = brush, shape = RoundedCornerShape(dragSize))
    ) {
        Column(
            Modifier
                .align(Alignment.Center)
                .alpha(1f - progress.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Открыть дверь",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        AcceptBall(
            modifier = Modifier
                .offset { IntOffset(animatableOffset.value.roundToInt(), 0) }
                .size(dragSize),
            progress = progress.value,
            onAcceptCall = {
                onAcceptCall(it)
            }
        )
    }
}


@Composable
private fun AcceptBall(
    modifier: Modifier,
    progress: Float,
    onAcceptCall: (Boolean) -> Unit
) {
    val limitState = remember { mutableStateOf(true) }
    Box(
        modifier
            .padding(4.dp)
            .shadow(elevation = 8.dp, CircleShape, clip = false)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val isConfirmed = derivedStateOf { progress >= 0.8f }
        Crossfade(targetState = isConfirmed.value, label = "") {
            if (it) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = ColorCustomResources.colorBazaMainBlue,
                    modifier = Modifier.size(40.dp)
                )

                if (limitState.value && progress >= 1.0) {
                    onAcceptCall(true)
                    limitState.value = false
                }
            } else {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_video_call_accept),
                    contentDescription = null,
                    tint = ColorCustomResources.colorBazaMainBlue,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
@Composable
fun DeclineBand(
    onDeclineCall: (Boolean) -> Unit
) {
    val shimmerColorShades = listOf(
        (ColorCustomResources.colorBazaMainRed).copy(1f),
        (ColorCustomResources.colorBazaMainRed).copy(0.0f),
        (ColorCustomResources.colorBazaMainRed).copy(1f)
    )
val scope = rememberCoroutineScope()
    val width = 300.dp
    val dragSize = 60.dp

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 4000f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(20f, 20f),
        end = Offset(translateAnim, translateAnim)
    )

    val maxOffsetPx = with(LocalDensity.current) { (width - dragSize).toPx() }
    var offsetX by remember { mutableStateOf(maxOffsetPx) } // Start from the right side
    val progress = derivedStateOf { offsetX / maxOffsetPx }
    val animatableOffset = remember { Animatable(maxOffsetPx) }

//    var offsetX by remember { mutableStateOf(0f) } // Start from the right side
    val maxOffset = with(LocalDensity.current) { (width - dragSize).toPx() }
    val halfOffset = maxOffset / 2
//    val progress = derivedStateOf { offsetX / maxOffset }
//    val animatableOffset = remember { Animatable(maxOffset) }

    LaunchedEffect(offsetX) {
        animatableOffset.snapTo(offsetX)
    }

    Box(
        modifier = Modifier
            .width(width)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        val targetOffset = if (offsetX < halfOffset) 0f else maxOffset
                        scope.launch {
                            animatableOffset.animateTo(
                                targetValue = targetOffset,
                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                            )
                            offsetX = targetOffset
                        }
                    }
                ) { _, dragAmount ->
                    offsetX = (offsetX + dragAmount).coerceIn(0f, maxOffset)
                }
            }
            .background(brush = brush, shape = RoundedCornerShape(dragSize))
    ) {
        Column(
            Modifier
                .align(Alignment.Center)
                .alpha(progress.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Отклонить",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        DeclineBall(
            modifier = Modifier
                .offset { IntOffset(animatableOffset.value.roundToInt(), 0) }
                .size(dragSize),
            progress = progress.value,
            onDeclineCall = {
                onDeclineCall(it)
            }
        )
    }
}

@Composable
private fun DeclineBall(
    modifier: Modifier,
    progress: Float,
    onDeclineCall: (Boolean) -> Unit
) {
    val limitState = remember { mutableStateOf(true) }
    Box(
        modifier
            .padding(4.dp)
            .shadow(elevation = 8.dp, CircleShape, clip = false)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val isConfirmed = derivedStateOf { progress <= 0.2f }
        Crossfade(targetState = isConfirmed.value, label = "") {
            if (it) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = ColorCustomResources.colorBazaMainRed,
                    modifier = Modifier.size(40.dp)
                )

                if (limitState.value && progress <= 0.02) {
                    limitState.value = false
                    onDeclineCall(true)
                }
            } else {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = vectorResource(Res.drawable.ic_video_call_decline), // замените на нужный ресурс
                    contentDescription = null,
                    tint = ColorCustomResources.colorBazaMainRed
                )
            }
        }
    }
}


@Composable
fun IncomingCallWebView(
    videoUrl: String?
) {
    // ссылка из домофона рабочая
    val videoUrl1 = "https://sputnikdvr1.baza.net/a8afbbde-981b-492f-8b4c-e1af5edd5b2b/embed.html?dvr=true&token=NzZhOGM1YmU1YmY5N2MyZWUwZWFkN2FkYzI5YjA4MjBlMjA5NDdkOC4xNzE5NjQ2NzAx"
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

//
//@Composable
//fun WebViewScreen(url: String) {
//    val webViewState = rememberWebViewState(url = url)
//    val context = LocalContext.current
//
//    AndroidView(factory = {
//        WebView(context).apply {
//            settings.javaScriptEnabled = true
//            settings.allowFileAccess = true
//            settings.domStorageEnabled = true
//            settings.mediaPlaybackRequiresUserGesture = false
//
//            webViewClient = object : WebViewClient() {
//                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                    super.onPageStarted(view, url, favicon)
//                    Log.d("WebViewScreen", "Loading: $url")
//                }
//
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    super.onPageFinished(view, url)
//                    Log.d("WebViewScreen", "Finished loading: $url")
//                }
//
//                override fun onReceivedError(
//                    view: WebView?,
//                    request: WebResourceRequest?,
//                    error: WebResourceError?
//                ) {
//                    super.onReceivedError(view, request, error)
//                    Log.e("WebViewScreen", "Error: ${error?.description}")
//                }
//            }
//
//            webChromeClient = object : WebChromeClient() {
//                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
//                    Log.d("WebViewScreen", "${consoleMessage?.message()} -- From line " +
//                            "${consoleMessage?.lineNumber()} of " +
//                            "${consoleMessage?.sourceId()}")
//                    return true
//                }
//            }
//        }
//    }, update = { webView ->
//        webView.loadUrl(url)
//    })
//}

@Composable
fun LifecycleOwnerIncomingCallActivity() {
    val localLifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = localLifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        Logger.d("4444 IOS IncomingCallActivity Lifecycle.Event.ON_START")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_START.name)
                        val scope = CoroutineScope(Dispatchers.Main)
                        scope.launch {
                            delay(28000L)
                            //stopRingtone(context = context)
                            //binding.webView.destroy()
                            //executeFinishCurrentActivity(activity = activity)
                        }
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        Logger.d("4444 IOS IncomingCallActivity Lifecycle.Event.ON_RESUME")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_RESUME.name)
                    }

                    Lifecycle.Event.ON_STOP -> { // когда свернул
                        Logger.d("4444 IOS IncomingCallActivity Lifecycle.Event.ON_STOP")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_STOP.name)
                    }

                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
                        Logger.d("4444 IOS IncomingCallActivity Lifecycle.Event.ON_DESTROY")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_DESTROY.name)
                    }

                    else -> {}
                }
            }
            localLifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                localLifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
}

enum class ConfirmationState {
    Default, Confirmed
}
