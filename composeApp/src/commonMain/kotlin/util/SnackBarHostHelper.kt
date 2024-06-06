package util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

object SnackBarHostHelper {

    @Composable
    fun WithOkButton(
        message: String
        ) {
        var isSnackBarVisible by remember { mutableStateOf(true) }
        if (isSnackBarVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    //.fillMaxWidth()
                    .padding(bottom = 22.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    actionOnNewLine = true,
                    action = {
                        TextButton(onClick = {
                            isSnackBarVisible = false
                        }) {
                            Text(
                                text = "OK",
                                color = Color.White
                            )
                        }
                    }
                ) {
                    Text(text = message)
                }
            }
        }
    }

    @Composable
    fun ShortShortTime(
        message: String,
        onFinishTime: () -> Unit
    ) {
        var isSnackBarVisible by remember { mutableStateOf(true) }
        if (isSnackBarVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Snackbar(
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                    actionOnNewLine = true
                ) {
                    Text(text = message)
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(5000)
            isSnackBarVisible = false
            onFinishTime()
        }
    }
}