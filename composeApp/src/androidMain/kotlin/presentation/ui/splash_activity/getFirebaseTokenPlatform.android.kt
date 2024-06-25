package presentation.ui.splash_activity

import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

actual suspend fun getFirebaseTokenPlatform(): String? {
    val scope = CoroutineScope(Dispatchers.IO)
    val jobFirebaseToken: Deferred<String> = scope.async {
        NotifierManager.getPushNotifier().getToken().toString()
    }
    return jobFirebaseToken.await()
}