package domain.model.home.tariffs_by_location

import kotlinx.serialization.Serializable

@Serializable
data class DataTariffs(
    //@SerialName("bitrate")
    val bitrate: Int? = null,
    //@SerialName("channels")
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
)
