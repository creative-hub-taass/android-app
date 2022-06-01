package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.creativehub.app.api.*
import com.creativehub.app.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

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

	object Saver : androidx.compose.runtime.saveable.Saver<CreatorState, String> {
		override fun restore(value: String): CreatorState? {
			val jsonObject = Json.decodeFromString<JsonObject>(value)
			val idJson = jsonObject["id"] ?: return null
			val userJson = jsonObject["user"] ?: return null
			val isLoadingJson = jsonObject["isLoading"] ?: return null
			val creatorJson = jsonObject["creator"] ?: return null
			val isFollowedJson = jsonObject["isFollowed"] ?: return null
			val isFollowerJson = jsonObject["isFollower"] ?: return null
			val postsJson = jsonObject["posts"] ?: return null
			val artworksJson = jsonObject["artworks"] ?: return null
			val eventsJson = jsonObject["events"] ?: return null
			val id = Json.decodeFromJsonElement<String>(idJson)
			val user = Json.decodeFromJsonElement<User?>(userJson)
			val isLoading = Json.decodeFromJsonElement<Boolean>(isLoadingJson)
			val creator = Json.decodeFromJsonElement<PublicUser?>(creatorJson)
			val isFollowed = Json.decodeFromJsonElement<Boolean?>(isFollowedJson)
			val isFollower = Json.decodeFromJsonElement<Boolean?>(isFollowerJson)
			val posts = Json.decodeFromJsonElement<List<Post>>(postsJson)
			val artworks = Json.decodeFromJsonElement<List<Artwork>>(artworksJson)
			val events = Json.decodeFromJsonElement<List<Event>>(eventsJson)
			val creatorState = CreatorState(id, user)
			creatorState.isLoading = isLoading
			creatorState.creator = creator
			creatorState.isFollowed = isFollowed
			creatorState.isFollower = isFollower
			creatorState.posts = posts
			creatorState.artworks = artworks
			creatorState.events = events
			return creatorState
		}

		override fun SaverScope.save(value: CreatorState): String {
			return buildJsonObject {
				put("id", Json.encodeToJsonElement(value.id))
				put("user", Json.encodeToJsonElement(value.user))
				put("isLoading", Json.encodeToJsonElement(value.isLoading))
				put("creator", Json.encodeToJsonElement(value.creator))
				put("isFollowed", Json.encodeToJsonElement(value.isFollowed))
				put("isFollower", Json.encodeToJsonElement(value.isFollower))
				put("posts", Json.encodeToJsonElement(value.posts))
				put("artworks", Json.encodeToJsonElement(value.artworks))
				put("events", Json.encodeToJsonElement(value.events))
			}.toString()
		}
	}
}

@Composable
fun rememberCreatorState(id: String, user: User?): CreatorState {
	val state = rememberSaveable(id, user, saver = CreatorState.Saver) { CreatorState(id, user) }
	state.onRemembered()
	return state
}