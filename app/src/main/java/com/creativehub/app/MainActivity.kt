package com.creativehub.app

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.views.HomeScreen
import com.creativehub.app.ui.views.LoginScreen
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

@Composable
fun CreativeHubApp() {
	CreativeHubTheme {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colors.background
		) {
			ApplicationSwitcher()
		}
	}
}

@Composable
fun ApplicationSwitcher() {
	val vm = UserState.current
	if (vm.isLoggedIn) {
		HomeScreen()
	} else {
		LoginScreen()
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
