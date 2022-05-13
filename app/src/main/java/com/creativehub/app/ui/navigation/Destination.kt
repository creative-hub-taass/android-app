package com.creativehub.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val label: String, val icon: ImageVector) {

	object Login : Destination(route = "login", "Login", Icons.Default.Login)
	object Register : Destination(route = "register", "Register", Icons.Default.AppRegistration)
	object Home : Destination(route = "home", "Home", Icons.Default.Home)
	object Feed : Destination(route = "home/feed", "Feed", Icons.Default.Feed)
	object Events : Destination(route = "home/events", "Events", Icons.Default.Event)
	object Profile : Destination(route = "home/profile", "Profile", Icons.Default.Person)
	object Artwork : Destination(route = "home/artwork", "Artwork", Icons.Default.Palette)
	object Event : Destination(route = "home/event", "Event", Icons.Default.Event)
	object Post : Destination(route = "home/post", "Post", Icons.Default.List)

	companion object {
		fun getStartDestination() = Login.route
	}
}