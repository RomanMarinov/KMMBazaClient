import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager

import net.baza.bazanetclientapp.notification.configuration.NotificationPlatformConfiguration
import di.commonModule
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_home
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.context.startKoin
import presentation.ui.auth_activity.AuthActivityContent
import presentation.ui.splash_activity.SplashActivityContent
import util.SnackBarHostHelper
import util.StartActivity


fun MainViewController() = ComposeUIViewController {

    val nextActivityState = remember { mutableStateOf(StartActivity.DEFAULT) }
    val snackBarStateAuthPhone = remember { mutableIntStateOf(-1) }
    val snackBarStateAuthWiFi = remember { mutableStateOf("") }
    val snackBarStateWarning = remember { mutableStateOf(false) }






    val configuration =  NotificationPlatformConfiguration.Ios(
        showPushNotification = true,
        askNotificationPermissionOnStart = true
    )

   // NotifierManager.initialize(configuration = configuration)


    SplashActivityContent(
        onMoveToNextActivity = {
            nextActivityState.value = it // или AUTH_ACTIVITY или MAIN_ACTIVITY
        }
    )
    // по идее всегда должна быть true после первой иниц
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

                }
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
}

fun initKoin() {
    startKoin {
        modules(commonModule())
    }
}