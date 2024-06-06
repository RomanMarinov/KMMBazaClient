package presentation.ui.attach_photo.model

data class Photo(
    val isAttach: Boolean,
    val imageByteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Photo

        return imageByteArray.contentEquals(other.imageByteArray)
    }

    override fun hashCode(): Int {
        return imageByteArray.contentHashCode()
    }
}
