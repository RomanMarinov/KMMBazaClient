package util



import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable
import presentation.ui.domofon_screen.DomofonScreen
import presentation.ui.help_screen.HelpScreen
import presentation.ui.history_call.HistoryCallScreen
import presentation.ui.home_screen.HomeScreen
import presentation.ui.internet_tv_screen.InternetTvScreen
import presentation.ui.map_screen.MapScreen
import presentation.ui.outdoor_screen.OutdoorScreen
import presentation.ui.payment_service_screen.PaymentServiceScreen
import presentation.ui.profile_screen.ProfileScreen
import presentation.ui.profile_screen.address_screen.AddressScreen
import presentation.ui.webview_screen.WebViewScreen


//const val ADDRESS = "address"
//const val VIDEO_URL = "videoUrl"


@Serializable
data class WebViewScreen2(
    val address: String,
    val videoUrl: String
)

@Serializable
data class ScreenB(
    val name: String?,
    val age: Int
)




@Composable
fun NavHostScreenScenes(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController,
    onMoveToAuthActivity: () -> Unit,
    onShowIncomingCallActivity: () -> Unit
) {

    NavHost(navController = navHostController, startDestination = ScreenRoute.HomeScreen.route) {
        composable(route = ScreenRoute.HomeScreen.route) {
            HomeScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController,
                onShowIncomingCallActivity = {
                    onShowIncomingCallActivity()
                })
        }

        composable(
            route = ScreenRoute.OutdoorScreen.route,
        ) {
            OutdoorScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }

        composable(
            route = ScreenRoute.MapScreen.route,
        ) {
            MapScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }

        composable(
            route = ScreenRoute.DomofonScreen.route,
        ) {
            DomofonScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }

        composable(
            route = ScreenRoute.HelpScreen.route,
        ) {
            HelpScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }
////////////////////////////


        composable(
            route = ScreenRoute.WebViewScreen.route,
            arguments = listOf(
                navArgument(ADDRESS) {
                    type = NavType.StringType
                },
                navArgument(VIDEOURL) {
                    type = NavType.StringType
                }
            )
        ) {
            val address = it.arguments?.getString(ADDRESS)
            val videoUrl = it.arguments?.getString(VIDEOURL)
            WebViewScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController,
                address = address,
                videoUrl = videoUrl
            )
        }

        composable(
            route = ScreenRoute.ProfileScreen.route
        ) {
            ProfileScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController,
                onMoveToAuthActivity = {
                    onMoveToAuthActivity()
                }
            )
        }

        composable(
            route = ScreenRoute.PaymentServiceScreen.route
        ) {
            PaymentServiceScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController,
//                onMoveToAuthActivity = {
//                    onMoveToAuthActivity()
//                }
            )
        }

        composable(
            route = ScreenRoute.InternetTvScreen.route
        ) {
            InternetTvScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }

        composable(
            route = ScreenRoute.HistoryCallScreen.route
        ) {
            HistoryCallScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }

        composable(
            route = ScreenRoute.AddressScreen.route
        ) {
            AddressScreen(
                bottomNavigationPaddingValue = bottomNavigationPaddingValue,
                navHostController = navHostController)
        }
    }
}