package util


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import util.ColorCustomResources

@Composable
fun PhoneNumberTransformationHelper(
    inputTextPhoneNumber: String,
    errorLineColorClick: Color,
    onInputTextPhoneNumber: (String) -> Unit,
    onErrorLineColor: (Color) -> Unit,
    onShowWarning: (Boolean) -> Unit
) {
    class ManualVisualTransformation(val enableCursorMove: Boolean) : VisualTransformation {
        // Этот метод преобразует входной текст в форматированный текст согласно маске.
        override fun filter(text: AnnotatedString): TransformedText {
            val inputText = text.text
            //Logger.d { "4444 inputText=" + inputText }
            // "+7 (XXX)-XXX-XX-XX" // моя маска 15 символов с пробелами

            val formattedText = when (inputText.length) {
                in (1..3) -> "+7 ($inputText"
                in (4..6) -> "+7 (${inputText.substring(0, 3)})-${inputText.substring(3)}"
                in (7..8) -> "+7 (${inputText.substring(0, 3)})-${
                    inputText.substring(
                        3,
                        6
                    )
                }-${inputText.substring(6)}"

                in (9..10) -> "+7 (${inputText.substring(0, 3)})-${
                    inputText.substring(
                        3,
                        6
                    )
                }-${inputText.substring(6, 8)}-${inputText.substring(8)}"

                else -> ""
            }

            val offsetTranslator = object : OffsetMapping {
                // Преобразует позицию курсора вперед из оригинального текста в позицию курсора в форматированном тексте.
                // offset: Int - позиция курсора в оригинальном тексте.
                // Int - позиция курсора в форматированном тексте.
                override fun originalToTransformed(offset: Int): Int {
                    // offset = input.count/original at cursor position -- "(11) 1|" offset = 3

                    // Logger.d { "4444 originalToTransformed offset=" + offset }
                    val transformedCursor = when (offset) {
                        in (1..3) -> offset + 4 // "(XX|" offset = 2, sep = 1, result = 3
                        in (4..6) -> offset + 6 // "(XX) X|" offset = 3, sep = 3, result = 6
                        in (7..8) -> offset + 7
                        in (9..10) -> offset + 8
                        else -> offset
                    }

                    // Logger.d{"4444 originalToTransformed transformedCursor=" + transformedCursor}
                    return if (enableCursorMove) transformedCursor // formattedText
                    else formattedText.length // "+7 (XXX)-XXX-XX-XX" return 17
                }

                // Преобразует позицию курсора при клике из форматированного текста в позицию курсора в оригинальном тексте.
                // offset: Int - позиция курсора в форматированном тексте.
                // Int - позиция курсора в оригинальном номере телефона (на Х-ах).
                override fun transformedToOriginal(offset: Int): Int {
                    // Logger.d { "4444 transformedToOriginal offset=" + offset }
                    val originalCursor = when (offset) {
                        in (4..7) -> offset - 4
                        in (9..12) -> offset - 6
                        in (13..15) -> offset - 7
                        in (16..18) -> offset - 8 // надо здесь
                        else -> offset // "+7 (XXX)-XXX-XX-XX" 18 элементов
                    }
                    val validCursor =
                        if (originalCursor > inputText.length) inputText.length else originalCursor

                    //Logger.d { "4444 originalCursor=" + validCursor }

                    return if (enableCursorMove) validCursor // inputs
                    else inputText.length // "(XXX) - XXX - XX - XX" return 10

//                    Logger.d { "4444 transformedToOriginal originalCursor=" + originalCursor }
//                    return if (enableCursorMove) originalCursor // inputs
//                    else inputText.length // "(XXX) - XXX - XX - XX" return 10
                }
            }
            return TransformedText(AnnotatedString(formattedText), offsetTranslator)
        }
    }

    val errorTextColor = remember { mutableStateOf(Color.Black) }
    //val errorLineColor = remember { mutableStateOf(errorLineColorClick) }
    //  errorLineColor.value = errorLineColorClick

    val mask = "+7 (XXX)-XXX-XX-XX"
    val maskCharInput = 'X'
    val inputMaxLength = mask.count { maskChar -> maskChar == maskCharInput }

    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    // Logger.d { " 4444 inputText=" + inputText }

    DisposableEffect(Unit) {
        Logger.d("4444 DisposableEffect сработал фокус на адресе")
        val job = scope.launch {
            delay(10L)
            focusRequester.requestFocus()
        }
        onDispose {
            job.cancel() // Отменяем выполнение корутины при уничтожении эффекта
        }
    }


    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            //.background(Color.Transparent)
            .border(0.5.dp, color = Color.Transparent)
    ) {
        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                // .background(Color.Transparent)
                .border(0.5.dp, color = ColorCustomResources.colorBazaMainBlue)
                .focusRequester(focusRequester = focusRequester)
                .clip(shape = RoundedCornerShape(10.dp)),
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                cursorColor = ColorCustomResources.colorBazaMainBlue,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = errorTextColor.value,
                unfocusedTextColor = errorTextColor.value,
                unfocusedIndicatorColor = errorLineColorClick,
                focusedIndicatorColor = errorLineColorClick
            ),
            placeholder = {
                Text(text = "Номер телефона", color = Color.LightGray)
            },
            value = inputTextPhoneNumber,
            onValueChange = { currentText ->

                if (isFirstCharDigit(currentText)) {
                    onShowWarning(false)
                    errorTextColor.value = Color.Black
                    onErrorLineColor(ColorCustomResources.colorBazaMainBlue)
                    //  errorLineColor.value = ColorCustomResources.colorBazaMainBlue
                    if (currentText.length > 1) {
                        val inputText = currentText.take(inputMaxLength).filter { it.isDigit() }
                        onInputTextPhoneNumber(inputText)
                    }
                    if (currentText.length == 1) {
                        onInputTextPhoneNumber("9")
                    }
                } else {
                    if (currentText.isNotEmpty()) {
                        onShowWarning(true)
                        errorTextColor.value = Color.Red
                        onErrorLineColor(Color.Red)
//                    errorLineColor.value = Color.Red
                        val inputText = currentText.substring(0, 1)
                        onInputTextPhoneNumber(inputText)
                    } else {
                        onShowWarning(false)
                        onInputTextPhoneNumber("")
                        errorTextColor.value = Color.Black
                        onErrorLineColor(ColorCustomResources.colorBazaMainBlue)
                        //  errorLineColor.value = ColorCustomResources.colorBazaMainBlue
                        // inputText = ""
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            visualTransformation = ManualVisualTransformation(enableCursorMove = true)
        )
    }
}

fun isFirstCharDigit(text: String): Boolean {
    return (text.isNotEmpty() && text.first() == '7' && text.first().isDigit()
            || text.isNotEmpty() && text.first() == '8' && text.first().isDigit()
            || text.isNotEmpty() && text.first() == '9' && text.first().isDigit())
}

fun Modifier.hideKeyboardOnOutsideClick(): Modifier = composed {
    val controller = LocalSoftwareKeyboardController.current
    this then Modifier.noRippleClickable {
        controller?.hide()
    }
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this then Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )
}