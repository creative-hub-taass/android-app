package com.creativehub.app.api

import com.creativehub.app.model.Comment
import com.creativehub.app.model.Like
import com.creativehub.app.model.PublicationInfo
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun APIClient.getLikes(publicationId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/-/likes/count/$publicationId").body<Int>()
}

suspend fun APIClient.toggleLike(info: PublicationInfo<*>, userId: String) {
	if (info.userLiked == true) {
		removeLike(Like(userId, info.publication.id)).isSuccess
	} else {
		setLike(Like(userId, info.publication.id)).isSuccess
	}
}

suspend fun APIClient.setLike(like: Like) = runCatching {
	APIClient().post("$INTERACTIONS_BASE_URL/like") {
		contentType(ContentType.Application.Json)
		setBody(like)
	}
}

suspend fun APIClient.removeLike(like: Like): Result<Any> = runCatching {
	APIClient().delete("$INTERACTIONS_BASE_URL/like") {
		contentType(ContentType.Application.Json)
		setBody(like)
	}
}

suspend fun APIClient.getUserLikedPublication(publicationId: String, userId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/userliked/$userId/$publicationId").body<Boolean>()
}

suspend fun APIClient.getComments(publicationId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/-/comments/$publicationId").body<List<Comment>>()
}

suspend fun APIClient.getCommentsCount(publicationId: String) = runCatching {
	APIClient().get("$INTERACTIONS_BASE_URL/-/comments/count/$publicationId").body<Int>()
}
