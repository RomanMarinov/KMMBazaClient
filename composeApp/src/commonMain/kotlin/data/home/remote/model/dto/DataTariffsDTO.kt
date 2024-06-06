package data.home.remote.model.dto

import domain.model.home.tariffs_by_location.DataTariffs
import kotlinx.serialization.Serializable

@Serializable
data class DataTariffsDTO(
   // @SerialName("bitrate")
    val bitrate: Int? = null,
   // @SerialName("channels")
    val channels: Int? = null,
    //@SerialName("duration_month")
    val durationMonth: Int? = null,
    //@SerialName("name")
    val name: String,
    //@SerialName("package_price")
    val packagePrice: Int? = null,
    //@SerialName("prepay_price")
    val prepayPrice: Int? = null,
    //@SerialName("price")
    val price: Int,
    //@SerialName("sale")
    val sale: Int? = null,
    //@SerialName("type")
    val type: String
) {
    fun mapToDomain() : DataTariffs {
        return DataTariffs(
            bitrate = bitrate,
            channels = channels,
            durationMonth = durationMonth,
            name = name,
            packagePrice = packagePrice,
            prepayPrice = prepayPrice,
            price = price,
            sale = sale,
            type = type
        )
    }
}
