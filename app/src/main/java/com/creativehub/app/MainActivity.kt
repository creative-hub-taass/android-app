package com.creativehub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import com.creativehub.app.ui.CreativeHubApp
import com.creativehub.app.viewmodel.UserState
import com.creativehub.app.viewmodel.UserStateViewModel

class MainActivity : ComponentActivity() {
	private val userState by viewModels<UserStateViewModel>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			CompositionLocalProvider(UserState provides userState) {
				CreativeHubApp()
			}
		}
	}
}
