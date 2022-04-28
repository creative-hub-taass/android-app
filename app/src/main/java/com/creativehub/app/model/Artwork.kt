package com.creativehub.app.model

import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

data class Artwork(
	override val id: UUID,
	override val timestamp: Instant,
	override val lastUpdate: Instant,
	override val creations: List<ArtworkCreation>,
	val creationDateTime: OffsetDateTime,
	val name: String,
	val description: String,
	val type: String,
	val copies: Int,
	val attributes: Map<String, String>,
	val images: Set<String>,
	val onSale: Boolean,
	val price: Double,
	val currency: Currency,
	val paymentEmail: String,
	val availableCopies: Int,
) : Publication<ArtworkCreation>(id, timestamp, lastUpdate, creations)


