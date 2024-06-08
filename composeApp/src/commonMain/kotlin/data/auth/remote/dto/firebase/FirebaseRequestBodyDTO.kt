package data.auth.remote.dto.firebase

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseRequestBodyDTO(
    val firebaseToken: String?,
    val fingerprint: String,
    val device: String,
    val version: Int
)
