package net.baza.bazanetclientapp

import App
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.Notifier
//import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.AndroidPermissionUtil
import com.mmk.kmpnotifier.permission.PermissionUtil
import com.mmk.kmpnotifier.permission.permissionUtil
import di.koinViewModel
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_home
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.scope.scope
import presentation.ui.home_screen.HomeScreenViewModel
import util.HasNotificationState
import util.LifeCycleState
import util.SnackBarHostHelper
import kotlin.random.Random


// навигация лакнера проще
// https://www.youtube.com/watch?v=AIC_OFQ1r3k

// room
// https://www.youtube.com/watch?v=IHs0yPa2Nv4

class MainActivity : ComponentActivity() {

    private val permissionUtil by permissionUtil()

//    private val viewModel: MainActivityViewModel = getViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = koinViewModel<MainActivityViewModel>()
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

            ////////////////////
            val localLifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(
                key1 = localLifecycleOwner,
                effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_START -> {
                                Log.d("4444", " MainActivity Lifecycle.Event.ON_START")
                                viewModel.saveLifeCycleState(state = LifeCycleState.ON_START.name)
                            }
                            Lifecycle.Event.ON_RESUME -> {
                                Log.d("4444", " MainActivity Lifecycle.Event.ON_RESUME")
                                viewModel.saveLifeCycleState(state = LifeCycleState.ON_RESUME.name)
                            }
                            Lifecycle.Event.ON_STOP -> { // когда свернул
                                Log.d("4444", " MainActivity Lifecycle.Event.ON_STOP")
                                viewModel.saveLifeCycleState(state = LifeCycleState.ON_STOP.name)
                            }
                            Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
                                Log.d("4444", " MainActivity Lifecycle.Event.ON_DESTROY")
                                viewModel.saveLifeCycleState(state = LifeCycleState.ON_DESTROY.name)
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
            //////////////////////
//            LifecycleOwnerMainActivity()


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
            //InitHuinit()




            // пока решил что совсем не надл
//            if (hasNotificationState.value == HasNotificationState.ALLOWED) {
//                SnackBarHostHelper.ShortShortTime(
//                    message = "notifications allowed",
//                    onFinishTime = {
//                        hasNotificationState.value = HasNotificationState.DEFAULT
//                    }
//                )
//            } else if (hasNotificationState.value == HasNotificationState.NOT_ALLOWED) {
//                SnackBarHostHelper.ShortShortTime(
//                    message = "notifications not allowed",
//                    onFinishTime = {
//                        hasNotificationState.value = HasNotificationState.DEFAULT
//                    }
//                )
//            }
        }

        permissionUtil.askNotificationPermission {
            Logger.d("4444 MainActivity HasNotification Permission: $it")
        }

        //  потом включить важно загугли
        //AndroidPermissionUtil(this)


        //  permissionUtil.askNotificationPermission()


//        val res = NotificationPlatformConfiguration
//
//
//
//        AndroidNotifier(
//            context = this,
//            androidNotificationConfiguration =   res  ,
//            notificationChannelFactory = null,
//            permissionUtil = NotifierManager.getPermissionUtil()
//        )
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

//@Composable
//fun InitHuinit() {
//    val configuration = NotificationPlatformConfiguration.Android(
//        notificationIconResId = R.drawable.ic_home,
//        notificationIconColorResId = androidx.compose.ui.graphics.Color.White.toArgb(),
//        notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
//            id = "CHANNEL_ID_GENERAL",
//            name = "General"
//        )
//    )
//    NotifierManager.initialize(configuration = configuration)
//    var myPushNotificationToken by remember { mutableStateOf("") }
//    LaunchedEffect(true) {
//        println("LaunchedEffectApp is called")
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                myPushNotificationToken = token
//                println("onNewToken: $token")
//            }
//        })
//        myPushNotificationToken = NotifierManager.getPushNotifier().getToken() ?: ""
//        Logger.d("4444 myPushNotificationToken=" + myPushNotificationToken)
//    }
//}


@Composable
fun LifecycleOwnerMainActivity(viewModel: MainActivityViewModel) {
//    val localLifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(
//        key1 = localLifecycleOwner,
//        effect = {
//            val observer = LifecycleEventObserver { _, event ->
//                when (event) {
//                    Lifecycle.Event.ON_START -> {
//                        Log.d("4444", " MainActivity Lifecycle.Event.ON_START")
//                        viewModel.saveLifeCycleState(state = LifeCycleState.ON_START.name)
//                    }
//                    Lifecycle.Event.ON_RESUME -> {
//                        Log.d("4444", " MainActivity Lifecycle.Event.ON_RESUME")
//                        viewModel.saveLifeCycleState(state = LifeCycleState.ON_RESUME.name)
//                    }
//                    Lifecycle.Event.ON_STOP -> { // когда свернул
//                        Log.d("4444", " MainActivity Lifecycle.Event.ON_STOP")
//                        viewModel.saveLifeCycleState(state = LifeCycleState.ON_STOP.name)
//                    }
//                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
//                        Log.d("4444", " MainActivity Lifecycle.Event.ON_DESTROY")
//                        viewModel.saveLifeCycleState(state = LifeCycleState.ON_DESTROY.name)
//                    }
//
//                    else -> {}
//                }
//            }
//            localLifecycleOwner.lifecycle.addObserver(observer)
//            onDispose {
//                localLifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }
//    )
}


//// класс для управления отправкой уведомления на android устройстве
//internal class AndroidNotifier(
//    private val context: Context,
//    private val androidNotificationConfiguration: NotificationPlatformConfiguration.Android,
//    private val notificationChannelFactory: NotificationChannelFactory,
//    private val permissionUtil: PermissionUtil,
//) : Notifier {
//
//
//    override fun notify(title: String, body: String, payloadData: Map<String, String>): Int {
//        val notificationID = Random.nextInt(0, Int.MAX_VALUE)
//        notify(notificationID, title, body, payloadData)
//        return notificationID
//    }
//
//    override fun notify(id: Int, title: String, body: String, payloadData: Map<String, String>) {
//        permissionUtil.hasNotificationPermission {
//            if (it.not())
//                Logger.d(
//                    "4444 AndroidNotifier Нужно спрашивать время выполнения " +
//                            "разрешение на уведомление (Manifest.permission.POST_NOTIFICATIONS) в вашей деятельности"
//                )
//        }
//
//
//
////        val notificationManager = context.notificationManager ?: return
//
//
//        val pendingIntent = getPendingIntent(payloadData)
//        notificationChannelFactory.createChannels()
//        val notification = NotificationCompat.Builder(
//            context,
//            androidNotificationConfiguration.notificationChannelData.id
//        ).apply {
//            setSmallIcon(R.drawable.ic_logo_push)
////            setBadgeIconType(R.drawable.ic_home) // не
//            // setLargeIcon(largeIconBitmap) не
//
//            setChannelId(androidNotificationConfiguration.notificationChannelData.id)
//            setContentTitle(title)
//            setContentText(body)
//            // setSmallIcon(androidNotificationConfiguration.notificationIconResId)
//            setAutoCancel(true)
//            setContentIntent(pendingIntent)
//            /////////
//            //  .setLargeIcon(res)
//            setPriority(NotificationCompat.PRIORITY_HIGH) // Приоритет уведомления
//            setVibrate(longArrayOf(100, 1000, 200, 340))
//            setAutoCancel(true) // удаляется после клика
//            setTicker("Notific")
//            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // звуковой файл по умолчанию для уведомления.
//            setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
//            setDefaults(Notification.DEFAULT_LIGHTS) // включает мигание светодиода уведомления
//            setCategory(NotificationCompat.CATEGORY_ALARM)
//            //.setContentIntent(getCallPendingIntent(context = context, address = address, imageUrl = imageUrl, videoUrl = videoUrl, uuid = uuid))
//            setOngoing(false) // false - уведомление не является постоянным, то есть может быть удалено пользователем.
//            //////////
//            androidNotificationConfiguration.notificationIconColorResId?.let {
//                color = ContextCompat.getColor(context, it)
//            }
//        }.build()
//
//
//
//
//        notificationManager.notify(id, notification)
//    }
//
//    override fun remove(id: Int) {
//        val notificationManager = context.notificationManager ?: return
//        notificationManager.cancel(id)
//    }
//
//    override fun removeAll() {
//        val notificationManager = context.notificationManager ?: return
//        notificationManager.cancelAll()
//    }
//
//    private fun getPendingIntent(payloadData: Map<String, String>): PendingIntent? {
//        val intent = Intent(context, IncomingCallActivity::class.java).apply {
//            putExtra(ACTION_NOTIFICATION_CLICK, ACTION_NOTIFICATION_CLICK)
//            payloadData.forEach { putExtra(it.key, it.value) }
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        return PendingIntent.getActivity(context, 0, intent, flags)
//    }
//
//    private fun getLauncherActivityIntent(): Intent? {
//        val packageManager = context.applicationContext.packageManager
//        return packageManager.getLaunchIntentForPackage(context.applicationContext.packageName)
//    }
//}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}