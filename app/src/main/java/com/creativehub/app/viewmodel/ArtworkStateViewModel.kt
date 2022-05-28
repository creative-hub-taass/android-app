package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import com.creativehub.app.api.*
import com.creativehub.app.model.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*


class ArtworkStateViewModel : BusyViewModel() {
	var artwork by mutableStateOf<Artwork?>(null)
	var listUser = mutableStateListOf<Pair<PublicUser, CreationType>>()
	var listImages = mutableStateListOf<String>()
	var listComments = mutableStateListOf<Comment>()
	var countLikes = mutableStateOf<Int?>(null)
	var listCommentsUser = mutableStateListOf<CommentInfo>()

	suspend fun fetchArtwork(artworkId: String): String? = runBusy {
		clear()
		val result = APIClient.getArtwork(artworkId)
		val comments = APIClient.getComments(artworkId).getOrNull()
		val likes = APIClient.getLikes(artworkId).getOrNull()

		artwork = result.getOrNull()
		if (artwork != null) {
			val list = (APIClient.getCreators(artwork!!.creations)).getOrNull()
			if(list != null) {
				listUser.addAll(list)
			}
			if(comments != null && likes != null) {
				listComments.addAll(comments)
				countLikes = mutableStateOf(likes)
			}
			listImages.addAll(artwork!!.images)
			fetchUserofComments()
		}

		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	private suspend fun fetchUserofComments(): String? = runBusy {
		val listId = mutableStateListOf<String>()
		listComments.forEach { comment ->
			if(!listId.contains(comment.userId))
				listId.add(comment.userId)
		}
		val result = APIClient.getListUsers(listId)
		if(result.getOrNull() != null){
			result.getOrNull()!!.forEach { publicUser ->
				listComments.forEach { comment ->
					if (publicUser.id.compareTo(comment.userId) == 0){
						listCommentsUser.add(CommentInfo(comment, publicUser))
					}
				}
			}
		}
		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	private fun clear() {
		artwork = null
		listUser.clear()
		listImages.clear()
		listComments.clear()
		listCommentsUser.clear()
		countLikes = mutableStateOf(null)
	}
}

val LocalArtworkState = compositionLocalOf<ArtworkStateViewModel> { error("Artwork State Not Found!") }
