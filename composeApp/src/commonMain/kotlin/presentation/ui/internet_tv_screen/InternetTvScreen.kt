package presentation.ui.internet_tv_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import data.public_info.remote.dto.Location
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_back
import kmm.composeapp.generated.resources.ic_favorite
import kmm.composeapp.generated.resources.ic_profile
import kmm.composeapp.generated.resources.internet_tv_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.ColorCustomResources
import util.ScreenRoute

class InternetTvScreenViewModelProvider : KoinComponent {
    val internetTvScreenViewModel: InternetTvScreenViewModel by inject()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternetTvScreen(
    bottomNavigationPaddingValue: PaddingValues,
    navHostController: NavHostController
) {

    val viewModelProvider = InternetTvScreenViewModelProvider()
    val viewModel = viewModelProvider.internetTvScreenViewModel
   // val outDoorsUiState by viewModel.outDoorsUiState.collectAsState()
    val locationsInternetTv by viewModel.locationsTitle.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


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
                    // modifier = Modifier.height(20.dp),
//                    colors = TopAppBarDefaults.smallTopAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//                    ),
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(Res.string.internet_tv_title),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navHostController.popBackStack()
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
        ) { internetTvTopBarPaddingValue ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = internetTvTopBarPaddingValue.calculateTopPadding())
                    .padding(bottom = bottomNavigationPaddingValue.calculateBottomPadding())
                    .background(ColorCustomResources.colorBackgroundMain)

            ) {
                InternetTvContentWithRefresh(
                    locationsInternetTv = locationsInternetTv,
//                    isRefreshing = isRefreshing,
//                    onRefresh = {
//                        scope.launch {
//                            isRefreshing = true
//                            delay(2000L)
//                            isRefreshing = false
//                        }
//                    },
                    navHostController = navHostController
                )

//                TopControl2(
//                    locations = listOf("1", "2"),
//                    setLocation = null,
//                    viewModel = viewModel,
//                    paddingValue = paddingValue
//                )
            }
        }
    }
}



@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopControl2(
    locations: List<String>,
    setLocation: Location?,
    viewModel: InternetTvScreenViewModel,
    paddingValue: PaddingValues,
) {
    var expanded by remember { mutableStateOf(false) }
    var labelClick by remember { mutableStateOf("г. Вологда") }
    var indexClick by remember { mutableStateOf(-1) }
    var dropdownMenuWidth by remember { mutableStateOf(0) }
    var dropdownMenuHeight by remember { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    LaunchedEffect(indexClick) {
        //viewModel.setMapLocation(position = indexClick)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 2.dp)
                    .weight(1f)
                    .height(40.dp),
                //.clip(RoundedCornerShape(15)),
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = ColorCustomResources.colorTransparentItem
                ),

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_favorite),
                        contentDescription = null,
                        //  modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Избранное",
                        //  modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "2",
                        //    modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
//                    .onGloballyPositioned { coordinates ->
//                        dropdownMenuWidth = with(localDensity) {
//                            (coordinates.size.width / density).toInt()
//                        }
//                    }
                //  horizontalAlignment = Alignment.End
            ) {

                OutlinedButton(
                    shape = RoundedCornerShape(10),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, end = 4.dp)
                        .onGloballyPositioned { coordinates ->
                            dropdownMenuWidth = with(localDensity) {
                                (coordinates.size.width / density).toInt()
                            }
                            dropdownMenuHeight = with(localDensity) {
                                (coordinates.size.height).toInt()
                            }

                        },
                    //   .weight(1f),
                    //.clip(RoundedCornerShape(15)),
                    onClick = {
                        expanded = !expanded
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = ColorCustomResources.colorBazaMainBlue
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = labelClick,
                            modifier = Modifier
                                //.padding(start = 16.dp)
                                .weight(1f) // Занимает все доступное пространство влево
                        )

                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            //  modifier = Modifier.padding(end = 16.dp) // Отступ справа
                        )
                    }
                }

                DropdownMenu(
                    modifier = Modifier
                        //.padding(0.dp)
                        .height(400.dp)
                        .background(color = Color.Transparent)
                        .shadow(0.dp, shape = RoundedCornerShape(10.dp)),
                    //  .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp),
                    //  .offset(x = 2.dp),
//                    modifier = Modifier.background(color = Color.Transparent),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(2.dp, 0.dp),
                    //scrollState = ,
                    content = {
                        locations.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .background(
                                        if (indexClick == index) ColorCustomResources.colorBazaMainBlue
                                        else Color.Transparent
                                    )
                                    .width(dropdownMenuWidth.dp)
                                    .height(40.dp),
                                text = {
                                    Text(
                                        text = label,
                                        color = if (indexClick == index) Color.White else Color.Black
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    labelClick = label
                                    indexClick = index
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

