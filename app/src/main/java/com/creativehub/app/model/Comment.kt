package com.creativehub.app.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
	val id: String,
	val userId: String,
	val publicationId: String,
	val message: String,
	val timestamp: Instant,
)

@Serializable
class CommentInfo(
	val comment: Comment,
	val user: PublicUser?,
)