package data.auth.remote.dto

import domain.model.auth.FingerprintBody
import kotlinx.serialization.Serializable

@Serializable
data class FingerprintBodyDTO(
    val fingerprint: String
) {
    fun mapToDomain() : FingerprintBody {
        return FingerprintBody(
            fingerprint = fingerprint
        )
    }
}
