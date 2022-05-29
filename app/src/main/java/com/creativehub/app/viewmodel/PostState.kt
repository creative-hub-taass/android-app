package com.creativehub.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getPost
import com.creativehub.app.model.Post
import com.creativehub.app.model.User

class PostState(id: String, user: User?) : PublicationState<Post>(id, user) {
	override suspend fun fetchPublication(id: String, user: User?) {
		publication = APIClient.getPost(id).getOrNull()
		getPublicationInfo(id, user)
	}
}

@Composable
fun rememberPostState(id: String, user: User?): PostState {
	val info = remember(id, user) { PostState(id, user) }
	info.onRemembered()
	return info
}