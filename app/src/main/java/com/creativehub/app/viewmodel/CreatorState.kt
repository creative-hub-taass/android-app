package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import com.creativehub.app.api.*
import com.creativehub.app.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest

class CreatorState(id: String, user: User?) : RememberObserver {
	private var rememberScope: CoroutineScope? = null
	var isLoading by mutableStateOf(false)
	var id by mutableStateOf(id)
	var user by mutableStateOf(user)
	var creator by mutableStateOf<PublicUser?>(null)
	var isFollowed by mutableStateOf<Boolean?>(null)
	var isFollower by mutableStateOf<Boolean?>(null)
	var posts by mutableStateOf<List<Post>?>(null)
	var artworks by mutableStateOf<List<Artwork>?>(null)
	var events by mutableStateOf<List<Event>?>(null)

	private fun clear() {
		rememberScope?.cancel()
		rememberScope = null
		id = "null"
		user = null
		creator = null
		isFollowed = null
		isFollower = null
		posts = null
		artworks = null
		events = null
	}

	fun refresh() {
		onForgotten()
		onRemembered()
	}

	override fun onAbandoned() {
		clear()
	}

	override fun onForgotten() {
		clear()
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override fun onRemembered() {
		if (rememberScope != null) return
		// Create a new scope to observe state and execute requests while we're remembered.
		val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
		rememberScope = scope
		// Observe the current request and execute any emissions.
		scope.launch {
			snapshotFlow { Pair(id, user) }
				.mapLatest {
					isLoading = true
					fetchCreator(it.first, it.second)
					isLoading = false
				}
				.collect()
		}
	}

	private suspend fun fetchCreator(id: String, user: User?) {
		creator = APIClient.getCreator(id).getOrNull()
		if (creator != null) {
			if (user != null) {
				isFollowed = user.inspirerIds.contains(id)
				isFollower = user.fanIds.contains(id)
			}
			posts = APIClient.getPostsByCreator(id).getOrNull()
			artworks = APIClient.getArtworksByCreator(id).getOrNull()
			events = APIClient.getEventsByCreator(id).getOrNull()
		}
	}
}

@Composable
fun rememberCreatorState(id: String, user: User?): CreatorState {
	val state = remember(id, user) { CreatorState(id, user) }
	state.onRemembered()
	return state
}