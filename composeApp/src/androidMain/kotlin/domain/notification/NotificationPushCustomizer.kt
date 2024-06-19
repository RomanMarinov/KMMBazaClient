package domain.notification

interface NotificationPushCustomizer {
    fun showNotificationCall(
        address: String,
        imageUrl: String,
        uuid: String,
       // title: String,
        videoUrl: String
    )

    fun showNotificationMissedCall(
        address: String,
        imageUrl: String,
        uuid: String,
        //title: String,
        accessToken: String
    )
}