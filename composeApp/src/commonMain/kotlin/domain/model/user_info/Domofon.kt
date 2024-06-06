package domain.model.user_info

import kotlinx.serialization.Serializable

@Serializable
data class Domofon(
    val sputnik: List<Sputnik>?,
    val ufanet: List<Ufanet>?
)