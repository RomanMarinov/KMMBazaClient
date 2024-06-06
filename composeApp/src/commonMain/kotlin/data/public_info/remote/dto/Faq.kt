package data.public_info.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Faq(
    val buttonAction: String = "",
    val buttonText: String = "",
    val content: String,
    val id: Int,
    val title: String
)