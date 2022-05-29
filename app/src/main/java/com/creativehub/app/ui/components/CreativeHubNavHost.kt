package com.creativehub.app.ui.components

import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.views.*
import com.creativehub.app.viewmodel.LocalUserState
import kotlinx.coroutines.launch

@Composable
fun CreativeHubNavHost(modifier: Modifier) {
	val context = LocalContext.current
	val userState = LocalUserState.current
	val navigation = LocalNavigationState.current
	val startDestination = Destination.getStartDestination()
	val coroutineScope = rememberCoroutineScope()
	LaunchedEffect(Unit) {
		userState.tryGoogleAutoSignIn(context)
	}
	NavHost(
		navController = navigation,
		startDestination = startDestination,
		modifier = modifier,
	) {
		navigation(startDestination = Destination.Feed.route, route = Destination.Home.route) {
			composable(Destination.Feed.route) {
				FeedScreen()
			}
			composable(Destination.Events.route) {
				EventsScreen()
			}
			composable(Destination.Profile.route) {
				ProfileScreen()
				//EventScreen("cc0a0c6e-866f-4d4c-a5f0-9205f9a0e977")
			}
			composable(Destination.Artwork.route, arguments = Destination.Artwork.arguments) {
				ArtworkScreen(it.arguments?.getString("id") ?: "")
			}
			composable(Destination.Event.route, arguments = Destination.Event.arguments) {
				EventScreen((it.arguments?.getString("id") ?: "?"))
			}
			composable(Destination.Post.route, arguments = Destination.Post.arguments) {
				PostScreen(it.arguments?.getString("id") ?: "")
			}
			composable(Destination.Creator.route, arguments = Destination.Creator.arguments) {
				// TODO
				Text(text = Destination.Creator.label + " " + (it.arguments?.getString("id") ?: "?"))
			}
		}
		composable(Destination.Login.route) {
			LoginScreen(
				{ email, password ->
					coroutineScope.launch {
						val result = userState.login(email, password)
						if (result != null) {
							Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
						}
					}
				},
				{ email, nickname, token ->
					coroutineScope.launch {
						val result = userState.socialLogin(email, nickname, token)
						if (result != null) {
							Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
						}
					}
				}
			)
		}
		composable(Destination.Register.route) {
			RegisterScreen { nickname, email, password ->
				coroutineScope.launch {
					val result = userState.register(nickname, email, password)
					val message =
						result ?: "Registration successful.\nWe have sent you an e-mail with a confirmation link."
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
				}.invokeOnCompletion {
					navigation.navigate(Destination.Login.route)
				}
			}
		}
	}
}