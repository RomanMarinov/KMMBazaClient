package net.baza.bazanetclientapp

import App
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
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
//import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.permission.AndroidPermissionUtil
import com.mmk.kmpnotifier.permission.permissionUtil


// навигация лакнера проще
// https://www.youtube.com/watch?v=AIC_OFQ1r3k

// room
// https://www.youtube.com/watch?v=IHs0yPa2Nv4

class MainActivity : ComponentActivity() {

    private val permissionUtil by permissionUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            Logger.d("4444 MainActivity(Home) loaded")
            // https://stackoverflow.com/questions/78190854/status-bar-color-change-in-compose-multiplatform
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

            App(
                onMoveToAuthActivity = {
                    startActivity(Intent(this, AuthActivity::class.java))
                },
                onShowIncomingCallActivity = {
                    startActivity(Intent(this, IncomingCallActivity::class.java))
                }
            )
            LifecycleOwnerMainActivity()




//            /////////////////
//            //        /**
////         * По умолчанию значение showPushNotification истинно.
////         * Если для параметра showPushNotification установлено значение false, push-уведомление на переднем плане не будет отображаться пользователю.
////         * Вы по-прежнему можете получать содержимое уведомлений, используя метод прослушивателя #onPushNotification.
////         */
//            NotifierManager.initialize(
//                configuration = NotificationPlatformConfiguration.Android(
//                    notificationIconResId = R.drawable.ic_launcher_foreground,
//                    showPushNotification = true,
//                )
//            )
        }

        AndroidPermissionUtil(this)

        permissionUtil.askNotificationPermission {
            Logger.d("4444 HasNotification Permission: $it")
           // AppLogger.d("HasNotification Permission: $it")
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("4444", " override fun onNewIntent")
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

//    fun onCreateOrOnNewIntent(intent: Intent?) {
//        if (intent == null) return
//        val extras = intent.extras ?: bundleOf()
//        val payloadData = mutableMapOf<String, Any>()
//
//        val isNotificationClicked =
//            extras.containsKey(ACTION_NOTIFICATION_CLICK)
//                    || extras.containsKey(KEY_ANDROID_FIREBASE_NOTIFICATION)
//                    || payloadData.containsKey(ACTION_NOTIFICATION_CLICK)
//
//        extras.keySet().forEach { key ->
//            val value = extras.get(key)
//            value?.let { payloadData[key] = it }
//        }
//
//
//        if (extras.containsKey(KEY_ANDROID_FIREBASE_NOTIFICATION))
//            notification.NotifierManagerImpl.onPushPayloadData(payloadData.minus(ACTION_NOTIFICATION_CLICK))
//        if (isNotificationClicked)
//            notification.NotifierManagerImpl.onNotificationClicked(payloadData.minus(ACTION_NOTIFICATION_CLICK))
//    }


}
//internal fun notification.NotifierManagerImpl.shouldShowNotification(): Boolean {
//    val configuration =
//        notification.NotifierManagerImpl.getConfiguration() as? NotificationPlatformConfiguration.Android
//    return configuration?.showPushNotification ?: true
//}


@Composable
fun LifecycleOwnerMainActivity() {
    val localLifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = localLifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        Log.d("4444", " MainActivity Lifecycle.Event.ON_START")
                    }

                    Lifecycle.Event.ON_STOP -> { // когда свернул
                        Log.d("4444", " MainActivity Lifecycle.Event.ON_STOP")
                    }

                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
                        Log.d("4444", " MainActivity Lifecycle.Event.ON_DESTROY")
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

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}