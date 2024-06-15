package data.auth.remote.dto.firebase

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseRequestBodyDTOTEST(
    val firebaseToken: String?,
    val fingerprint: String,
    val device: String,
    val version: Int,
    val title: String,
    val message: String
)
