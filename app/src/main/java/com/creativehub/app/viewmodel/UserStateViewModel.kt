@file:Suppress("PrivatePropertyName")

package com.creativehub.app.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.creativehub.app.api.*
import com.creativehub.app.model.User
import com.creativehub.app.util.BusyViewModel
import com.creativehub.app.util.getGoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserStateViewModel : BusyViewModel() {
	private var _onAuthStateChanged: (User?) -> Unit = {}
	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
	private val ACCESS_TOKEN_KEY = stringPreferencesKey("access-token")
	private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh-token")
	var user by mutableStateOf<User?>(null)

	init {
		snapshotFlow { user }
			.onEach { _onAuthStateChanged(it) }
			.launchIn(viewModelScope)
	}

	suspend fun login(email: String, password: String, context: Context): String? = runBusy {
		val result = APIClient.login(email, password)
		return@runBusy performLogin(result, context)
	}

	suspend fun socialLogin(email: String, nickname: String, token: String, context: Context): String? = runBusy {
		val result = APIClient.socialLogin(email, nickname, token)
		return@runBusy performLogin(result, context)
	}

	private suspend fun performLogin(result: Result<Pair<User, BearerTokens?>>, context: Context): String? {
		val (user, tokens) = result.getOrDefault(Pair(null, null))
		this.user = user
		context.dataStore.edit { settings ->
			settings[ACCESS_TOKEN_KEY] = tokens?.accessToken ?: ""
			settings[REFRESH_TOKEN_KEY] = tokens?.refreshToken ?: ""
		}
		return when (val exception = result.exceptionOrNull()) {
			is ClientRequestException -> exception.response.bodyAsText()
			is ServerResponseException -> "Server error"
			null -> null
			else -> exception.message
		}
	}

	private suspend fun socialLoginGoogle(account: GoogleSignInAccount?, context: Context): String? {
		val mail = account?.email
		val nickname = account?.displayName
		val token = account?.idToken
		return if (mail != null && nickname != null) {
			socialLogin(mail, nickname, token ?: "", context)
		} else null
	}

	private suspend fun tryGoogleAutoSignIn(context: Context) {
		val task = getGoogleSignInClient(context).silentSignIn()
		if (task.isSuccessful) {
			socialLoginGoogle(task.result, context)
		} else {
			task.addOnSuccessListener {
				viewModelScope.launch {
					socialLoginGoogle(it, context)
				}
			}
		}
	}

	private suspend fun tryTokenLogin(context: Context): Boolean {
		val bearerTokens = context.dataStore.data.map {
			val accessToken = it[ACCESS_TOKEN_KEY]?.ifBlank { null }
			val refreshToken = it[REFRESH_TOKEN_KEY]?.ifBlank { null }
			if (accessToken != null && refreshToken != null) BearerTokens(accessToken, refreshToken) else null
		}.first()
		if (bearerTokens != null) {
			val jwt = JWT(bearerTokens.accessToken)
			val userId = jwt.subject
			if (userId != null) {
				APIClient.setTokens(bearerTokens)
				val user = APIClient.getUser(userId).getOrNull()
				if (user != null) {
					this.user = user
					return true
				}
			}
		}
		return false
	}

	suspend fun tryAutoLogin(context: Context) {
		if (!tryTokenLogin(context)) {
			tryGoogleAutoSignIn(context)
		}
	}

	suspend fun logout(context: Context) = runBusy {
		APIClient.logout()
		getGoogleSignInClient(context).signOut()
		user = null
		context.dataStore.edit {
			it.remove(ACCESS_TOKEN_KEY)
			it.remove(REFRESH_TOKEN_KEY)
		}
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

	suspend fun follow(followedId: String) {
		val followerId = user?.id
		if (followerId != null) {
			user = APIClient.follow(followerId, followedId).getOrDefault(user)
		}
	}

	suspend fun unfollow(followedId: String) {
		val followerId = user?.id
		if (followerId != null) {
			user = APIClient.unfollow(followerId, followedId).getOrDefault(user)
		}
	}
}

val LocalUserState = compositionLocalOf<UserStateViewModel> { error("User State Not Found!") }