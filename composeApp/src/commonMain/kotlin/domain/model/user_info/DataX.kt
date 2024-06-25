package domain.model.user_info

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


object AnyNullableSerializer : KSerializer<Any?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Any?", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Any?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeString(value.toString())
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Any? {
        return if (decoder.decodeNotNullMark()) {
            decoder.decodeString()
        } else {
            decoder.decodeNull()
        }
    }
}


@Serializable
data class DataX(
    val continuousPayment: ContinuousPayment? = null,
    @Serializable(with = AnyNullableSerializer::class)
    val opDate: Any? = null,
    val statusInt: Int?,
    val statusStr: String?,
    val tarif: Tarif?,
    val uid: Int,
    val oper: String
)