package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.creativehub.app.APIClient
import com.creativehub.app.model.User

class UserStateViewModel : ViewModel() {
	var isBusy by mutableStateOf(false)
	var user by mutableStateOf<User?>(null)

	suspend fun signIn(email: String, password: String) {
		isBusy = true
		user = APIClient.login(email, password)
		isBusy = false
	}

	suspend fun signOut() {
		isBusy = true
		APIClient.logout()
		user = null
		isBusy = false
	}

	val isLoggedIn get() = user != null
}

val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }