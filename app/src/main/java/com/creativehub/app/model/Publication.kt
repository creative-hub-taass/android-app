package com.creativehub.app.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = PublicationDeserializer::class)
sealed class Publication {
	abstract val id: String
	abstract val timestamp: Instant
	abstract val lastUpdate: Instant
	abstract val creations: List<Creation>
}

object PublicationDeserializer : JsonContentPolymorphicSerializer<Publication>(Publication::class) {
	override fun selectDeserializer(element: JsonElement) = when {
		"creationDateTime" in element.jsonObject -> Artwork.serializer()
		"startDateTime" in element.jsonObject -> Event.serializer()
		else -> Post.serializer()
	}
}