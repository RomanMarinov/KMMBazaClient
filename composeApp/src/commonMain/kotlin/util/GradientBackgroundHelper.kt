package util

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color


data class ColorsGradient(
    val color1: Color,
    val color2: Color,
    val color3: Color,
)

object GradientBackgroundHelper {
//    @Composable
//    fun SetGradientBackground() : ColorsGradient {
//        val colorAnimation = rememberInfiniteTransition()
//        val color1 by colorAnimation.animateColor(
//            initialValue = ColorCustomResources.colorBazaMainRed,
//            targetValue = ColorCustomResources.colorBazaMainBlue,
//            animationSpec = infiniteRepeatable(
//                animation = tween(durationMillis = 5000),
//                repeatMode = RepeatMode.Reverse
//            )
//        )
//        val color2 by colorAnimation.animateColor(
//            initialValue = ColorCustomResources.colorBazaMainBlue,
//            targetValue = ColorCustomResources.colorBazaMainRed,
//            animationSpec = infiniteRepeatable(
//                animation = tween(durationMillis = 3000),
//                repeatMode = RepeatMode.Reverse
//            )
//        )
//        val color3 by colorAnimation.animateColor(
//            initialValue = ColorCustomResources.colorBazaMainBlue,
//            targetValue = ColorCustomResources.colorBazaMainRed,
//            animationSpec = infiniteRepeatable(
//                animation = tween(durationMillis = 2000),
//                repeatMode = RepeatMode.Reverse
//            )
//        )
//
//        return ColorsGradient(
//            color1 = color1,
//            color2 = color2,
//            color3 = color3
//        )
//    }

    @Composable
    fun SetGradientBackground() : ColorsGradient {
        val colorAnimation = rememberInfiniteTransition()
        val color1 by colorAnimation.animateColor(
            initialValue = ColorCustomResources.colorBazaMainRed,
            targetValue = ColorCustomResources.colorBazaMainBlue,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000),
                repeatMode = RepeatMode.Reverse
            )
        )
        val color2 by colorAnimation.animateColor(
            initialValue = ColorCustomResources.colorBazaMainBlue,
            targetValue = ColorCustomResources.colorBazaMainRed,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000),
                repeatMode = RepeatMode.Reverse
            )
        )
        val color3 by colorAnimation.animateColor(
            initialValue = ColorCustomResources.colorBazaMainBlue,
            targetValue = ColorCustomResources.colorBazaMainRed,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        return ColorsGradient(
            color1 = color1,
            color2 = color2,
            color3 = color3
        )
    }
}