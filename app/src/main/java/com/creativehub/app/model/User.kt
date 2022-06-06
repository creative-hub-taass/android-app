package com.creativehub.app.model

import kotlinx.serialization.Serializable

enum class Role {
	USER, ADMIN
}

sealed interface IPublicUser {
	val id: String
	val username: String
	val nickname: String
	val creator: IPublicCreator?
	val inspirerIds: List<String>
	val fanIds: List<String>
}

@Serializable
class PublicUser(
	override val id: String,
	override val username: String,
	override val nickname: String,
	override val creator: PublicCreator?,
	override val inspirerIds: List<String>,
	override val fanIds: List<String>,
) : IPublicUser

@Serializable
class User(
	override val id: String,
	override val username: String,
	override val nickname: String,
	val email: String,
	val role: Role,
	override val creator: Creator?,
	override val inspirerIds: List<String>,
	override val fanIds: List<String>,
) : IPublicUser

