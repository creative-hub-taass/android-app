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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.getPreviewArtwork
import com.creativehub.app.viewmodel.FeedStateViewModel
import com.creativehub.app.viewmodel.LocalFeedState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArtworkFeedElement(info: PublicationInfo<Artwork>, navController: NavController) {
	val indication = LocalIndication.current
	val context = LocalContext.current
	val feed = LocalFeedState.current
	val artwork = info.publication
	if (!info.loaded) {
		LaunchedEffect(Unit) {
			val index = feed.feed.indexOf(info)
			feed.feed[index] = feed.fetchPublicationInfo(info, false)
		}
	}
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(2.dp, 4.dp)) {
		Card(
			onClick = { navController.navigate(Destination.Artwork.argRoute(artwork.id)) },
			modifier = Modifier.fillMaxWidth(),
			elevation = 4.dp,
			indication = indication
		) {
			val date = artwork.creationDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).year
			val creators = info.creators?.joinToString { it.first.nickname } ?: ""
//			val price = when {
//				artwork.price != null && artwork.currency != null -> when {
//					artwork.availableCopies > 0 -> "${artwork.currency.symbol} ${"%,.2f".format(artwork.price)}"
//					else -> "Sold"
//				}
//				else -> "Not for sale"
//			}
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
							.placeholder(info.loading, highlight = PlaceholderHighlight.shimmer()),
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
		ArtworkFeedElement(getPreviewArtwork(), rememberNavController())
	}
}
