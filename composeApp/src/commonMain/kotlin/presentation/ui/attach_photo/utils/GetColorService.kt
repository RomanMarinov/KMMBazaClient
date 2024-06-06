package presentation.ui.attach_photo.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import domain.add_address.Data
import util.ColorCustomResources


data class ColorService(
    val colorInternetTv: Color,
    val colorVision: Color,
    val colorDomofon: Color,
)

@Composable
fun GetColorService(dataAddress: Data?): ColorService {
    val colorInternetTv: Color = if (dataAddress?.inet == true && dataAddress.ktv) {
        ColorCustomResources.colorBazaMainBlue
    } else {
        Color.Red
    }

    val colorVision: Color = if (dataAddress?.dvr == true) {
        ColorCustomResources.colorBazaMainBlue
    } else {
        Color.Red
    }

    val colorDomofon: Color = if (dataAddress?.domofon == true) {
        ColorCustomResources.colorBazaMainBlue
    } else {
        Color.Red
    }

    return ColorService(
        colorInternetTv = colorInternetTv,
        colorVision = colorVision,
        colorDomofon = colorDomofon
    )
}


