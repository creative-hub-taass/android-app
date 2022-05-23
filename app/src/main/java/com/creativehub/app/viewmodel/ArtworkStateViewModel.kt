package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getArtwork
import com.creativehub.app.api.getCreators
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.Creation
import com.creativehub.app.model.CreationType
import com.creativehub.app.model.PublicUser
import io.ktor.client.plugins.*
import io.ktor.client.statement.*


class ArtworkStateViewModel : BusyViewModel() {
	var artwork by mutableStateOf<Artwork?>(null)
	var listUser = mutableStateListOf<Pair<PublicUser, CreationType>>()
	var listImages = mutableStateListOf<String>()

	suspend fun fetchArtwork(artworkId: String): String? = runBusy {
		clear()
		val result = APIClient.getArtwork(artworkId)
		artwork = result.getOrNull()
		listUser.clear()
		listImages.clear()
		if (artwork != null) {
			val list = (APIClient.getCreators(artwork!!.creations)).getOrNull()
			if(list != null) {
				listUser.addAll(list)
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
	}
}

val LocalArtworkState = compositionLocalOf<ArtworkStateViewModel> { error("Artwork State Not Found!") }
