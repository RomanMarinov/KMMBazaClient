package domain.model.auth

data class AuthLoginBody(
    val phone: Long,
    val fingerprint: String
)
