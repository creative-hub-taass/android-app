package com.creativehub.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getArtwork
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.User

class ArtworkState(id: String, user: User?) : PublicationState<Artwork>(id, user) {
	override suspend fun fetchPublication(id: String, user: User?) {
		publication = APIClient.getArtwork(id).getOrNull()
		getPublicationInfo(id, user)
	}
}

@Composable
fun rememberArtworkState(id: String, user: User?): ArtworkState {
	val info = remember(id, user) { ArtworkState(id, user) }
	info.onRemembered()
	return info
}