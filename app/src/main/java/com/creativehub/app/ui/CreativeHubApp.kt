package com.creativehub.app.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.Destination
import com.creativehub.app.Destination.*
import com.creativehub.app.ui.components.AppBar
import com.creativehub.app.ui.components.BottomBar
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.views.HomeScreen
import com.creativehub.app.ui.views.LoginScreen
import com.creativehub.app.ui.views.RegisterScreen
import com.creativehub.app.viewmodel.UserState
import com.creativehub.app.viewmodel.UserStateViewModel
import kotlinx.coroutines.launch

@Composable
fun CreativeHubApp() {
	CreativeHubTheme {
		val context = LocalContext.current
		val navController = rememberNavController()
		val vm = UserState.current
		val startDestination = Destination.getStartDestination()
		val coroutineScope = rememberCoroutineScope()
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = { AppBar(navController) },
			bottomBar = { BottomBar(navController) }
		) {
			NavHost(navController = navController,
					startDestination = startDestination,
					modifier = Modifier.padding(it)) {
				navigation(startDestination = Feed.route, route = Home.route) {
					composable(Feed.route) {
						HomeScreen(navController)
					}
					composable(Events.route) {
						Text(text = Events.label)
					}
					composable(Profile.route) {
						Text(text = Profile.label)
					}
					composable(Artwork.route) {
						Text(text = Artwork.label)
					}
					composable(Event.route) {
						Text(text = Event.label)
					}
					composable(Post.route) {
						Text(text = Post.label)
					}
				}
				composable(Login.route) {
					if (vm.isLoggedIn) {
						LaunchedEffect(key1 = Unit) {
							navController.navigate(route = Feed.route) {
								popUpTo(route = Login.route) {
									inclusive = true
								}
							}
						}
					} else {
						LoginScreen(navController) { email, password ->
							coroutineScope.launch {
								val result = vm.login(email, password)
								if (result != null) {
									Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
								}
							}
						}
					}
				}
				composable(Register.route) {
					RegisterScreen(navController) { nickname, email, password ->
						coroutineScope.launch {
							val result = vm.register(nickname, email, password)
							val message =
								result ?: "Registration successful.\nWe have sent you an e-mail with a confirmation link."
							Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
						}.invokeOnCompletion {
							navController.navigate(Login.route)
						}
					}
				}
			}
		}
	}
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreviewLight() {
	CompositionLocalProvider(UserState provides UserStateViewModel()) {
		CreativeHubApp()
	}
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreviewDark() {
	CompositionLocalProvider(UserState provides UserStateViewModel()) {
		CreativeHubApp()
	}
}