package com.creativehub.app.api

import com.creativehub.app.model.Publication
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun APIClient.getPublicFeed(limit: Int?) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/-/feed") {
		limit?.let { parameter("limit", it) }
	}.body<List<Publication>>()
}

suspend fun APIClient.getUserFeed(uuid: String, limit: Int?) = runCatching {
	APIClient().get("$PUBLICATIONS_BASE_URL/feed/$uuid") {
		limit?.let { parameter("limit", it) }
	}.body<List<Publication>>()
}
