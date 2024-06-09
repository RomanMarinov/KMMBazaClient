package net.baza.bazanetclientapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class IncomingCallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            CallScreen(
               // navController = ,
                recipientName = "recipientName",
                recipientPhone = "recipientPhone",
                senderPhone = "senderPhone",
                typeEvent = "typeEvent"
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CallScreen(
   // navController: NavHostController,
    recipientName: String?,
    recipientPhone: String?,
    senderPhone: String?,
    typeEvent: String?,
) {
    Log.d("4444", " CallScreen загрузился")

    val context = LocalContext.current

    UnlockAndTurnOnTheScreen()

    val recipientNameState by remember { mutableStateOf(recipientName) }
    val recipientPhoneState by remember { mutableStateOf(recipientPhone) }
    val senderPhoneState by remember { mutableStateOf(senderPhone) }
    val typeEventState by remember { mutableStateOf(typeEvent) }

    val scope = rememberCoroutineScope()

    val isFinishCallScreen = remember { mutableStateOf(false) }

    IncomingCallContent( // входящий
        context = context,
        recipientName = recipientNameState,
        recipientPhone = recipientPhoneState,
        //viewModel = viewModel,
        //navController = navController,
        senderPhone = senderPhoneState,
        onAcceptCall = {
            scope.launch {
                delay(500L)
                isFinishCallScreen.value = true
            }
        },
        onDeclineCall = {
            scope.launch {
                delay(500L)
                isFinishCallScreen.value = true
            }
        }
    )

    if (isFinishCallScreen.value) {
        NavigateToHomeScreen()
        isFinishCallScreen.value = false
    }
}

@Composable
fun IncomingCallContent(
    context: Context,
    modifier: Modifier = Modifier,
    recipientName: String?,
    recipientPhone: String?,
    // viewModel: CallScreenViewModel,
   // navController: NavHostController,
    senderPhone: String?,
    onAcceptCall: (Boolean) -> Unit,
    onDeclineCall: (Boolean) -> Unit
) {

    // viewModel.saveHideNavigationBar(true)

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

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Text(
                color = Color.White,
                fontSize = 24.sp,
                text = "Звонок в домофон"
//                text = "Мой дружище"
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp),
                color = Color.White,
                fontSize = 24.sp,
                text = "Адрес: Тестовая ул. 1",
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
            Image(
                painter = painterResource(id = R.drawable.jiraf),
                modifier = Modifier.fillMaxWidth()
                //    .size(356.dp, 283.dp)
                ,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

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
                  //  navController = navController,
                    senderPhone = senderPhone,
                    recipientPhone = recipientPhone,
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
   // navController: NavHostController,
    senderPhone: String?,
    recipientPhone: String?,
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
           // navController = navController,
            senderPhone = senderPhone,
            recipientPhone = recipientPhone,
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
  //  navController: NavHostController,
    senderPhone: String?,
    recipientPhone: String?,
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
