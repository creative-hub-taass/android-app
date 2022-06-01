package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.creativehub.app.api.*
import com.creativehub.app.model.Like
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.model.User
import com.creativehub.app.util.BusyViewModel
import com.creativehub.app.util.InvalidatingPagingSourceFactory
import kotlinx.coroutines.launch

class FeedStateViewModel : BusyViewModel() {
	private var user: User? = null
	private val config = PagingConfig(pageSize = 10, initialLoadSize = 10)
	private val feedPagingSourceFactory = InvalidatingPagingSourceFactory { FeedSource(user?.id) }
	private val eventsPagingSourceFactory = InvalidatingPagingSourceFactory { EventsSource(user?.id) }
	val feed = Pager(config, 0, feedPagingSourceFactory).flow.cachedIn(viewModelScope)
	val events = Pager(config, 0, eventsPagingSourceFactory).flow.cachedIn(viewModelScope)

	fun updateFeed(user: User?) {
		this.user = user
		feedPagingSourceFactory.invalidate()
		eventsPagingSourceFactory.invalidate()
	}

	suspend fun toggleLike(info: PublicationInfo<*>, userId: String) {
		if (info.userLiked == true) {
			APIClient.removeLike(Like(userId, info.publication.id)).isSuccess
		} else {
			APIClient.setLike(Like(userId, info.publication.id)).isSuccess
		}
		viewModelScope.launch {
			feedPagingSourceFactory.invalidate()
			eventsPagingSourceFactory.invalidate()
		}
	}
}

val LocalFeedState = compositionLocalOf<FeedStateViewModel> { error("Feed State Not Found!") }
