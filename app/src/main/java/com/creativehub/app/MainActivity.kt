package com.creativehub.app

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.creativehub.app.AppNav.*
import com.creativehub.app.HomeNav.*
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.views.HomeScreen
import com.creativehub.app.ui.views.LoginScreen
import com.creativehub.app.ui.views.RegisterScreen
import com.creativehub.app.viewmodel.UserState
import com.creativehub.app.viewmodel.UserStateViewModel
import kotlinx.coroutines.launch

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

@Composable
fun CreativeHubApp() {
	CreativeHubTheme {
		val navController = rememberNavController()
		val backstackEntry = navController.currentBackStackEntryAsState()
		val currentScreen = backstackEntry.value?.destination?.route
		val vm = UserState.current
		val startDestination = if (vm.isLoggedIn) Home.name else Login.name
		val coroutineScope = rememberCoroutineScope()
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = {
				if (currentScreen == Home.name) {
					TopAppBar(
						title = { Text(stringResource(id = R.string.app_name)) },
						actions = {
							IconButton(onClick = {
								coroutineScope.launch {
									vm.signOut()
								}
							}) {
								Icon(Icons.Filled.ExitToApp, null)
							}
						}
					)
				}
			}
		) {
			NavHost(navController = navController,
					startDestination = startDestination,
					modifier = Modifier.padding(it)) {
				navigation(startDestination = Feed.name, route = Home.name) {
					composable(Feed.name) {
						HomeScreen(navController)
					}
					composable(Profile.name) {
						Text(text = Profile.name)
					}
					composable(Artwork.name) {
						Text(text = Artwork.name)
					}
					composable(Event.name) {
						Text(text = Event.name)
					}
					composable(Post.name) {
						Text(text = Post.name)
					}
				}
				composable(Login.name) {
					LoginScreen(navController)
				}
				composable(Register.name) {
					RegisterScreen(navController)
				}
			}
		}
	}
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreviewLight() {
	CompositionLocalProvider(UserState provides UserStateViewModel()) {
		CreativeHubApp()
	}
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreviewDark() {
	CompositionLocalProvider(UserState provides UserStateViewModel()) {
		CreativeHubApp()
	}
}
