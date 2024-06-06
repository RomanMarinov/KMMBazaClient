package presentation.ui.auth_activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.call_activity_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthActivityContent(
    onMoveToMainActivity: () -> Unit,
    onShowSnackBarAuthPhone: (Int) -> Unit,
    onShowSnackBarAuthWiFi: (String) -> Unit,
    onShowWarning: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val inputTextPhoneNumber = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .hideKeyboardOnOutsideClick() // убираем клаву
            .pointerInput(Unit) { // снимаем фокус
                detectTapGestures { focusManager.clearFocus() }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(3f))

        Image(
            painter = painterResource(Res.drawable.call_activity_logo),
            modifier = Modifier
                .padding(top = 50.dp)
                .size(width = 156.dp, height = 120.dp),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Личный кабинет",
            fontSize = 30.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                ViewPagerAuth(
                    inputTextPhoneNumber = inputTextPhoneNumber.value,
                    onInputTextPhoneNumber = {
                        inputTextPhoneNumber.value = it
                    },
                    onMoveToMainActivity = {
                        onMoveToMainActivity()
                    },
                    onShowSnackBarAuth = {
                        onShowSnackBarAuthPhone(it)
                    },
                    onShowSnackBarAuthWiFi = {
                        onShowSnackBarAuthWiFi(it)
                    },
                    onShowWarning = {
                        onShowWarning(it)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(16.dp).imePadding(),
            text = "Версия четкая"
        )
    }
}