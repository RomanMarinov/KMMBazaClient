package presentation.ui.outdoor_screen

import OutdoorListContent
import OutdoorListGroupContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import co.touchlab.kermit.Logger
import di.koinViewModel
import domain.model.user_info.Dvr
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_back
import kmm.composeapp.generated.resources.ic_profile
import kmm.composeapp.generated.resources.outdoor_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.ui.map_screen.MapScreenViewModel
import util.ColorCustomResources
import util.ScreenRoute

enum class OutdoorContent {
    LIST_GROUP, LIST, LIST_ZERO, DEFAULT
}

//class  OutdoorScreenViewModelProvider : KoinComponent {
//    val outdoorScreenViewModel: OutdoorScreenViewModel by inject()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutdoorScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController
) {
    Logger.d { " 4444 OutdoorScreen opened" }

// анимация топбара при скроле
    // https://www.youtube.com/watch?v=EqCvUETekjk
//    val viewModelProvider = OutdoorScreenViewModelProvider()
//    val viewModel = viewModelProvider.outdoorScreenViewModel
    val viewModel = koinViewModel<OutdoorScreenViewModel>()
    val outDoorsUiState by viewModel.outDoorsUiState.collectAsState()
    val items: List<Dvr> = outDoorsUiState?.outdoors ?: emptyList()
    val isLoading by viewModel.isLoading.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    val outdoorContentState = remember { mutableStateOf(OutdoorContent.DEFAULT) }
    val addrIdFromGroup = remember { mutableIntStateOf(-1) }
    val countGroup = remember { mutableStateOf(-1) }

    val groupItems: Map<Int, List<Dvr>>? = items?.groupBy { it.addrId }

    LaunchedEffect(groupItems?.size) {
        groupItems?.let {
            if (it.size == 0) {
                countGroup.value = 0
                outdoorContentState.value = OutdoorContent.LIST_ZERO
                Logger.d("4444 -2 outdoorContentState.value=" + outdoorContentState.value + " countGroup.value=" + countGroup.value)
            }
            if (it.size in 1..2) {
                countGroup.value = it.size
                outdoorContentState.value = OutdoorContent.LIST
                Logger.d("4444 -1 outdoorContentState.value=" + outdoorContentState.value + " countGroup.value=" + countGroup.value)
            }
            if (it.size >= 3) {
                countGroup.value = it.size
                outdoorContentState.value = OutdoorContent.LIST_GROUP
                Logger.d("4444 0 outdoorContentState.value=" + outdoorContentState.value + " countGroup.value=" + countGroup.value)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
                //.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(Res.string.outdoor_title),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // <3
                                if (countGroup.value in 1..2 && outdoorContentState.value == OutdoorContent.LIST) {
//                                if (!isShowGroupStateFirst.value) {
                                    navHostController.navigate(
                                        ScreenRoute.HomeScreen.route,
                                        NavOptions.Builder().setPopUpTo(
                                            // The destination of popUpTo
                                            route = ScreenRoute.HomeScreen.route,
                                            // Whether the popUpTo destination should be popped from the back stack.
                                            inclusive = false,
                                        ).build()
                                    )
                                } // >=3
                                if (countGroup.value > 2 && outdoorContentState.value == OutdoorContent.LIST_GROUP) { // стоит на группе
//                                    if (isShowGroupState.value) { // стоит на группе
                                    navHostController.navigate(
                                        ScreenRoute.HomeScreen.route,
                                        NavOptions.Builder().setPopUpTo(
                                            // The destination of popUpTo
                                            route = ScreenRoute.HomeScreen.route,
                                            // Whether the popUpTo destination should be popped from the back stack.
                                            inclusive = false,
                                        ).build()
                                    )
                                }
                                if (countGroup.value > 2 && outdoorContentState.value == OutdoorContent.LIST) { // стоит на группе
                                    outdoorContentState.value = OutdoorContent.LIST_GROUP
                                }

                                if (outdoorContentState.value == OutdoorContent.LIST_ZERO) {
                                    navHostController.navigate(
                                        ScreenRoute.HomeScreen.route,
                                        NavOptions.Builder().setPopUpTo(
                                            // The destination of popUpTo
                                            route = ScreenRoute.HomeScreen.route,
                                            // Whether the popUpTo destination should be popped from the back stack.
                                            inclusive = false,
                                        ).build()
                                    )
                                }
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
        ) { outdoorTopBarPaddingValue ->
            Logger.d("4444 пусто outdoorTopBarPaddingValue=" + outdoorTopBarPaddingValue)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = outdoorTopBarPaddingValue.calculateTopPadding())
                    .padding(bottom = bottomNavigationPaddingValue.calculateBottomPadding())
                    .background(ColorCustomResources.colorBackgroundMain)
            ) {
                Box(
                    modifier = Modifier
                        .nestedScroll(pullToRefreshState.nestedScrollConnection)
                ) {
                    groupItems?.let {

                        if (it.size == 0 && outdoorContentState.value == OutdoorContent.LIST_ZERO) {
                            Logger.d("4444 пусто outdoorContentState.value=" + outdoorContentState.value)

                            OutdoorListContent(
                                items = items,
                                isLoading = isLoading,
                                onRefresh = {
                                    viewModel.getOutdoors(isLoading = true)
                                },
                                snackbarHostState = snackbarHostState,
                                navHostController = navHostController,
                                viewModel = viewModel
                            )
                        }

                        if (it.size in 1..2 && outdoorContentState.value == OutdoorContent.LIST) {
                            Logger.d("4444 1 outdoorContentState.value=" + outdoorContentState.value)

                            OutdoorListContent(
                                items = items,
                                isLoading = isLoading,
                                onRefresh = {
                                    viewModel.getOutdoors(isLoading = true)
                                },
                                snackbarHostState = snackbarHostState,
                                navHostController = navHostController,
                                viewModel = viewModel
                            )
                        }

                        if (it.size > 2) {
                            if (outdoorContentState.value == OutdoorContent.LIST_GROUP) {
                                Logger.d("4444 2 outdoorContentState.value=" + outdoorContentState.value)

                                OutdoorListGroupContent(
                                    items = items,
                                    isLoading = isLoading,
                                    onRefresh = {
                                        viewModel.getOutdoors(isLoading = true)
                                    },
                                    onGoOutdoorContentList = {
                                        outdoorContentState.value = OutdoorContent.LIST
                                        Logger.d("4444 3 outdoorContentState.value=" + outdoorContentState.value)
//
                                    },
                                    onAddrId = { addrId ->
                                        addrIdFromGroup.value = addrId
                                    },
                                    viewModel = viewModel,
                                    onShowSnackBarUnlockDoorStatus = {
                                        // isShowSnackBarUnlockDoorStatus.value = status
                                    },
                                    navHostController = navHostController
                                )
                            }

                            if (outdoorContentState.value == OutdoorContent.LIST) {
                                Logger.d("4444 4 outdoorContentState.value=" + outdoorContentState.value)
                                val listFilterByAddrId =
                                    items.filter { sputnikItem -> sputnikItem.addrId == addrIdFromGroup.value }

                                OutdoorListContent(
                                    items = listFilterByAddrId,
                                    isLoading = isLoading,
                                    onRefresh = {
                                        viewModel.getOutdoors(isLoading = true)
                                    },
                                    snackbarHostState = snackbarHostState,
                                    navHostController = navHostController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}