package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.LocalAtm
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.BuildConfig
import com.creativehub.app.R
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.toggleLike
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.toCurrencyString
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.creativehub.app.viewmodel.rememberArtworkState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtworkScreen(id: String) {
	val context = LocalContext.current
	val uriHandler = LocalUriHandler.current
	val userState = LocalUserState.current
	val user = userState.user
	val coroutineScope = rememberCoroutineScope()
	val artworkState = rememberArtworkState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = artworkState.isLoading)
	val artwork = artworkState.publication
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		backgroundColor = MaterialTheme.colors.background,
		floatingActionButton = {
			if (artwork?.onSale == true) {
				val buyURL = "${BuildConfig.CLIENT_URL}/artwork/${artwork.id}"
				ExtendedFloatingActionButton(
					onClick = {
						uriHandler.openUri(buyURL)
					},
					backgroundColor = MaterialTheme.colors.primary,
					contentColor = MaterialTheme.colors.onPrimary,
					icon = {
						Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
					},
					text = {
						Text(text = "BUY THIS ARTWORK")
					}
				)
			}
		}
	) {
		SwipeRefresh(state = swipeRefreshState, onRefresh = {
			artworkState.refresh()
		}) {
			val scrollState = rememberScrollState()
			Column(
				modifier = Modifier
					.fillMaxSize()
					.verticalScroll(scrollState)
					.background(MaterialTheme.colors.background)
					.padding(bottom = 72.dp)
			) {
				CreatorsList(artworkState.creatorsInfo)
				Text(
					text = artwork?.name?.trim() ?: "",
					modifier = Modifier
						.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
						.placeholder(artwork == null, highlight = PlaceholderHighlight.shimmer())
						.fillMaxWidth(),
					style = Typography.h6
				)
				val images = artwork?.images ?: listOf(null)
				HorizontalPager(
					count = images.size,
					modifier = Modifier.fillMaxWidth(),
					key = { images[it] ?: "" }
				) { page ->
					AsyncImage(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(min = 128.dp),
						model = ImageRequest.Builder(context)
							.data(images[page])
							.crossfade(true)
							.build(),
						placeholder = painterResource(R.drawable.placeholder),
						error = painterResource(R.drawable.placeholder),
						contentDescription = "image",
						contentScale = ContentScale.FillWidth
					)
				}
				AnimatedVisibility(artwork != null) {
					SocialBar(
						artworkState.publicationInfo,
						onLikeClick = { info, user ->
							coroutineScope.launch {
								APIClient.toggleLike(info, user)
								artworkState.refresh()
							}
						},
						onCommentClick = {
							showComments = true
							coroutineScope.launch {
								scrollState.animateScrollTo(Int.MAX_VALUE)
							}
						}
					)
					Spacer(modifier = Modifier.height(8.dp))
				}
				if (artwork == null || artwork.description.isNotBlank()) {
					Text(
						text = artwork?.description ?: "",
						modifier = Modifier
							.padding(8.dp)
							.placeholder(artwork == null, highlight = PlaceholderHighlight.shimmer())
							.fillMaxWidth(),
						style = Typography.body1
					)
				}
				Row(
					modifier = Modifier.padding(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					val date = artwork?.creationDateTime
						?.toLocalDateTime(TimeZone.currentSystemDefault())
						?.toJavaLocalDateTime()
						?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
					Icon(
						imageVector = Icons.Rounded.CalendarToday,
						contentDescription = "Icon",
						modifier = Modifier
							.padding(end = 8.dp)
							.alpha(0.5f)
					)
					Text(
						text = date ?: "",
						modifier = Modifier
							.placeholder(artwork == null, highlight = PlaceholderHighlight.shimmer())
							.fillMaxWidth()
					)
				}
				if (artwork?.onSale == true) {
					Row(
						modifier = Modifier.padding(8.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						val price = artwork.price?.toCurrencyString(artwork.currency)
						Icon(
							imageVector = Icons.Rounded.LocalAtm,
							contentDescription = "Icon",
							modifier = Modifier
								.padding(end = 8.dp)
								.alpha(0.5f)
						)
						Text(
							text = "Price: $price"
						)
					}
					Row(
						modifier = Modifier.padding(8.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							imageVector = Icons.Rounded.Sell,
							contentDescription = "Icon",
							modifier = Modifier
								.padding(end = 8.dp)
								.alpha(0.5f)
						)
						Text(
							text = context.resources.getQuantityString(R.plurals.available_copies,
																	   artwork.availableCopies),
						)
					}
				}
				Spacer(modifier = Modifier.height(8.dp))
				AnimatedVisibility(
					visible = !artworkState.commentInfos.isNullOrEmpty(),
					modifier = Modifier.fillMaxWidth(),
				) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						OutlinedButton(onClick = { showComments = !showComments }) {
							Text(if (showComments) "Hide comments" else "Show comments")
						}
						AnimatedVisibility(showComments) {
							CommentsList(artworkState.commentInfos ?: emptyList())
						}
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun ArtworkScreenPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		ArtworkScreen("")
	}
}
