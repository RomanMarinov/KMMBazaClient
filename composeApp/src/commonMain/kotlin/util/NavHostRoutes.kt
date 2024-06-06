package util

const val ADDRESS = "address"
const val VIDEOURL = "videourl"

sealed class ScreenRoute(val route: String) {
    object HomeScreen : ScreenRoute("/baza_screen")
    object OutdoorScreen : ScreenRoute("outdoor_screen")
    object MapScreen : ScreenRoute("/map_screen")
    object DomofonScreen : ScreenRoute("domofon_screen")
    object HelpScreen : ScreenRoute("/help_screen")

    object WebViewScreen : ScreenRoute("webview_screen/{$ADDRESS}/{$VIDEOURL}") {
        fun withArgs(address: String, videourl: String) : String {
            return "webview_screen/$address/$videourl"
        }
    }

    ////////
//    object ChatScreen : ScreenRoute("chat_screen/{$RECIPIENT_NAME}/{$RECIPIENT_PHONE}/{$SENDER_PHONE}") {
//        fun withArgs(recipientName: String, recipientPhone: String, senderPhone: String) : String {
//            return "chat_screen/$recipientName/$recipientPhone/$senderPhone"
//        }
//    }
    ///////////

    object ProfileScreen : ScreenRoute("/profile_screen")
    object InternetTvScreen : ScreenRoute("/internet_tv_screen")

    object HistoryCallScreen : ScreenRoute("/history_call_screen")

    object AddressScreen : ScreenRoute("/address_screen")
}