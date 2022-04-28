package com.creativehub.app.model

import java.util.*

enum class Role {
	USER, ADMIN
}

open class PublicUser(
	open val id: UUID,
	open val username: String,
	open val nickname: String,
	open val creator: PublicCreator,
	open val inspirerIds: Set<UUID>,
	open val fanIds: Set<UUID>,
)

class User(
	override val id: UUID,
	override val username: String,
	override val nickname: String,
	val email: String,
	val role: Role,
	override val creator: Creator,
	override val inspirerIds: Set<UUID>,
	override val fanIds: Set<UUID>,
) : PublicUser(id, username, nickname, creator, inspirerIds, fanIds)

