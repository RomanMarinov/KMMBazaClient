package presentation.ui.help_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import data.public_info.remote.dto.Faq
import data.public_info.remote.dto.MarkerOffice
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.help_call_number_one
import kmm.composeapp.generated.resources.help_call_number_two
import kmm.composeapp.generated.resources.help_check_internet
import kmm.composeapp.generated.resources.help_contact_title
import kmm.composeapp.generated.resources.help_make_call
import kmm.composeapp.generated.resources.help_make_question
import kmm.composeapp.generated.resources.help_make_question_desc
import kmm.composeapp.generated.resources.help_title_first
import kmm.composeapp.generated.resources.help_title_second
import kmm.composeapp.generated.resources.help_title_support
import kmm.composeapp.generated.resources.ic_arrow_right
import kmm.composeapp.generated.resources.ic_call
import kmm.composeapp.generated.resources.ic_tg
import kmm.composeapp.generated.resources.ic_vk
import kmm.composeapp.generated.resources.ic_wa
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import util.ColorCustomResources
import util.ShimmerListOutdoorOrDomofonHelper
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpContentWithRefresh(
    itemsFaq: List<Faq>,
    itemsOffices: List<MarkerOffice>,
    // content: @Composable (T) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    viewModel: HelpScreenViewModel,
    navHostController: NavHostController
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    var isLoadingState by remember { mutableStateOf(true) }
    LaunchedEffect(itemsFaq) {
        if (itemsFaq.isNotEmpty()) {
            isLoadingState = false
        }
    }
    /////////////////////////////////////////////
//    помотреть тут где я на шару писал navigationBarsPadding
/////////////////////////////////////////////////

//    val colorsList = listOf(Color.LightGray, Color.White)


    Box(
        modifier = modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
            // .navigationBarsPadding()
//            .padding(
//                bottom = paddingValue.calculateBottomPadding()
//            )
            .background(ColorCustomResources.colorBackgroundMain)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {

            helpContentTitle()


            helpContentListFaq(
                lazyListState = lazyListState,
                listFaq = itemsFaq,
                isLoading = isLoadingState,
//                isLoadingChange = {
//                    isLoadingState = it
//                }
            )
            helpContentSpeedTest()

            helpContentMessengers(
                viewModel = viewModel
            )

            helpContentContactTitle()

            helpContentContactTowns(itemsOffices = itemsOffices)

            helpContentVersionApp()

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

}

@OptIn(ExperimentalResourceApi::class)
fun LazyListScope.helpContentTitle() {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.help_title_first),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.help_title_second),
                )
            }
        }
    }
}

fun LazyListScope.helpContentListFaq(
    lazyListState: LazyListState,
    listFaq: List<Faq>,
    isLoading: Boolean,
    //isLoadingChange: (Boolean) -> Unit
) {

    items(listFaq) { faq ->

        var contentExpanded by remember { mutableStateOf(false) }
        //  Row(modifier = Modifier.animateContentSize()) {
        Spacer(modifier = Modifier.height(8.dp))

        ShimmerListOutdoorOrDomofonHelper(
            isLoading = isLoading,
            contentAfterLoading = {

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)

                ) {
                    Column(
//                verticalArrangement = Arrangement.spacedBy(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            //.padding(start = 16.dp, end = 16.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)) // Добавление границы
//                    .background(color = Color.White)
                            .clickable(
                                indication = null, // Убираем визуальный эффект клика
                                interactionSource = remember { MutableInteractionSource() } // Не забываем про interactionSource
                            ) {
                                contentExpanded = !contentExpanded
                            }
                            .animateContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                        //.shadow(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = faq.title,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(2f)
                            )
                            IconButton(
                                onClick = { contentExpanded = !contentExpanded }) {
                                Icon(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .weight(1f)
                                        .width(IntrinsicSize.Max),
                                    imageVector = if (contentExpanded) {
                                        Icons.Filled.KeyboardArrowUp
                                    } else {
                                        Icons.Filled.KeyboardArrowDown
                                    },
                                    contentDescription = if (contentExpanded) {
                                        "ExpandLess"
                                    } else {
                                        "ExpandMore"
                                    }
                                )
                            }
                        }

                        if (contentExpanded) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                                text = faq.content,
                                color = Color.Gray
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp).clip(
                    RoundedCornerShape(10.dp)
                )
        )
    }
}

fun LazyListScope.helpContentSpeedTest() {
    item {

        val isShowSpeedTest = remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 16.dp, end = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Проверьте, как работает Ваш интернет",
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .size(100.dp, 100.dp)
                    ) {
                        LottieSpeedTest()
                    }

                    ElevatedButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        onClick = {
                            isShowSpeedTest.value = true
                        },
                        content = { Text(stringResource(Res.string.help_check_internet)) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = ColorCustomResources.colorBazaMainBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        if (isShowSpeedTest.value) {
            OpenUrlFromHelpPlatform().execute(url = "http://baza.net/test")
            isShowSpeedTest.value = false
        }
    }
}

@Composable
fun LottieSpeedTest() {

    val lottieData = """
{"nm":"Spee Test","ddd":0,"h":600,"w":600,"meta":{"g":"LottieFiles AE "},"layers":[{"ty":4,"nm":"fill/Speed Test - Animation Outlines","sr":1,"st":0,"op":300.00001221925,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[382.5,254,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[300.46,300.48,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 1","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":false,"i":[[0,0],[-141.962,0],[0,-141.962]],"o":[[0,-141.962],[141.962,0],[0,0]],"v":[[-257.04,128.52],[0,-128.52],[257.04,128.52]]},"ix":2}},{"ty":"gs","bm":0,"hd":false,"mn":"ADBE Vector Graphic - G-Stroke","nm":"Gradient Stroke 1","e":{"a":0,"k":[100,0],"ix":5},"g":{"p":3,"k":{"a":0,"k":[0,0.6,0.14901960784313725,0.14901960784313725,0.5,0.7686274509803922,0.1843137254901961,0.1843137254901961,1,0.9294117647058824,0.2235294117647059,0.2235294117647059],"ix":8}},"t":1,"a":{"a":0,"k":0},"h":{"a":0,"k":0},"s":{"a":0,"k":[0,0],"ix":4},"lc":2,"lj":1,"ml":10,"o":{"a":0,"k":100,"ix":9},"w":{"a":0,"k":54,"ix":10}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[382.04,253.52],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"tm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Trim","nm":"Trim Paths 1","ix":2,"e":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[0],"t":0},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[63],"t":69},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[59],"t":95},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[69],"t":121},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[50],"t":137},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[89],"t":181},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[83],"t":199},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[88],"t":281},{"s":[0],"t":295.000012015596}],"ix":2},"o":{"a":0,"k":0,"ix":3},"s":{"a":0,"k":0,"ix":1},"m":1}],"ind":1},{"ty":4,"nm":"meter/Speed Test - Animation Outlines","sr":1,"st":0,"op":300.00001221925,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[25,151.5,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[298,429,0],"ix":2},"r":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[-96],"t":0},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[30],"t":69},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[18],"t":95},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[40],"t":121},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[2],"t":137},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[70],"t":181},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[62],"t":198},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[70],"t":282},{"s":[-96],"t":295.000012015596}],"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 1","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[6.338,4.824],[0,0],[0,0],[0,0],[-0.267,-8.909],[-13.759,-0.379],[0,14.833]],"o":[[0,0],[0,0],[0,0],[-6.536,4.974],[0.412,13.759],[14.925,0.411],[0,-8.579]],"v":[[16.087,40.98],[9.302,-88.793],[-9.048,-88.793],[-15.832,40.98],[-26.268,62.791],[-0.618,88.382],[26.535,61.984]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.2275,0.3451,0.4745],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[26.785,89.043],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":2},{"ty":4,"nm":"speed/Speed Test - Animation Outlines","sr":1,"st":0,"op":300.00001221925,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[204.5,118,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[300,338.5,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 1","ix":1,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.866,3.515],[3.866,-3.517],[0,-7.41],[-3.866,-3.517],[-3.866,3.515],[0,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.738,3.329],[-6.738,-3.329],[0,-10.067],[6.738,-3.329],[6.738,3.329],[0,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[401.35,225.637],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 2","ix":2,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.867,3.515],[3.867,-3.517],[0.001,-7.41],[-3.866,-3.517],[-3.866,3.515],[0.001,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.738,3.329],[-6.738,-3.329],[0.001,-10.067],[6.738,-3.329],[6.738,3.329],[0.001,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[384.106,225.637],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 3","ix":3,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"o":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"v":[[0.377,-6.577],[-3.248,-4.376],[-3.248,-7.49],[0.377,-9.664],[3.248,-9.664],[3.248,9.665],[0.377,9.665]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[369.015,225.636],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 4","ix":4,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,-3.599],[3.625,0],[0.779,2.843],[0,0],[-1.637,0],[0,2.067],[2.041,0],[0.619,-0.805],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[-1.128,0]],"o":[[0,3.596],[-2.951,0],[0,0],[0.43,1.745],[2.122,0],[0,-2.093],[-1.61,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0.968,-0.672],[3.599,0]],"v":[[6.483,3.369],[-0.229,9.867],[-6.483,5.061],[-3.773,4.335],[-0.122,7.154],[3.663,3.369],[-0.122,-0.389],[-3.424,1.139],[-5.866,0.309],[-5.276,-9.867],[5.166,-9.867],[5.166,-7.21],[-2.753,-7.21],[-3.075,-1.919],[0.227,-2.967]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[390.298,152.629],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 5","ix":5,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"o":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"v":[[2.807,-6.979],[-5.812,-6.979],[-5.812,-9.665],[5.812,-9.665],[5.812,-6.979],[-0.979,9.664],[-4.013,9.664]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[375.312,152.427],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 6","ix":6,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.867,3.515],[3.867,-3.517],[0.001,-7.41],[-3.866,-3.517],[-3.866,3.515],[0.001,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.738,3.329],[-6.738,-3.329],[0.001,-10.067],[6.738,-3.329],[6.738,3.329],[0.001,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[360.948,88.038],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 7","ix":7,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,-3.599],[3.625,0],[0.779,2.843],[0,0],[-1.637,0],[0,2.067],[2.041,0],[0.619,-0.805],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[-1.128,0]],"o":[[0,3.596],[-2.951,0],[0,0],[0.43,1.745],[2.122,0],[0,-2.093],[-1.61,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0.968,-0.672],[3.599,0]],"v":[[6.483,3.369],[-0.229,9.867],[-6.483,5.062],[-3.773,4.335],[-0.122,7.154],[3.663,3.369],[-0.122,-0.389],[-3.424,1.14],[-5.866,0.309],[-5.276,-9.867],[5.166,-9.867],[5.166,-7.209],[-2.753,-7.209],[-3.075,-1.919],[0.227,-2.967]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[344.497,88.238],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 8","ix":8,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.867,3.515],[3.867,-3.517],[0.001,-7.41],[-3.866,-3.517],[-3.866,3.515],[0.001,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.738,3.329],[-6.738,-3.329],[0.001,-10.067],[6.738,-3.329],[6.738,3.329],[0.001,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[296.192,32.442],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 9","ix":9,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,-3.169],[3.596,0],[0.834,2.739],[0,0],[-1.559,0.027],[0,2.119],[2.119,0],[0.564,-0.297],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"o":[[0,3.677],[-2.901,0],[0,0],[0.509,1.663],[2.119,-0.026],[0,-2.148],[-0.619,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[3.169,0.401]],"v":[[6.336,3.491],[-0.106,9.852],[-6.336,5.369],[-3.624,4.619],[-0.132,7.168],[3.464,3.491],[-0.132,-0.215],[-1.987,0.27],[-3.301,-1.96],[2.658,-7.195],[-5.583,-7.195],[-5.583,-9.852],[6.04,-9.852],[6.04,-7.166],[1.234,-2.576]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[279.621,32.629],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 10","ix":10,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.867,3.515],[3.867,-3.517],[0,-7.41],[-3.866,-3.517],[-3.866,3.515],[0,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.738,3.329],[-6.738,-3.329],[0,-10.067],[6.738,-3.329],[6.738,3.329],[0,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[212.572,10.317],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 11","ix":11,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,1.073],[2.067,0],[-0.053,-1.933],[0,0],[-3.867,0],[0,-3.678],[2.096,-1.852]],"o":[[0,0],[0,0],[0,0],[0,0],[0,0],[1.343,-1.206],[0,-2.093],[-2.174,0],[0,0],[0,-3.759],[3.651,0],[0,2.657],[0,0]],"v":[[-2.187,7.195],[6.537,7.195],[6.537,9.852],[-6.537,9.852],[-6.537,7.381],[1.891,-0.163],[3.665,-3.518],[0.094,-7.195],[-3.585,-3.544],[-6.485,-3.544],[0.148,-9.852],[6.537,-3.491],[3.394,2.201]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[196.068,10.102],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 12","ix":12,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,-3.599],[3.625,0],[0.779,2.843],[0,0],[-1.637,0],[0,2.067],[2.041,0],[0.619,-0.805],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[-1.128,0]],"o":[[0,3.596],[-2.951,0],[0,0],[0.43,1.745],[2.122,0],[0,-2.093],[-1.61,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0.968,-0.672],[3.599,0]],"v":[[6.483,3.369],[-0.229,9.867],[-6.483,5.062],[-3.773,4.335],[-0.122,7.154],[3.663,3.369],[-0.122,-0.389],[-3.424,1.14],[-5.866,0.309],[-5.276,-9.867],[5.166,-9.867],[5.166,-7.209],[-2.753,-7.209],[-3.075,-1.919],[0.227,-2.967]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[122.202,32.643],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 13","ix":13,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"o":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"v":[[0.376,-6.578],[-3.249,-4.376],[-3.249,-7.491],[0.376,-9.664],[3.249,-9.664],[3.249,9.664],[0.376,9.664]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[107.901,32.441],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 14","ix":14,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.867,3.515],[3.867,-3.517],[0,-7.41],[-3.867,-3.517],[-3.867,3.515],[0,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.739,3.329],[-6.739,-3.329],[0,-10.067],[6.739,-3.329],[6.739,3.329],[0,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[59.182,88.038],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 15","ix":15,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"o":[[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0]],"v":[[0.376,-6.578],[-3.249,-4.376],[-3.249,-7.491],[0.376,-9.664],[3.249,-9.664],[3.249,9.664],[0.376,9.664]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[44.09,88.037],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 16","ix":16,"cix":2,"np":4,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,2.148],[0,0],[2.148,0],[0,-2.175],[0,0],[-2.149,0]],"o":[[0,0],[0,-2.175],[-2.149,0],[0,0],[0,2.148],[2.148,0]],"v":[[3.867,3.515],[3.867,-3.517],[0.001,-7.41],[-3.867,-3.517],[-3.867,3.515],[0.001,7.381]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,3.945],[0,0],[-3.945,0],[0,-3.947],[0,0],[3.947,0]],"o":[[0,0],[0,-3.947],[3.947,0],[0,0],[0,3.945],[-3.945,0]],"v":[[-6.738,3.329],[-6.738,-3.329],[0.001,-10.067],[6.738,-3.329],[6.738,3.329],[0.001,10.067]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[6.988,223.12],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 17","ix":17,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,-3.599],[3.625,0],[0.779,2.843],[0,0],[-1.637,0],[0,2.067],[2.041,0],[0.619,-0.805],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[-1.128,0]],"o":[[0,3.596],[-2.951,0],[0,0],[0.43,1.745],[2.122,0],[0,-2.093],[-1.61,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0,0],[0.968,-0.672],[3.599,0]],"v":[[6.483,3.369],[-0.229,9.867],[-6.483,5.061],[-3.773,4.335],[-0.122,7.154],[3.663,3.369],[-0.122,-0.389],[-3.424,1.139],[-5.866,0.309],[-5.276,-9.867],[5.166,-9.867],[5.166,-7.21],[-2.753,-7.21],[-3.075,-1.919],[0.227,-2.967]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.3804,0.3804,0.3804],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[16.994,152.629],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":3},{"ty":4,"nm":"unfill/Speed Test - Animation Outlines","sr":1,"st":0,"op":300.00001221925,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[286.5,156.5,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[301,300,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 1","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,-157.73],[-14.584,0],[0,14.584],[-128.61,0],[0,-128.61],[-14.584,0],[0,14.584],[157.73,0]],"o":[[0,14.584],[14.583,0],[0,-128.61],[128.611,0],[0,14.584],[14.584,0],[0,-157.73],[-157.731,0]],"v":[[-286.057,129.824],[-259.649,156.232],[-233.241,129.824],[0,-103.417],[233.241,129.824],[259.649,156.232],[286.057,129.824],[0,-156.232]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.9216,0.9216,0.9216],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[286.307,156.482],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":4}],"v":"4.8.0","fr":100,"op":300.00001221925,"ip":0,"assets":[]}
"""
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(lottieData))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever, // бесконечно
        isPlaying = true, // пауза/воспроизведение
        speed = 2.0f,
        restartOnPlay = false // передать false, чтобы продолжить анимацию на котором он был приостановлен
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Проверка на null
        composition?.let {
            LottieAnimation(
                composition = it,
                progress = { progress.absoluteValue },
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }
}

fun LazyListScope.helpContentMessengers(
    viewModel: HelpScreenViewModel
) {
    item {
        val phone by viewModel.phone.collectAsState()
        val makeCall = remember { mutableStateOf(false) }

        val colorsList = listOf(
            ColorCustomResources.colorBazaMainRed,
            ColorCustomResources.colorBazaMainBlueCard
        )

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(colors = colorsList))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
//                    Icon(
//                        imageVector = vectorResource(Res.drawable.ic_chat),
//                        contentDescription = "chat",
//                        tint = Color.White
//                    )
                    Text(
//                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(Res.string.help_make_question),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = stringResource(Res.string.help_make_question_desc),
                    color = Color.White
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(Res.drawable.ic_vk),
                        modifier = Modifier
                            .size(80.dp),
                        contentDescription = null
                    )
                    Image(
                        painter = painterResource(Res.drawable.ic_tg),
                        modifier = Modifier
                            .size(80.dp),
                        contentDescription = null
                    )
                    Image(
                        painter = painterResource(Res.drawable.ic_wa),
                        modifier = Modifier
                            .size(80.dp),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_call),
                        contentDescription = "call",
                        tint = Color.White
                    )
                    Text(
                        text = stringResource(Res.string.help_title_support),
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start),
                    text = stringResource(Res.string.help_call_number_one),
                    color = Color.White,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start),
                    text = stringResource(Res.string.help_call_number_two),
                    color = Color.White,
                    fontSize = 20.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ElevatedButton(
                        modifier = Modifier
                            .padding(16.dp),
                        onClick = {
                            makeCall.value = true
                        },
                        content = { Text(stringResource(Res.string.help_make_call)) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = ColorCustomResources.colorBazaMainBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        if (makeCall.value) {
            CallHelpScreenPlatform().makeCall(phoneNumber = phone ?: "+78172700239")
            makeCall.value = false
        }
    }
}

fun LazyListScope.helpContentContactTitle() {
    item {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
            content = {
                Text(
                    text = stringResource(Res.string.help_contact_title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        )
    }
}

fun LazyListScope.helpContentContactTowns(
    itemsOffices: List<MarkerOffice>,
) {
    item {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    // .padding(16.dp)
                    .background(Color.White)
            ) { // Отступ для элементов внутри Card
                itemsOffices.forEachIndexed { index, office ->
                    Column {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            }) {
                            Text(
                                text = office.title,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .weight(1f)
                            )
                            Icon( // потом исправить на право предупреждение было в ios
                                imageVector = vectorResource(Res.drawable.ic_arrow_right),
                                contentDescription = "arrow",
                                modifier = Modifier
                                    .padding(16.dp)
                            )
                        }
                        if (index < itemsOffices.size - 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun LazyListScope.helpContentVersionApp() {
    item {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
            content = {
                Text(
                    text = "Версия: четкая",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}
