package net.baza.bazanetclientapp.firebase

import co.touchlab.kermit.Logger
import com.google.firebase.messaging.FirebaseMessaging
import com.mmk.kmpnotifier.notification.PushNotifier
import kotlinx.coroutines.tasks.asDeferred

// пока методы класса не используются
internal class FirebasePushNotifierImpl : PushNotifier {

    init {
        Logger.d("4444 FirebasePushNotifier is initialized")
    }
    override suspend fun getToken(): String? {
        Logger.d("4444 FirebasePushNotifier getToken")
        return FirebaseMessaging.getInstance().token.asDeferred().await()
    }

    override suspend fun deleteMyToken() {
        Logger.d("4444 FirebasePushNotifier deleteMyToken")
        FirebaseMessaging.getInstance().deleteToken()
    }

    override suspend fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    override suspend fun unSubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
    }


}