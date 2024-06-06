package util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import net.thauvin.erik.urlencoder.UrlEncoderUtil

fun navigateToWebViewHelper(
    navHostController: NavHostController,
    route: String,
    address: String,
    videoUrl: String
) {
    val videoUrlEncode = UrlEncoderUtil.encode(videoUrl)
    navHostController.navigate(
        ScreenRoute.WebViewScreen.withArgs(
            address = address,
            videourl = videoUrlEncode
        ),
        NavOptions.Builder().setPopUpTo(
            route = route,
            inclusive = false
        ).build()
    )
}