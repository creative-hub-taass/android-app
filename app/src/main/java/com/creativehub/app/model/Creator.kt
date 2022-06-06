package com.creativehub.app.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

sealed interface IPublicCreator {
	val bio: String
	val creatorType: String
	val avatar: String
}

@Serializable
class PublicCreator(
	override val bio: String,
	override val creatorType: String,
	override val avatar: String,
) : IPublicCreator

@Serializable
class Creator(
	val name: String,
	val surname: String,
	val birthDate: LocalDate,
	override val bio: String,
	override val creatorType: String,
	override val avatar: String,
	val paymentEmail: String,
) : IPublicCreator
