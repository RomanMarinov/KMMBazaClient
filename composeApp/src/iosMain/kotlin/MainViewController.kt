//import net.baza.bazanetclientapp.notification.configuration.NotificationPlatformConfiguration
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import di.commonModule
//import net.baza.bazanetclientapp.notification.NotifierManagerImpl
import org.koin.core.context.startKoin
import platform.UIKit.UIApplication
import presentation.ui.auth_activity.AuthActivityContent
import presentation.ui.incoming_call_screen_ios.IncomingCallScreenIos

import presentation.ui.splash_activity.SplashActivityContent
import util.SnackBarHostHelper
import util.StartActivity


//@Composable
//fun SplashActivityContent2(onDismiss: () -> Unit) {
//
//    Button(onClick = {
//        onDismiss()
//        UIApplication.sharedApplication.keyWindow?.rootViewController?.dismissViewControllerAnimated(true, completion = null)
//    }) {
//        Text("Dismiss")
//    }
//}
//
//
//fun MainViewController2(onDismiss: () -> Unit) = ComposeUIViewController {
//    SplashActivityContent2(onDismiss)
//}
//
//@Composable
//fun presentSecondaryScreen() {
//    val viewController = MainViewController2(onDismiss = {})
//    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
//    rootViewController?.presentViewController(viewController, animated = true, completion = null)
//}

fun MainViewController() = ComposeUIViewController {
    Logger.d("4444 MainViewController IOS")

    val nextActivityState = rememberSaveable { mutableStateOf(StartActivity.DEFAULT) }
    val snackBarStateAuthPhone = remember { mutableIntStateOf(-1) }
    val snackBarStateAuthWiFi = remember { mutableStateOf("") }
    val snackBarStateWarning = remember { mutableStateOf(false) }

    val payloadDataCustom = remember { mutableStateOf(PayloadDataCustom()) }

//    val configuration =  NotificationPlatformConfiguration.Ios(
//        showPushNotification = true,
//        askNotificationPermissionOnStart = true
//    )
////    NotifierManager.initialize(configuration = configuration)
//
//    NotifierManagerImpl.initialize(configuration = configuration)


    SplashActivityContent(
        onMoveToNextActivity = {
            nextActivityState.value = it // или AUTH_ACTIVITY или MAIN_ACTIVITY
        }
    )

    Logger.d("4444 nextActivityState.value=" + nextActivityState.value)
    when (nextActivityState.value) {
        StartActivity.AUTH_ACTIVITY -> {
            AuthActivityContent(
                onMoveToMainActivity = {
                    nextActivityState.value = StartActivity.MAIN_ACTIVITY
                },
                onShowSnackBarAuthPhone = {
                    snackBarStateAuthPhone.intValue = it
                },
                onShowSnackBarAuthWiFi = {
                    snackBarStateAuthWiFi.value = it
                },
                onShowWarning = {
                    snackBarStateWarning.value = it
                }
            )
        }

        StartActivity.MAIN_ACTIVITY -> {
            App(
                onMoveToAuthActivity = {
                    nextActivityState.value = StartActivity.AUTH_ACTIVITY
                },
                onShowIncomingCallActivity = {
                    payloadDataCustom.value = PayloadDataCustom(
                        type = it.type,
                        address = it.address,
                        imageUrl = it.imageUrl,
                        uuid = it.uuid,
                        videoUrl = it.videoUrl
                    )
                    nextActivityState.value = StartActivity.INCOMING_CALL_IOS
                },
                onPayloadData = {

                }
            )
        }

        StartActivity.INCOMING_CALL_IOS -> {
            //presentSecondaryScreen()
            IncomingCallScreenIos(
                data = payloadDataCustom.value,
                onFinishIncomingCall = {
                    nextActivityState.value = StartActivity.MAIN_ACTIVITY
                },
            )
        }

        StartActivity.DEFAULT -> { // ничего }
        }
    }

    when (snackBarStateAuthPhone.intValue) {
        404 -> {
            Logger.d("4444 MainViewController 404 С указанного номера не было звонка")
            SnackBarHostHelper.WithOkButton(
                message = "С указанного номера не было звонка"
            )
        }

        0 -> {
            Logger.d("4444 MainViewController 0 Ошибка авторизации")
            SnackBarHostHelper.WithOkButton(
                message = "Ошибка авторизации"
            )
        }
    }

    if (snackBarStateWarning.value) {
        SnackBarHostHelper.ShortShortTime(
            message = "Проверьте правильность введенного номера",
            onFinishTime = {
                snackBarStateWarning.value = false
            }
        )
    }

    if (snackBarStateAuthWiFi.value.isNotEmpty()) {
        SnackBarHostHelper.WithOkButton(
            message = snackBarStateAuthWiFi.value
        )
    }


    //var myPushNotificationToken by remember { mutableStateOf("") }
//    LaunchedEffect(true) {
//        println("LaunchedEffectApp is called")
//
//       // myPushNotificationToken = NotifierManager.getPushNotifier().getToken() ?: ""
//      //  Logger.d("4444 myPushNotificationToken=" + myPushNotificationToken)
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNewToken(token: String) {
//                Logger.d("4444 Push Notification ios onNewToken: $token")
//            }
//
//            override fun onPushNotification(title: String?, body: String?) {
//                super.onPushNotification(title, body)
//                Logger.d("4444 Push Notification  ios type message is received: Title: $title and Body: $body")
//            }
//
//            override fun onPayloadData(data: PayloadData) {
//                super.onPayloadData(data)
//                Logger.d("4444 Push Notification ios payloadData: $data")
//            }
//
//            override fun onNotificationClicked(data: PayloadData) {
//                super.onNotificationClicked(data)
//                Logger.d("4444 Notification clicked, ios  Notification payloadData: $data")
//            }
//        })
//    }
}

//@Throws(Exception::class)
fun initKoin() {
//    val configuration =  NotificationPlatformConfiguration.Ios(
//        showPushNotification = true,
//        askNotificationPermissionOnStart = true
//    )
////    NotifierManager.initialize(configuration = configuration)
//
//    NotifierManager.initialize(configuration = configuration)

    Logger.d("4444 MainViewController initKoin IOS")
    startKoin {
        modules(commonModule())
    }
}