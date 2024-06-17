package presentation.ui.history_call

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import di.koinViewModel
import domain.model.history_call.HistoryCallAddress
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.history_call_title
import kmm.composeapp.generated.resources.ic_back
import kmm.composeapp.generated.resources.ic_phone_answered
import kmm.composeapp.generated.resources.ic_phone_not_answered
import kmm.composeapp.generated.resources.ic_play_history_call
import kmm.composeapp.generated.resources.ic_profile
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.ui.help_screen.HelpScreenViewModel
import util.ColorCustomResources
import util.GetVideoUrl
import util.ScreenRoute
import util.SnackBarHostHelper
import util.navigateToWebViewHelper

//class HistoryCallViewModelProvider : KoinComponent {
//    val historyCallScreenViewModel: HistoryCallScreenViewModel by inject()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryCallScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController
) {
//    val viewModelProvider = HistoryCallViewModelProvider()
//    val viewModel = viewModelProvider.historyCallScreenViewModel
    val viewModel = koinViewModel<HistoryCallScreenViewModel>()
    val historyCalls by viewModel.historyCalls.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        //.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                // modifier = Modifier.height(20.dp),
//                    colors = TopAppBarDefaults.smallTopAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//                    ),
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(Res.string.history_call_title),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.navigateUp()
//                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                //.padding(),
                                //.systemBarsPadding() // Добавить отступ от скрытого статус-бара
                                .size(35.dp),
                            // .clip(RoundedCornerShape(50)),
                            imageVector = vectorResource(Res.drawable.ic_back),
                            contentDescription = "Go back",

                            )
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
                    .shadow(4.dp),
                scrollBehavior = scrollBehavior

            )
        }
    ) { historyCallTopBarPaddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = historyCallTopBarPaddingValue.calculateTopPadding())
                .padding(bottom = bottomNavigationPaddingValue.calculateBottomPadding())
                .background(ColorCustomResources.colorBackgroundMain)
        ) {
            HistoryCallContentWithRefresh(
                historyCalls = historyCalls,
//                    isRefreshing = isRefreshing,
//                    onRefresh = {
//                        scope.launch {
//                            isRefreshing = true
//                            delay(2000L)
//                            isRefreshing = false
//                        }
//                    },
                navHostController = navHostController,
                viewModel = viewModel
            )
        }
    }
    //  }
}

@Composable
fun HistoryCallContentWithRefresh(
    historyCalls: List<HistoryCallAddress>,
    navHostController: NavHostController,
    viewModel: HistoryCallScreenViewModel
) {

//    val viewModelProvider = HistoryCallViewModelProvider()
//    val viewModel = viewModelProvider.historyCallScreenViewModel
//    val scope = rememberCoroutineScope()

    val videoUrl by viewModel.videoUrl.collectAsStateWithLifecycle()
    val isTransitionWebViewScreen = remember { mutableStateOf(false) }
    val address = remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val isShowSnackBarScroll = remember { mutableStateOf(false) }
    var lastVisibleItemIndex by remember { mutableStateOf(-1) }

    val historyCallsSize = remember { mutableStateOf(-1) }


    Logger.d("4444 ewefwefwef")
    // Следим за состоянием прокрутки
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .collect { index ->
                lastVisibleItemIndex = index
                // Logger.d("4444 lastVisibleItemIndex=" + lastVisibleItemIndex)
                //Logger.d("4444 lastVisibleItemIndex historyCalls.size=" + historyCalls.size)
                if (historyCallsSize.value > 10 && lastVisibleItemIndex == historyCallsSize.value - 1) {
                    isShowSnackBarScroll.value = true
                    //  Logger.d("4444 lastVisibleItemIndex есть=" + lastVisibleItemIndex)
                }
            }
    }

    LaunchedEffect(historyCalls) {
        historyCallsSize.value = historyCalls.size
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box {
            ElevatedCard(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = lazyListState
                ) {
                    itemsIndexed(historyCalls) { index, call ->
                        HistoryCallItem(
                            index = index,
                            historyCalls = historyCalls,
                            historyCallAddress = call,
                            onClickItem = {
                                address.value = it
                                isTransitionWebViewScreen.value = true
                            },
                            viewModel = viewModel
                        )

                    }
                }
            }

            if (isShowSnackBarScroll.value) {
                Logger.d("4444 dnwdkwndknwd")
                SnackBarHostHelper.ShortShortTime(
                    message = "Архив истории звонков предоставляется за 7 дней",
                    onFinishTime = {
                        isShowSnackBarScroll.value = false
                    }
                )
            }
        }
    }

    LaunchedEffect(isTransitionWebViewScreen.value) {
        if (isTransitionWebViewScreen.value) {
            navigateToWebViewHelper(
                navHostController = navHostController,
                route = ScreenRoute.MapScreen.route,
                address = address.value,
                videoUrl = videoUrl
            )
        }
    }
}

@Composable
fun HistoryCallItem(
    index: Int,
    historyCalls: List<HistoryCallAddress>,
    historyCallAddress: HistoryCallAddress,
    onClickItem: (String) -> Unit,
    viewModel: HistoryCallScreenViewModel
) {
    val typePhone =
        if (historyCallAddress.answered) vectorResource(Res.drawable.ic_phone_not_answered) else vectorResource(
            Res.drawable.ic_phone_answered
        )
    val typeColor =
        if (historyCallAddress.answered) ColorCustomResources.colorBazaMainBlue else Color.Red

    Column(modifier = Modifier
        .clickable {
            val timestamp = GetVideoUrl.getTimeStamp(dateString = historyCallAddress.time)
            viewModel.getVideoUrl(
                deviceID = historyCallAddress.deviceID,
                timestamp = timestamp
            )
            onClickItem(historyCallAddress.address)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = typePhone,
                contentDescription = "back",
                tint = typeColor,
                modifier = Modifier
                    .padding(8.dp)
                    .size(34.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Row {
                    Text(
                        text = historyCallAddress.address
                    )
                }

                Row {
                    Text(
                        text = historyCallAddress.time
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable {
                        // navHostController.popBackStack()
                    },
                imageVector = vectorResource(Res.drawable.ic_play_history_call),
                contentDescription = "back",
                tint = ColorCustomResources.colorBazaMainBlue,
            )
        }

        if (index < historyCalls.size - 1) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
        }
    }
}
