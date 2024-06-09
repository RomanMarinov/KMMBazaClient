package net.baza.bazanetclientapp.permission

/**
 * Permission utility class
 */
public interface PermissionUtil {


    public fun hasNotificationPermission(onPermissionResult: (Boolean) -> Unit = {})
    public fun askNotificationPermission(onPermissionGranted: () -> Unit = {})
}