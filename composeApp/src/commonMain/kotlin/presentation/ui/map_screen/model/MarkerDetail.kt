package presentation.ui.map_screen.model

data class MarkerDetail(
    val cameraName: String? = "",
    val server: String? = "",
    val token: String? = "",
    val titleType: String? = "",
    val titleAddress: String? = "",
    val previewUrl: String? = "",
    val videoUrl: String? = "",
    val worktime: String? = "",
    val visible: String? = "",
    val dialer: String? = "",
    val phone: String? = "",
    val dtpCounts: Int = 0,
    val albumId: Int = 0,
    val isFavorite: Boolean = false,
    val latitude: String = "",
    val longitude: String = ""
)