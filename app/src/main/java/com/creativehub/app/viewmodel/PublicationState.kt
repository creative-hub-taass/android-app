package com.creativehub.app.viewmodel

import androidx.compose.runtime.*
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getListUsers
import com.creativehub.app.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest

abstract class PublicationState<T : Publication>(id: String, user: User?) : RememberObserver {
	private var rememberScope: CoroutineScope? = null
	var id by mutableStateOf(id)
	var user by mutableStateOf(user)
	var isLoading by mutableStateOf(false)
	var publication by mutableStateOf<T?>(null)
	var creatorsInfo by mutableStateOf<List<Pair<PublicUser, CreationType>>?>(null)
	var likes by mutableStateOf<Int?>(null)
	var userLiked by mutableStateOf<Boolean?>(null)
	var comments by mutableStateOf<List<Comment>?>(null)
	var commentsCount by mutableStateOf<Int?>(null)
	var commentInfos by mutableStateOf<List<CommentInfo>?>(null)
	val publicationInfo: PublicationInfo<T>
		get() = PublicationInfo(
			publication!!,
			creatorsInfo?.map { it.first },
			likes,
			userLiked,
			comments,
			commentsCount
		)

	abstract suspend fun fetchPublication(id: String, user: User?)

	protected suspend fun fetchUsersOfComments(): List<CommentInfo>? {
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
		id = "null"
		user = null
		publication = null
		creatorsInfo = null
		likes = null
		userLiked = null
		comments = null
		commentsCount = null
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
}