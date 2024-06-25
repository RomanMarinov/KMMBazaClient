import androidx.compose.runtime.Composable
import com.mmk.kmpnotifier.notification.PayloadData

@Composable
expect fun FirebaseNotificationPlatform(
    data: PayloadDataCustom,
    onShowIncomingCallActivity: (PayloadDataCustom) -> Unit
)