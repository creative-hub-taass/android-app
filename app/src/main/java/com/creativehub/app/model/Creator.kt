package com.creativehub.app.model

import java.util.*

open class PublicCreator(
	open val id: UUID,
	open val bio: String,
	open val creatorType: String,
	open val avatar: String,
)

class Creator(
	override val id: UUID,
	val name: String,
	val surname: String,
	val birthDate: Date,
	override val bio: String,
	override val creatorType: String,
	override val avatar: String,
	val paymentEmail: String,
) : PublicCreator(id, bio, creatorType, avatar)
