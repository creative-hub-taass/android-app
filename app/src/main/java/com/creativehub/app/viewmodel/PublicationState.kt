package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import com.creativehub.app.api.*
import com.creativehub.app.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

abstract class PublicationState<T : Publication>(val id: String, val user: User?) : RememberObserver {
	private var rememberScope: CoroutineScope? = null
	var isLoading by mutableStateOf(false)
	var publication by mutableStateOf<T?>(null)
	var creatorsInfo by mutableStateOf<List<Pair<PublicUser, CreationType>>?>(null)
	var likes by mutableStateOf<Int?>(null)
	var userLiked by mutableStateOf<Boolean?>(null)
	var comments by mutableStateOf<List<Comment>?>(null)
	var commentInfos by mutableStateOf<List<CommentInfo>?>(null)
	val publicationInfo: PublicationInfo<T>?
		get() = publication?.let { publication ->
			PublicationInfo(
				publication,
				creatorsInfo?.map { it.first },
				likes,
				userLiked,
				comments,
				comments?.size
			)
		}

	abstract suspend fun fetchPublication(id: String, user: User?)

	protected suspend fun getPublicationInfo(id: String, user: User?) {
		val publication = this.publication
		if (publication != null) {
			this.creatorsInfo = APIClient.getCreators(publication.creations.map { it.user }).map { list ->
				list.zip(publication.creations)
					.filter { it.first.id == it.second.user }
					.map { Pair(it.first, it.second.creationType) }
			}.getOrNull()
			this.likes = APIClient.getLikes(id).getOrNull()
			if (user != null) {
				this.userLiked = APIClient.getUserLikedPublication(id, user.id).getOrDefault(false)
			}
			this.comments = APIClient.getComments(id).getOrNull()
			commentInfos = fetchUsersOfComments()
		}
	}

	private suspend fun fetchUsersOfComments(): List<CommentInfo>? {
		val users = comments?.mapTo(mutableSetOf()) { it.userId } ?: emptySet()
		val result = APIClient.getListUsers(users.toList()).getOrDefault(emptyList())
		return comments?.mapNotNull { comment ->
			val user = result.find { it.id == comment.userId }
			user?.let { CommentInfo(comment, it) }
		}
	}

	private fun clear() {
		rememberScope?.cancel()
		rememberScope = null
		publication = null
		creatorsInfo = null
		likes = null
		userLiked = null
		comments = null
		commentInfos = null
	}

	fun refresh() {
		onForgotten()
		onRemembered()
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
					fetchPublication(it.first, it.second)
					isLoading = false
				}
				.collect()
		}
	}

	override fun onForgotten() {
		clear()
	}

	override fun onAbandoned() {
		clear()
	}

	companion object {
		@Suppress("UNCHECKED_CAST")
		fun <T : Publication, P : PublicationState<T>> Saver(): Saver<P, String> = object : Saver<P, String> {
			override fun restore(value: String): P? {
				val jsonObject = Json.decodeFromString<JsonObject>(value)
				val idJson = jsonObject["id"] ?: return null
				val userJson = jsonObject["user"] ?: return null
				val isLoadingJson = jsonObject["isLoading"] ?: return null
				val publicationJson = jsonObject["publication"] ?: return null
				val creatorsInfoJson = jsonObject["creatorsInfo"] ?: return null
				val likesJson = jsonObject["likes"] ?: return null
				val userLikedJson = jsonObject["userLiked"] ?: return null
				val commentsJson = jsonObject["comments"] ?: return null
				val commentInfosJson = jsonObject["commentInfos"] ?: return null
				val id = Json.decodeFromJsonElement<String>(idJson)
				val user = Json.decodeFromJsonElement<User?>(userJson)
				val isLoading = Json.decodeFromJsonElement<Boolean>(isLoadingJson)
				val publication = Json.decodeFromJsonElement<Publication?>(publicationJson)
				val creatorsInfo =
					Json.decodeFromJsonElement<List<Pair<PublicUser, CreationType>>?>(creatorsInfoJson)
				val likes = Json.decodeFromJsonElement<Int?>(likesJson)
				val userLiked = Json.decodeFromJsonElement<Boolean?>(userLikedJson)
				val comments = Json.decodeFromJsonElement<List<Comment>?>(commentsJson)
				val commentInfos = Json.decodeFromJsonElement<List<CommentInfo>?>(commentInfosJson)
				val publicationState = when (publication) {
					is Artwork -> ArtworkState(id, user).apply { this.publication = publication }
					is Event -> EventState(id, user).apply { this.publication = publication }
					is Post -> PostState(id, user).apply { this.publication = publication }
					null -> null
				} as P?
				publicationState?.isLoading = isLoading
				publicationState?.creatorsInfo = creatorsInfo
				publicationState?.likes = likes
				publicationState?.userLiked = userLiked
				publicationState?.comments = comments
				publicationState?.commentInfos = commentInfos
				return publicationState
			}

			override fun SaverScope.save(value: P): String {
				return buildJsonObject {
					put("id", Json.encodeToJsonElement(value.id))
					put("user", Json.encodeToJsonElement(value.user))
					put("isLoading", Json.encodeToJsonElement(value.isLoading))
					put("publication", Json.encodeToJsonElement<Publication?>(value.publication))
					put("creatorsInfo", Json.encodeToJsonElement(value.creatorsInfo))
					put("likes", Json.encodeToJsonElement(value.likes))
					put("userLiked", Json.encodeToJsonElement(value.userLiked))
					put("comments", Json.encodeToJsonElement(value.comments))
					put("commentInfos", Json.encodeToJsonElement(value.commentInfos))
				}.toString()
			}
		}
	}
}
