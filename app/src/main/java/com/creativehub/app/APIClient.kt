package com.creativehub.app

import com.creativehub.app.model.LoginRequest
import com.creativehub.app.model.RegistrationRequest
import com.creativehub.app.model.SocialLoginRequest
import com.creativehub.app.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

object APIClient {
	private const val API_BASE_URL = "${BuildConfig.API_GATEWAY_URL}/api/v1"
	private const val USERS_BASE_URL = "$API_BASE_URL/users"
	private const val PUBLICATIONS_BASE_URL = "$API_BASE_URL/publications"
	private const val INTERACTIONS_BASE_URL = "$API_BASE_URL/interactions"
	private const val PAYMENTS_BASE_URL = "$API_BASE_URL/payments"
	private val bearerTokenStorage = mutableListOf<BearerTokens>()
	private val client = HttpClient(CIO) {
		expectSuccess = true
		install(ContentNegotiation) {
			json()
		}
		Auth {
			bearer {
				loadTokens {
					bearerTokenStorage.lastOrNull()
				}
				refreshTokens {
					val response = client.post("$USERS_BASE_URL/-/auth/refresh") {
						markAsRefreshTokenRequest()
						setBody(oldTokens?.refreshToken)
					}
					val access = response.headers["X-ACCESS-TOKEN"]
					val refresh = response.headers["X-REFRESH-TOKEN"]
					val tokens = if (!access.isNullOrEmpty() && !refresh.isNullOrBlank()) {
						BearerTokens(access, refresh)
					} else null
					tokens?.let { bearerTokenStorage.add(it) }
					tokens
				}
				sendWithoutRequest { request ->
					request.url.buildString().contains("/-/")
				}
			}
		}
	}

	operator fun invoke() = client

	suspend fun login(email: String, password: String) = runCatching {
		client.post("$USERS_BASE_URL/-/auth/login") {
			contentType(ContentType.Application.Json)
			setBody(LoginRequest(email, password))
		}.body<User>()
	}

	suspend fun loginSocial(email: String, password: String, token: String) = runCatching {
		client.post("$USERS_BASE_URL/-/auth/loginsocial") {
			contentType(ContentType.Application.Json)
			setBody(SocialLoginRequest(email, password, token))
		}.body<User>()
	}

	suspend fun register(nickname: String, email: String, password: String) = runCatching {
		client.post("$USERS_BASE_URL/-/auth/register") {
			contentType(ContentType.Application.Json)
			setBody(RegistrationRequest(nickname, email, password))
		}.bodyAsText()
	}

	fun logout() {
		bearerTokenStorage.clear()
	}
}
