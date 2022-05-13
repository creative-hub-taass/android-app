package com.creativehub.app.model

import kotlinx.datetime.Instant
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
	val startDateTime: Instant,
	val endDateTime: Instant,
	val bookingURL: String?,
) : Publication() {
	@Serializable
	data class Coordinates(
		val latitude: Double,
		val longitude: Double,
	)
}