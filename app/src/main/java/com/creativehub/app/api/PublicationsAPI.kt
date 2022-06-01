package com.creativehub.app.api

import com.creativehub.app.model.Artwork
import com.creativehub.app.model.Event
import com.creativehub.app.model.Post
import com.creativehub.app.model.PublicUser
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun APIClient.getCreators(ids: List<String>) = runCatching {
	APIClient().post("$USERS_BASE_URL/-/public") {
		contentType(ContentType.Application.Json)
		setBody(ids)
	}.body<List<PublicUser>>()
}

suspend fun APIClient.getArtwork(artworkId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/artworks/$artworkId").body<Artwork>()
}

suspend fun APIClient.getEvent(eventId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/events/$eventId").body<Event>()
}

suspend fun APIClient.getPost(postId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/posts/$postId").body<Post>()
}

suspend fun APIClient.getListUsers(listId: List<String>) = runCatching {
	APIClient().post("$USERS_BASE_URL/-/public") {
		contentType(ContentType.Application.Json)
		setBody(listId)
	}.body<List<PublicUser>>()
}

suspend fun APIClient.getCreator(creatorId: String) = runCatching {
	APIClient().get("$USERS_BASE_URL/-/$creatorId").body<PublicUser>()
}

suspend fun APIClient.getArtworksByCreator(creatorId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/artworks/creator/$creatorId").body<List<Artwork>>()
}

suspend fun APIClient.getEventsByCreator(creatorId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/events/creator/$creatorId").body<List<Event>>()
}

suspend fun APIClient.getPostsByCreator(creatorId: String) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/posts/creator/$creatorId").body<List<Post>>()
}
