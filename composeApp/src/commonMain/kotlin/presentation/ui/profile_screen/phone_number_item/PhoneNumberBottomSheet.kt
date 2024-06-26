package presentation.ui.profile_screen.phone_number_item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.vectorResource
import util.ColorCustomResources
import util.PhoneNumberTransformationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberBottomSheet(
    onShowBottomSheet: (Boolean) -> Unit
){
    val openBottomSheetState by rememberSaveable { mutableStateOf(true) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
//            containerColor = Color.White, не
//            contentColor = Color.White,
        onDismissRequest = { onShowBottomSheet(false) },
        sheetState = bottomSheetState,
        dragHandle = { },
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .padding(PaddingValues().calculateBottomPadding())
            ) {
                PhoneNumberBottomSheetContent(
                    onShowBottomSheet = {
                        onShowBottomSheet(it)
                    }
                )
            }
        }
    }
}

@Composable
fun PhoneNumberBottomSheetContent(
    onShowBottomSheet: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
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
                text = "Номер телефона",
                color = Color.Black,
                fontWeight = FontWeight.Bold
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
                            onShowBottomSheet(false)
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

    PhoneNumberContent()
}

@Composable
fun PhoneNumberContent(

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
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Номер телефона для получения СМС-уведомлений личного кабинета"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PhoneNumberTransformationHelper(
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
                       // onSendRequest(inputTextPhoneNumber.value)
                    }
                },
                content = {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp),
                        text = "Сохранить"
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
