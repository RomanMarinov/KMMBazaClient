package presentation.ui.request_address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import domain.add_address.Data
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.add_address.AddAddressViewModel
import presentation.ui.request_result.RequestResultBottomSheet
import util.ColorCustomResources
import util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestAddressBottomSheet(
    address: String,
    dataAddress: Data?,
    navigationFrom: String,
    onShowCurrentBottomSheet: (Boolean) -> Unit,
    onShowPreviousBottomSheet: (Boolean) -> Unit,
    viewModel: AddAddressViewModel
) {
//    val openBottomSheetState by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val requestNumber by viewModel.requestNumber.collectAsState()

    val isSendRequest = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val inputTextPhoneNumber = remember { mutableStateOf("") }
    val isShowRequestResultBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(requestNumber) {
        if (requestNumber != Constants.RequestResultTicketId.DEFAULT_INT) {
            isShowRequestResultBottomSheet.value = true
        }
    }

    // if (openBottomSheetState) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
//            containerColor = Color.White, не
//            contentColor = Color.White,
        onDismissRequest = { onShowCurrentBottomSheet(false) },
        sheetState = bottomSheetState,
        dragHandle = { },
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth().hideKeyboardOnOutsideClick()
                .background(Color.White)
        ) {
            RequestAddressBottomSheetContent(
                navigationFrom = navigationFrom,
                address = address,
                onShowCurrentBottomSheet = {
                    onShowCurrentBottomSheet(it)
                },
                onSendRequest = { phone ->
                    inputTextPhoneNumber.value = phone
                    isSendRequest.value = true
                }
            )
        }
    }
    // }

    LaunchedEffect(isSendRequest.value) {
        if (isSendRequest.value) {
            viewModel.sendRequest(
                navigationFrom = navigationFrom,
                inputTextPhoneNumber = inputTextPhoneNumber.value,
                dataAddress = dataAddress
            )
        }
    }

    if (isShowRequestResultBottomSheet.value) {
        RequestResultBottomSheet(
            ticketId = requestNumber,
            navigationFrom = navigationFrom,
            onCloseAllBottomSheet = {
                onShowPreviousBottomSheet(false)
            }
        )
    }
}

@Composable
fun RequestAddressBottomSheetContent(
    navigationFrom: String,
    address: String,
    onShowCurrentBottomSheet: (Boolean) -> Unit,
    onSendRequest: (String) -> Unit
) {

    val isCallingPhone = remember { mutableStateOf(false) }
    val isShowWarning = remember { mutableStateOf(false) }
    val isFilledPhoneNumber = remember { mutableStateOf(false) }
    val isShowBottomSheetQuestion = remember { mutableStateOf(false) }
    val inputTextPhoneNumber = remember { mutableStateOf("") }
    val errorTextWarning = remember { mutableStateOf("") }
    val errorLineColorClick = remember { mutableStateOf(ColorCustomResources.colorBazaMainBlue) }

    val focusManager = LocalFocusManager.current
    LaunchedEffect(inputTextPhoneNumber.value) {

        Logger.d("4444 inputTextPhoneNumber=" + inputTextPhoneNumber.value)
        if (inputTextPhoneNumber.value.length == 10) {
            focusManager.clearFocus()
            isFilledPhoneNumber.value = true
            isCallingPhone.value = false
        } else {
            isFilledPhoneNumber.value = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Добавление адреса",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Card(
                modifier = Modifier
                    .size(34.dp),
                //    .align(Alignment.CenterEnd)
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = ColorCustomResources.colorBackgroundClose),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onShowCurrentBottomSheet(false)
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
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Услуга Умного домофона пока не доступна на Вашем доме",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = address,
            fontSize = 18.sp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Оставьте заявку на ее подключение"
        )
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RequestAddressPhoneNumberTransformation(
            inputTextPhoneNumber = inputTextPhoneNumber.value,
            errorLineColorClick = errorLineColorClick.value,
            onInputTextPhoneNumber = {
                inputTextPhoneNumber.value = it
            },
            onErrorLineColor = {
                errorLineColorClick.value = it
            },
            onShowWarning = {
                isShowWarning.value = it
                errorTextWarning.value = "Только мобильные номера"
            }
        )

        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            if (isShowWarning.value) {
                Text(
                    color = Color.Red,
                    text = errorTextWarning.value
                )
            }
        }


        Row() {
            Text(
                text = "Отправляя заявку, Вы соглашаетесь на обработку персональных даных.",
                fontSize = 14.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                modifier = Modifier
                    // .fillMaxWidth()
                    .padding(bottom = 16.dp),
//                .shadow(2.dp, RoundedCornerShape(2.dp)),
                shape = RoundedCornerShape(8.dp),
                enabled = isFilledPhoneNumber.value,
                onClick = {
                    if (inputTextPhoneNumber.value.length == 10) {
                        onSendRequest(inputTextPhoneNumber.value)
                    }
                },
                content = {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp),
                        text = "Отправить заявку"
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