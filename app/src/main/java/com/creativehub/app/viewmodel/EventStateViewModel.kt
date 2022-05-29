package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import com.creativehub.app.api.*
import com.creativehub.app.model.*
import com.creativehub.app.util.fetchUsersOfComments
import io.ktor.client.plugins.*
import io.ktor.client.statement.*

class EventStateViewModel : BusyViewModel() {
	var event by mutableStateOf<Event?>(null)
	var listUser = mutableStateListOf<Pair<PublicUser, CreationType>>()
	var listComments = mutableStateListOf<Comment>()
	var countLikes = mutableStateOf<Int?>(null)
	var listCommentUser = mutableStateListOf<CommentInfo>()

	suspend fun fetchEvent(eventId: String): String? = runBusy {
		clear()
		val result = APIClient.getEvent(eventId)
		val comments = APIClient.getComments(eventId).getOrNull()
		val likes = APIClient.getLikes(eventId).getOrNull()
		event = result.getOrNull()
		if (event != null) {
			val list = (APIClient.getCreators(event!!.creations)).getOrNull()
			if (list != null) {
				listUser.addAll(list)
			}
			if (comments != null && likes != null) {
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
		event = null
		listUser.clear()
		listComments.clear()
		listCommentUser.clear()
		countLikes = mutableStateOf(null)
	}
}
val LocalEventState = compositionLocalOf<EventStateViewModel> { error("Artwork State Not Found!") }
