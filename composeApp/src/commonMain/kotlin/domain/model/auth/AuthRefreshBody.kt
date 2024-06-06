package domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRefreshBody(
    val refreshToken: String,
    val fingerprint: String
) {
    override fun toString(): String {
        return "AuthRefreshBody(refreshToken = $refreshToken, fingerprint = $fingerprint)"
    }
}