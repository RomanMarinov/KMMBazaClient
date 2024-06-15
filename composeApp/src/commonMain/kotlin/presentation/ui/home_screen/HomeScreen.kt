@file:OptIn(ExperimentalMaterial3Api::class)

package presentation.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_plus
import kmm.composeapp.generated.resources.ic_profile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import util.ColorCustomResources
import util.ScreenRoute
import util.TextUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController,
    viewModel: HomeScreenViewModel = koinInject(),
    onShowIncomingCallActivity: () -> Unit
) {
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    // modifier = Modifier.height(20.dp),
//                    colors = TopAppBarDefaults.smallTopAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//                    ),
//                    colors = TopAppBarColors(
//                        containerColor = Color.Magenta,
//                        scrolledContainerColor = Color.Yellow,
//                        navigationIconContentColor = Color.Green,
//                        titleContentColor = Color.Cyan,
//                        actionIconContentColor = Color.Red
//                    ),
                    title = {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.clickable {
                                onShowIncomingCallActivity()
                            }) {
                            var name = ""
                            userInfo?.data?.profile?.firstName?.let {
                                name = if (it.isNotEmpty()) {
                                    ", $it"
                                } else {
                                    ""
                                }
                            } ?: run {
                                name = ""
                            }

                            Text(
                                text = "Привет$name!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )

                            var phone = ""
                            userInfo?.data?.profile?.phone?.let {
                                if (it != 0L) {
                                    phone = TextUtils.getPhoneAsFormattedString(it)
                                }
                            }
                            Text(text = phone, fontSize = 16.sp)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navHostController.navigate(ScreenRoute.ProfileScreen.route)
                            }
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.ic_profile),
                                contentDescription = "Open profile",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .shadow(4.dp)
                )
            },
        ) { homeTopBarPaddingValue ->

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(top = homeTopBarPaddingValue.calculateTopPadding())
                    .padding(bottom = bottomNavigationPaddingValue.calculateBottomPadding())
                    //.padding(top = paddingValues.calculateTopPadding())
                    .background(ColorCustomResources.colorBackgroundMain)
            ) {


                HomeContentWithRefresh(
                   // items = outDoorsUiState.outdoors,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true
                            delay(2000L)
                            isRefreshing = false
                        }
                    },
                    navHostController = navHostController,
                   viewModel = viewModel
                )
            }
        }
   // }
}


//Scaffold(
//modifier = Modifier
//.fillMaxSize(),
//topBar = {
//    TopAppBar(
//        title = {
//
//        },
//        actions = {
//        },
//        modifier = Modifier
//            .shadow(4.dp)
//    )
//},
//
//) {
//    paddingValue ->
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//            .padding(paddingValue)
//            .padding(bottom = paddingValue.calculateBottomPadding())
//            .background(ColorCustomResources.colorBackgroundMain)
//    ) {
//
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth()
//                .padding(bottom = paddingValue.calculateBottomPadding())
//        ) {
//
//        }
//    }
//}


