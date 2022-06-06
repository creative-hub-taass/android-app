package com.creativehub.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Shapes
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.formatDates
import com.creativehub.app.viewmodel.CreatorState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.creativehub.app.viewmodel.rememberCreatorState
import java.time.format.FormatStyle
import kotlin.math.ceil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsCreatorTab(creatorState: CreatorState) {
	val context = LocalContext.current
	val navigation = LocalNavigationState.current
	val configuration = LocalConfiguration.current
	val items = creatorState.events ?: emptyList()
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
		items(items) {
			val dates = it.formatDates(R.string.interval_dates, FormatStyle.SHORT, null)
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(1f),
				contentAlignment = Alignment.BottomEnd
			) {
				AsyncImage(
					model = ImageRequest.Builder(context)
						.data(it.image)
						.crossfade(true)
						.build(),
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(1f)
						.padding(1.dp)
						.clickable { navigation.navigate(Destination.Artwork.argRoute(it.id)) },
					error = painterResource(R.drawable.placeholder),
					placeholder = painterResource(R.drawable.placeholder),
					contentDescription = "image",
					contentScale = ContentScale.Crop,
				)
				Text(
					modifier = Modifier
						.padding(8.dp)
						.clip(Shapes.small)
						.background(Color.White)
						.padding(6.dp, 2.dp),
					text = dates,
					color = Color.Black,
					style = Typography.body1,
					fontWeight = FontWeight.Bold
				)
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
fun EventsCreatorTabPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		val creatorState = rememberCreatorState(id = "", user = null)
		EventsCreatorTab(creatorState)
	}
}