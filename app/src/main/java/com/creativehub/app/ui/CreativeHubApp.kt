package com.creativehub.app.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.ui.components.AppBar
import com.creativehub.app.ui.components.BottomBar
import com.creativehub.app.ui.components.CreativeHubNavHost
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel

@Composable
fun CreativeHubApp() {
	CreativeHubTheme {
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = { AppBar() },
			bottomBar = { BottomBar() }
		) {
			CreativeHubNavHost(Modifier.padding(it))
		}
	}
}

val LocalNavigationState = compositionLocalOf<NavHostController> { error("Navigation State Not Found!") }

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreviewLight() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		CreativeHubApp()
	}
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreviewDark() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		CreativeHubApp()
	}
}