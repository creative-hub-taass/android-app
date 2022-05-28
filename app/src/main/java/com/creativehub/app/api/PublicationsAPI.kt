package com.creativehub.app.api

import com.creativehub.app.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun APIClient.getCreators(creations: List<Creation>) = runCatching {
	APIClient().post("$USERS_BASE_URL/-/public") {
		contentType(ContentType.Application.Json)
		setBody(creations.map { it.user })
	}.body<List<PublicUser>>()
		.zip(creations)
		.filter { it.first.id == it.second.user }
		.map { Pair(it.first, it.second.creationType) }
}

suspend fun APIClient.getArtwork(artworkId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/artworks/$artworkId").body<Artwork>()
}

suspend fun APIClient.getEvent(eventId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/artworks/$eventId").body<Event>()
}

suspend fun APIClient.getPost(postId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/artworks/$postId").body<Post>()
}

suspend fun APIClient.getListUsers(listId: List<String>) = runCatching {
	APIClient().post("$USERS_BASE_URL/-/public") {
		contentType(ContentType.Application.Json)
		setBody(listId)
	}.body<List<PublicUser>>()
}