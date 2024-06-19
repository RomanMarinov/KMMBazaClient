package net.baza.bazanetclientapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import co.touchlab.kermit.Logger
import com.multiplatform.webview.jsbridge.WebViewJsBridge
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.util.toKermitSeverity
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
import di.koinViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.baza.bazanetclientapp.service.RingtoneService
import net.baza.bazanetclientapp.ui.IncomingCallViewModel
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import presentation.ui.domofon_screen.model.UnLockState
import util.SnackBarHostHelper
import kotlin.math.roundToInt


enum class OpenDoorState {
    DOOR_OPENED, ERROR_OPEN, DEFAULT, DECLINE
}

class IncomingCallActivity : ComponentActivity() {
    private val closeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "close_call_activity") {
                val shouldClose = intent.getBooleanExtra("close_activity", false)
                if (shouldClose) {
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val activity = LocalContext.current as Activity
            val viewModel = koinViewModel<IncomingCallViewModel>()
            val isShowSnackBar = remember { mutableStateOf(OpenDoorState.DEFAULT) }

            // Регистрация приемника широковещательных сообщений
            val filter = IntentFilter("close_call_activity")
            LocalBroadcastManager.getInstance(this).registerReceiver(closeReceiver, filter)

            val statusDomofonOpenDoor by viewModel.statusDomofonOpenDoor.collectAsStateWithLifecycle()

            Log.d("4444", " statusDomofonOpenDoor=" + statusDomofonOpenDoor.name)

            unlockAndTurnOnTheScreen()

            val address = intent.getStringExtra("address")
            val imageUrl = intent.getStringExtra("imageUrl")
            val videoUrl = intent.getStringExtra("videoUrl")
            val channelID = intent.getStringExtra("channelID")
            val uuid = intent.getStringExtra("uuid")

            LaunchedEffect(statusDomofonOpenDoor) {
                if (statusDomofonOpenDoor == UnLockState.OPENED_DOOR) {
                    isShowSnackBar.value = OpenDoorState.DOOR_OPENED
                } else if (statusDomofonOpenDoor == UnLockState.ERROR_OPEN) {
                    isShowSnackBar.value = OpenDoorState.ERROR_OPEN
                }
            }

            CallScreen(
                address = address,
                deviceId = uuid,
                videoUrl = videoUrl,
                onDeclineCall = {
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
                stopRingtone(context = context)
                executeFinishCurrentActivity(activity = activity)
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
                    })
            }

            LifecycleOwnerIncomingCallActivity()
        }
    }

    // метод нужен если установлен пароль для блокировки экрана
    private fun unlockAndTurnOnTheScreen() {
        Log.d("4444", " unlockAndTurnOnTheScreen выполнился")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) { // для Android выше от 8.1
            Log.d("4444", " unlockAndTurnOnTheScreen выполнился для Android выше от 8.1")
            setShowWhenLocked(true) // позволяет отобразить активити поверх блокировки экрана.
            setTurnScreenOn(true) // позволяет включить экран.
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            //  запрос на отключение блокировки экрана.
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}


@Composable
fun UnlockAndTurnOnTheScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) { // для Android выше от 8.1
        val context = LocalContext.current
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val windowContext = LocalContext.current as? Activity
        windowContext?.apply {
            setShowWhenLocked(true) // позволяет отобразить активити поверх блокировки экрана.
            setTurnScreenOn(true) // позволяет включить экран.
            keyguardManager.requestDismissKeyguard(this, null)
            // при бездействии экран не гаснет
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    } else {
//        val window = LocalWindow.current
//        window.addFlags(
//            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
//                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
//                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//        )
    }
}

// при бездействии экран не гаснет
private fun setKeepScreenOn() {
    // window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CallScreen(
    address: String?,
    videoUrl: String?,
    deviceId: String?,
    onDeclineCall: () -> Unit,
    viewModel: IncomingCallViewModel
) {
    Log.d("4444", " CallScreen загрузился")

    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    UnlockAndTurnOnTheScreen()
    val scope = rememberCoroutineScope()
    val isFinishCallScreen = remember { mutableStateOf(false) }

    IncomingCallContent( // входящий
        address = address,
        videoUrl = videoUrl,
        context = context,
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
        stopRingtone(context = context)
        executeFinishCurrentActivity(activity = activity)
        isFinishCallScreen.value = false
    }
}

@Composable
fun IncomingCallContent(
    address: String?,
    videoUrl: String?,
    context: Context,
    modifier: Modifier = Modifier,
    // viewModel: CallScreenViewModel,
    onAcceptCall: (Boolean) -> Unit,
    onDeclineCall: (Boolean) -> Unit
) {

    // viewModel.saveHideNavigationBar(true)

    Log.d("4444", "IncomingCallContent loaded")

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
                .padding(16.dp),
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
            IncomingCallWebView(
                videoUrl = videoUrl
            )
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
                    context = context
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

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AcceptBand(
    // viewModel: CallScreenViewModel,
    onAcceptCall: (Boolean) -> Unit,
    context: Context,
) {
    val shimmerColorShades = listOf(
        colorResource(R.color.colorBazaMainBlue).copy(1f),
        colorResource(R.color.colorBazaMainBlue).copy(0.0f),
        colorResource(R.color.colorBazaMainBlue).copy(1f)
    )

    val width = 300.dp
    val dragSize = 60.dp

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 4000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            // RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(20f, 20f),
        end = Offset(translateAnim, translateAnim)
    )

    val swipeableState = rememberSwipeableState(ConfirmationState.Default)
    val sizePx = with(LocalDensity.current) { (width - dragSize).toPx() }
    val anchors = mapOf(0f to ConfirmationState.Default, sizePx to ConfirmationState.Confirmed)
    val progress = derivedStateOf {
        if (swipeableState.offset.value == 0f) 0f else swipeableState.offset.value / sizePx
    }


    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .background(brush = brush, RoundedCornerShape(dragSize))
    ) {
        Column(
            Modifier
                .align(Alignment.Center)
                .alpha(1f - progress.value),
            //.alpha(if (progress.value in 0.0f..0.5f) 1f else 0f),
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
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(dragSize),
            progress = progress.value,
            // viewModel = viewModel,
            onAcceptCall = {
                onAcceptCall(it)
            },
            context = context
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun AcceptBall(
    modifier: Modifier,
    progress: Float,
    // viewModel: CallScreenViewModel,
    onAcceptCall: (Boolean) -> Unit,
    context: Context,
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
                    tint = colorResource(id = R.color.colorBazaMainBlue),
                    modifier = Modifier.size(40.dp)
                )

                //  Log.d("4444", " CallScreen onAcceptCall progress=" +progress)

                if (limitState.value && progress >= 1.0) {
                    //  Log.d("4444", " CallScreen onAcceptCall progress=" +progress)
                    onAcceptCall(true)
                    limitState.value = false
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_video_call_accept),
                    contentDescription = null,
                    tint = colorResource(id = R.color.colorBazaMainBlue),
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeclineBand(
    //viewModel: CallScreenViewModel,
    onDeclineCall: (Boolean) -> Unit
) {
//    val shimmerColorShades = listOf(
//        (Color.White).copy(1f),
//        (Color.White).copy(0.0f),
//        (Color.White).copy(1f)
//    )

    val shimmerColorShades = listOf(
        colorResource(R.color.colorBazaMainRed).copy(1f),
        colorResource(R.color.colorBazaMainRed).copy(0.0f),
        colorResource(R.color.colorBazaMainRed).copy(1f)
    )

    val width = 300.dp
    val dragSize = 60.dp

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 4000f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            //RepeatMode.Reverse
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(20f, 20f),
        end = Offset(translateAnim, translateAnim)
    )


    val swipeableState = rememberSwipeableState(ConfirmationState.Default)
    val sizePx = with(LocalDensity.current) { (width - dragSize).toPx() }
    val anchors = mapOf(
        0f to ConfirmationState.Confirmed,
        sizePx to ConfirmationState.Default
    ) // Измените порядок состояний по умолчанию и подтверждения
    val progress = derivedStateOf {
        if (swipeableState.offset.value == 0f) 0f else swipeableState.offset.value / sizePx
    }

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .background(brush, RoundedCornerShape(dragSize))

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
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(dragSize),
            progress = progress.value,
            //viewModel = viewModel,
            onDeclineCall = {
                onDeclineCall(it)
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun DeclineBall(
    modifier: Modifier,
    progress: Float,
    //viewModel: CallScreenViewModel,
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
                    tint = colorResource(id = R.color.colorBazaMainRed),
                    modifier = Modifier.size(40.dp)
                )

                //  Log.d("4444", " isConfirmed progress=" + progress)

                if (limitState.value && progress <= 0.02) {
                    limitState.value = false
                    onDeclineCall(true)
                }


                // viewModel.executeFinishCallScreen(finish = true)
                // тут надо сделать закрытие экрана
            } else {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = R.drawable.ic_video_call_decline),
                    contentDescription = null,
                    tint = colorResource(id = R.color.colorBazaMainRed)
                )
            }
        }
    }
}


@Composable
fun IncomingCallWebView(
    videoUrl: String?
) {
//    val decodedUrl = UrlEncoderUtil.decode(videoUrl ?: "")
    // val decodedAddress = UrlEncoderUtil.decode(address ?: "")

    val decodedUrl = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        decodedUrl.value = UrlEncoderUtil.decode("https://sputnikdvr1.baza.net/a8afbbde-981b-492f-8b4c-e1af5edd5b2b/embed.html?dvr=true&token=YjY1ZDZkMWYwYTI3NTRhYTc5MzZiNWU1Y2E3MjJmOTRlZTlhNzhiMS4xNzE5NDE5ODYy")
    }

    Logger.d { "webview =" + decodedUrl }

    val webViewState = remember { WebViewState(WebContent.Url(decodedUrl.value)) }
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

@Composable
fun LifecycleOwnerIncomingCallActivity() {
    val localLifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val activity = LocalContext.current as Activity


    DisposableEffect(
        key1 = localLifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        Log.d("4444", " IncomingCallActivity Lifecycle.Event.ON_START")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_START.name)
                        val scope = CoroutineScope(Dispatchers.Main)
                        scope.launch {
                            delay(28000L)
                            stopRingtone(context = context)
                            //binding.webView.destroy()
                            executeFinishCurrentActivity(activity = activity)
                        }
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        Log.d("4444", " IncomingCallActivity Lifecycle.Event.ON_RESUME")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_RESUME.name)
                    }

                    Lifecycle.Event.ON_STOP -> { // когда свернул
                        Log.d("4444", " IncomingCallActivity Lifecycle.Event.ON_STOP")
                        //viewModel.saveLifeCycleState(state = LifeCycleState.ON_STOP.name)
                    }

                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
                        Log.d("4444", " IncomingCallActivity Lifecycle.Event.ON_DESTROY")
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

private fun stopRingtone(context: Context) {


    try {
        val intent = Intent(context, RingtoneService::class.java)
        intent.action = RingtoneService.ACTION_STOP_RINGTONE
        context.startService(intent)
        //8999 c.startForegroundService(intent2);
    } catch (e: Exception) {
        Log.d("4444", " try catch Ошибка воспроизведения звука звонка: ", e)
    }
}

private fun executeFinishCurrentActivity(activity: Activity) {
    Log.d("4444", " executeFinishCurrentActivity")
    activity.finish()
}


@Composable
fun NavigateToHomeScreen() {

    val context = LocalContext.current

    // Создание Intent для перехода к другой активности
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    context.startActivity(intent)
    // Завершение текущей активности
}

enum class ConfirmationState {
    Default, Confirmed
}
