package com.creativehub.app.model

import java.time.Instant
import java.util.*

data class Post(
	override val id: UUID,
	override val timestamp: Instant,
	override val lastUpdate: Instant,
	override val creations: List<PostCreation>,
	val title: String,
	val body: String,
) : Publication<PostCreation>(id, timestamp, lastUpdate, creations)

