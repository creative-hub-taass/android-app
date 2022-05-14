package com.creativehub.app.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.getPreviewArtwork
import com.creativehub.app.viewmodel.FeedStateViewModel
import com.creativehub.app.viewmodel.LocalFeedState
import com.creativehub.app.viewmodel.LocalUserState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArtworkFeedElement(info: PublicationInfo<Artwork>) {
	val indication = LocalIndication.current
	val context = LocalContext.current
	val feed = LocalFeedState.current
	val user = LocalUserState.current
	val navigation = LocalNavigationState.current
	val artwork = info.publication
	if (!info.loaded) {
		LaunchedEffect(Unit) {
			feed.fetchPublicationInfo(info, user.user?.id)
		}
	}
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(2.dp, 4.dp)) {
		Card(
			onClick = { navigation.navigate(Destination.Artwork.argRoute(artwork.id)) },
			modifier = Modifier.fillMaxWidth(),
			elevation = 4.dp,
			indication = indication
		) {
			val date = artwork.creationDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).year
			val creators = info.creators?.joinToString { it.first.nickname } ?: ""
			Column {
				AsyncImage(
					model = ImageRequest.Builder(context)
						.data(artwork.images.first())
						.crossfade(true)
						.build(),
					modifier = Modifier.fillMaxWidth(),
					placeholder = painterResource(R.drawable.placeholder),
					contentDescription = "image",
					contentScale = ContentScale.FillWidth,
				)
				SocialBar(info)
				Column(Modifier.padding(8.dp)) {
					Text(
						text = "${artwork.name.trim()}, $date",
						style = Typography.subtitle1
					)
					Spacer(modifier = Modifier.height(4.dp))
					Text(
						text = creators,
						modifier = Modifier
							.fillMaxWidth()
							.placeholder(info.creators == null, highlight = PlaceholderHighlight.shimmer()),
						style = Typography.body1,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun ArtworkFeedElementPreview() {
	CompositionLocalProvider(LocalFeedState provides FeedStateViewModel()) {
		ArtworkFeedElement(getPreviewArtwork())
	}
}
