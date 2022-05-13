package com.creativehub.app.api

import com.creativehub.app.model.Publication
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun APIClient.getPublicFeed(limit: Int?) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/feed") {
		contentType(ContentType.Application.Json)
		limit?.let { parameter("limit", it) }
	}.body<List<Publication>>()
}

suspend fun APIClient.getUserFeed(uuid: String, limit: Int?) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/feed/$uuid") {
		contentType(ContentType.Application.Json)
		limit?.let { parameter("limit", it) }
	}.body<List<Publication>>()
}