package com.creativehub.app

sealed class Destination(val route: String) {
	object Login : Destination(route = "login")
	object Register : Destination(route = "register")
	object Home : Destination(route = "home")
	object Feed : Destination(route = "home/feed")
	object Profile : Destination(route = "home/profile")
	object Artwork : Destination(route = "home/artwork")
	object Event : Destination(route = "home/event")
	object Post : Destination(route = "home/post")

	companion object {
		fun getStartDestination() = Login.route
	}
}