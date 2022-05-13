package com.creativehub.app.api

import com.creativehub.app.model.Comment
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun APIClient.getLikes(publicationId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/-/likes/count/$publicationId").body<Int>()
}

suspend fun APIClient.getComments(publicationId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/-/comments/$publicationId").body<List<Comment>>()
}

suspend fun APIClient.getCommentsCount(publicationId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/-/comments/count/$publicationId").body<Int>()
}
