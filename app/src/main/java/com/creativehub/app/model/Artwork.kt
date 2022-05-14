package com.creativehub.app.model

import com.creativehub.app.BuildConfig
import com.creativehub.app.model.serializers.CurrencySerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
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
	val images: List<String>,
	val onSale: Boolean,
	val price: Double?,
	@Serializable(with = CurrencySerializer::class)
	val currency: Currency?,
	val paymentEmail: String?,
	val availableCopies: Int,
) : Publication() {
	override val url: String get() = "${BuildConfig.CLIENT_URL}/artwork/$id"
}
