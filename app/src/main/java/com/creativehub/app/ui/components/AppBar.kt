package com.creativehub.app.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.viewmodel.LocalUserState

@Composable
fun AppBar() {
	val vm = LocalUserState.current
	val context = LocalContext.current
	val navigation = LocalNavigationState.current
	val backstackEntry = navigation.currentBackStackEntryAsState()
	val currentScreen = backstackEntry.value?.destination?.route
	if (currentScreen?.contains(Destination.Home.route) == true) {
		TopAppBar(
			title = {
				Text(buildAnnotatedString {
					pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
					append("creative")
					pushStyle(SpanStyle(fontWeight = FontWeight.Black))
					append("Hub")
				})
			},
			actions = {
				if (vm.isLoggedIn) {
					IconButton(onClick = {
						vm.logout(context)
						navigation.navigate(Destination.Login.route)
					}) {
						Icon(Icons.Filled.Logout, "Logout")
					}
				} else {
					IconButton(onClick = {
						navigation.navigate(Destination.Login.route)
					}) {
						Text("Login")
					}
				}
			}
		)
	}
}