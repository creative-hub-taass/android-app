package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
		}
		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}



	fun clear() {
		artwork = null
		listUser.clear()
		listImages.clear()
		listComments.clear()
		countLikes = mutableStateOf<Int?>(null)
	}
}

val LocalArtworkState = compositionLocalOf<ArtworkStateViewModel> { error("Artwork State Not Found!") }
