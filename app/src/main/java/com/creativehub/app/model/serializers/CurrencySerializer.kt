package com.creativehub.app.model.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object CurrencySerializer : KSerializer<Currency> {
	override val descriptor: SerialDescriptor =
		PrimitiveSerialDescriptor("Currency", PrimitiveKind.STRING)

	override fun serialize(encoder: Encoder, value: Currency) {
		encoder.encodeString(value.toString())
	}

	override fun deserialize(decoder: Decoder): Currency {
		val string = decoder.decodeString()
		return Currency.getInstance(string)
	}
}