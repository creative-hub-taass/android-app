package com.creativehub.app.model

import kotlinx.serialization.Serializable

enum class Role {
	USER, ADMIN
}

interface IPublicUser {
	val id: String
	val username: String
	val nickname: String
	val creator: IPublicCreator?
	val inspirerIds: Set<String>
	val fanIds: Set<String>
}

@Serializable
open class PublicUser(
	override val id: String,
	override val username: String,
	override val nickname: String,
	override val creator: PublicCreator,
	override val inspirerIds: Set<String>,
	override val fanIds: Set<String>,
) : IPublicUser

@Serializable
class User(
	override val id: String,
	override val username: String,
	override val nickname: String,
	val email: String,
	val role: Role,
	override val creator: Creator?,
	override val inspirerIds: Set<String>,
	override val fanIds: Set<String>,
) : IPublicUser

