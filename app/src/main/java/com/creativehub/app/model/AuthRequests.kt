package com.creativehub.app.model

data class LoginRequest(
	val email: String,
	val password: String,
)

data class SocialLoginRequest(
	val email: String,
	val nickname: String,
	val token: String,
)

data class RegistrationRequest(
	val email: String,
	val password: String,
	val nickname: String,
)