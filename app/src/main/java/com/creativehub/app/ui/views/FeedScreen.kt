package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.creativehub.app.ui.components.ErrorItem
import com.creativehub.app.ui.components.FeedElement
import com.creativehub.app.viewmodel.LocalFeedState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun FeedScreen() {
	val lazyFeed = LocalFeedState.current.publicFeed.collectAsLazyPagingItems()
	Column(Modifier.fillMaxSize(),
		   verticalArrangement = Arrangement.Center,
		   horizontalAlignment = Alignment.CenterHorizontally
	) {
		SwipeRefresh(
			state = rememberSwipeRefreshState(lazyFeed.loadState.refresh is LoadState.Loading
													  || lazyFeed.loadState.prepend is LoadState.Loading),
			onRefresh = { lazyFeed.refresh() }
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize(),
				contentPadding = PaddingValues(2.dp)
			) {
				items(lazyFeed, { it.publication.id }) {
					if (it != null) {
						FeedElement(it)
					}
				}
				lazyFeed.apply {
					when (val refreshState = loadState.refresh) {
						is LoadState.Error -> {
							item {
								ErrorItem(
									message = refreshState.error.message?.ifEmpty { null } ?: "Network error",
									modifier = Modifier.fillParentMaxSize(),
									onClickRetry = { retry() }
								)
							}
						}
						is LoadState.NotLoading -> {}
						is LoadState.Loading -> {}
					}
					when (val appendState = loadState.append) {
						is LoadState.NotLoading -> {}
						is LoadState.Loading -> {
							item {
								CircularProgressIndicator(
									modifier = Modifier
										.fillMaxWidth()
										.padding(16.dp)
										.wrapContentWidth(Alignment.CenterHorizontally)
								)
							}
						}
						is LoadState.Error -> {
							item {
								ErrorItem(
									message = appendState.error.message?.ifEmpty { null } ?: "Network error",
									onClickRetry = { retry() }
								)
							}
						}
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun FeedPreview() {
	FeedScreen()
}