package com.creativehub.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Like(
	val userId: String,
	val publicationId: String,
)
