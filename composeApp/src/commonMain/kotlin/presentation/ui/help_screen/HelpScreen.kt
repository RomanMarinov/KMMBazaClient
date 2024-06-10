package presentation.ui.help_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.help_title
import kmm.composeapp.generated.resources.ic_back
import kmm.composeapp.generated.resources.ic_profile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import util.ColorCustomResources
import util.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController,
    viewModel: HelpScreenViewModel = koinInject()
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val faqUiState by viewModel.faqUiState.collectAsStateWithLifecycle()
    val officesUiState by viewModel.officesUiState.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

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
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(Res.string.help_title),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
//                                navigator.popBackStack()
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
                        ) {
                            Icon(
                                modifier = Modifier
                                    //.padding(),
                                    //.systemBarsPadding() // Добавить отступ от скрытого статус-бара
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(50)),
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
                        .shadow(4.dp)
                )
            }
        ) { helpTopBarPaddingValue ->

            Column(
                modifier = Modifier
                    .padding(top = helpTopBarPaddingValue.calculateTopPadding())
                    .padding(bottom = bottomNavigationPaddingValue.calculateBottomPadding())
                    .background(ColorCustomResources.colorBackgroundMain)
            ) {
                HelpContentWithRefresh(
                    itemsFaq = faqUiState.faq,
                    itemsOffices = officesUiState.offices,
                    isLoading = isLoading,
                    onRefresh = {
                        viewModel.getPublicInfo(isLoading = true)
                    },
                    viewModel = viewModel,
                    navHostController = navHostController
                )
            }
        }
    }
}
