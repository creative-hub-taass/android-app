package com.creativehub.app.viewmodel

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.creativehub.app.APIClient
import com.creativehub.app.model.User
import io.ktor.client.plugins.*
import io.ktor.client.statement.*

class UserStateViewModel : ViewModel() {
	var isBusy by mutableStateOf(false)
	var user by mutableStateOf<User?>(null)

	suspend fun login(email: String, password: String): String? = runBusy {
		val result = APIClient.login(email, password)
		user = result.getOrNull()
		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	fun logout() = runBusy {
		APIClient.logout()
		user = null
	}

	suspend fun register(nickname: String, email: String, password: String): String? = runBusy {
		val result = APIClient.register(nickname, email, password)
		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	val isLoggedIn get() = user != null

	private inline fun <T> runBusy(block: () -> T): T {
		isBusy = true
		val result = block()
		isBusy = false
		return result
	}
}

val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }