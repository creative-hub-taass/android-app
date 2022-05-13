package com.creativehub.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(
	val route: String,
	val label: String,
	val icon: ImageVector,
	val arguments: List<NamedNavArgument> = emptyList(),
	val argRoute: (String) -> String = { route },
) {

	object Login : Destination(
		"login",
		"Login",
		Icons.Default.Login
	)

	object Register : Destination(
		"register",
		"Register",
		Icons.Default.AppRegistration
	)

	object Home : Destination(
		"home",
		"Home",
		Icons.Default.Home
	)

	object Feed : Destination(
		"home/feed",
		"Feed",
		Icons.Default.Feed
	)

	object Events : Destination(
		"home/events",
		"Events",
		Icons.Default.Event
	)

	object Profile : Destination(
		"home/profile",
		"Profile",
		Icons.Default.Person
	)

	object Artwork : Destination(
		"home/artwork/{id}",
		"Artwork",
		Icons.Default.Palette,
		listOf(navArgument("id") {
			nullable = false
			type = NavType.StringType
		}),
		{ "home/artwork/${it}" }
	)

	object Event : Destination(
		"home/event/{id}",
		"Event",
		Icons.Default.Event,
		listOf(navArgument("id") {
			nullable = false
			type = NavType.StringType
		}),
		{ "home/event/${it}" }
	)

	object Post : Destination(
		"home/post/{id}",
		"Post",
		Icons.Default.List,
		listOf(navArgument("id") {
			nullable = false
			type = NavType.StringType
		}),
		{ "home/post/${it}" }
	)

	companion object {
		fun getStartDestination() = Home.route
	}
}