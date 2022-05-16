package com.creativehub.app.util

import com.creativehub.app.model.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*

fun getPreviewArtwork(): PublicationInfo<Artwork> {
	val id = UUID.randomUUID().toString()
	val user = getPreviewCreator()
	val artwork = Artwork(
		id = id,
		timestamp = Clock.System.now(),
		lastUpdate = Clock.System.now(),
		creations = listOf(ArtworkCreation(
			id = UUID.randomUUID().toString(),
			user = user.id,
			creationType = CreationType.AUTHOR,
			artworkId = id
		)),
		creationDateTime = Instant.parse("1927-01-01T00:00:00+00:00"),
		name = "Early Canvas Model B3 \"Wassily\" Chair, Black Eisengarn",
		description = "Chromium-plated tubular steel and original Eisengarn canvas",
		type = "Design/Decorative Art",
		copies = 1,
		attributes = mapOf(
			"size" to "71.8 × 78.1 × 71.1 cm"
		),
		images = listOf("https://d7hftxdivxxvm.cloudfront.net/?resize_to=fit&width=653&height=800&quality=80&src=https%3A%2F%2Fd32dm0rphc51dk.cloudfront.net%2Fd-WaAo2rvGJ-Jmd--7Gk4g%2Fnormalized.jpg"),
		onSale = true,
		price = 30000.0,
		currency = Currency.getInstance("USD"),
		paymentEmail = "pay@creativehub.com",
		availableCopies = 1
	)
	val creators = listOf(user)
	val likes = 15
	val comments = listOf(
		Comment(UUID.randomUUID().toString(), UUID.randomUUID().toString(), id, "Test comment"),
		Comment(UUID.randomUUID().toString(), UUID.randomUUID().toString(), id, "Lorem ipsum dolor sit amet."),
		Comment(UUID.randomUUID().toString(), UUID.randomUUID().toString(), id, "Test unicode \uD83D\uDC4D")
	)
	val commentsCount = comments.size
	return PublicationInfo(artwork, creators, likes, false, comments, commentsCount)
}

fun getPreviewCreator() = PublicUser(
	id = UUID.randomUUID().toString(),
	username = "marcel-breuer",
	nickname = "Marcel Breuer",
	creator = PublicCreator(
		id = UUID.randomUUID().toString(),
		bio = "Marcel Breuer holds a legacy as an accomplished architect, furniture designer, and master of Modernism who pioneered the design of tubular steel furniture. During the 1920s, Breuer studied and taught at the Bauhaus in Germany, where his curriculum was equally focused on visual art as it was concerned with technology and industrial production. During this time, Breuer became acquainted with modern architects Walter Gropius, Le Corbusier, and Mies van der Rohe, all of whom would heavily influence his work. By 1935, Breuer had established a reputation as a sought-after designer, known for his steel furniture and the Wassily chair, so named after its production due to an anecdotal connection to Kandinsky (who had admired the design and commissioned a duplicate for his home). Upon WWII, Breuer followed Gropius to London, moved on to teach architecture at Harvard University, and later established his own New York-based firm and designed the Whitney Museum.",
		creatorType = "ARTIST",
		avatar = "https://d32dm0rphc51dk.cloudfront.net/0HJBjhMKIe9zGYEALt-22Q/large.jpg"
	),
	inspirerIds = listOf(),
	fanIds = listOf()
)