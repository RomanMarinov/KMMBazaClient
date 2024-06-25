import androidx.compose.runtime.Composable
import com.mmk.kmpnotifier.notification.PayloadData

@Composable
actual fun FirebaseNotificationPlatform(
    data: PayloadDataCustom,
    onShowIncomingCallActivity: (PayloadDataCustom) -> Unit
) {

}