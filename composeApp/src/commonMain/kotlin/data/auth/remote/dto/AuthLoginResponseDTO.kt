package data.auth.remote.dto

import domain.model.auth.AuthLoginResponse
import domain.model.auth.PayloadLogin
import kotlinx.serialization.Serializable

@Serializable
data class AuthLoginResponseDTO(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val payload: PayloadLoginDTO? = null
) {
    fun mapToDomain() : AuthLoginResponse {
        return AuthLoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            payload = payload?.mapToDomain()
        )
    }
}

@Serializable
data class PayloadLoginDTO(
    val phone: Long? = null
) {
    fun mapToDomain() : PayloadLogin {
        return PayloadLogin(
            phone = phone
        )
    }
}