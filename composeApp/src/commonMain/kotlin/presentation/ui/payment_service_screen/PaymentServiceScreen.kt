package presentation.ui.payment_service_screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import di.koinViewModel
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_back
import kmm.composeapp.generated.resources.ic_profile
import kmm.composeapp.generated.resources.payment_service_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.ui.outdoor_screen.OutdoorScreenViewModel
import util.ColorCustomResources
import util.ScreenRoute

//class  PaymentServiceViewModelProvider : KoinComponent {
//    val paymentServiceViewModel: PaymentServiceViewModel by inject()
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentServiceScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController
) {
    Logger.d { " 4444 PaymentServiceScreen opened" }

// анимация топбара при скроле
    // https://www.youtube.com/watch?v=EqCvUETekjk
//    val viewModelProvider = PaymentServiceViewModelProvider()
//    val viewModel = viewModelProvider.paymentServiceViewModel
    val viewModel = koinViewModel<PaymentServiceViewModel>()

    val isLoading = viewModel.isLoading.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                            text = stringResource(Res.string.payment_service_title),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
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
                                contentDescription = null,
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
        ) { paymentServiceTopBarPaddingValue ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paymentServiceTopBarPaddingValue.calculateTopPadding())
                    .padding(bottom = bottomNavigationPaddingValue.calculateBottomPadding())
                    .background(ColorCustomResources.colorBackgroundMain)
            ) {
                Box(
                    modifier = Modifier
                        .nestedScroll(pullToRefreshState.nestedScrollConnection)
                ) {
                    PaymentServiceContentWithRefresh(
                        //items = items,
                        isLoading = isLoading.value,
                        onRefresh = {
                            //viewModel.getOutdoors(isLoading = true)
                        },
                        snackBarHostState = snackbarHostState,
                        navHostController = navHostController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
