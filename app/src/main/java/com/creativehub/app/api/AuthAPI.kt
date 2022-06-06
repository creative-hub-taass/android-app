package com.creativehub.app.api

import com.creativehub.app.model.LoginRequest
import com.creativehub.app.model.RegistrationRequest
import com.creativehub.app.model.SocialLoginRequest
import com.creativehub.app.model.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun APIClient.login(email: String, password: String) = runCatching {
	val response = APIClient().post("$USERS_BASE_URL/-/auth/login") {
		contentType(ContentType.Application.Json)
		setBody(LoginRequest(email, password))
	}
	val tokens = extractTokens(response)
	val user = response.body<User>()
	Pair(user, tokens)
}

suspend fun APIClient.socialLogin(email: String, nickname: String, token: String) = runCatching {
	val response = APIClient().post("$USERS_BASE_URL/-/auth/loginsocial") {
		contentType(ContentType.Application.Json)
		setBody(SocialLoginRequest(email, nickname, token))
	}
	val tokens = extractTokens(response)
	val user = response.body<User>()
	Pair(user, tokens)
}

suspend fun APIClient.register(nickname: String, email: String, password: String) = runCatching {
	APIClient().post("$USERS_BASE_URL/-/auth/register") {
		contentType(ContentType.Application.Json)
		setBody(RegistrationRequest(nickname, email, password))
	}.bodyAsText()
}

suspend fun APIClient.getUser(userId: String) = runCatching {
	APIClient().get("$USERS_BASE_URL/$userId").body<User>()
}