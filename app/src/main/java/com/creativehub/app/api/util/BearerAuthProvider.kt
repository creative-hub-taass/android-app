package com.creativehub.app.api.util

import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.auth.*

class BearerAuthProvider(
	private val refreshTokens: suspend RefreshTokensParams.() -> BearerTokens?,
	private val loadTokens: () -> BearerTokens?,
	private val sendWithoutRequestCallback: (HttpRequestBuilder) -> Boolean,
) : AuthProvider {
	override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean = sendWithoutRequestCallback(request)

	/**
	 * Check if current provider is applicable to the request.
	 */
	override fun isApplicable(auth: HttpAuthHeader): Boolean {
		if (auth.authScheme != AuthScheme.Bearer) return false
		return true
	}

	@Deprecated("Deprecated", ReplaceWith("sendWithoutRequest { }"))
	override val sendWithoutRequest: Boolean
		get() = error("deprecated")

	/**
	 * Add authentication method headers and creds.
	 */
	override suspend fun addRequestHeaders(request: HttpRequestBuilder, authHeader: HttpAuthHeader?) {
		val token = loadTokens() ?: return
		request.headers {
			val tokenValue = "Bearer ${token.accessToken}"
			if (contains(HttpHeaders.Authorization)) {
				remove(HttpHeaders.Authorization)
			}
			append(HttpHeaders.Authorization, tokenValue)
		}
	}

	override suspend fun refreshToken(response: HttpResponse): Boolean {
		val newToken = refreshTokens(RefreshTokensParams(response.call.client, response, loadTokens()))
		return newToken != null
	}
}

fun Auth.bearerToken(block: BearerAuthConfig.() -> Unit) {
	val config = BearerAuthConfig().apply(block)
	val authProvider = BearerAuthProvider(config.refreshTokens, config.loadTokens, config.sendWithoutRequest)
	this@bearerToken.providers.add(authProvider)
}