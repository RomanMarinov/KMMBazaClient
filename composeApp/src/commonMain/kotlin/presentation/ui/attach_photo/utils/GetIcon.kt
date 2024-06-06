package presentation.ui.attach_photo.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import domain.add_address.Data
import kmm.composeapp.generated.resources.Res
import kmm.composeapp.generated.resources.ic_address_service_false
import kmm.composeapp.generated.resources.ic_address_service_true
import org.jetbrains.compose.resources.vectorResource

data class IconService(
    val iconInternetTv: ImageVector,
    val iconVision: ImageVector,
    val iconDomofon: ImageVector,
)

@Composable
fun GetIconService(dataAddress: Data?): IconService {
    val iconInternetTv: ImageVector = if (dataAddress?.inet == true && dataAddress.ktv) {
        vectorResource(Res.drawable.ic_address_service_true)
    } else {
        vectorResource(Res.drawable.ic_address_service_false)
    }

    val iconVision: ImageVector = if (dataAddress?.dvr == true) {
        vectorResource(Res.drawable.ic_address_service_true)
    } else {
        vectorResource(Res.drawable.ic_address_service_false)
    }

    val iconDomofon: ImageVector = if (dataAddress?.domofon == true) {
        vectorResource(Res.drawable.ic_address_service_true)
    } else {
        vectorResource(Res.drawable.ic_address_service_false)
    }

    return IconService(
        iconInternetTv = iconInternetTv,
        iconVision = iconVision,
        iconDomofon = iconDomofon
    )
}