package com.creativehub.app.model

import android.util.Log

interface PublicationInfo<T : Publication> {
	val publication: T
	val loading: Boolean
	val loaded: Boolean
	val creators: List<Pair<PublicUser, CreationType>>?
	val likes: Int?
	val comments: List<Comment>?
	val commentsCount: Int?
}

class EmptyPublicationInfo<T : Publication>(
	override val publication: T,
) : PublicationInfo<T> {
	override val loading: Boolean
		get() = true
	override val loaded: Boolean
		get() = false
	override val creators: List<Pair<PublicUser, CreationType>>?
		get() = null
	override val likes: Int?
		get() = null
	override val comments: List<Comment>?
		get() = null
	override val commentsCount: Int?
		get() = null
}

data class FullPublicationInfo<T : Publication>(
	override val publication: T,
	override val creators: List<Pair<PublicUser, CreationType>>?,
	override val likes: Int?,
	override val comments: List<Comment>?,
	override val commentsCount: Int?,
) : PublicationInfo<T> {
	override val loading: Boolean
		get() = false
	override val loaded: Boolean
		get() = true

	init {
		Log.i(javaClass.name, creators.toString())
	}
}
