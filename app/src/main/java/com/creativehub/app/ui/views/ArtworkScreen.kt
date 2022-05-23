package com.creativehub.app.ui.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.creativehub.app.viewmodel.LocalArtworkState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.creativehub.app.R
import com.creativehub.app.model.Artwork
import com.creativehub.app.ui.theme.Typography
import com.google.accompanist.placeholder.placeholder
import androidx.compose.ui.graphics.Color.Companion.Gray as Gray1

@Composable
fun ArtworkScreen(id: String) {

	val artworkService = LocalArtworkState.current

	LaunchedEffect(Unit, block = {
		artworkService.fetchArtwork(id)
	})


	Column(
		Modifier
			.padding(16.dp)
			.fillMaxWidth()) {
		val date = artworkService.artwork?.creationDateTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.year
		Text(
			text = "${artworkService.artwork?.name?.trim()}, $date",
			modifier = Modifier.padding(8.dp),
			fontWeight = FontWeight.Bold
		)
		LazyRow(
			modifier = Modifier.fillMaxWidth(),
			contentPadding = PaddingValues(2.dp)
		) {
			items(artworkService.listImages.size) { index ->
				if (artworkService.listImages[index].isNotEmpty()) {
					AsyncImage(
						model = ImageRequest.Builder(LocalContext.current)
							.data(artworkService.listImages[index])
							.crossfade(true)
							.build(),
						placeholder = painterResource(R.drawable.placeholder),
						contentDescription = "image",
						contentScale = ContentScale.Crop,
						modifier = Modifier.fillParentMaxSize()
					)
				}
			}
		}
		LazyRow(contentPadding = PaddingValues(4.dp),
				horizontalArrangement = Arrangement.spacedBy(4.dp),
				) {
			items(artworkService.listUser.size) { index ->
				if (artworkService.listUser[index].first.id.isNotEmpty()) {
					Text(
						text = artworkService.listUser[index].first.nickname,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(top = 1000.dp),
						style = Typography.subtitle1
					)
				}
				if (artworkService.listUser[index].second.name.isNotEmpty()) {
					Text(
						text = artworkService.listUser[index].second.name,
						modifier = Modifier.padding(10.dp),
						fontStyle = FontStyle.Italic,
						style = Typography.subtitle1
					)
				}
			}
		}
	}
}