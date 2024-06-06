package presentation.ui.domofon_screen.model

import domain.model.user_info.Sputnik

data class DomofonGroup(
    val listSputnikControl: List<Sputnik>,
    val listSputnik: List<List<Sputnik>>
)
