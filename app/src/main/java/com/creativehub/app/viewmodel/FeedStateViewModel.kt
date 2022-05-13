package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getPublicFeed
import com.creativehub.app.api.getUserFeed
import com.creativehub.app.model.Publication
import com.creativehub.app.model.User

class FeedStateViewModel : BusyViewModel() {
	var feed by mutableStateOf(emptyList<Publication>())

	suspend fun fetchFeed(user: User? = null, limit: Int? = null) = runBusy {
		feed = if (user != null) {
			APIClient.getUserFeed(user.id, limit).getOrDefault(listOf())
		} else {
			APIClient.getPublicFeed(limit).getOrDefault(listOf())
		}
	}
}

val LocalFeedState = compositionLocalOf<FeedStateViewModel> { error("Feed State Not Found!") }