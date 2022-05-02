package com.creativehub.app

/**
 * Screen metadata for Rally.
 */
enum class AppNav {
	Login,
	Register,
	Home;

	companion object {
		fun fromRoute(route: String?) = valueOf(route ?: Login.name)
	}
}

enum class HomeNav {
	Feed,
	Profile,
	Artwork,
	Event,
	Post;

	companion object {
		fun fromRoute(route: String?) = valueOf(route ?: Feed.name)
	}
}