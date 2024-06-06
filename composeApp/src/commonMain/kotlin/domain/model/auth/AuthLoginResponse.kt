package domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val payload: PayloadLogin? = null
)

@Serializable
data class PayloadLogin(
    val phone: Long? = null
)

