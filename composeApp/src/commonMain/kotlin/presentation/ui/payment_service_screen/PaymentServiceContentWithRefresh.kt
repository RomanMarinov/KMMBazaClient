package presentation.ui.payment_service_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.help_call_number_one
import kmm.composeapp.generated.resources.help_call_number_two
import kmm.composeapp.generated.resources.help_make_call
import kmm.composeapp.generated.resources.help_make_question
import kmm.composeapp.generated.resources.help_make_question_desc
import kmm.composeapp.generated.resources.help_title_support
import kmm.composeapp.generated.resources.ic_call
import kmm.composeapp.generated.resources.ic_tg
import kmm.composeapp.generated.resources.ic_vk
import kmm.composeapp.generated.resources.ic_wa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import util.ColorCustomResources

@Composable
fun PaymentServiceContentWithRefresh(
    isLoading: Boolean,
    onRefresh: () -> Unit,
    snackBarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: PaymentServiceViewModel
) {

    val state = remember { mutableStateOf(true) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Выберите лицевой счет для пополнения и введите сумму",
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .selectableGroup()
              ,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(10.dp)),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Gray,
                                unselectedColor = Color.Gray,
                                disabledSelectedColor = Color.Gray,
                                disabledUnselectedColor = Color.White
                            ),
                            selected = state.value,
                            onClick = { state.value = true }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {

                            Text(
                                text = "Договор №",
                                color = Color.Black
                            )

                            Text(
                                text = "г. Вологда, Зосимовская (ул.) 83, кв.33",
                                color = Color.LightGray
                            )

                        }

                    }
                    Divide()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                         verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Название тарифа/пакета/услуги"
                        )

                    }


                }


            }




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
                        // makeCall.value = true
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

}

@Composable
fun Divide() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .height(1.dp)
            .background(Color.LightGray)
    )
}