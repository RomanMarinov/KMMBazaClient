package presentation.ui.map_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_back
import kmm.composeapp.generated.resources.ic_profile
import kmm.composeapp.generated.resources.map_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.ui.map_screen.map_screen_bottom_sheet.BottomSheetCityCam
import presentation.ui.map_screen.map_screen_bottom_sheet.BottomSheetDomofonCam
import presentation.ui.map_screen.map_screen_bottom_sheet.BottomSheetOffice
import presentation.ui.map_screen.map_screen_bottom_sheet.BottomSheetOutdoorCam
import presentation.ui.map_screen.model.MarkerDetail
import util.ScreenRoute
import util.Strings

class MapScreenViewModelProvider : KoinComponent {
    val mapScreenViewModel: MapScreenViewModel by inject()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun MapScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController
) {
    val viewModelProvider = MapScreenViewModelProvider()
    val viewModel = viewModelProvider.mapScreenViewModel
    // анимация топбара при скроле
    // https://www.youtube.com/watch?v=EqCvUETekjk

    val markerDetailTitleTypeState = remember { mutableStateOf("") }
    val markerDetailState = remember { mutableStateOf(MarkerDetail()) }

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


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
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            //      text = MapViewPlatform().getMapViewManipulation(),
                            text = stringResource(Res.string.map_title),
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
                        .shadow(4.dp)
                )
            }
        ) { paddingValue ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                // .padding(paddingValue)
                // .padding(bottom = paddingValue.calculateBottomPadding())
            ) {
                MapViewPlatform().SetMapView(
                    paddingValue = paddingValue,
                    viewModel = viewModel,
                    moveToBottomSheetMapFragment = { markerDetail ->
                        markerDetail.titleType?.let {
                            markerDetailTitleTypeState.value = it
                            markerDetailState.value = markerDetail
                        }
                    }
                )
            }
        }

        when (markerDetailTitleTypeState.value) {
            Strings.typeCity -> {
                BottomSheetCityCam(
                    navHostController = navHostController,
                    markerDetailState.value,
                    openBottomSheet = {
                        markerDetailTitleTypeState.value = ""
                    }
                )
            }
            Strings.typeOutDoor -> {
                BottomSheetOutdoorCam(
                    navHostController = navHostController,
                    markerDetailState.value,
                    openBottomSheet = {
                        markerDetailTitleTypeState.value = ""
                    }
                )
            }

            Strings.typeDomofon -> {
                BottomSheetDomofonCam(
                    navHostController = navHostController,
                    markerDetailState.value,
                    openBottomSheet = {
                        markerDetailTitleTypeState.value = ""
                    }
                )
            }

            Strings.typeOffice -> {
                BottomSheetOffice(
                    navHostController = navHostController,
                    markerDetailState.value,
                    openBottomSheet = {
                        markerDetailTitleTypeState.value = ""
                    }
                )
            }
        }
    }
}


