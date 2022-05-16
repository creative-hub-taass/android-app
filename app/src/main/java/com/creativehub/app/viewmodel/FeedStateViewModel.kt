package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.creativehub.app.api.*
import com.creativehub.app.model.Comment
import com.creativehub.app.model.Like
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.model.User

class InvalidatingPagingSourceFactory<K : Any, V : Any, PS : PagingSource<K, V>>(val factory: () -> PS) : () -> PS {
	private val list = mutableListOf<PagingSource<K, V>>()

	override fun invoke(): PS = factory().also { list.add(it) }

	fun invalidate() {
		while (list.isNotEmpty()) {
			list.removeFirst().invalidate()
		}
	}
}

class FeedStateViewModel : BusyViewModel() {
	private var user: User? = null
	private val config = PagingConfig(pageSize = 20, initialLoadSize = 20)
	private val pagingSourceFactory = InvalidatingPagingSourceFactory { FeedSource(user?.id) }
	val publicFeed = Pager(config, 0, pagingSourceFactory).flow.cachedIn(viewModelScope)

	fun updateFeed(user: User?) {
		this.user = user
		pagingSourceFactory.invalidate()
	}

	suspend fun fetchComments(info: PublicationInfo<*>): List<Comment>? {
		return APIClient.getComments(info.publication.id).getOrNull()
	}

	suspend fun toggleLike(info: PublicationInfo<*>, userId: String) {
		if (info.userLiked == true) {
			APIClient.removeLike(Like(info.publication.id, userId)).isSuccess
		} else {
			APIClient.setLike(Like(userId, info.publication.id)).isSuccess
		}
	}
}

val LocalFeedState = compositionLocalOf<FeedStateViewModel> { error("Feed State Not Found!") }
