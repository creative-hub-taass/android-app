package com.creativehub.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.creativehub.app.api.*
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.User

class ArtworkState(id: String, user: User?) : PublicationState<Artwork>(id, user) {
	override suspend fun fetchPublication(id: String, user: User?) {
		val result = APIClient.getArtwork(id)
		publication = result.getOrNull()
		if (publication != null) {
			this.creatorsInfo = APIClient.getCreators(publication!!.creations).getOrNull()
			this.likes = APIClient.getLikes(id).getOrNull()
			if (user != null) {
				this.userLiked = APIClient.getUserLikedPublication(id, user.id).getOrDefault(false)
			}
			this.comments = APIClient.getComments(id).getOrNull()
			commentInfos = fetchUsersOfComments()
		}
	}
}

@Composable
fun rememberArtworkState(id: String, user: User?): ArtworkState {
	val info = remember(id, user) { ArtworkState(id, user) }
	info.onRemembered()
	return info
}