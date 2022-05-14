package com.creativehub.app.ui.components

import androidx.compose.runtime.Composable
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.Event
import com.creativehub.app.model.Post
import com.creativehub.app.model.PublicationInfo

@Suppress("UNCHECKED_CAST")
@Composable
fun FeedElement(publicationInfo: PublicationInfo<*>) {
	when (publicationInfo.publication) {
		is Artwork -> ArtworkFeedElement(publicationInfo as PublicationInfo<Artwork>)
		is Event -> {}
		is Post -> {}
	}
}
