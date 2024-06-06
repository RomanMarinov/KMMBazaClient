package presentation.ui.domofon_screen.model

import domain.model.user_info.Domofon
import kotlinx.serialization.Serializable

@Serializable
data class SputnikUiState(
    val domofon: Domofon
)
