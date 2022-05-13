package com.creativehub.app.model.serializers

import com.creativehub.app.model.Artwork
import com.creativehub.app.model.Event
import com.creativehub.app.model.Post
import com.creativehub.app.model.Publication
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object PublicationDeserializer : JsonContentPolymorphicSerializer<Publication>(Publication::class) {
	override fun selectDeserializer(element: JsonElement) = when {
		"creationDateTime" in element.jsonObject -> Artwork.serializer()
		"startDateTime" in element.jsonObject -> Event.serializer()
		else -> Post.serializer()
	}
}