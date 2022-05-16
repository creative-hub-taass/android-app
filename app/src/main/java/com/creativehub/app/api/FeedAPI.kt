package com.creativehub.app.api

import com.creativehub.app.api.util.parsePagingLinks
import com.creativehub.app.model.Publication
import com.creativehub.app.model.PublicationInfo
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun APIClient.getFeed(userId: String?, page: Int, size: Int) = runCatching {
	val urlString = when (userId) {
		null -> "$PUBLICATIONS_BASE_URL/-/feed/paginated"
		else -> "$PUBLICATIONS_BASE_URL/feed/paginated/$userId"
	}
	val response = APIClient().get(urlString) {
		parameter("page", page)
		parameter("size", size)
	}
	val feed = response.body<List<PublicationInfo<Publication>>>()
	val links = response.headers.getAll("Link").orEmpty().parsePagingLinks()
	Pair(feed, links)
}

