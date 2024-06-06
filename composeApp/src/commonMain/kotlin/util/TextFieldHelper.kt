package util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun TextFieldHelper(
   value: String = "",
   onValueChanged: (String) -> Unit,
   hintText: String
) {
    var isFocusedEmail by remember { mutableStateOf(false) }
    var valueEmail by remember { mutableStateOf("") }

    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChanged(newValue)
            valueEmail = newValue
        },
       // textStyle = textStyle,
        //maxLines = maxLines,
        decorationBox = { innerTextField ->  // для расширения в высоту строк подсказки
            Card(
                border = BorderStroke(2.dp,Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorCustomResources.colorBackgroundClose)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier,
                            text = if (isFocusedEmail) "" else hintText,
                          //  style = textStyle,
                            color = Color.DarkGray,
                        )
                    innerTextField()
                }
            }
        },
        modifier = Modifier.onFocusChanged { focusState ->
            isFocusedEmail = focusState.isFocused
        }
    )
}

