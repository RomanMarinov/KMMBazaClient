package domain.model.history_call

data class HistoryCallAddress(
    val address: String,
    val answered: Boolean,
    val deviceID: String,
    val imageUrl: String,
    val time: String,
    val title: String,
    val streamerUrl: String,
    val token: String
)
