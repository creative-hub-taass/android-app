package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.creativehub.app.viewmodel.LocalFeedState
import com.creativehub.app.viewmodel.LocalUserState

@Composable
fun FeedScreen(navController: NavHostController) {
	val auth = LocalUserState.current
	val feed = LocalFeedState.current
	Column(Modifier.fillMaxSize(),
		   verticalArrangement = Arrangement.Center,
		   horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (feed.feed.isEmpty()) {
			LaunchedEffect(feed) {
				feed.fetchFeed(auth.user)
			}
		}
		if (auth.isBusy || feed.isBusy) {
			CircularProgressIndicator()
		} else {
			LazyColumn {
				items(feed.feed, { it.id }) {
					Card {
						Text(it.id)
					}
				}
			}
		}
	}
}