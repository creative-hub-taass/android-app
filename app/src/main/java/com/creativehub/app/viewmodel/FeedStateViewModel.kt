package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import com.creativehub.app.api.*
import com.creativehub.app.model.EmptyPublicationInfo
import com.creativehub.app.model.FullPublicationInfo
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.model.User

class FeedStateViewModel : BusyViewModel() {
	var feed = mutableStateListOf<PublicationInfo<*>>()

	suspend fun fetchFeed(user: User? = null, limit: Int? = null) = runBusy {
		if (user != null) {
			APIClient.getUserFeed(user.id, limit).getOrDefault(listOf())
		} else {
			APIClient.getPublicFeed(limit).getOrDefault(listOf())
		}.map {
			EmptyPublicationInfo(it)
		}.forEach {
			feed.add(it)
		}
	}

	suspend fun fetchPublicationInfo(publicationInfo: PublicationInfo<*>, full: Boolean): PublicationInfo<*> {
		val result = APIClient.getCreators(publicationInfo.publication.creations)
		val creators = result.getOrNull()
		val likes = APIClient.getLikes(publicationInfo.publication.id).getOrNull()
		val comments = if (full) {
			APIClient.getComments(publicationInfo.publication.id).getOrNull()
		} else {
			emptyList()
		}
		val commentsCount = if (full) {
			comments?.size
		} else {
			APIClient.getCommentsCount(publicationInfo.publication.id).getOrNull()
		}
		return FullPublicationInfo(publicationInfo.publication, creators, likes, comments, commentsCount)
	}
}

val LocalFeedState = compositionLocalOf<FeedStateViewModel> { error("Feed State Not Found!") }
