package presentation.ui.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mmk.kmpnotifier.notification.NotifierManager
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.home_button_order
import kmm.composeapp.generated.resources.home_img_domofon
import kmm.composeapp.generated.resources.home_img_internet_tv
import kmm.composeapp.generated.resources.home_img_map
import kmm.composeapp.generated.resources.home_img_mobile
import kmm.composeapp.generated.resources.home_img_outdoor
import kmm.composeapp.generated.resources.home_personal_account
import kmm.composeapp.generated.resources.ic_plus
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.home_screen.home_screen_bottom_sheet.BottomSheetOrder
import presentation.ui.home_screen.home_screen_bottom_sheet.BottomSheetPersonalAccount
import util.ColorCustomResources
import util.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContentWithRefresh(
    //  itemsFaq: List<Faq>,
    // itemsOffices: List<MarkerOffice>,
    // content: @Composable (T) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    navHostController: NavHostController,
    viewModel: HomeScreenViewModel
) {
    val scope = rememberCoroutineScope()

    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }
    val openBottomSheetPersonalAccountState = remember { mutableStateOf(false) }
    val openBottomSheetOrderState = remember { mutableStateOf(false) }
//    var isLoadingState by remember { mutableStateOf(true) }
//    LaunchedEffect(itemsFaq) {
//        if (itemsFaq.isNotEmpty()) {
//            isLoadingState = false
//        }
//    }
    /////////////////////////////////////////////
//    помотреть тут где я на шару писал navigationBarsPadding
/////////////////////////////////////////////////
    val colorsList = listOf(
        ColorCustomResources.colorGradientLightBlueStart,
        ColorCustomResources.colorGradientWhiteEnd
    )

//    val colorsList = listOf(Color.LightGray, Color.White)

    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            //.navigationBarsPadding()
            //.padding(bottom = paddingValue.calculateBottomPadding())
//            .background(
//                Brush.linearGradient(
//                    colors = colorsList,
//                    start = Offset.Zero,
//                    end = Offset.Infinite
//                )
//            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                //.padding(bottom = paddingValue.calculateBottomPadding()
             //   )
        ) {
            sendSelfPush(viewModel = viewModel)

            homePersonalAccountCard(
                openBottomSheet = {
                    openBottomSheetPersonalAccountState.value = it
                }
            )
            homeOrderCard(openBottomSheet = {
                openBottomSheetOrderState.value = it
            })
            homeInternetTvCard(navHostController = navHostController)
            homeMobileCard(navHostController = navHostController)
            homeOutdoorCard(navHostController = navHostController)
            homeMapCard(navHostController = navHostController)
            homeDomofonCard(
                navHostController = navHostController,
            //    paddingValue = paddingValue
            )
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            //  modifier = Modifier.align(Alignment.CenterHorizontally),

        )
    }

    if (openBottomSheetPersonalAccountState.value) {
        BottomSheetPersonalAccount(
            navHostController = navHostController,
            //markerDetailState.value,
            openBottomSheet = {
                openBottomSheetPersonalAccountState.value = false
            }
        )
    }
    if (openBottomSheetOrderState.value) {
        BottomSheetOrder(
            navHostController = navHostController,
            //markerDetailState.value,
            openBottomSheet = {
                openBottomSheetOrderState.value = false
            }
        )
    }
}
fun LazyListScope.sendSelfPush(
    viewModel: HomeScreenViewModel
){
    item {
        val scope = rememberCoroutineScope()

        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
               // .height(40.dp)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        ) {
            Row(modifier = Modifier
                //22.fillMaxWidth()
                .background(Color.Red)
                .clickable {
                    scope.launch {
                        viewModel.sendSelfPush()
                    }
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Отправить себе пуш",
                    color = Color.White
                )
            }

            val notifier = remember { NotifierManager.getLocalNotifier() }
            val permissionUtil = remember { NotifierManager.getPermissionUtil() }
            var notificationId by remember { mutableStateOf(0) }
            Button(onClick = {
                notificationId = notifier.notify("Title", "bodyMessage")
            }) {
                Text("Send Local Notification")
            }
            Button(onClick = { notifier.removeAll() }) {
                Text("Remove all notifications")
            }

            Button(enabled = notificationId != 0, onClick = {
                notifier.remove(notificationId)
                notificationId = 0
            }) {
                Text("Remove NotificationID #$notificationId")
            }



            Button(onClick = {
                permissionUtil.askNotificationPermission {
                    println("Permission is granted")
                }
            }) {
                Text("Ask permission")
            }
        }
    }
}
fun LazyListScope.homePersonalAccountCard(
    openBottomSheet: (Boolean) -> Unit,
) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        openBottomSheet(true)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        openBottomSheet(true)
                    }) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_plus),
                        contentDescription = null
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = stringResource(Res.string.home_personal_account)
                )

            }
        }
    }
}

fun LazyListScope.homeOrderCard(
    openBottomSheet: (Boolean) -> Unit
) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        openBottomSheet(true)
                    }
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                    text = "Подключить услуги Baza.net",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Работай, учись, отдыхай",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                )

                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        openBottomSheet(true)
                    },
                    content = { Text(stringResource(Res.string.home_button_order)) },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = ColorCustomResources.colorBazaMainBlue
                    )
                )
            }
        }
    }
}

fun LazyListScope.homeInternetTvCard(navHostController: NavHostController) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navHostController.navigate(ScreenRoute.InternetTvScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        // .fillMaxWidth()
                        .background(Color.White)

                ) {

                    Text(
                        modifier = Modifier
                            // .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        text = "Интернет и ТВ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                        // textAlign = TextAlign.Center,
                    )

                    Text(
                        modifier = Modifier
                            //.fillMaxWidth()
                            .padding(16.dp),
                        text = "Подключите услуги\nна выгодных условиях",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        // textAlign = TextAlign.Center,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    //    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.home_img_internet_tv),
                        modifier = Modifier.size(width = 130.dp, height = 79.dp),
                        contentDescription = null
                    )
                }

            }
        }
    }
}

fun LazyListScope.homeMobileCard(navHostController: NavHostController) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {

                    },
                verticalAlignment = Alignment.Bottom
            ) {

                Column(
                    modifier = Modifier
                    // .fillMaxWidth()
                ) {

                    Text(
                        modifier = Modifier
                            // .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        text = "Мобильная связь",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                        // textAlign = TextAlign.Center,
                    )

                    Text(
                        modifier = Modifier
                            //.fillMaxWidth()
                            .padding(16.dp),
                        text = "Всегда оставайся\nна связи с любимыми",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        // textAlign = TextAlign.Center,
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = painterResource(Res.drawable.home_img_mobile),
                        modifier = Modifier
                            .size(width = 73.dp, height = 112.dp)
                            .align(Alignment.BottomCenter), // Опускаем картинку вниз,
                        contentDescription = null
                    )
                }

            }
        }
    }
}

fun LazyListScope.homeOutdoorCard(navHostController: NavHostController) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navHostController.navigate(ScreenRoute.OutdoorScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                        // .fillMaxWidth()
                        .background(Color.White)

                ) {

                    Text(
                        modifier = Modifier
                            // .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        text = "Мой двор",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                        // textAlign = TextAlign.Center,
                    )

                    Text(
                        modifier = Modifier
                            //.fillMaxWidth()
                            .padding(16.dp),
                        text = "Смотрите камеры во дворе и свободную парковку",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        // textAlign = TextAlign.Center,
                    )
                }

                Box(
                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
                        .padding(end = 16.dp)
                    //    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.home_img_outdoor),
                        modifier = Modifier
                            .size(width = 105.dp, height = 98.dp),
                        contentDescription = null
                    )
                }

            }
        }
    }
}

fun LazyListScope.homeMapCard(navHostController: NavHostController) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navHostController.navigate(ScreenRoute.MapScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Карта",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    )

                    Text(
                        text = "Посмотри, что происходит в твоем городе",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.Bottom)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.home_img_map),
                        modifier = Modifier
                            .size(width = 117.dp, height = 110.dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

fun LazyListScope.homeDomofonCard(
    navHostController: NavHostController,
   // paddingValue: PaddingValues
) {
    item {
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {
                        navHostController.navigate(ScreenRoute.DomofonScreen.route)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Умный домофон",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    )

                    Text(
                        text = "Знайте, кто звонит Вам в дверь!",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .align(Alignment.Bottom),
                ) {
                    Image(
                        painter = painterResource(Res.drawable.home_img_domofon),
                        modifier = Modifier
                            .size(width = 75.dp, height = 110.dp),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}