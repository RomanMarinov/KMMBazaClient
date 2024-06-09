//package extensions
//
//import net.baza.bazanetclientapp.Constants.ACTION_NOTIFICATION_CLICK
//import net.baza.bazanetclientapp.Constants.KEY_ANDROID_FIREBASE_NOTIFICATION
//import android.content.Intent
//import androidx.core.os.bundleOf
//import com.mmk.kmpnotifier.notification.NotifierManager
//import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
//
//
//
///***
// * Чтобы получить полезную нагрузку данных уведомления, необходимо вызвать эту функцию.
// *  * Сторона Android в методах Launcher Activity #onCreate и #onNewIntent.
// *
// * Example:
// *
// * ```
// * class MainActivity : ComponentActivity() {
// *     override fun onCreate(savedInstanceState: Bundle?) {
// *         super.onCreate(savedInstanceState)
// *         NotifierManager.onCreateOrOnNewIntent(intent)
// *         setContent {
// *             App()
// *         }
// *     }
// *
// *     override fun onNewIntent(intent: Intent?) {
// *         super.onNewIntent(intent)
// *         NotifierManager.onCreateOrOnNewIntent(intent)
// *     }
// *
// * }
// *
// * ```
// */
//public fun NotifierManager.onCreateOrOnNewIntent(intent: Intent?) {
//    if (intent == null) return
//    val extras = intent.extras ?: bundleOf()
//    val payloadData = mutableMapOf<String, Any>()
//
//    val isNotificationClicked =
//        extras.containsKey(ACTION_NOTIFICATION_CLICK)
//                || extras.containsKey(KEY_ANDROID_FIREBASE_NOTIFICATION)
//                || payloadData.containsKey(ACTION_NOTIFICATION_CLICK)
//
//    extras.keySet().forEach { key ->
//        val value = extras.get(key)
//        value?.let { payloadData[key] = it }
//    }
//
//
//    if (extras.containsKey(KEY_ANDROID_FIREBASE_NOTIFICATION))
//        notification.NotifierManagerImpl.onPushPayloadData(payloadData.minus(ACTION_NOTIFICATION_CLICK))
//    if (isNotificationClicked)
//        notification.NotifierManagerImpl.onNotificationClicked(payloadData.minus(ACTION_NOTIFICATION_CLICK))
//}
//
//internal fun notification.NotifierManagerImpl.shouldShowNotification(): Boolean {
//    val configuration =
//        notification.NotifierManagerImpl.getConfiguration() as? NotificationPlatformConfiguration.Android
//    return configuration?.showPushNotification ?: true
//}
