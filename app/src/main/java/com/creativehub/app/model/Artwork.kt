package com.creativehub.app.model

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable
data class Artwork(
	override val id: String,
	override val timestamp: Instant,
	override val lastUpdate: Instant,
	override val creations: List<ArtworkCreation>,
	val creationDateTime: Instant,
	val name: String,
	val description: String,
	val type: String,
	val copies: Int,
	val attributes: Map<String, String>,
	val images: Set<String>,
	val onSale: Boolean,
	val price: Double?,
	@Serializable(with = CurrencySerializer::class)
	val currency: Currency?,
	val paymentEmail: String?,
	val availableCopies: Int,
) : Publication()


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