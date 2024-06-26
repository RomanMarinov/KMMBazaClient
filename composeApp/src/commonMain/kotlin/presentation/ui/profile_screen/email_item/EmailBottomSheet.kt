package presentation.ui.profile_screen.email_item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.ktor.client.plugins.api.Send
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.vectorResource
import presentation.ui.profile_screen.ProfileScreenViewModel
import util.CheckEmailTextFieldHelper
import util.ColorCustomResources
import util.PhoneNumberTransformationHelper
import util.SnackBarHostHelper
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailBottomSheet(
    onShowBottomSheet: (Boolean) -> Unit,
    viewModel: ProfileScreenViewModel
) {
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
                EmailBottomSheetContent(
                    onShowBottomSheet = {
                        onShowBottomSheet(it)
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun EmailBottomSheetContent(
    onShowBottomSheet: (Boolean) -> Unit,
    viewModel: ProfileScreenViewModel
) {
    Column {
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
                    text = "Электронная почта",
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

        EmailContent(
            viewModel = viewModel,
            onShowBottomSheet = {
                onShowBottomSheet(false)
        })
    }
}

@Composable
fun EmailContent(
    viewModel: ProfileScreenViewModel,
    onShowBottomSheet: (Boolean) -> Unit
) {
    val isShowWarning = remember { mutableStateOf(false) }
    val isCheckValidEmail = remember { mutableStateOf(false) }
    val emailOutlinedTextField = remember { mutableStateOf("") }

    val responseSendEmail = viewModel.responseSendEmail.collectAsStateWithLifecycle()

    val elevatedButtonTitle = remember { mutableStateOf("Сохранить") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Привяжите электронную почту для отправки чеков и документов."
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

        EmailOutlinedTextField(
            emailOutlinedTextField = emailOutlinedTextField.value,
            onValueChange = {
                emailOutlinedTextField.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            onClearText = {
                emailOutlinedTextField.value = ""
            }
        )

        Box(
            modifier = Modifier
                .height(100.dp)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isShowWarning.value) {
                Text(
                    color = Color.Red,
                    text = "Проверьте правильность ввода email"
                )
            }
            when(responseSendEmail.value) {
                SendEmailState.SUCCESS -> {
                    LottieSuccessResultSendEmail()
                    elevatedButtonTitle.value = "ОК"
                }
                SendEmailState.FAILURE -> {
                    LottieFailureResultSendEmail()
                    elevatedButtonTitle.value = "Сохранить"
                }
                SendEmailState.DEFAULT -> {
                    elevatedButtonTitle.value = "Сохранить"
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
            //  .padding(top = 8.dp)

            ,
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                modifier = Modifier
                    // .fillMaxWidth()
                    .padding(bottom = 16.dp),
//                .shadow(2.dp, RoundedCornerShape(2.dp)),
                shape = RoundedCornerShape(8.dp),
                enabled = emailOutlinedTextField.value.isNotEmpty(),
                onClick = {
                    when(elevatedButtonTitle.value) {
                        "Сохранить" -> {
                            isCheckValidEmail.value = true
                        }
                        "ОК" -> {
                            onShowBottomSheet(false)
                        }
                    }
                },
                content = {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp),
                        text = elevatedButtonTitle.value
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = ColorCustomResources.colorBazaMainBlue
                )
            )
        }
    }

    LaunchedEffect(emailOutlinedTextField.value) {
        isShowWarning.value = false
        viewModel.resetSendEmailState()
    }

    LaunchedEffect(isCheckValidEmail.value) {
        if (isCheckValidEmail.value) {
            val emailValid = CheckEmailTextFieldHelper.check(emailOutlinedTextField.value)
            isShowWarning.value = !emailValid
            if (emailValid) {
                viewModel.sendActualEmailToServer(email = emailOutlinedTextField.value)
            }
            isCheckValidEmail.value = false
        }
    }
}

@Composable
fun LottieSuccessResultSendEmail() {
    val lottieData = """
{"nm":"Comp 1","ddd":0,"h":512,"w":512,"meta":{"g":"@lottiefiles/toolkit-js 0.33.2"},"layers":[{"ty":0,"nm":"trait","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[15,15,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[263.334,471.109,0],"ix":2},"r":{"a":0,"k":90,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_0","ind":1},{"ty":0,"nm":"trait","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[15,15,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[51.641,253.275,0],"ix":2},"r":{"a":0,"k":-180,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_0","ind":2},{"ty":0,"nm":"trait","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[15,15,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[266.322,44.315,0],"ix":2},"r":{"a":0,"k":-90,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_0","ind":3},{"ty":0,"nm":"trait","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[15,15,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[469.91,258.792,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_0","ind":4},{"ty":0,"nm":"firefly","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[20,20,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[400.635,189.708,0],"ix":2},"r":{"a":0,"k":-18.097,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_1","ind":5},{"ty":0,"nm":"firefly","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[20,20,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[359.413,150.912,0],"ix":2},"r":{"a":0,"k":-72.471,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_1","ind":6},{"ty":0,"nm":"firefly","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[30,30,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[396.894,150.961,0],"ix":2},"r":{"a":0,"k":-45.707,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_1","ind":7},{"ty":0,"nm":"trait 2","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[-19.512,19.512,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[410.865,406.53,0],"ix":2},"r":{"a":0,"k":-135.205,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_2","ind":8},{"ty":0,"nm":"trait 2","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[-19.512,19.512,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[105.535,402.598,0],"ix":2},"r":{"a":0,"k":-45.606,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_2","ind":9},{"ty":0,"nm":"trait 2","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[19.512,19.512,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[104.864,111.71,0],"ix":2},"r":{"a":0,"k":-135.205,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_2","ind":10},{"ty":0,"nm":"trait 2","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[256,256,0],"ix":1},"s":{"a":0,"k":[19.512,19.512,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[416.722,113.206,0],"ix":2},"r":{"a":0,"k":-45.606,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"w":512,"h":512,"refId":"comp_2","ind":11},{"ty":4,"nm":"Shape Layer 2","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[69.59,69.59,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[236.888,240.258,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Shape 1","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":false,"i":[[0,0],[0,0],[0,0]],"o":[[0,0],[0,0],[0,0]],"v":[[-76.426,37.999],[12.056,114.074],[169.991,-68.635]]},"ix":2}},{"ty":"st","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Stroke","nm":"Stroke 1","lc":2,"lj":1,"ml":4,"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":35,"ix":5},"c":{"a":0,"k":[1,1,1],"ix":3}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[-7,11],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"tm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Trim","nm":"Trim Paths 1","ix":2,"e":{"a":1,"k":[{"o":{"x":0.167,"y":0},"i":{"x":0.833,"y":-2.986},"s":[0],"t":0},{"o":{"x":0.167,"y":0.042},"i":{"x":0,"y":0.973},"s":[0],"t":14.791},{"o":{"x":0.828,"y":0.011},"i":{"x":0.833,"y":0.833},"s":[32],"t":19.791},{"s":[100],"t":24.7912510097683}],"ix":2},"o":{"a":0,"k":0,"ix":3},"s":{"a":0,"k":0,"ix":1},"m":1}],"ind":12},{"ty":4,"nm":"Shape Layer 1","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[80,80,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[256,256,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Ellipse 1","ix":1,"cix":2,"np":2,"it":[{"ty":"el","bm":0,"hd":false,"mn":"ADBE Vector Shape - Ellipse","nm":"Ellipse Path 1","d":1,"p":{"a":0,"k":[0,0],"ix":3},"s":{"a":1,"k":[{"o":{"x":0.654,"y":0.007},"i":{"x":0.976,"y":0.968},"s":[0,0],"t":0},{"o":{"x":0.346,"y":-4.83},"i":{"x":0.468,"y":1.057},"s":[401.025,401.025],"t":7},{"o":{"x":0.364,"y":0.031},"i":{"x":0.375,"y":1.543},"s":[372.7,372.7],"t":12},{"o":{"x":0.327,"y":-8.038},"i":{"x":0.833,"y":1},"s":[401.025,401.025],"t":16},{"s":[401.025,401.025],"t":20.0000008146167}],"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.2941,0.5529,0.6157],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[5.992,3.49],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":13}],"v":"5.0.1","fr":29.9700012207031,"op":45.0000018328876,"ip":0,"assets":[{"nm":"","id":"comp_0","layers":[{"ty":4,"nm":"Shape Layer 1","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[256,256,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.572,"y":0.556},"s":[100],"t":7},{"o":{"x":0.641,"y":0.056},"i":{"x":0.833,"y":0.833},"s":[92.154],"t":13},{"s":[30],"t":17.0000006924242}],"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Shape 1","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[-230,4],[214,4]]},"ix":2}},{"ty":"st","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Stroke","nm":"Stroke 1","lc":2,"lj":1,"ml":4,"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":70,"ix":5},"c":{"a":0,"k":[0.2941,0.5529,0.6157],"ix":3}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"tm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Trim","nm":"Trim Paths 1","ix":2,"e":{"a":1,"k":[{"o":{"x":0.457,"y":0.063},"i":{"x":0.566,"y":0.999},"s":[0],"t":7},{"s":[100],"t":16.0000006516934}],"ix":2},"o":{"a":0,"k":0,"ix":3},"s":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.588,"y":-51709.363},"s":[0],"t":7},{"o":{"x":0.509,"y":0.003},"i":{"x":0.696,"y":0.999},"s":[0],"t":10},{"s":[100],"t":16.0000006516934}],"ix":1},"m":1}],"ind":1}]},{"nm":"","id":"comp_1","layers":[{"ty":4,"nm":"Shape Layer 1","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[256,256,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Ellipse 1","ix":1,"cix":2,"np":2,"it":[{"ty":"el","bm":0,"hd":false,"mn":"ADBE Vector Shape - Ellipse","nm":"Ellipse Path 1","d":1,"p":{"a":0,"k":[0,0],"ix":3},"s":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0,"y":0.997},"s":[40,40],"t":24},{"o":{"x":0.574,"y":-0.004},"i":{"x":0.833,"y":0.833},"s":[90,90],"t":27},{"s":[18.394,18.394],"t":38.0000015477717}],"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,1,1],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.667,"y":1},"s":[-181.074,-5.414],"t":24,"ti":[-26.72825050354,0],"to":[34.0465698242188,0]},{"s":[200,-5.414],"t":38.0000015477717}],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[0],"t":24},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[100],"t":25},{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[100],"t":29},{"s":[0],"t":38.0000015477717}],"ix":7}}]}],"ind":1}]},{"nm":"","id":"comp_2","layers":[{"ty":4,"nm":"Shape Layer 1","sr":1,"st":0,"op":150.000006109625,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[256,256,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.572,"y":0.556},"s":[100],"t":10},{"o":{"x":0.641,"y":0.056},"i":{"x":0.833,"y":0.833},"s":[92.154],"t":16},{"s":[30],"t":20.0000008146167}],"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Shape 1","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[-230,4],[214,4]]},"ix":2}},{"ty":"st","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Stroke","nm":"Stroke 1","lc":2,"lj":1,"ml":4,"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":70,"ix":5},"c":{"a":0,"k":[0.2941,0.5529,0.6157],"ix":3}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[0,0],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]},{"ty":"tm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Trim","nm":"Trim Paths 1","ix":2,"e":{"a":1,"k":[{"o":{"x":0.457,"y":0.063},"i":{"x":0.566,"y":0.999},"s":[0],"t":10},{"s":[100],"t":19.0000007738859}],"ix":2},"o":{"a":0,"k":0,"ix":3},"s":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.588,"y":-51709.363},"s":[0],"t":10},{"o":{"x":0.509,"y":0.003},"i":{"x":0.696,"y":0.999},"s":[0],"t":13},{"s":[100],"t":19.0000007738859}],"ix":1},"m":1}],"ind":1}]}]}
"""
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(lottieData))
    val progress by animateLottieCompositionAsState(
        composition = composition,
       // iterations = LottieConstants.IterateForever, // бесконечно
        isPlaying = true, // пауза/воспроизведение
        speed = 1.0f,
        restartOnPlay = false // передать false, чтобы продолжить анимацию на котором он был приостановлен
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.padding(end = 16.dp)) {
            composition?.let {
                LottieAnimation(
                    composition = it,
                    progress = { progress.absoluteValue },
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Column {
            Text(text = "Вы привязали email")
        }
    }
}


@Composable
fun LottieFailureResultSendEmail() {
    val lottieData = """
{"nm":"checkanimation","ddd":0,"h":800,"w":800,"meta":{"g":"LottieFiles AE 0.1.20","tc":"#ffffff"},"layers":[{"ty":4,"nm":"exclamation Outlines","sr":1,"st":0,"op":724.999937249057,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[76,344,0],"ix":1},"s":{"a":0,"k":[42.106,42.106,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[400,394,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 1","ix":1,"cix":2,"np":6,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[-0.917,-18.494],[0,0],[0,0],[18.935,-0.896],[0,0],[0,0],[0.918,18.494],[0,0],[0,0],[-18.935,0.896],[0,0]],"o":[[19.11,0],[0,0],[0,0],[0,18.665],[0,0],[0,0],[-19.11,0],[0,0],[0,0],[0,-18.665],[0,0],[0,0]],"v":[[23.606,-344],[59.959,-310.094],[60,-308.454],[60,60.454],[25.285,95.96],[23.606,96],[-23.607,96],[-59.96,62.094],[-60,60.454],[-60,-308.454],[-25.286,-343.96],[-23.607,-344]]},"ix":2}},{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 2","ix":2,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[-49.674,0],[0,-34.546],[42.719,0],[0,40.468]],"o":[[42.719,0],[0,40.468],[-49.674,0],[0,-34.546]],"v":[[3.477,192],[76,265.039],[3.477,344],[-76,265.039]]},"ix":2}},{"ty":"mm","bm":0,"hd":false,"mn":"ADBE Vector Filter - Merge","nm":"Merge Paths 1","mm":1},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[1,1,1],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.833,"y":0.8},"s":[76,344],"t":34,"ti":[0,0],"to":[0,-16.138]},{"o":{"x":0.333,"y":0},"i":{"x":0.833,"y":0.833},"s":[76,247.169],"t":37,"ti":[0,-16.138],"to":[0,0]},{"s":[76,344],"t":38.9999966244321}],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":1},{"ty":4,"nm":"Icon","sr":1,"st":0,"op":724.999937249057,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[400,400,0],"ix":1},"s":{"a":0,"k":[104.688,104.688,100],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[400,400,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":0,"k":100,"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Group 2","ix":1,"cix":2,"np":2,"it":[{"ty":"sh","bm":0,"hd":false,"mn":"ADBE Vector Shape - Group","nm":"Path 1","ix":1,"d":1,"ks":{"a":0,"k":{"c":true,"i":[[-141.385,0],[0,-141.385],[141.385,0],[0,141.385]],"o":[[141.385,0],[0,141.385],[-141.385,0],[0,-141.385]],"v":[[0,-256],[256,0],[0,256],[-256,0]]},"ix":2}},{"ty":"fl","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Fill","nm":"Fill 1","c":{"a":0,"k":[0.7882,0.3765,0.3961],"ix":4},"r":1,"o":{"a":0,"k":100,"ix":5}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[400,400],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":2},{"ty":4,"nm":"Circle-fade-1","sr":1,"st":0,"op":724.999937249057,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.833,"y":0.833},"s":[100,100,100],"t":0},{"s":[140,140,100],"t":47.9999958454548}],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[400,400,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[100],"t":0},{"s":[0],"t":47.9999958454548}],"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Ellipse 1","ix":1,"cix":2,"np":3,"it":[{"ty":"el","bm":0,"hd":false,"mn":"ADBE Vector Shape - Ellipse","nm":"Ellipse Path 1","d":1,"p":{"a":0,"k":[0,0],"ix":3},"s":{"a":0,"k":[497.5,497.5],"ix":2}},{"ty":"st","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Stroke","nm":"Stroke 1","lc":1,"lj":1,"ml":4,"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":6,"ix":5},"c":{"a":0,"k":[0.7882,0.3765,0.3961],"ix":3}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[103.83,103.83],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[-1.328,0.391],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":3},{"ty":4,"nm":"Circle-fade-3","sr":1,"st":0,"op":724.999937249057,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":1,"k":[{"o":{"x":0.333,"y":0},"i":{"x":0.833,"y":0.833},"s":[100,100,100],"t":24},{"s":[135,135,100],"t":71.9999937681822}],"ix":6},"sk":{"a":0,"k":0},"p":{"a":0,"k":[400,400,0],"ix":2},"r":{"a":0,"k":0,"ix":10},"sa":{"a":0,"k":0},"o":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.833,"y":0.833},"s":[100],"t":24},{"s":[0],"t":71.9999937681822}],"ix":11}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"mn":"ADBE Vector Group","nm":"Ellipse 1","ix":1,"cix":2,"np":3,"it":[{"ty":"el","bm":0,"hd":false,"mn":"ADBE Vector Shape - Ellipse","nm":"Ellipse Path 1","d":1,"p":{"a":0,"k":[0,0],"ix":3},"s":{"a":0,"k":[497.5,497.5],"ix":2}},{"ty":"st","bm":0,"hd":false,"mn":"ADBE Vector Graphic - Stroke","nm":"Stroke 1","lc":1,"lj":1,"ml":4,"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":6,"ix":5},"c":{"a":0,"k":[0.7882,0.3765,0.3961],"ix":3}},{"ty":"tr","a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[103.83,103.83],"ix":3},"sk":{"a":0,"k":0,"ix":4},"p":{"a":0,"k":[-1.328,0.391],"ix":2},"r":{"a":0,"k":0,"ix":6},"sa":{"a":0,"k":0,"ix":5},"o":{"a":0,"k":100,"ix":7}}]}],"ind":4}],"v":"5.5.7","fr":23.9759979248047,"op":70.9999938547353,"ip":5.99999948068185,"assets":[]}
"""
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(lottieData))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever, // бесконечно
        isPlaying = true, // пауза/воспроизведение
        speed = 1.0f,
        restartOnPlay = false // передать false, чтобы продолжить анимацию на котором он был приостановлен
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.padding(end = 16.dp)) {
            composition?.let {
                LottieAnimation(
                    composition = it,
                    progress = { progress.absoluteValue },
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Column {
            Text(text = "Ошибка привязки email")
            Text(text = "Попробуйте еще раз")
        }
    }
}

@Composable
fun EmailOutlinedTextField(
    emailOutlinedTextField: String,
    onValueChange: (String) -> Unit,
    onClearText: () -> Unit,
    modifier: Modifier = Modifier
) {

    val borderColor = if (emailOutlinedTextField.isNotEmpty()) {
        ColorCustomResources.colorBazaMainBlue
    } else {
        Color.Gray
    }

    OutlinedTextField(
        value = emailOutlinedTextField,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = TextStyle(fontSize = 18.sp),
        placeholder = { Text(text = "Email", color = Color.Gray) },
        modifier = modifier
            .border(1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)),
        singleLine = true,
        trailingIcon = {
            if (emailOutlinedTextField.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearText()
                    }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White
        )
    )
}
