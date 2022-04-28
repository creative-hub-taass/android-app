package com.creativehub.app.model

import java.net.URL
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

data class Event(
	override val id: UUID,
	override val timestamp: Instant,
	override val lastUpdate: Instant,
	override val creations: List<EventCreation>,
	val name: String,
	val description: String,
	val image: String,
	val locationName: String,
	val coordinates: Coordinates,
	val startDateTime: OffsetDateTime,
	val endDateTime: OffsetDateTime,
	val bookingURL: URL,
) : Publication<EventCreation>(id, timestamp, lastUpdate, creations) {
	data class Coordinates(
		val latitude: Double,
		val longitude: Double,
	)
}