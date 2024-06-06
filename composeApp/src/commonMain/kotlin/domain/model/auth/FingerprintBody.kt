package domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class FingerprintBody(
    val fingerprint: String
)
