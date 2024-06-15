package domain.model.auth.firebase

import data.auth.remote.dto.firebase.FirebaseRequestBodyDTO
import data.auth.remote.dto.firebase.FirebaseRequestBodyDTOTEST

data class FirebaseRequestBodyTEST(
    val firebaseToken: String?,
    val fingerprint: String,
    val device: String,
    val version: Int,
    val title: String,
    val message: String
) {
    fun mapToData() : FirebaseRequestBodyDTOTEST {
        return FirebaseRequestBodyDTOTEST(
            firebaseToken = firebaseToken,
            fingerprint = fingerprint,
            device = device,
            version = version,
            title = title,
            message = message
        )
    }
}
