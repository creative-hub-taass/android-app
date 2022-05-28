package com.creativehub.app.model

import com.creativehub.app.BuildConfig
import com.google.android.gms.maps.model.LatLng
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
	override val url: String get() = "${BuildConfig.CLIENT_URL}/event/$id"

	@Serializable
	data class Coordinates(
		val latitude: Double,
		val longitude: Double,
	) {
		fun toLatLng() = LatLng(latitude, longitude)
	}
}
