package presentation.ui.profile_screen.address_item.utils

import domain.model.user_info.AdditionalAddresses

object VerifyStatus {
    fun getStatus(additionalAddresses: AdditionalAddresses) : String {
        return when (additionalAddresses.verificationStatus) {
            "NOT_VERIFIED" -> {
                "Прикрепите фото документа"
            }
            "VERIFIED" -> {
                "Подтвержден"
            }
            "WAITING" -> {
                "На рассмотрении"
            }
            "DENY" -> {
                "Отказано"
            }

            else -> { "" }
        }
    }
}