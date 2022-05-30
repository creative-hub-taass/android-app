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
import androidx.compose.material.icons.rounded.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.formatDates
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.rememberEventState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.format.FormatStyle

@Composable
fun EventScreen(id: String) {
	val userState = LocalUserState.current
	val user = userState.user
	val eventState = rememberEventState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = eventState.isLoading)
	SwipeRefresh(state = swipeRefreshState, onRefresh = {
		eventState.refresh()
	}) {
		val event = eventState.publication
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.background(MaterialTheme.colors.background)
				.padding(bottom = 16.dp)
		) {
			CreatorsList(eventState.creatorsInfo)
			if (event != null) {
				Text(
					text = event.name.trim(),
					modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
					style = Typography.h6
				)
				AsyncImage(
					model = ImageRequest.Builder(LocalContext.current)
						.data(event.image)
						.crossfade(true)
						.build(),
					placeholder = painterResource(R.drawable.placeholder),
					error = painterResource(R.drawable.placeholder),
					contentDescription = "image",
					contentScale = ContentScale.FillWidth,
					modifier = Modifier.fillMaxWidth()
				)
				SocialBar(info = eventState.publicationInfo)
				if (event.description.isNotBlank()) {
					Text(
						text = event.description,
						modifier = Modifier.padding(8.dp),
						style = Typography.body1
					)
				}
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						imageVector = Icons.Rounded.CalendarToday,
						contentDescription = "Icon",
						modifier = Modifier
							.padding(8.dp)
							.alpha(0.5f)
					)
					Text(
						text = event.formatDates(R.string.from_to_dates, dateStyle = FormatStyle.MEDIUM)
					)
				}
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						imageVector = Icons.Rounded.Place,
						contentDescription = "Icon",
						modifier = Modifier
							.padding(8.dp)
							.alpha(0.5f)
					)
					Text(
						text = event.locationName
					)
				}
			}
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

