package com.creativehub.app.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.login
import com.creativehub.app.api.register
import com.creativehub.app.api.socialLogin
import com.creativehub.app.model.User
import com.creativehub.app.util.getGoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserStateViewModel : BusyViewModel() {
	private var _onAuthStateChanged: (User?) -> Unit = {}
	var user by mutableStateOf<User?>(null)

	init {
		snapshotFlow { user }
			.onEach { _onAuthStateChanged(it) }
			.launchIn(viewModelScope)
	}

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

	suspend fun socialLogin(email: String, nickname: String, token: String): String? = runBusy {
		val result = APIClient.socialLogin(email, nickname, token)
		user = result.getOrNull()
		return@runBusy when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	suspend fun socialLoginGoogle(account: GoogleSignInAccount?): String? {
		val mail = account?.email
		val nickname = account?.displayName
		val token = account?.idToken
		return if (mail != null && nickname != null) {
			socialLogin(mail, nickname, token ?: "")
		} else null
	}

	suspend fun tryGoogleAutoSignIn(context: Context) {
		val task = getGoogleSignInClient(context).silentSignIn()
		if (task.isSuccessful) {
			socialLoginGoogle(task.result)
		} else {
			task.addOnSuccessListener {
				viewModelScope.launch {
					socialLoginGoogle(it)
				}
			}
		}
	}

	fun logout(context: Context) = runBusy {
		APIClient.logout()
		getGoogleSignInClient(context).signOut()
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

	fun onAuthStateChanged(action: (User?) -> Unit) {
		_onAuthStateChanged = action
	}
}

val LocalUserState = compositionLocalOf<UserStateViewModel> { error("User State Not Found!") }