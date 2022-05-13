package com.creativehub.app.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.creativehub.app.R
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.viewmodel.LocalUserState

@Composable
fun AppBar(navController: NavController) {
	val backstackEntry = navController.currentBackStackEntryAsState()
	val currentScreen = backstackEntry.value?.destination?.route
	val vm = LocalUserState.current
	val context = LocalContext.current
	if (currentScreen?.contains(Destination.Home.route) == true) {
		TopAppBar(
			title = { Text(stringResource(id = R.string.app_name)) },
			actions = {
				if (vm.isLoggedIn) {
					IconButton(onClick = {
						vm.logout(context)
						navController.navigate(Destination.Login.route)
					}) {
						Icon(Icons.Filled.Logout, "Logout")
					}
				} else {
					IconButton(onClick = {
						navController.navigate(Destination.Login.route)
					}) {
						Text("Login")
					}
				}
			}
		)
	}
}