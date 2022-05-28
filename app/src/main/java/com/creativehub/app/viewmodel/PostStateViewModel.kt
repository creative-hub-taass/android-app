package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import com.creativehub.app.api.*
import com.creativehub.app.model.*
import com.creativehub.app.util.fetchUsersOfComments
import io.ktor.client.plugins.*
import io.ktor.client.statement.*



class PostStateViewModel : BusyViewModel() {
	var post by mutableStateOf<Post?>(null)
	var listUser = mutableStateListOf<Pair<PublicUser, CreationType>>()
	var listComments = mutableStateListOf<Comment>()
	var countLikes = mutableStateOf<Int?>(null)
	var listCommentsUser = mutableStateListOf<CommentInfo>()

	suspend fun fetchPost(postId: String): String? = runBusy {
		clear()
		val result = APIClient.getPost(postId)
		val comments = APIClient.getComments(postId).getOrNull()
		val likes = APIClient.getLikes(postId).getOrNull()

		post = result.getOrNull()
		if (post != null) {
			val list = (APIClient.getCreators(post!!.creations)).getOrNull()
			if(list != null)
				listUser.addAll(list)
			if(comments != null && likes != null) {
				listComments.addAll(comments)
				countLikes = mutableStateOf(likes)
			}
			fetchUsersOfComments(listComments)
		}

		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	private fun clear() {
		post = null
		listUser.clear()
		listComments.clear()
		listCommentsUser.clear()
		countLikes = mutableStateOf(null)
	}
}

val LocalPostState = compositionLocalOf<PostStateViewModel> { error("Post State Not Found!") }
