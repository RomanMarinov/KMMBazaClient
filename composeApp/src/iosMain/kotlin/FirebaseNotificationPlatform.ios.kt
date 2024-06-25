import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import data.data_store.AppPreferencesRepository
import domain.repository.AuthRepository
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import util.TextUtils

@Composable
actual fun FirebaseNotificationPlatform(
    data: PayloadDataCustom,
    onShowIncomingCallActivity: (PayloadDataCustom) -> Unit
) {
    Logger.d("4444 IOS FirebaseNotificationPlatform data type=" + data.type)
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        // logManager.writeLogToDB("Входящее событие FirebaseMessagingService message=" + message)
    }
    val notifierManager by lazy { NotifierManager }
    val notifier: Notifier by lazy { notifierManager.getLocalNotifier() }

//    val notificationPushCustomizer: NotificationPushCustomizer by inject()
    val appPreferencesRepository: AppPreferencesRepository = koinInject()
    val authRepository: AuthRepository = koinInject()



    lateinit var accessToken: String
    lateinit var refreshToken: String
    lateinit var fingerPrint: String

    scope.launch {
        val jobAccessToken: Deferred<String> = async {
            appPreferencesRepository.fetchInitialPreferences().accessToken
        }

        val jobRefreshToken: Deferred<String> = async {
            appPreferencesRepository.fetchInitialPreferences().refreshToken
        }

        val jobFingerPrint: Deferred<String> = async {
            appPreferencesRepository.fetchInitialPreferences().fingerPrint
        }

        accessToken = jobAccessToken.await()
        refreshToken = jobRefreshToken.await()
        fingerPrint = jobFingerPrint.await()

        checkAndUpdateToken(
            data = data,
            accessToken = accessToken,
            authRepository = authRepository,
            onShowIncomingCallActivity = {
                onShowIncomingCallActivity(it)
            }
        )
    }
}

private suspend fun checkAndUpdateToken(
    data: PayloadDataCustom,
    accessToken: String,
    authRepository: AuthRepository,
    onShowIncomingCallActivity: (PayloadDataCustom) -> Unit
) {
    val scope = CoroutineScope(Dispatchers.IO)

    if (accessToken.isNotEmpty()) { // юзер авторизован
        val needUpdate = TextUtils.isItTimeToUpdateToken(accessToken = accessToken)
        if (needUpdate) { // обновить токен

            Logger.d("4444 IOS FirebaseNotificationPlatform обновить токен")
            try {
                val response = authRepository.refreshTokenSync()
                if (response?.status?.isSuccess() == true) {
//                        checkVersionCode(message = message)
                    checkTypeMessageReceived(
                        data = data,
                        accessToken = accessToken,
                        onShowIncomingCallActivity = {
                            onShowIncomingCallActivity(it)
                        }
                    )
                } else {
                    scope.launch {
                        //logManager.writeLogToDB("ошибка обновления токена для пуша")
                    }
                    Logger.d("4444 IOS FirebaseNotificationPlatform ошибка обновления токена ")
                }
            } catch (e: Exception) {
                Logger.d("4444 try catch checkAndUpdateToken e=" + e)
            }
        } else {
//                checkVersionCode(message = message)
            checkTypeMessageReceived(
                data = data,
                accessToken = accessToken,
                onShowIncomingCallActivity = {
                    onShowIncomingCallActivity(it)
                }
            )
        }
    } else {
        scope.launch {
            //logManager.writeLogToDB("Нет авторизации для отображения пуша")
        }
        Logger.d(
            "4444 IOS FirebaseNotificationPlatform токен пустой значит юзер выполнил логаут"
        )
    }
}

private fun checkTypeMessageReceived(
    data: PayloadDataCustom,
    accessToken: String,
    onShowIncomingCallActivity: (PayloadDataCustom) -> Unit
) {
    val scope = CoroutineScope(Dispatchers.IO)
    when (data.type) {
        TypeEventNotification.CALL.value -> {
            scope.launch {
                //logManager.writeLogToDB("BazaNetFirebaseMessagingService type CALL")
            }
            Logger.d("4444 IOS FirebaseNotificationPlatform showNotificationCall ")
            showNotificationCall(
                data = data,
                onShowIncomingCallActivity = {
                    onShowIncomingCallActivity(it)
                })
        }

        TypeEventNotification.MISSED_CALL.value -> {
            scope.launch {
                //logManager.writeLogToDB("BazaNetFirebaseMessagingService type MISSED_CALL")
            }
            Logger.d("4444 IOS FirebaseNotificationPlatform showNotificationMissedCall")
            showNotificationMissedCall(data = data, accessToken = accessToken)
        }

        TypeEventNotification.NOTIFY_INFO.value -> {
            scope.launch {
                //logManager.writeLogToDB("BazaNetFirebaseMessagingService type NOTIFY_INFO")
            }
            showNotificationInfo(data = data)
        }
    }
}

private fun showNotificationCall(
    data: PayloadDataCustom,
    onShowIncomingCallActivity: (PayloadDataCustom) -> Unit
    ) {

    onShowIncomingCallActivity(data)
//    notificationPushCustomizer.showNotificationCall(
//        address = data.address,
//        imageUrl = data.imageUrl,
//        uuid = data.uuid,
//        videoUrl = data.videoUrl
//    )
}

private fun showNotificationMissedCall(data: PayloadDataCustom, accessToken: String) {

//    notificationPushCustomizer.showNotificationMissedCall(
//        address = data.address,
//        imageUrl = data.imageUrl,
//        uuid = data.uuid,
//        accessToken = accessToken
//    )
}

private fun showNotificationInfo(data: PayloadDataCustom) {

}
