package com.creativehub.app.model

import kotlinx.serialization.Serializable

enum class CreationType {
	AUTHOR, COAUTHOR, FEATURING, COLLAB,  // Artworks and Posts
	ORGANIZER, PARTICIPANT,  // Events
	OTHER
}

interface Creation {
	val id: String
	val user: String
	val creationType: CreationType
}

@Serializable
class ArtworkCreation(
	override val id: String,
	override val user: String,
	override val creationType: CreationType,
	val artworkId: String,
) : Creation

@Serializable
class EventCreation(
	override val id: String,
	override val user: String,
	override val creationType: CreationType,
	val eventId: String,
) : Creation

@Serializable
class PostCreation(
	override val id: String,
	override val user: String,
	override val creationType: CreationType,
	val postId: String,
) : Creation