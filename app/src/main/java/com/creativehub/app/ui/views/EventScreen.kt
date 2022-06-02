package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Place
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
import com.creativehub.app.R
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.toggleLike
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.MapElement
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.formatDates
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.creativehub.app.viewmodel.rememberEventState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.time.format.FormatStyle

@Composable
fun EventScreen(id: String) {
	val userState = LocalUserState.current
	val uriHandler = LocalUriHandler.current
	val user = userState.user
	val coroutineScope = rememberCoroutineScope()
	val eventState = rememberEventState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = eventState.isLoading)
	val event = eventState.publication
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		backgroundColor = MaterialTheme.colors.background,
		floatingActionButton = {
			val bookingURL = event?.bookingURL
			if (bookingURL != null) {
				ExtendedFloatingActionButton(
					onClick = { uriHandler.openUri(bookingURL) },
					backgroundColor = MaterialTheme.colors.primary,
					contentColor = MaterialTheme.colors.onPrimary,
					icon = {
						Icon(imageVector = Icons.Default.LocalActivity, contentDescription = null)
					},
					text = {
						Text(text = "TICKETS")
					}
				)
			}
		}
	) {
		SwipeRefresh(
			modifier = Modifier.fillMaxSize(),
			state = swipeRefreshState,
			onRefresh = {
				eventState.refresh()
			}
		) {
			val scrollState = rememberScrollState()
			Column(
				modifier = Modifier
					.fillMaxSize()
					.verticalScroll(scrollState)
					.padding(bottom = 72.dp)
			) {
				CreatorsList(eventState.creatorsInfo)
				Text(
					text = event?.name?.trim() ?: "",
					modifier = Modifier
						.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
						.placeholder(event == null, highlight = PlaceholderHighlight.shimmer())
						.fillMaxWidth(),
					style = Typography.h6
				)
				AsyncImage(
					modifier = Modifier.fillMaxWidth(),
					model = ImageRequest.Builder(LocalContext.current)
						.data(event?.image)
						.crossfade(true)
						.build(),
					placeholder = painterResource(R.drawable.placeholder),
					error = painterResource(R.drawable.placeholder),
					contentDescription = "image",
					contentScale = ContentScale.FillWidth,
				)
				AnimatedVisibility(event != null) {
					SocialBar(
						eventState.publicationInfo,
						onLikeClick = { info, user ->
							coroutineScope.launch {
								APIClient.toggleLike(info, user)
								eventState.refresh()
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
				if (event == null || event.description.isNotBlank()) {
					Text(
						text = event?.description ?: "",
						modifier = Modifier
							.padding(8.dp)
							.placeholder(event == null, highlight = PlaceholderHighlight.shimmer())
							.fillMaxWidth(),
						style = Typography.body1
					)
				}
				Row(
					modifier = Modifier.padding(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						imageVector = Icons.Rounded.CalendarToday,
						contentDescription = "Icon",
						modifier = Modifier
							.padding(end = 8.dp)
							.alpha(0.5f)
					)
					Text(
						text = event?.formatDates(R.string.from_to_dates, dateStyle = FormatStyle.MEDIUM) ?: "",
						modifier = Modifier
							.placeholder(event == null, highlight = PlaceholderHighlight.shimmer())
							.fillMaxWidth()
					)
				}
				Row(
					modifier = Modifier.padding(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						imageVector = Icons.Rounded.Place,
						contentDescription = "Icon",
						modifier = Modifier
							.padding(end = 8.dp)
							.alpha(0.5f)
					)
					Text(
						text = event?.locationName ?: "",
						modifier = Modifier
							.placeholder(event == null, highlight = PlaceholderHighlight.shimmer())
							.fillMaxWidth()
					)
				}
				Spacer(modifier = Modifier.height(8.dp))
				MapElement(
					events = event?.let { listOf(event) },
					modifier = Modifier
						.height(250.dp)
						.fillMaxWidth()
				)
				Spacer(modifier = Modifier.height(8.dp))
				AnimatedVisibility(
					visible = !eventState.commentInfos.isNullOrEmpty(),
					modifier = Modifier.fillMaxWidth(),
				) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						OutlinedButton(onClick = { showComments = !showComments }) {
							Text(if (showComments) "Hide comments" else "Show comments")
						}
						AnimatedVisibility(showComments) {
							CommentsList(eventState.commentInfos ?: emptyList())
						}
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun EventScreenPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		EventScreen("")
	}
}