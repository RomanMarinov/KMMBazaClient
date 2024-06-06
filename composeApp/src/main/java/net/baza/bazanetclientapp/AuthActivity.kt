package net.baza.bazanetclientapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import co.touchlab.kermit.Logger
import presentation.ui.auth_activity.AuthActivityContent
import util.SnackBarHostHelper

//import util.ConnectivityLiveDataHelper

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            Logger.d("4444 AuthActivity loaded")
            val context = LocalContext.current
            val moveState = remember { mutableStateOf(false) }
            val snackBarStateAuthPhone = remember { mutableIntStateOf(-1) }
            val snackBarStateAuthWiFi = remember { mutableStateOf("") }
            val snackBarStateWarning = remember { mutableStateOf(false) }

            // https://stackoverflow.com/questions/78190854/status-bar-color-change-in-compose-multiplatform
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    Color.TRANSPARENT, Color.TRANSPARENT
                ),
                navigationBarStyle = SystemBarStyle.light(
                    Color.TRANSPARENT, Color.TRANSPARENT
                )
            )
            // содержимое вашего приложения располагаться за системными элементами, такими как
            // StatusBar (строка состояния) или NavigationBar (панель навигации) в Android.
            // сначала работало потом изменений не заметил
            WindowCompat.setDecorFitsSystemWindows(window, false)

            AuthActivityContent(
                onMoveToMainActivity = {
                    moveState.value = true
                },
                onShowSnackBarAuthPhone = {
                    Logger.d{"4444 onShowSnackBarAuth доходит ли сюда it=" + it}
                    snackBarStateAuthPhone.intValue = it
                },
                onShowSnackBarAuthWiFi = {
                    snackBarStateAuthWiFi.value = it
                },
                onShowWarning = {
                    Logger.d{"4444 onShowWarning it=" + it}
                    snackBarStateWarning.value = it
                }
            )

            if (moveState.value) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                //App()
            }

            when (snackBarStateAuthPhone.intValue) {
                404 -> {
                    // statusCodeSnackBarState = -1
                    SnackBarHostHelper.WithOkButton(
                        message = "С указанного номера не было звонка",
//                        onAction = {
//                            snackBarStateAuthPhone.intValue - 1
//                        }
                    )
                }

                0 -> {
                    //statusCodeSnackBarState = -1
                    SnackBarHostHelper.WithOkButton(
                        message = "Hе правильно введен номер телефона",
//                        onAction = {
//                            snackBarStateAuthPhone.intValue - 1
//                        }
                    )
                }
            }

            if(snackBarStateAuthWiFi.value.isNotEmpty()) {
                SnackBarHostHelper.WithOkButton(
                    message = snackBarStateAuthWiFi.value,
//                    onAction = {
//                        snackBarStateAuthWiFi.value = ""
//                    }
                )
            }

            if(snackBarStateWarning.value) {
                SnackBarHostHelper.ShortShortTime(
                    message = "Проверьте правильность введенного номера",
                    onFinishTime = {
                        snackBarStateWarning.value = false
                    }
                )
            }

            LifecycleOwnerAuthActivity()
        }
    }
}

@Composable
fun LifecycleOwnerAuthActivity() {
    val localLifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = localLifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        Log.d("4444", " AuthActivity Lifecycle.Event.ON_START")
                    }

                    Lifecycle.Event.ON_STOP -> { // когда свернул
                        Log.d("4444", " AuthActivity Lifecycle.Event.ON_STOP")
                    }

                    Lifecycle.Event.ON_DESTROY -> { // когда удалил из стека
                        Log.d("4444", " AuthActivity Lifecycle.Event.ON_DESTROY")
                    }

                    else -> {}
                }
            }
            localLifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                localLifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
}

//@Composable
//fun Perm() {
//    val permissionsState = rememberMultiplePermissionsState(
//        permissions = listOf(
//            Manifest.permission.READ_PHONE_NUMBERS,
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//        )
//    )
//    permissionsState.permissions.forEach {
//        when (it.permission) {
//            Manifest.permission.READ_PHONE_NUMBERS -> {
//                when {
//                    it.status.isGranted -> {
//                        Log.d("4444", " MainScreensActivity READ_PHONE_NUMBERS isGranted")
//                        savePermissionReadPhoneNumbers(viewModel = viewModel, isGranted = true)
//                    }
//
//                    it.status.shouldShowRationale -> {
//                        Log.d("4444", " MainScreensActivity READ_PHONE_NUMBERS shouldShowRationale ")
//                        savePermissionReadPhoneNumbers(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_READ_PHONE_NUMBERS),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//
//                    it.isAlwaysDenied() -> {
//                        Log.d("4444", " MainScreensActivity READ_PHONE_NUMBERS isAlwaysDenied")
//                        savePermissionReadPhoneNumbers(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_denied_READ_PHONE_NUMBERS),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//                }
//            }
//
//            Manifest.permission.READ_CONTACTS -> {
//                when {
//                    it.status.isGranted -> {
//                        Log.d("4444", " MainScreensActivity READ_CONTACTS isGranted")
//                        savePermissionReadContacts(viewModel = viewModel, isGranted = true)
//                    }
//
//                    it.status.shouldShowRationale -> {
//                        Log.d("4444", " MainScreensActivity READ_CONTACTS shouldShowRationale")
//                        savePermissionReadContacts(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_CONTACTS),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//
//                    it.isAlwaysDenied() -> {
//                        Log.d("4444", " MainScreensActivity READ_CONTACTS isAlwaysDenied")
//                        savePermissionReadContacts(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_denied_CONTACTS),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//                }
//            }
//
//            Manifest.permission.CAMERA -> {
//                when {
//                    it.status.isGranted -> {
//                        Log.d("4444", " MainScreensActivity CAMERA isGranted")
//                        savePermissionCamera(viewModel = viewModel, isGranted = true)
//                    }
//
//                    it.status.shouldShowRationale -> {
//                        Log.d("4444", " MainScreensActivity CAMERA shouldShowRationale")
//                        savePermissionCamera(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_CAMERA),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//
//                    it.isAlwaysDenied() -> {
//                        Log.d("4444", " MainScreensActivity CAMERA isAlwaysDenied")
//                        savePermissionCamera(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_denied_CAMERA),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//                }
//            }
//
//            Manifest.permission.RECORD_AUDIO -> {
//                when {
//                    it.status.isGranted -> {
//                        Log.d("4444", " MainScreensActivity RECORD_AUDIO isGranted")
//                        savePermissionRecordAudio(viewModel = viewModel, isGranted = true)
//                    }
//
//                    it.status.shouldShowRationale -> {
//                        Log.d("4444", " MainScreensActivity RECORD_AUDIO shouldShowRationale")
//                        savePermissionRecordAudio(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_RECORD_AUDIO),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//
//                    it.isAlwaysDenied() -> {
//                        Log.d("4444", " MainScreensActivity RECORD_AUDIO isAlwaysDenied")
//                        savePermissionRecordAudio(viewModel = viewModel, isGranted = false)
//                        DialogPermissions(
//                            message = stringResource(id = R.string.justification_denied_RECORD_AUDIO),
//                            onDismiss = { },
//                            onConfirm = {
//                                openAppSettings(context = context)
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}


// код из туториала


//@Composable
//fun TutorialInputMaskScreen() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
//    ) {
//        Text(text = "Char", fontSize = 22.sp)
//        TutorialCharVisualTransformation()
//        Text(text = "Manual", fontSize = 22.sp)
//        TutorialManualVisualTransformation()
//        Text(text = "Auto", fontSize = 22.sp)
//        TutorialAutoVisualTransformation()
//    }
//}
//
//@Composable
//fun TutorialCharVisualTransformation() {
//    var inputText by remember { mutableStateOf("") }
//    PasswordVisualTransformation()
//    TextField(
//        value = inputText,
//        onValueChange = { currentText -> inputText = currentText },
//        label = { Text(text = "Char") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//        visualTransformation = { inputAnnotatedString ->
//            TransformedText(
//                text = AnnotatedString("$".repeat(inputAnnotatedString.text.length)),
//                offsetMapping = OffsetMapping.Identity
//            )
//        }
//    )
//}
//
//

// как работает
// при длине inputText(это только Х-ы) в диапазоне от 1 до 2 я буду видеть (00
// как только длина будет от 3 до 7

//
//@Composable
//fun TutorialManualVisualTransformation() {
//    class ManualVisualTransformation(val enableCursorMove: Boolean) : VisualTransformation {
//        override fun filter(text: AnnotatedString): TransformedText {
//            val inputText = text.text // "1111"
//            // "(XX) XXXXX - XXXX"
//            val formattedText = when(inputText.length) {
//                in (1..2) -> "($inputText"
//                in (3..7) -> "(${inputText.substring(0, 2)}) ${inputText.substring(2)}" // inputText.substring(2) вернет с 3 символа и до конца
//                in (8..11) ->  {
//                    "(${inputText.substring(0, 2)}) ${inputText.substring(2, 7)} - ${inputText.substring(7)}"
//                }
//                else -> ""
//            }
//
//            val offsetTranslator = object : OffsetMapping {
//                override fun originalToTransformed(offset: Int): Int {
//                    // offset = input.count/original at cursor position -- "(11) 1|" offset = 3
//                    val transformedCursor = when (offset) {
//                        in (1..2) -> offset + 1 // "(XX|" offset = 2, sep = 1, result = 3
//                        in (3..7) -> offset + 3 // "(XX) X|" offset = 3, sep = 3, result = 6
//                        in (8..11) -> offset + 6
//                        else -> offset
//                    }
//
//                    return if (enableCursorMove) transformedCursor // formattedText
//                    else formattedText.length // "(XX) XXXXX - XXXX" return 17
//                }
//
//                override fun transformedToOriginal(offset: Int): Int {
//                    // offset = formatted.length/transformed at cursor position -- "(11) 1|" offset = 6
//                    val originalCursor = when (offset) {
//                        in (1..3) -> offset - 1 // "(XX|" offset = 3, sep = 1, result = 2
//                        in (4..10) -> offset - 3 // "(XX) X|" offset = 6, sep = 3, result = 3
//                        in (11..17) -> offset - 6
//                        else -> offset
//                    }
//
//                    return if (enableCursorMove) originalCursor // inputs
//                    else inputText.length // "(XX) XXXXX - XXXX" return 11
//                }
//            }
//            return TransformedText(AnnotatedString(formattedText), offsetTranslator)
//        }
//    }
//
//    var inputText by remember { mutableStateOf("") }
//    val mask = "(XX) XXXXX - XXXX"
//    val maskCharInput = 'X'
//    val inputMaxLength = mask.count { maskChar -> maskChar == maskCharInput }
//
//    TextField(
//        value = inputText,
//        onValueChange = { currentText ->
//            inputText = currentText.take(inputMaxLength).filter { it.isDigit() }
//        },
//        label = { Text(text = "Manual") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//        visualTransformation = ManualVisualTransformation(enableCursorMove = true)
//    )
//}
//
//@Composable
//fun TutorialAutoVisualTransformation() {
//    class AutoVisualTransformation(
//        val mask: String,
//        val maskCharInput: Char,
//        val enableCursorMove: Boolean
//    ) : VisualTransformation {
//        override fun filter(text: AnnotatedString): TransformedText {
//            val inputText = text.text
//            val formattedText = buildAnnotatedString {
//                var separatorCount = 0
//                var formattedTextLength = 0 // "(11) 1" input = 3, sep = 3, result = 6
//                while (formattedTextLength < inputText.length + separatorCount) { // until 6
//                    if (mask[formattedTextLength++] != maskCharInput) separatorCount++ // '(' != 'X'
//                }
//                var inputIndex = 0
//                mask.take(formattedTextLength).forEach { maskCurrentChar -> // "(XX) XXXXX - XXXX"
//                    if (maskCurrentChar != maskCharInput) append(maskCurrentChar) // '(', ')', ' '
//                    else append(inputText[inputIndex++]) // '1','1','1'
//                }
//            }
//
//            val offsetTranslator = object : OffsetMapping {
//                override fun originalToTransformed(offset: Int): Int {
//                    // offset = input.count/original at cursor position -- "(11) 1|" offset = 3
//                    var separatorCount = 0 // "(11) 1" sep = 3
//                    var transformedCursor = 0 // inputs + separators = formatted.length using offset
//                    while (transformedCursor < offset + separatorCount) { // "(11) 1" until 6
//                        if (mask[transformedCursor++] != maskCharInput) separatorCount++ // '(' != 'X'
//                    }
//                    return if (enableCursorMove) transformedCursor // "(11) 1|" offset/input + sep = 6
//                    else formattedText.length // "(XX) XXXXX - XXXX" return 17
//                }
//
//                override fun transformedToOriginal(offset: Int): Int {
//                    // offset = formatted.count/transformed at cursor position -- "(11) 1|" offset = 6
//                    return if (enableCursorMove) {
//                        val sepCount = mask.take(offset).count { it != maskCharInput } // "() " sep = 3
//                        offset - sepCount // originalCursor/inputs "(11) 1|" offset/form.length - sep = 3
//                    } else inputText.length // "(XX) XXXXX - XXXX" return 11
//                }
//            }
//            return TransformedText(formattedText, offsetTranslator)
//        }
//    }
//
//    var inputText by remember { mutableStateOf("") }
//    val mask = "(XX) XXXXX - XXXX"
//    val maskCharInput = 'X'
//    val inputMaxLength = mask.count { maskChar -> maskChar == maskCharInput }
//
//    TextField(
//        value = inputText,
//        onValueChange = { currentText ->
//            inputText = currentText.take(inputMaxLength).filter { it.isDigit() }
//        },
//        label = { Text(text = "Auto") },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//        visualTransformation = AutoVisualTransformation(
//            mask = mask,
//            maskCharInput = maskCharInput,
//            enableCursorMove = false
//        )
//    )
//}