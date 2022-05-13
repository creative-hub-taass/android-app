package com.creativehub.app.api

import com.creativehub.app.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object APIClient {
	private const val API_BASE_URL = "${BuildConfig.API_GATEWAY_URL}/api/v1"
	const val USERS_BASE_URL = "$API_BASE_URL/users"
	const val PUBLICATIONS_BASE_URL = "$API_BASE_URL/publications"
	const val INTERACTIONS_BASE_URL = "$API_BASE_URL/interactions"
	const val PAYMENTS_BASE_URL = "$API_BASE_URL/payments"
	private val bearerTokenStorage = mutableListOf<BearerTokens>()
	private val client = HttpClient(CIO) {
		expectSuccess = true
		install(ContentNegotiation) {
			json(Json {
				ignoreUnknownKeys = true
			})
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

	fun logout() {
		bearerTokenStorage.clear()
	}
}
