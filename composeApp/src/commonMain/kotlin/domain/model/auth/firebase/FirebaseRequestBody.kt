package domain.model.auth.firebase

import data.auth.remote.dto.firebase.FirebaseRequestBodyDTO

data class FirebaseRequestBody(
    val firebaseToken: String?,
    val fingerprint: String,
    val device: String,
    val version: Int
) {
    fun mapToData() : FirebaseRequestBodyDTO {
        return FirebaseRequestBodyDTO(
            firebaseToken = firebaseToken,
            fingerprint = fingerprint,
            device = device,
            version = version
        )
    }
}
