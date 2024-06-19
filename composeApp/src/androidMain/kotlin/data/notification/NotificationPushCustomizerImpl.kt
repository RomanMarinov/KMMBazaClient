package data.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.squareup.picasso.Picasso
import data.data_store.AppPreferencesRepository
import domain.notification.NotificationPushCustomizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.baza.bazanetclientapp.IncomingCallActivity
import net.baza.bazanetclientapp.R
import net.baza.bazanetclientapp.service.BroadcastReceiverNotification
import net.baza.bazanetclientapp.service.RingtoneService
import util.LifeCycleState

class NotificationPushCustomizerImpl(
    private val context: Context,
    private val appPreferencesRepository: AppPreferencesRepository

) : NotificationPushCustomizer {

    private val notifierManager by lazy { NotifierManager }
    private val notifier: Notifier by lazy { notifierManager.getLocalNotifier() }
//    private val channelId = NotificationPlatformConfiguration.Android.NotificationChannelData().id
    private val channelId = "1"
    private val channelName = "channel_name"
    private val notificationId = 0
    private var isCollectEnabledForCall = false
    private var isCollectEnabledForMissedCall = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun showNotificationCall(
        address: String,
        imageUrl: String,
        uuid: String,
        //title: String,
        videoUrl: String
    ) {

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            chooseTypeNotification(
                address = address,
                imageUrl = imageUrl,
                uuid = uuid,
                // title = title,
                videoUrl = videoUrl
            )
        }

//        notifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPushNotification(title: String?, body: String?) {
//                super.onPushNotification(title, body)
//
//            }
//        })
//
//        //  notify(id: Int, title: String, body: String, payloadData: Map<String, String>) {
//        notifierManager.getPermissionUtil().hasNotificationPermission {
//            if (it.not())
//                Logger.d(
//                    "4444 AndroidNotifier Нужно спрашивать время выполнения " +
//                            "разрешение на уведомление (Manifest.permission.POST_NOTIFICATIONS) в вашей деятельности"
//                )
//        }
//
//        val res = NotificationManager.
//
//        val notificationManager = context.notificationManager ?: return
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
//        notificationManager.notify(id, notification)
//        //  }
////        notifier.notify(
////            id =
////        )
//
//        notifierManager.addListener(object : NotifierManager.Listener {
//            override fun onPayloadData(data: PayloadData) {
//                super.onPayloadData(data)
//                Logger.d("4444 MyFirebaseMessagingService android payloadData: $data")
//
//                val payloadData = message.data
//                message.notification?.let {
//                    // if (notifierManager.shouldShowNotification())
//                    notifier.notify(
//                        title = it.title ?: "",
//                        body = it.body ?: "",
//                        payloadData = payloadData
//                    )
//                    onPushNotification(title = it.title, body = it.body)
//                }
//
//
//            }
//        })
//
//
////
////        val payloadData = message.data
////        message.notification?.let {
////            if (notifierManager.shouldShowNotification())
////                notifier.notify(
////                    title = it.title ?: "",
////                    body = it.body ?: "",
////                    payloadData = payloadData
////                )
////
////            notifierManager.onPushNotification(title = it.title, body = it.body)
////        }
////        if (payloadData.isNotEmpty()) {
////            val data =
////                payloadData + mapOf(Constants.ACTION_NOTIFICATION_CLICK to Constants.ACTION_NOTIFICATION_CLICK)
////            notifierManager.onPushPayloadData(data)
////        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun chooseTypeNotification(
        address: String,
        imageUrl: String,
        uuid: String,
//        title: String,
        videoUrl: String
    ) {
//        // Log.d("4444", " showNotificationCall notificationsAppEnabled=" + notificationsAppEnabled)
//        val powerManager = context.getSystemService(FirebaseMessagingService.POWER_SERVICE) as PowerManager
//        if (!powerManager.isInteractive) { // if screen is not already on, turn it on (get wake_lock)
//            @SuppressLint("InvalidWakeLockTag") val wl = powerManager.newWakeLock(
//                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE or PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
//                "id:wakeupscreen"
//            )
//            wl.acquire(1000L)
//        }
        // ringtoneStart()

        // написать условие если нет разрешения показывать по верх экрана
        // то отрисовать пуш с сообщением Звонок в домофон Включить показ на экране смартфона

        // есть ли у приложения разрешение на отображение наложений поверх других приложений
        val hasOverlayPermission = Settings.canDrawOverlays(context)

        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val isScreenLocked =
            keyguardManager.isDeviceLocked // позволяет получить информацию о состоянии блокировки экрана
        Log.d(
            "4444",
            " BroadcastReceiver Notification hasOverlayPermission=" + hasOverlayPermission + " isScreenLocked=" + isScreenLocked
        )

        if (isScreenLocked) { // экран погашен
            if (hasOverlayPermission) {
                showIncomingCallActivity(
                    address = address,
                    imageUrl = imageUrl,
                    videoUrl = videoUrl,
                    uuid = uuid
                )
            } else {
                // отрисовать пуш
                showPushCall(
                    //title = title,
                    address = address,
                    imageUrl = imageUrl,
                    videoUrl = videoUrl,
                    uuid = uuid,
                    context
                )
            }
        } else { // экран включен
            Log.d(
                "4444",
                " 222 BroadcastReceiver Notification hasOverlayPermission=" + hasOverlayPermission + " isScreenLocked=" + isScreenLocked
            )

            var stateTemp: String
            val res = appPreferencesRepository.fetchInitialPreferences().lifeCycleState
            runBlocking {
                stateTemp = res
            }
            Log.d("4444", " Constants.LifeCycleState.stateTemp=" + stateTemp)
            when (stateTemp) {
                LifeCycleState.ON_START.name, LifeCycleState.ON_RESUME.name -> {
                    Log.d("4444", " Constants.LifeCycleState.ON_START")
                    showIncomingCallActivity(
                        address = address,
                        imageUrl = imageUrl,
                        videoUrl = videoUrl,
                        uuid = uuid
                    )
                }

                LifeCycleState.ON_STOP.name -> {
                    Log.d("4444", " Constants.LifeCycleState.ON_STOP")
                    if (hasOverlayPermission) {
                        showIncomingCallActivity(
                            address = address,
                            imageUrl = imageUrl,
                            videoUrl = videoUrl,
                            uuid = uuid
                        )
                    } else {
                        showPushCall(
                            // title = title,
                            address = address,
                            imageUrl = imageUrl,
                            videoUrl = videoUrl,
                            uuid = uuid,
                            context
                        )
                    }
                }

                LifeCycleState.ON_DESTROY.name -> {
                    Log.d("4444", " Constants.LifeCycleState.ON_DESTROY")
                    if (hasOverlayPermission) {
                        showIncomingCallActivity(
                            address = address,
                            imageUrl = imageUrl,
                            videoUrl = videoUrl,
                            uuid = uuid
                        )
                    } else {
                        showPushCall(
                            // title = title,
                            address = address,
                            imageUrl = imageUrl,
                            videoUrl = videoUrl,
                            uuid = uuid,
                            context
                        )
                    }
                }
            }
        }
    }

    private fun showIncomingCallActivity(
        address: String,
        imageUrl: String,
        videoUrl: String,
        uuid: String
    ) {
        unlockScreen()

        ringtoneStart(context = context)

        val intent = Intent(context, BroadcastReceiverNotification::class.java)
        intent.action = "full_screen"
        intent.putExtra("address", address)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("videoUrl", videoUrl)
        intent.putExtra("channelID", channelId)
        intent.putExtra("uuid", uuid)
        context.sendBroadcast(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showPushCall(
        //title: String,
        address: String,
        imageUrl: String,
        videoUrl: String,
        uuid: String,
        context: Context
    ) {
        Log.d("4444", " showPushCall")

        // проверяю есть ли разрешения для уведомлений (true / false)
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                unlockScreen()

                ringtoneStart(context = context)

                createNotificationChannel()

                val deleteIntent = Intent(context, BroadcastReceiverNotification::class.java)
                deleteIntent.action = "call_notification_swipe"
                val pendingIntent = PendingIntent.getBroadcast(
                    context, 0, deleteIntent,
                    PendingIntent.FLAG_IMMUTABLE
                ) // вместо PendingIntent.FLAG_IMMUTABLE был 0

                // https://api.baza.net/domofon/preview/0a2a0820-6774-48ea-80bb-a0fd5d04efe0?ts=1670592955&token=YjZhODY2OWJiZTE3NGNhN2Q1NTQ4MjRmZjM2NzgyZmFiNmEzZjE1OC4xNjcxMTk3NzU1
                // val icon = Picasso.get().load(imageUrl).placeholder(R.drawable.img_placeholder_camera_dialog).get()

                val notification = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_logo_push)
                    .setColor(ContextCompat.getColor(context, R.color.colorBazaMainRed))
                    .setLargeIcon(Picasso.get().load(imageUrl).get())
                    .setContentTitle("Входящий звонок") // Заголовок
                    .setContentText(address) // Основной текст
                    .setDeleteIntent(pendingIntent)
//                    .addAction(0, "\uD83D\uDD34 Отклонить", getCallPendingIntentForIconTray(action = "drop",  uuid = uuid))
//                    .addAction(0, "\uD83D\uDFE2 Открыть дверь", getCallPendingIntentForIconTray(action = "open",  uuid = uuid))
//                                            .addAction(0, "\uD83D\uDD34 Отклонить", getCallPendingIntent(context, "drop", address = address, imageUrl = imageUrl, videoUrl = videoUrl, uuid = uuid))
//                                            .addAction(0, "\uD83D\uDFE2 Открыть дверь", getCallPendingIntent(context, "open", address = address, imageUrl = imageUrl, videoUrl = videoUrl, uuid = uuid))
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // Приоритет уведомления
                    .setVibrate(longArrayOf(100, 1000, 200, 340))
                    .setAutoCancel(true) // удаляется после клика
                    .setTicker("Notific")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // звуковой файл по умолчанию для уведомления.
                    .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_LIGHTS) // включает мигание светодиода уведомления
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setContentIntent(
                        getCallPendingIntent(
                            context = context,
                            address = address,
                            imageUrl = imageUrl,
                            videoUrl = videoUrl,
                            uuid = uuid
                        )
                    )
                    .setOngoing(false) // false - уведомление не является постоянным, то есть может быть удалено пользователем.

                // Отображаем уведомление
                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        //return
                    }
                    notify(notificationId, notification.build())
                }
            } catch (e: Exception) {
                Log.d("4444", " try catch notification e=" + e)
            }

        } else {
            val message = context.getString(R.string.notification_allow)
            showToastPermission(toastMessage = message)
        }
    }

    private fun getCallPendingIntent(
        context: Context,
        address: String,
        imageUrl: String,
        videoUrl: String,
        uuid: String
    ): PendingIntent {
        val intent = Intent(context, IncomingCallActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.action = "full_screen"
        intent.putExtra("address", address)
        intent.putExtra("imageUrl", imageUrl)
        intent.putExtra("videoUrl", videoUrl)
        intent.putExtra("channelID", channelId)
        intent.putExtra("uuid", uuid)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getManageOverlayPendingIntent(): PendingIntent {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun showNotificationMissedCall(
        address: String,
        imageUrl: String,
        uuid: String,
        //content: String,
        // title: String,
        // token: String?,
        accessToken: String
    ) {
        isCollectEnabledForMissedCall = true
        val coroutineScope = CoroutineScope(context = Dispatchers.IO)
        coroutineScope.launch {
//            domofonRepository.getDomofonCallSelectable.collect {
//                it?.let {
//                    Log.d(
//                        "4444",
//                        " showNotificationCall isCollectEnabled =" + isCollectEnabledForCall
//                    )
//                    if (isCollectEnabledForMissedCall) {
//                        disableCollectMissedCall()
//                        for (i in it.indices) {
//                            if (uuid == it[i].deviceID && it[i].isSelected == true) {
            // проверяю есть ли разрешения для уведомлений (true / false)
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                try {
                    ringtoneStop()

                    createNotificationChannel()

                    val deleteIntent = Intent(
                        context,
                        BroadcastReceiverNotification::class.java
                    )
                    deleteIntent.action = "missed_call_notification_swipe"
                    // deleteIntent.putExtra("channelID", "111")
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        deleteIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    ) // вместо PendingIntent.FLAG_IMMUTABLE был 0

                    // https://api.baza.net/domofon/preview/0a2a0820-6774-48ea-80bb-a0fd5d04efe0?ts=1670592955&token=YjZhODY2OWJiZTE3NGNhN2Q1NTQ4MjRmZjM2NzgyZmFiNmEzZjE1OC4xNjcxMTk3NzU1
                    // val icon = Picasso.get().load(imageUrl).placeholder(R.drawable.img_placeholder_camera_dialog).get()

                    val notification =
                        NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.drawable.ic_logo_push)
                            .setColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.colorBazaMainRed
                                )
                            )
                            .setLargeIcon(Picasso.get().load(imageUrl).get())
                            .setContentTitle("Пропущенный звонок") // Заголовок
                            .setContentText(address) // Основной текст
                            .setDeleteIntent(pendingIntent)
                            .setStyle(
                                NotificationCompat.BigTextStyle()
                                    .bigText("адрес: $address")
                            )
                            .setPriority(NotificationCompat.PRIORITY_HIGH) // Приоритет уведомления
                            .setVibrate(longArrayOf(100, 1000, 200, 340))
                            .setAutoCancel(true) // удаляется после клика
                            .setTicker("Notific")

                            //.addAction(0, "Открыть", getMissedCallPendingIntent(context))
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI) // звуковой файл по умолчанию для уведомления.
                            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
                            .setDefaults(Notification.DEFAULT_LIGHTS) // включает мигание светодиода уведомления
                            .setCategory(NotificationCompat.CATEGORY_ALARM)
//                                                .setContentIntent(getMissedCallPendingIntent(context))
//                                                .setContentIntent(getMissedCallPendingIntent(context))
                           // .setContentIntent(getMissedCallPendingIntent(context)) // НАДО ВКЛЮЧИТЬ ДЛЯ ПЕРЕХОДА НА ХИСТОРИ КОЛЛ
                            .setOngoing(false) // false - уведомление не является постоянным, то есть может быть удалено пользователем.
                    // Отображаем уведомление
                    with(NotificationManagerCompat.from(context)) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            //return
                        }
                        notify(notificationId, notification.build())
                    }
                } catch (e: Exception) {
                    Log.d(
                        "4444",
                        " try catch showNotificationMissedCall e=" + e
                    )
                }
            } else {
                val message = context.getString(R.string.notification_allow)
                //                                  showToastPermission(toastMessage = message)

                // ShowToastHelper.createToast(message = message, context = context)
            }
            //  }
//                        }
//                        killIncomingCallActivity()
//                    }
//                }
//            }
        }
    }

    private fun unlockScreen() {
        // Log.d("4444", " showNotificationCall notificationsAppEnabled=" + notificationsAppEnabled)
        val powerManager =
            context.getSystemService(FirebaseMessagingService.POWER_SERVICE) as PowerManager
        if (!powerManager.isInteractive) { // if screen is not already on, turn it on (get wake_lock)
            @SuppressLint("InvalidWakeLockTag") val wl = powerManager.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE or PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "id:wakeupscreen"
            )
            wl.acquire(1000L)
        }
    }

    private fun killIncomingCallActivity() {
        val intent = Intent(context, BroadcastReceiverNotification::class.java)
        intent.action = "kill_screen"
        context.sendBroadcast(intent)
    }

    private fun ringtoneStart(context: Context) {
//        if (isSoundEnabled(context = context)) {
//            try {
//                val intent = Intent(context, RingtoneService::class.java)
//                intent.action = RingtoneService.ACTION_PLAY_RINGTONE
//                context.startService(intent)
//                //8999 c.startForegroundService(intent2);
//            } catch (e: Exception) {
//                Log.d("4444", " try catch Ошибка воспроизведения звука звонка: ", e)
//            }
//        }
    }

    private fun ringtoneStop() {
        try {
            val intent = Intent(context, RingtoneService::class.java)
            intent.action = RingtoneService.ACTION_STOP_RINGTONE
            context.startService(intent)
        } catch (e: Exception) {
            Log.d("4444", " try catch Ошибка остановки звука звонка: ", e)
        }
    }

    private fun isSoundEnabled(context: Context): Boolean { // только нажатие на кнопку отключить звук
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode != AudioManager.RINGER_MODE_SILENT
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = android.app.NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.lightColor = Color.BLUE
        channel.enableLights(true)
        channel.vibrationPattern =
            longArrayOf(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000) // 10сек

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    // ЭТОТ ВКЛЮЧИТЬ
//    private fun getMissedCallPendingIntent(context: Context): PendingIntent {
//        Log.d("4444", " типа проверил accessToken")
//        return NavDeepLinkBuilder(context)
//            .setComponentName(MainActivity::class.java)
//            .setGraph(R.navigation.nav_graph)
//            .setDestination(R.id.historyCallFragment)
//            .createPendingIntent()
//    }

//    private fun getMissedCallPendingIntent(context: Context): PendingIntent {
//        val intent = Intent(context, BroadcastReceiverNotification::class.java)
//        intent.action = "click_notification_missed_call"
//        context.sendBroadcast(intent)
//        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//    }

    private fun disableCollectCall() {
        isCollectEnabledForCall = false
    }

    private fun disableCollectMissedCall() {
        isCollectEnabledForMissedCall = false
    }

    private fun showToastPermission(toastMessage: String) {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG)
            val toastView = toast.view?.findViewById<TextView>(android.R.id.message)
            toastView?.gravity = Gravity.CENTER
            toast.setGravity(Gravity.TOP, 0, 20) // Установить гравитацию для показа Toast наверху
            toast.show()
            delay(2500L)
            toast.show()
        }
    }
}