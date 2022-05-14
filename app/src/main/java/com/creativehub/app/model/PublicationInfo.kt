package com.creativehub.app.model

class PublicationInfo<T : Publication> private constructor(
	val publication: T,
	val creators: List<Pair<PublicUser, CreationType>>?,
	val likes: Int?,
	val userLiked: Boolean?,
	val comments: List<Comment>?,
	val commentsCount: Int?,
	val loaded: Boolean,
) {
	constructor(
		publication: T,
		creators: List<Pair<PublicUser, CreationType>>,
		likes: Int,
		userLiked: Boolean,
		comments: List<Comment>,
		commentsCount: Int,
	) : this(publication, creators, likes, userLiked, comments, commentsCount, true)

	constructor(publication: T) : this(
		publication,
		null,
		null,
		null,
		null,
		null,
		false
	)

	constructor(
		info: PublicationInfo<T>,
		creators: List<Pair<PublicUser, CreationType>>? = null,
		likes: Int? = null,
		userLiked: Boolean? = null,
		comments: List<Comment>? = null,
		commentsCount: Int? = null,
	) : this(
		info.publication,
		info.creators ?: creators,
		info.likes ?: likes,
		info.userLiked ?: userLiked,
		info.comments ?: comments,
		info.commentsCount ?: commentsCount,
		true
	)
}
