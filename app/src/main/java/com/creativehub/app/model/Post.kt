package com.creativehub.app.model

import com.creativehub.app.BuildConfig
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Post(
	override val id: String,
	override val timestamp: Instant,
	override val lastUpdate: Instant,
	override val creations: List<PostCreation>,
	val title: String,
	val body: String,
) : Publication() {
	override val url: String get() = "${BuildConfig.CLIENT_URL}/post/$id"
}
