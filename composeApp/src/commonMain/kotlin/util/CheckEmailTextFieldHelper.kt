package util

object CheckEmailTextFieldHelper {
    fun check(
        textEmailState: String,
    ): Boolean {
        val isEmailValid = EmailValidator.isValid(textEmailState)
        val emailTypes = listOf("@gmail.com", "@mail.ru", "@yandex.ru", "@bk.ru")

        return if (isEmailValid) {
            emailTypes.any { emailType ->
                textEmailState.contains(emailType)
            }
        } else {
            false
        }
    }
}

object EmailValidator {
    private val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    )

    fun isValid(email: String): Boolean {
        return emailRegex.matches(email)
    }
}