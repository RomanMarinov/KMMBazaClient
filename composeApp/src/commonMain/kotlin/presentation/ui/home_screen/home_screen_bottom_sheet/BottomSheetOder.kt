package presentation.ui.home_screen.home_screen_bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import util.ColorCustomResources
import util.TextFieldHelper

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun BottomSheetOrder(
    navHostController: NavHostController,
    // markerDetail: MarkerDetail,
    openBottomSheet: (Boolean) -> Unit,
) {
    //Log.d("4444", " BottomSheet сработал markerDetail=" + markerDetail)
    val scope = rememberCoroutineScope()

    val openBottomSheetState by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (openBottomSheetState) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),
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
                    .height(500.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                        //    .align(Alignment.Center)
                        ,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Заявка на подключение услуги",
                            fontSize = 20.sp
                        )
                    }
                    Card(
                        modifier = Modifier
                             .padding(start = 16.dp)
                            .size(24.dp)
                            .align(Alignment.CenterEnd),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
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


                Box(modifier = Modifier.fillMaxWidth()) {
                    OrderContent()
                }


            }
        }
    }
}
@Composable
fun OrderContent() {
    var textLastNameState by remember { mutableStateOf("") }
    var textNameState by remember { mutableStateOf("") }
    var textMiddleNameState by remember { mutableStateOf("") }
    var isFocusTextFiled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(),
      //  verticalArrangement = Arrangement.Top

        ) {

        TextFieldHelper(
            value = textLastNameState,
            onValueChanged = { textLastNameState = it },
            hintText = "Фамилия"
        )
        TextFieldHelper(
            value = textNameState,
            onValueChanged = { textNameState = it },
            hintText = "Имя"
        )
        TextFieldHelper(
            value = textMiddleNameState,
            onValueChanged = { textMiddleNameState = it },
            hintText = "Отчество"
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            text = "Отправляя заявку, Вы соглашаетесь на обработку персональных данных"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

                ElevatedButton(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 30.dp),
    //                .shadow(2.dp, RoundedCornerShape(2.dp)),
                    shape = RoundedCornerShape(8.dp),

                    onClick = {

                    },
                    content = { Text("Отправить") },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = ColorCustomResources.colorBazaMainBlue
                    ),
                    //shape = RoundedCornerShape(10.dp)
                )

        }
    }
}
