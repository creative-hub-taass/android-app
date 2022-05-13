package com.creativehub.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
	val id: String,
	val userId: String,
	val publicationId: String,
	val message: String,
)
