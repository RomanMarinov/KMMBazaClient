package net.baza.bazanetclientapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import presentation.ui.splash_activity.SplashActivityContent
import util.StartActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {
            Log.d("4444", " SplashActivity loaded")
            val nextActivityState = remember { mutableStateOf(StartActivity.DEFAULT) }

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    Color.TRANSPARENT, Color.TRANSPARENT
                ),
                navigationBarStyle = SystemBarStyle.light(
                    Color.TRANSPARENT, Color.TRANSPARENT
                )
            )
            // содержимое вашего приложения располагаться за системными элементами, такими как
            // StatusBar (строка состояния) или NavigationBar (панель навигации) в Android.
            // сначала работало потом изменений не заметил
            WindowCompat.setDecorFitsSystemWindows(window, false)

            SplashActivityContent(
                onMoveToNextActivity = {
                    nextActivityState.value = it
                }
            )

            when (nextActivityState.value) {
                StartActivity.AUTH_ACTIVITY -> {
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }

                StartActivity.MAIN_ACTIVITY -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    //App()
                }

                StartActivity.DEFAULT -> { // ничего }
                }
            }
            LifecycleOwnerSplashActivity()


            // тут не надо
//            NotifierManager.initialize(
//                NotificationPlatformConfiguration.Android(
//                    notificationIconResId = R.drawable.ic_home,
//                    notificationIconColorResId = R.color.color_outdoor_create_shortcut,
//                    notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
//                        id = "CHANNEL_ID_GENERAL",
//                        name = "General"
//                    )
//                )
//            )



//            LaunchedEffect(Unit) {
//                val res = NotifierManager.getPushNotifier().getToken()
//
//                Logger.d("4444 NotifierManager res=" + res)
//            }
        }
    }
}

internal fun NotifierManagerImpl.shouldShowNotification(): Boolean {
    val configuration = NotifierManagerImpl.getConfiguration() as? NotificationPlatformConfiguration.Android
    return configuration?.showPushNotification ?: true
}


@Composable
fun LifecycleOwnerSplashActivity() {



    val localLifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = localLifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        Log.d("4444", " SplashActivity Lifecycle.Event.ON_START")
                    }

                    Lifecycle.Event.ON_STOP -> { // когда свернул
                        Log.d("4444", " SplashActivity Lifecycle.Event.ON_STOP")
                    }

                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
                        Log.d("4444", " SplashActivity Lifecycle.Event.ON_DESTROY")
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