package net.baza.bazanetclientapp.permission

import android.content.Context
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.permission.PermissionUtil
import net.baza.bazanetclientapp.extensions.hasNotificationPermission




/**
 * Этот класс предназначен только для проверки разрешения на уведомление.
 * для запроса разрешения во время выполнения используйте AndroidPermissionUtil в своей деятельности.
 */
// не трогать
internal class AndroidMockPermissionUtil(private val context: Context) : PermissionUtil {
    override fun hasNotificationPermission(onPermissionResult: (Boolean) -> Unit) {
        onPermissionResult(context.hasNotificationPermission())
    }


    override fun askNotificationPermission(onPermissionGranted: () -> Unit) = Unit.also {
        Logger.d(
            "4444 Android эта функция — просто макет. Вам нужно спросить разрешение в Activity"
        + "используя, как показано ниже: val permissionUtil by PermissionUtil()\n" +
                "permissionUtil.askNotificationPermission() \n"
        )
    }
}