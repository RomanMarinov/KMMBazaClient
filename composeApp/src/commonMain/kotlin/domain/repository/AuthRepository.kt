package domain.repository

import domain.model.auth.AuthLoginBody
import domain.model.auth.FingerprintBody
import domain.model.auth.firebase.FirebaseRequestBody
import io.ktor.client.statement.HttpResponse

interface AuthRepository {

    // auth
    fun resetIntervalCounters(resetCounters: Boolean)

    suspend fun logIn(authLoginBody: AuthLoginBody): HttpResponse?
    suspend fun logOut(fingerprintBody: FingerprintBody): Boolean

    //  fun refreshTokenSync(body: AuthRefreshBody): Response<AuthLoginResponse>?
    suspend fun ipAuthorization(fingerprintBody: FingerprintBody): HttpResponse?

    suspend fun getAccessTokenFromPrefs(): String
    suspend fun refreshTokenSync(): HttpResponse?

    //    suspend fun saveFireBaseToken()
    suspend fun sendRegisterFireBaseData(firebaseRequestBody: FirebaseRequestBody)
    suspend fun setFingerPrint(fingerPrint: String)

}