import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import domain.model.user_info.Dvr
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.bannerOutDoor
import kmm.composeapp.generated.resources.bannerOutDoorHeader
import kmm.composeapp.generated.resources.ic_play
import kmm.composeapp.generated.resources.ic_plus
import kmm.composeapp.generated.resources.ic_plus_square
import kmm.composeapp.generated.resources.img_presentation_outdoor_banner
import kmm.composeapp.generated.resources.outdoor_create_shortcut
import kmm.composeapp.generated.resources.outdoor_more_info
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.add_address.AddAddressBottomSheet
import presentation.ui.outdoor_screen.OutdoorScreenViewModel
import util.ColorCustomResources
import util.ScreenRoute
import util.navigateToWebViewHelper
import util.shimmerEffect

//package presentation.ui.outdoor_screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutdoorListContent(
    items: List<Dvr>,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: OutdoorScreenViewModel
) {
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            state = lazyListState,
//        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            modifier = Modifier
                //.navigationBarsPadding()
                .fillMaxSize()
            //    .navigationBarsPadding()
            ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (!isLoading && (items.isEmpty())) {
                item {
                    PresentationContent()
                }
            }

            items(items) { dvr ->
                ContentLazyListItem(
                    dvr = dvr,
                    snackbarHostState = snackbarHostState,
                    navHostController = navHostController,
                    viewModel = viewModel
                )
            }

            if (items.isNotEmpty()) {
                item {
                    ContentLazyListItemBottom()
                }
            }
        }


        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = Color.White,
            contentColor = ColorCustomResources.colorBazaMainBlue
        )
    }
}

@Composable
fun PresentationContent() {
    val isShowBottomSheet = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(Res.string.bannerOutDoorHeader),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(Res.string.bannerOutDoor)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(10.dp),
//            colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
        ) {
            Image(
                painter = painterResource(Res.drawable.img_presentation_outdoor_banner),
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
//                .shadow(2.dp, RoundedCornerShape(2.dp)),

                onClick = {

                },
                content = { Text(stringResource(Res.string.outdoor_more_info)) },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = ColorCustomResources.colorBazaMainBlue
                ),
                //shape = RoundedCornerShape(10.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            //.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier,
                //.fillMaxWidth(),

            ) {
                Card(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(100.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
//                                onShowBottomSheet(true)
                                isShowBottomSheet.value = true
                            }
                            .fillMaxHeight()
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            imageVector = vectorResource(Res.drawable.ic_plus),
                            contentDescription = null,
                            tint = ColorCustomResources.colorBazaMainBlue
                        )
                        Text(
                            modifier = Modifier
                                //.fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp),
                            text = "Новый адрес",
                            color = ColorCustomResources.colorBazaMainBlue
                        )
                    }
                }
            }
        }

        if (isShowBottomSheet.value) {
            AddAddressBottomSheet(
                fromScreen = ScreenRoute.OutdoorScreen.route,
                onShowCurrentBottomSheet = {
                    isShowBottomSheet.value = it
                }
            )
        }
    }
}

@Composable
fun ContentLazyListItemBottom() {

    val isShowBottomSheet = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            //.weight(1f)
//            .fillMaxWidth()
            .padding(start = 16.dp),
        //.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
//        Box(
//            modifier = Modifier,
//            //.fillMaxWidth(),
//            contentAlignment = Alignment.CenterEnd,
//        ) {
            Card(
                modifier = Modifier
                    //.fillMaxWidth()
                    .heightIn(40.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .clickable {
                            //onShowBottomSheet(true)
                            isShowBottomSheet.value = true
                        }
                        .fillMaxHeight()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = vectorResource(Res.drawable.ic_plus),
                        contentDescription = null,
                        tint = ColorCustomResources.colorBazaMainBlue
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = "Новый адрес",
                        color = ColorCustomResources.colorBazaMainBlue
                    )
                }
            }
      //  }
    }

    if (isShowBottomSheet.value) {
        AddAddressBottomSheet(
            fromScreen = ScreenRoute.OutdoorScreen.route,
            onShowCurrentBottomSheet = {
                isShowBottomSheet.value = it
            }
        )
    }
}

@Composable
fun ContentLazyListItem(
    dvr: Dvr,
    //snackBarState: Boolean,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: OutdoorScreenViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
        //.navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            // horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                //.fillMaxWidth()
                verticalAlignment = Alignment.CenterVertically,
                // horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier,
                    text = dvr.title,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.padding(bottom = 4.dp),
                //.fillMaxWidth()
                //.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier,
                    //.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Card(
                        modifier = Modifier
                            //.fillMaxWidth()
                            .heightIn(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                    ) {
                        Column(
                            modifier = Modifier
                                //.fillMaxWidth()
                                .clickable {

                                }
                                .fillMaxHeight()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp),
                                imageVector = vectorResource(Res.drawable.ic_plus_square),
                                contentDescription = null,
                                tint = ColorCustomResources.colorBazaMainBlue
                            )
                            Text(
                                modifier = Modifier
                                //.fillMaxWidth()
                                //    .padding(start = 8.dp, end = 8.dp)
                                ,
                                text = stringResource(Res.string.outdoor_create_shortcut),
                                color = ColorCustomResources.colorBazaMainBlue
                            )
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                KamelImage(
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shimmerEffect()
                                .height(200.dp)
                                .padding(start = 16.dp, end = 16.dp)
                        )
                    },
                    onFailure = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shimmerEffect()
                                .height(200.dp)
                                .padding(start = 16.dp, end = 16.dp)
                        )
                    },
                    resource = asyncPainterResource(dvr.previewUrl),
                    contentDescription = dvr.previewUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))

//                        .placeholder(
//                            color = Color.Gray,
//                            shape = RoundedCornerShape(8.dp) // Опционально, если нужно закруглить углы
//                        )
//                        .shimmer(
//                            baseColor = Color.LightGray,
//                            highlightColor = Color.Gray,
//                            animationSpec = shimmerAnimationSpec
//                        )
                        .clickable {
                            navigateToWebViewHelper(
                                navHostController = navHostController,
                                route = ScreenRoute.OutdoorScreen.route,
                                address = dvr.title,
                                videoUrl = dvr.videoUrl
                            )
                        }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        vectorResource(Res.drawable.ic_play),
                        contentDescription = "play",
                        tint = Color.White,
                        modifier = Modifier
                            //.fillMaxWidth()
                            // .weight(1f)
                            .size(80.dp)
                            .clickable {
                                navigateToWebViewHelper(
                                    navHostController = navHostController,
                                    route = ScreenRoute.OutdoorScreen.route,
                                    address = dvr.title,
                                    videoUrl = dvr.videoUrl
                                )
                            }
                    )
                }
            }
        }
    }
}