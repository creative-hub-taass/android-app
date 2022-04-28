package com.creativehub.app.model

import java.util.*

enum class CreationType {
	AUTHOR, COAUTHOR, FEATURING, COLLAB,  // Artworks and Posts
	ORGANIZER, PARTICIPANT,  // Events
	OTHER
}

open class Creation(
	open val id: UUID,
	open val user: UUID,
	open val creationType: CreationType,
)

class ArtworkCreation(
	override val id: UUID,
	override val user: UUID,
	override val creationType: CreationType,
	val artworkId: UUID,
) : Creation(id, user, creationType)

class EventCreation(
	override val id: UUID,
	override val user: UUID,
	override val creationType: CreationType,
	val eventId: UUID,
) : Creation(id, user, creationType)

class PostCreation(
	override val id: UUID,
	override val user: UUID,
	override val creationType: CreationType,
	val postId: UUID,
) : Creation(id, user, creationType)