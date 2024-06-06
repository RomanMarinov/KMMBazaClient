package presentation.ui.map_screen.map_screen_bottom_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_close
import kmm.composeapp.generated.resources.ic_tg
import kmm.composeapp.generated.resources.ic_vk
import kmm.composeapp.generated.resources.ic_wa
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.map_screen.model.MarkerDetail
import util.ColorCustomResources

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun BottomSheetOffice(
    navHostController: NavHostController,
    markerDetail: MarkerDetail,
    openBottomSheet: (Boolean) -> Unit,
){
    //Log.d("4444", " BottomSheet сработал markerDetail=" + markerDetail)
    val scope = rememberCoroutineScope()

    val openBottomSheetState by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (openBottomSheetState) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),
              //  .height(500.dp),
            onDismissRequest = { openBottomSheet(false) },
            sheetState = bottomSheetState,
            dragHandle = { },
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
//                    Icon(
//                        modifier = Modifier
//                            .size(35.dp)
//                            .padding(start = 16.dp)
//                            .align(Alignment.CenterStart),
//                        imageVector = vectorResource(Res.drawable.ic_map_city_cam),
//                        contentDescription = null
//                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Офис Baza.net"
                        )
                    }
                    Card(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(34.dp)
                            .align(Alignment.CenterEnd),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    openBottomSheet(false)
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                // close
                                modifier = Modifier
                                    .size(24.dp),
                                imageVector = vectorResource(Res.drawable.ic_close),
                                contentDescription = null,
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = markerDetail.titleAddress.toString()
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    text = "Часы работы:\n${markerDetail.worktime}"
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Телефон:\n${markerDetail.visible}"
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
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    OutlinedButton(
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            //.fillMaxWidth()
                            .padding(16.dp)
                            .height(40.dp),
                        //.clip(RoundedCornerShape(15)),
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = ColorCustomResources.colorBazaMainRed
                        ),

                        ) {
                        Text(
                            text = "Позвонить",
                            //  modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}