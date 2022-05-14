package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import com.creativehub.app.api.*
import com.creativehub.app.model.Like
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.model.User

class FeedStateViewModel : BusyViewModel() {
	var feed = mutableStateListOf<PublicationInfo<*>>()

	suspend fun fetchFeed(user: User? = null) = runBusy {
		val limit = null
		feed.clear()
		if (user != null) {
			val userFeed = APIClient.getUserFeed(user.id, limit)
			userFeed.getOrDefault(listOf())
		} else {
			APIClient.getPublicFeed(limit).getOrDefault(listOf())
		}.map {
			PublicationInfo(it)
		}.forEach {
			feed.add(0, it)
		}
	}

	suspend fun fetchPublicationInfo(info: PublicationInfo<*>, userId: String?) {
		info.fetchCreators()
			.fetchUserLikedPublication(userId)
			.fetchLikes()
			.fetchCommentsCount()
	}

	suspend fun fetchFullPublicationInfo(info: PublicationInfo<*>, userId: String?) {
		info.fetchCreators()
			.fetchUserLikedPublication(userId)
			.fetchLikes()
			.fetchComments()
	}

	suspend fun togglePublicationLike(info: PublicationInfo<*>, userId: String) {
		info.toggleLike(userId)
			.fetchUserLikedPublication(userId)
			.fetchLikes()
	}

	private suspend fun PublicationInfo<*>.fetchUserLikedPublication(userId: String?): PublicationInfo<*> = update {
		PublicationInfo(it, userLiked = userId?.let { id ->
			APIClient.getUserLikedPublication(it.publication.id, id).getOrNull()
		})
	}

	private suspend fun PublicationInfo<*>.fetchCreators(): PublicationInfo<*> = update {
		PublicationInfo(it, creators = APIClient.getCreators(it.publication.creations).getOrNull())
	}

	private suspend fun PublicationInfo<*>.fetchLikes(): PublicationInfo<*> = update {
		PublicationInfo(it, likes = APIClient.getLikes(it.publication.id).getOrNull())
	}

	private suspend fun PublicationInfo<*>.fetchComments(): PublicationInfo<*> = update {
		val comments = APIClient.getComments(it.publication.id).getOrNull()
		PublicationInfo(it, comments = comments, commentsCount = comments?.size)
	}

	private suspend fun PublicationInfo<*>.fetchCommentsCount(): PublicationInfo<*> = update {
		PublicationInfo(it, likes = APIClient.getLikes(it.publication.id).getOrNull())
	}

	private suspend fun PublicationInfo<*>.toggleLike(userId: String): PublicationInfo<*> = update {
		val success = if (it.userLiked == true) {
			APIClient.removeLike(Like(it.publication.id, userId)).isSuccess
		} else {
			APIClient.setLike(Like(userId, it.publication.id)).isSuccess
		}
		if (success) PublicationInfo(it, userLiked = it.userLiked?.not(), likes = it.likes?.plus(1)) else it
	}

	private inline fun PublicationInfo<*>.update(
		transform: (old: PublicationInfo<*>) -> PublicationInfo<*>,
	): PublicationInfo<*> {
		val index = feed.indexOf(this)
		val new = transform(this)
		feed[index] = new
		return new
	}
}

val LocalFeedState = compositionLocalOf<FeedStateViewModel> { error("Feed State Not Found!") }
