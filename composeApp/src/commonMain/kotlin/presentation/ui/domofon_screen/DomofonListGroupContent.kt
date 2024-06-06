package presentation.ui.domofon_screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import domain.model.user_info.Sputnik
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_lock
import kmm.composeapp.generated.resources.ic_play
import kmm.composeapp.generated.resources.ic_share
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.add_address.AddAddressBottomSheet
import util.AddAddressButtonHelper
import util.ColorCustomResources
import util.ScreenRoute
import util.navigateToWebViewHelper
import util.shimmerEffect

@Composable
fun DomofonListGroupContent(
    items: List<Sputnik>?, // МОЖЕТ НАДО УБРАТЬ
    onGoDomofonContentList: () -> Unit,
    onAddrId: (Int) -> Unit,
    viewModel: DomofonScreenViewModel,
    onShowSnackBarUnlockDoorStatus: (Boolean) -> Unit,
    navHostController: NavHostController
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

//    val sputnikUiState by viewModel.domofonUiState.collectAsState()
//    val items = sputnikUiState?.domofon?.sputnik
    val snackbarHostState = remember { SnackbarHostState() }
    val groupItems: Map<Int, List<Sputnik>>? = items?.groupBy { it.addrId }
    groupItems?.let {
        val listSputnikByFullControl = groupItems.flatMap { mapEntry ->
            mapEntry.value.filter { sputnik ->
                sputnik.fullControl
            }
        }
        val groupItems1 = items.groupBy { sputnik ->
            sputnik.addrId
        }.values.toList()

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier
                    //.navigationBarsPadding()
                    .fillMaxSize()
                //.navigationBarsPadding()
                ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    PermissionBannerContent()
                }

                item {
                    TopTitleContentGroup(
                        navHostController = navHostController
                    )
                }

                items(listSputnikByFullControl) { sputnik ->
                    val listSputnikItem: List<Sputnik> =
                        groupItems1.flatten().filter { it.addrId == sputnik.addrId }

                    GroupContentItem(
                        sputnikControl = sputnik,
                        listSputnik = listSputnikItem,
                        onGoDomofonContentList = {
                            onGoDomofonContentList()
                        },
                        onAddrId = {
                            onAddrId(it)
                        },
                        onShowSnackBarUnlockDoorStatus = {
                            onShowSnackBarUnlockDoorStatus(it)
                        },
                        navHostController = navHostController, viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TopTitleContentGroup(
    navHostController: NavHostController
) {
    val isShowBottomSheet = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
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
                text = "Адреса",
                fontWeight = FontWeight.Bold
            )
        }

        AddAddressButtonHelper(
            onShowBottomSheet = {
                isShowBottomSheet.value = it
            }
        )
    }

    if (isShowBottomSheet.value) {
        AddAddressBottomSheet(
            fromScreen = ScreenRoute.DomofonScreen.route,
            onShowCurrentBottomSheet = {
                isShowBottomSheet.value = it
            }
        )
    }
}




@Composable
fun GroupContentItem(
    sputnikControl: Sputnik,
    listSputnik: List<Sputnik>,
    onGoDomofonContentList: () -> Unit,
    onAddrId: (Int) -> Unit,
    navHostController: NavHostController,
    onShowSnackBarUnlockDoorStatus: (Boolean) -> Unit,
    viewModel: DomofonScreenViewModel
) {

    val isOpenDoor = remember { mutableStateOf(false) }
    val isShare = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBazaMainBlue),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // .navigationBarsPadding()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                // horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
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
                        text = sputnikControl.title,
                    )
                }

                Row(
                    modifier = Modifier,
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
                                .height(40.dp),
                            shape = RoundedCornerShape(100.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBazaMainBlue),
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        isShare.value = true
                                    }
                                    .fillMaxHeight()
                                    .padding(8.dp)
                                    .background(ColorCustomResources.colorBazaMainBlue),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {


                                Icon(
                                    modifier = Modifier
                                        .size(24.dp),
                                    imageVector = vectorResource(Res.drawable.ic_share),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            val title = getTitle(listSputnik.size)
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = title
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
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
                        resource = asyncPainterResource(sputnikControl.previewUrl),
                        contentDescription = "",
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
                                    route = ScreenRoute.DomofonScreen.route,
                                    address = sputnikControl.title,
                                    videoUrl = sputnikControl.videoUrl
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
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    navigateToWebViewHelper(
                                        navHostController = navHostController,
                                        route = ScreenRoute.DomofonScreen.route,
                                        address = sputnikControl.title,
                                        videoUrl = sputnikControl.videoUrl
                                    )
                                }
                        )

                        if (sputnikControl.fullControl) {
                            Icon(
                                vectorResource(Res.drawable.ic_lock),
                                contentDescription = "lock",
                                tint = Color.White,
                                modifier = Modifier
                                    //.weight(1f)
                                    //.fillMaxWidth()
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        viewModel.onClickUnLock(deviceId = sputnikControl.deviceID)
//                                        scope.launch {
//                                            onShowSnackBarUnlockDoorStatus(true)
//                                            delay(500L)
//
//                                        }
                                    }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ElevatedButton(
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        onGoDomofonContentList()
                        onAddrId(sputnikControl.addrId)
                    },
                    content = {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp),
                            text = "Другие камеры"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = ColorCustomResources.colorBazaMainBlue
                    ),
                    //shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }

    if (isShare.value) {
    }
}

private fun getTitle(size: Int): String {
    var title = ""
    if (size == 1) {
        title = "По этому адресу Вам доступна $size камера"
    }
    if (size in 2..4) {
        title = "По этому адресу Вам доступно $size камеры"
    }
    if (size >= 5) {
        title = "По этому адресу Вам доступно $size камер"
    }
    return title
}
