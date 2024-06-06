package domain.model.domofon

data class SputnikSelected(
    val title: String,
    val addrId: Int,
    val deviceID: String,
    val fullControl: Boolean,
    val flat: Int,
    val oper: String,
    val porch: Int,
    val previewUrl: String,
    val videoUrl: String,
    val isSelected: Boolean
)
