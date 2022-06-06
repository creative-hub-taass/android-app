package com.creativehub.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getCreators
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.Event
import com.creativehub.app.model.Post
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.util.rememberState
import com.creativehub.app.viewmodel.CreatorState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.creativehub.app.viewmodel.rememberCreatorState
import kotlin.math.ceil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollabsCreatorTab(creatorState: CreatorState) {
	val context = LocalContext.current
	val navigation = LocalNavigationState.current
	val configuration = LocalConfiguration.current
	val artworks = creatorState.artworks?.filter { it.creations.size > 1 }.orEmpty()
	val events = creatorState.events?.filter { it.creations.size > 1 }.orEmpty()
	val items = (artworks + events).sortedBy {
		when (it) {
			is Artwork -> it.creationDateTime
			is Event -> it.startDateTime
			is Post -> it.lastUpdate
		}
	}
	val artistIds = items.flatMap { it.creations }.map { it.user }.toSet().toList()
	val allCreators by rememberState(artistIds) { list ->
		APIClient.getCreators(list).getOrNull()?.associateBy { it?.id }
	}
	var width = configuration.screenWidthDp.toDouble()
	val height = when (items.size) {
		0 -> width
		else -> ceil(1 + (items.size) / 2.0) * (width / 2.0)
	}
	LazyVerticalGrid(
		modifier = Modifier
			.fillMaxWidth()
			.onGloballyPositioned { coordinates ->
				width = coordinates.size.toSize().width.toDouble()
			}
			.height(height.dp),
		cells = GridCells.Fixed(2),
		horizontalArrangement = Arrangement.Start,
		verticalArrangement = Arrangement.Top,
	) {
		items(items) { publication ->
			val creators = publication.creations.map { allCreators?.get(it.user) }
			val image = when (publication) {
				is Artwork -> publication.images.first()
				is Event -> publication.image
				else -> null
			}
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(1f),
				contentAlignment = Alignment.TopStart
			) {
				AsyncImage(
					model = ImageRequest.Builder(context)
						.data(image)
						.crossfade(true)
						.build(),
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(1f)
						.padding(1.dp)
						.clickable { navigation.navigate(Destination.Artwork.argRoute(publication.id)) },
					error = painterResource(R.drawable.placeholder),
					placeholder = painterResource(R.drawable.placeholder),
					contentDescription = "image",
					contentScale = ContentScale.Crop,
				)
				Box(
					modifier = Modifier.padding(8.dp)
				) {
					for ((i, creator) in creators.withIndex()) {
						AsyncImage(
							model = ImageRequest.Builder(context)
								.data(creator?.creator?.avatar)
								.crossfade(true)
								.build(),
							contentDescription = "profile picture",
							modifier = Modifier
								.padding(start = (i * 20).dp)
								.zIndex((creators.size - i).toFloat())
								.size(34.dp)
								.border(2.dp, Color.White, CircleShape)
								.clip(CircleShape)
								.then(if (creator != null) {
									Modifier.clickable {
										navigation.navigate(Destination.Creator.argRoute(creator.id))
									}
								} else Modifier),
							contentScale = ContentScale.Crop,
							error = painterResource(R.drawable.placeholder),
							placeholder = painterResource(R.drawable.placeholder),
						)
					}
				}
			}
		}
		if (!creatorState.isLoading && creatorState.artworks == null) {
			item(span = { GridItemSpan(3) }) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.height(height.dp),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(text = "No events yet")
				}
			}
		}
	}
}

@Preview
@Composable
fun CollabsCreatorTabPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		val creatorState = rememberCreatorState(id = "", user = null)
		CollabsCreatorTab(creatorState)
	}
}