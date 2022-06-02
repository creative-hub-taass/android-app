package com.creativehub.app.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.creativehub.app.model.Event
import com.creativehub.app.model.PublicationInfo

class EventsSource(private val userId: String?) : PagingSource<Int, PublicationInfo<Event>>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PublicationInfo<Event>> {
		val page = when (params) {
			is LoadParams.Refresh -> 0
			else -> params.key ?: 0
		}
		val pageSize = params.loadSize
		val result = APIClient.getEventsFeed(userId, page, pageSize)
		val feedData = result.getOrNull()
		val error = result.exceptionOrNull()
		return if (feedData != null) {
			val (list, links) = feedData
			LoadResult.Page(
				data = list,
				prevKey = if (links.prev != null) page - 1 else null,
				nextKey = if (links.next != null) page + 1 else null,
				itemsBefore = page * pageSize
			)
		} else {
			LoadResult.Error(error!!)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, PublicationInfo<Event>>): Int? {
		return state.anchorPosition?.let { state.closestPageToPosition(it)?.nextKey }
	}
}