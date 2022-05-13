package com.creativehub.app.model

import com.creativehub.app.model.serializers.PublicationDeserializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable(with = PublicationDeserializer::class)
sealed class Publication {
	abstract val id: String
	abstract val timestamp: Instant
	abstract val lastUpdate: Instant
	abstract val creations: List<Creation>
}
