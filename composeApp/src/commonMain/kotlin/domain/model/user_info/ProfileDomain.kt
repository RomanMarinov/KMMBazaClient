package domain.model.user_info

import kotlinx.serialization.Serializable

// !!! назван не Profile, т.к. компилятор распознает его как android.provider.ContactsContract.Profile

@Serializable
data class ProfileDomain(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val needAssociatePhoneToServices: Boolean = false,
    val phone: Long = 0L
)