package data.auth.remote.dto

import domain.model.auth.AuthLogoutResponse

data class AuthLogoutResponseDTO(
    val result: Boolean
) {
    fun mapToDomain() : AuthLogoutResponse {
        return AuthLogoutResponse(
            result = result
        )
    }
}
