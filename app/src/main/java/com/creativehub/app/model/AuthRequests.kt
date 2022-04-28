package com.creativehub.app.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
	val email: String,
	val password: String,
)

@Serializable
data class SocialLoginRequest(
	val email: String,
	val nickname: String,
	val token: String,
)

@Serializable
data class RegistrationRequest(
	val email: String,
	val password: String,
	val nickname: String,
)