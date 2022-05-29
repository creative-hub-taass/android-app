package com.creativehub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.ui.CreativeHubApp
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.viewmodel.*


class MainActivity : ComponentActivity() {
	private val userState by viewModels<UserStateViewModel>()
	private val feedState by viewModels<FeedStateViewModel>()
	private val postState by viewModels<PostStateViewModel>()
	private val eventState by viewModels<EventStateViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		userState.onAuthStateChanged { user ->
			feedState.updateFeed(user)
		}
		setContent {
			CompositionLocalProvider(
				LocalUserState provides userState,
				LocalFeedState provides feedState,
				LocalNavigationState provides rememberNavController(),
				LocalPostState provides postState,
				LocalPostState provides postState,
				LocalEventState provides eventState,
			) {
				CreativeHubApp()
			}
		}
	}
}
