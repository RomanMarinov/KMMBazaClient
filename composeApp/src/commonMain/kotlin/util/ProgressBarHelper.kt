package util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

object ProgressBarHelper {
    @Composable
    fun Start(trackColor: Color, mainColor: Color) {
        Row(
            modifier = Modifier
                .fillMaxSize() // Fill the whole screen
                .background(Color.Transparent),
               // .align(Alignment.Center), // Center the Row
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .background(Color.Transparent)
                    .size(55.dp),
                color = mainColor,
                strokeWidth = 4.dp,
                trackColor = trackColor,
                strokeCap = StrokeCap.Square
            )
        }
    }
}