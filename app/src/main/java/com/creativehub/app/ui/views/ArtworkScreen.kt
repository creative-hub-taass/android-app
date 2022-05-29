package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.toCurrencyString
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.rememberArtworkState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtworkScreen(id: String) {
	val userState = LocalUserState.current
	val user = userState.user
	val artworkState = rememberArtworkState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = artworkState.isLoading)
	SwipeRefresh(state = swipeRefreshState, onRefresh = {
		artworkState.refresh()
	}) {
		val artwork = artworkState.publication
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.background(MaterialTheme.colors.surface)
		) {
			CreatorsList(artworkState.creatorsInfo)
			if (artwork != null) {
				val images = artwork.images
				HorizontalPager(
					count = images.size,
					modifier = Modifier.fillMaxWidth(),
					key = { images[it] }
				) { page ->
					AsyncImage(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(min = 128.dp),
						model = ImageRequest.Builder(LocalContext.current)
							.data(images[page])
							.crossfade(true)
							.build(),
						placeholder = painterResource(R.drawable.placeholder),
						contentDescription = "image",
						contentScale = ContentScale.FillWidth
					)
				}
				SocialBar(info = artworkState.publicationInfo)
				Text(
					text = artwork.name.trim(),
					modifier = Modifier.padding(8.dp),
					fontWeight = FontWeight.Bold
				)
				Text(
					text = artwork.description,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.padding(8.dp),
					style = Typography.subtitle1
				)
				Row {
					val date = artwork.creationDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
						.toJavaLocalDateTime().format(
							DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
					Icon(
						imageVector = Icons.Rounded.CalendarToday,
						contentDescription = "Icon",
						modifier = Modifier.padding(8.dp),
						tint = Color.Gray
					)
					Text(
						text = date,
						modifier = Modifier.padding(0.dp, 8.dp),
						fontWeight = FontWeight.Bold
					)
				}
				if (artwork.onSale) {
					val price = artwork.price?.toCurrencyString(LocalContext.current.resources.configuration.locales[0])
					val currency = artwork.currency?.symbol
					Text(
						modifier = Modifier.padding(8.dp, 0.dp),
						text = "Price: $currency $price",
						fontWeight = FontWeight.Bold,
						style = Typography.subtitle1
					)
					Text(
						modifier = Modifier.padding(8.dp, 0.dp),
						text = LocalContext.current.resources.getQuantityString(R.plurals.available_copies,
																				artwork.availableCopies),
						fontWeight = FontWeight.Bold,
						style = Typography.subtitle1
					)
				}
				OutlinedButton(
					modifier = Modifier.align(Alignment.CenterHorizontally),
					onClick = { showComments = !showComments },
				) {
					Text(if (showComments) "Hide comments" else "Show comments")
				}
				AnimatedVisibility(showComments) {
					CommentsList(artworkState.commentInfos ?: emptyList())
				}
			}
		}
	}
}
