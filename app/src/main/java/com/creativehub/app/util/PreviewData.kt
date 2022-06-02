package com.creativehub.app.util

import com.creativehub.app.model.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*

fun getPreviewArtwork(): PublicationInfo<Artwork> {
	val id = UUID.randomUUID().toString()
	val creations = getPreviewCreations()
	val artwork = Artwork(
		id = id,
		timestamp = Clock.System.now(),
		lastUpdate = Clock.System.now(),
		creations = creations.map {
			ArtworkCreation(
				id = UUID.randomUUID().toString(),
				user = it.first.id,
				creationType = it.second,
				artworkId = id
			)
		},
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
	val creators = creations.map { it.first }
	val likes = 15
	val comments = getPreviewComments(id)
	val commentsCount = comments.size
	return PublicationInfo(artwork, creators, likes, false, comments, commentsCount)
}

fun getPreviewEvent(): PublicationInfo<Event> {
	val id = UUID.randomUUID().toString()
	val creations = getPreviewCreations()
	val event = Event(
		id = id,
		timestamp = Clock.System.now(),
		lastUpdate = Clock.System.now(),
		creations = creations.map {
			EventCreation(
				id = UUID.randomUUID().toString(),
				user = it.first.id,
				creationType = it.second,
				eventId = id
			)
		},
		startDateTime = Instant.parse("2022-05-05T12:00:00+00:00"),
		endDateTime = Instant.parse("2022-05-28T12:00:00+00:00"),
		name = "Eric Zener - Rising",
		description = "Chromium-plated tubular steel and original Eisengarn canvas",
		image = "https://d32dm0rphc51dk.cloudfront.net/MzKCuTuN7-2HyebSnZcw4g/large.jpg",
		locationName = "Gallery Henoch, New York",
		coordinates = Event.Coordinates(40.750118, -74.005277),
		bookingURL = "https://www.artsy.net/show/gallery-henoch-eric-zener-rising",
	)
	val creators = creations.map { it.first }
	val likes = 15
	val comments = getPreviewComments(id)
	val commentsCount = comments.size
	return PublicationInfo(event, creators, likes, false, comments, commentsCount)
}

fun getPreviewPost(): PublicationInfo<Post> {
	val id = UUID.randomUUID().toString()
	val creations = getPreviewCreations()
	val post = Post(
		id = id,
		timestamp = Clock.System.now(),
		lastUpdate = Clock.System.now(),
		creations = creations.map {
			PostCreation(
				id = UUID.randomUUID().toString(),
				user = it.first.id,
				creationType = it.second,
				postId = id
			)
		},
		title = "Lorem ipsum dolor sit amet",
		body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sagittis ipsum ultricies dolor mollis, id interdum lorem euismod. Morbi sit amet urna felis. Donec rhoncus tempor ligula vel tincidunt. Vestibulum congue urna a est tempor semper vel quis diam. Nullam vitae est at risus placerat porta at dictum turpis. Morbi volutpat sem ante, non ornare massa tincidunt nec. Phasellus egestas neque et justo venenatis, id varius turpis consequat. Nullam interdum porttitor dapibus. Donec congue ante et risus hendrerit tincidunt.\nQuisque semper tempor arcu, nec dictum neque egestas vel. Sed ultricies massa nisl, et porta mi ultricies vel. In elementum diam porttitor tincidunt gravida. Ut nisl metus, porttitor nec lorem in, porta aliquam tellus. Suspendisse ipsum felis, porttitor sit amet orci eu, dictum pretium dolor. Donec euismod, nisi eget mattis venenatis, diam elit placerat nulla, eu hendrerit felis nisi id risus. Quisque mollis ut lacus vitae dapibus. Vivamus sed elit arcu. Aliquam erat volutpat. Maecenas pulvinar feugiat eros at tristique. Aliquam auctor iaculis facilisis. Nulla facilisi. Quisque bibendum enim in sapien sodales lobortis. Nullam sagittis ligula id risus posuere ultrices. Curabitur molestie cursus justo, non condimentum mauris bibendum in. Pellentesque molestie, ex ut ornare viverra, est felis venenatis ex, sit amet tempor justo ex at nunc.\nDonec accumsan orci quis tristique finibus. Nullam quis sagittis mi. Suspendisse id tortor at lorem efficitur vehicula. Vivamus est arcu, posuere quis pellentesque non, placerat eget ligula. Mauris ut venenatis augue. Fusce tincidunt id sem eu porta. Proin sodales sapien eget vulputate euismod. Praesent tincidunt urna pulvinar, auctor diam quis, sagittis tortor. Ut dignissim iaculis vestibulum. Praesent id tortor tempor, lacinia nisl nec, dignissim leo. Suspendisse potenti. Etiam facilisis, metus nec consequat pulvinar, orci nunc sollicitudin lectus, eu vestibulum eros urna vitae nunc. Fusce in pharetra felis. Etiam vel arcu et est vestibulum finibus. Fusce risus lectus, blandit ut lectus vel, volutpat sodales nibh.",
	)
	val creators = creations.map { it.first }
	val likes = 15
	val comments = getPreviewComments(id)
	val commentsCount = comments.size
	return PublicationInfo(post, creators, likes, false, comments, commentsCount)
}

fun getPreviewComments(publicationId: String) = listOf(
	Comment(
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		publicationId,
		"Test comment",
		Clock.System.now()
	),
	Comment(
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		publicationId,
		"Lorem ipsum dolor sit amet.",
		Clock.System.now()
	),
	Comment(
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		publicationId,
		"Test unicode \uD83D\uDC4D",
		Clock.System.now()
	)
)

fun getPreviewCreations(): List<Pair<PublicUser, CreationType>> = listOf(
	getPreviewCreator() to CreationType.AUTHOR,
	getPreviewCreator() to CreationType.COAUTHOR
)

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