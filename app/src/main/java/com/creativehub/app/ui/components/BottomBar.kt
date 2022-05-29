package com.creativehub.app.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination.*

@Composable
fun BottomBar() {
	val navigation = LocalNavigationState.current
	val backstackEntry = navigation.currentBackStackEntryAsState()
	val currentScreen = backstackEntry.value?.destination?.route
	if (currentScreen?.contains(Home.route) == true) {
		BottomNavigation(
			backgroundColor = MaterialTheme.colors.secondaryVariant,
			contentColor = MaterialTheme.colors.onSecondary,
		) {
			for (item in listOf(Feed, Events, Profile)) {
				BottomNavigationItem(
					selected = currentScreen == item.route,
					onClick = { navigation.navigate(item.route) },
					label = { Text(text = item.label) },
					icon = { Icon(item.icon, "Logout") }
				)
			}
		}
	}
}