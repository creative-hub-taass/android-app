package com.creativehub.app.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.creativehub.app.model.PublicationInfo

class FeedSource(private val userId: String?) : PagingSource<Int, PublicationInfo<*>>() {
	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PublicationInfo<*>> {
		val page = when (params) {
			is LoadParams.Refresh -> 0
			else -> params.key ?: 0
		}
		val pageSize = params.loadSize
		val result = APIClient.getFeed(userId, page, pageSize)
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

	override fun getRefreshKey(state: PagingState<Int, PublicationInfo<*>>): Int? {
		return state.anchorPosition?.let { state.closestPageToPosition(it)?.nextKey }
	}
}