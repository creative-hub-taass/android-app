package com.creativehub.app.api.util

import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*

class BearerAuthConfig {
	internal var refreshTokens: suspend RefreshTokensParams.() -> BearerTokens? = { null }
	internal var loadTokens: () -> BearerTokens? = { null }
	internal var sendWithoutRequest: (HttpRequestBuilder) -> Boolean = { true }

	/**
	 * Configures a callback that refreshes a token when the 401 status code is received.
	 */
	fun onRefreshTokens(block: suspend RefreshTokensParams.() -> BearerTokens?) {
		refreshTokens = block
	}

	/**
	 * Configures a callback that loads a cached token from a local storage.
	 * Note: Using the same client instance here to make a request will result in a deadlock.
	 */
	fun onLoadTokens(block: () -> BearerTokens?) {
		loadTokens = block
	}

	/**
	 * Send credentials in without waiting for [HttpStatusCode.Unauthorized].
	 */
	fun sendDirectlyIf(block: (HttpRequestBuilder) -> Boolean) {
		sendWithoutRequest = block
	}
}