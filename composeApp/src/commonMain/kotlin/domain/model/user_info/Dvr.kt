package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Dvr(
    val addrId: Int,
    val address: String,
    val coordinates: String,
    val name: String,
    val oper: String,
    val previewUrl: String,
    val server: String,
    val storageTime: Int,
    val title: String,
    val token: String,
    val videoUrl: String
)