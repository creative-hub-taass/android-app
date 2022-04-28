package com.creativehub.app.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
	override val id: String,
	override val timestamp: Instant,
	override val lastUpdate: Instant,
	override val creations: List<EventCreation>,
	val name: String,
	val description: String,
	val image: String,
	val locationName: String,
	val coordinates: Coordinates,
	val startDateTime: LocalDateTime,
	val endDateTime: LocalDateTime,
	val bookingURL: String,
) : Publication<EventCreation> {
	@Serializable
	data class Coordinates(
		val latitude: Double,
		val longitude: Double,
	)
}