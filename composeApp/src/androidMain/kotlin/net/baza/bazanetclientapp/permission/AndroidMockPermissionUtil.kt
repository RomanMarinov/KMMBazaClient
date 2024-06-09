package net.baza.bazanetclientapp.permission

import android.content.Context
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
        println(
            "In Android this function is just a mock. You need to ask permission in Activity " +
                    "using like below: \n" +
                    "val permissionUtil by permissionUtil()\n" +
                    "permissionUtil.askNotificationPermission() \n"
        )
    }
}