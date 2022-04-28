package com.creativehub.app.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

interface IPublicCreator {
	val id: String
	val bio: String
	val creatorType: String
	val avatar: String
}

@Serializable
open class PublicCreator(
	override val id: String,
	override val bio: String,
	override val creatorType: String,
	override val avatar: String,
) : IPublicCreator

@Serializable
class Creator(
	override val id: String,
	val name: String,
	val surname: String,
	val birthDate: LocalDate,
	override val bio: String,
	override val creatorType: String,
	override val avatar: String,
	val paymentEmail: String,
) : IPublicCreator
