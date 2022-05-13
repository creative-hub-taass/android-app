package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.ui.components.FeedElement
import com.creativehub.app.viewmodel.LocalFeedState
import com.creativehub.app.viewmodel.LocalUserState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(navController: NavController) {
	val auth = LocalUserState.current
	val feed = LocalFeedState.current
	val coroutineScope = rememberCoroutineScope()
	Column(Modifier.fillMaxSize(),
		   verticalArrangement = Arrangement.Center,
		   horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (feed.feed.isEmpty()) {
			LaunchedEffect(auth.isLoggedIn) {
				feed.fetchFeed(auth.user, 10)
			}
		}
		SwipeRefresh(
			state = rememberSwipeRefreshState(auth.isBusy || feed.isBusy),
			onRefresh = {
				coroutineScope.launch {
					feed.fetchFeed(auth.user, 10)
				}
			}
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize(),
				contentPadding = PaddingValues(2.dp)
			) {
				items(feed.feed, { it.publication.id }) {
					FeedElement(it, navController)
				}
			}
		}
	}
}

@Preview
@Composable
fun FeedPreview() {
	FeedScreen(rememberNavController())
}