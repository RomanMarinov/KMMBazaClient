package presentation.ui.auth_activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class AuthPlatform actual constructor() {

    @Composable
    actual fun MakeCall() {
        val context = LocalContext.current
        val uri = Uri.parse("tel:" + "88001000249")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        context.startActivity(intent)
    }

    @SuppressLint("HardwareIds")
    @Composable
    actual fun getFingerprint(): String {
        val context = LocalContext.current
        val fingerprint = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return fingerprint
    }

    @Composable
    actual fun getInetCellular(): Boolean {
        val context = LocalContext.current
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}