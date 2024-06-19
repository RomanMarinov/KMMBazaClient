package domain.model.auth.firebase

import data.auth.remote.dto.firebase.FirebaseRequestBodyDTO
import data.auth.remote.dto.firebase.FirebaseRequestBodyDTOTEST
import kmm.composeapp.generated.resources.Res

data class FirebaseRequestBodyTEST(
    val firebaseToken: String?,
    val fingerprint: String,
    val device: String,
    val version: Int,
    val title: String,
    val message: String,

val type: String,
val address: String,
val imageUrl: String,
val uuid: String,
val videoUrl: String
) {
    fun mapToData() : FirebaseRequestBodyDTOTEST {
        return FirebaseRequestBodyDTOTEST(
            firebaseToken = firebaseToken,
            fingerprint = fingerprint,
            device = device,
            version = version,
            title = title,
            message = message,

            type = type,
            address = address,
            imageUrl = imageUrl,
            uuid = uuid,
            videoUrl = videoUrl
        )
    }
}
