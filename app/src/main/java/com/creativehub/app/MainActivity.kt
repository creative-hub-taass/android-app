package com.creativehub.app

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.Destination.*
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
		val context = LocalContext.current
		val navController = rememberNavController()
		val vm = UserState.current
		val startDestination = Destination.getStartDestination()
		val coroutineScope = rememberCoroutineScope()
		val scaffoldState = rememberScaffoldState()
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = { AppBar(navController) }
		) {
			NavHost(navController = navController,
					startDestination = startDestination,
					modifier = Modifier.padding(it)) {
				composable(Feed.route) {
					HomeScreen(navController)
				}
				composable(Profile.route) {
					Text(text = Profile.route)
				}
				composable(Artwork.route) {
					Text(text = Artwork.route)
				}
				composable(Event.route) {
					Text(text = Event.route)
				}
				composable(Post.route) {
					Text(text = Post.route)
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

@Composable
fun AppBar(navController: NavController) {
	val backstackEntry = navController.currentBackStackEntryAsState()
	val currentScreen = backstackEntry.value?.destination?.route
	val vm = UserState.current
	if (currentScreen?.contains(Home.route) == true) {
		TopAppBar(
			title = { Text(stringResource(id = R.string.app_name)) },
			actions = {
				if (vm.isLoggedIn) {
					IconButton(onClick = {
						vm.logout()
						navController.navigate(Login.route)
					}) {
						Icon(Icons.Filled.Logout, null)
					}
				} else {
					IconButton(onClick = {
						navController.navigate(Login.route)
					}) {
						Text("Login")
					}
				}
			}
		)
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
