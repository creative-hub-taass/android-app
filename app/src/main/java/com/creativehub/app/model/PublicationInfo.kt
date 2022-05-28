package com.creativehub.app.model

import kotlinx.serialization.Serializable

@Serializable
class PublicationInfo<T : Publication>(
	val publication: T,
	val creators: List<PublicUser>? = null,
	val likes: Int? = null,
	val userLiked: Boolean? = null,
	val comments: List<Comment>? = null,
	val commentsCount: Int? = null,
	val creatorsFollowedByUser: Boolean? = null,
)
