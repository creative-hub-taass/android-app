package com.creativehub.app.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.toggleLike
import com.creativehub.app.model.Event
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.formatDates
import com.creativehub.app.util.getPreviewEvent
import com.creativehub.app.viewmodel.FeedStateViewModel
import com.creativehub.app.viewmodel.LocalFeedState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventFeedElement(info: PublicationInfo<Event>) {
	val event = info.publication
	val indication = LocalIndication.current
	val navigation = LocalNavigationState.current
	val context = LocalContext.current
	val feedState = LocalFeedState.current
	val coroutineScope = rememberCoroutineScope()
	val dates = event.formatDates(R.string.interval_dates)
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(2.dp, 4.dp)) {
		Card(
			onClick = { navigation.navigate(Destination.Event.argRoute(event.id)) },
			modifier = Modifier.fillMaxWidth(),
			elevation = 4.dp,
			indication = indication
		) {
			Column {
				CreatorsBar(info)
				ConstraintLayout(Modifier.fillMaxWidth()) {
					val (overlay) = createRefs()
					AsyncImage(
						model = ImageRequest.Builder(context)
							.data(event.image)
							.crossfade(true)
							.build(),
						modifier = Modifier.fillMaxWidth(),
						placeholder = painterResource(R.drawable.placeholder),
						contentDescription = "image",
						contentScale = ContentScale.FillWidth,
					)
					Column(
						horizontalAlignment = Alignment.Start,
						verticalArrangement = Arrangement.SpaceEvenly,
						modifier = Modifier
							.background(Color.Black.copy(0.5f))
							.padding(8.dp)
							.fillMaxWidth()
							.constrainAs(overlay) {
								bottom.linkTo(parent.bottom)
								start.linkTo(parent.start)
								end.linkTo(parent.end)
							}
					) {
						Text(
							text = event.locationName,
							modifier = Modifier
								.fillMaxWidth(),
							style = Typography.subtitle1,
							color = Color.White
						)
						Text(
							text = dates,
							modifier = Modifier.fillMaxWidth(),
							style = Typography.h6,
							color = Color.White
						)
					}
				}
				SocialBar(
					info,
					onLikeClick = { info, user ->
						coroutineScope.launch {
							APIClient.toggleLike(info, user)
							feedState.refresh()
						}
					},
					onCommentClick = {
						navigation.navigate(Destination.Event.argRoute(event.id))
					}
				)
				Text(
					text = event.name.trim(),
					modifier = Modifier.padding(8.dp),
					style = Typography.h6
				)
			}
		}
	}
}

@Preview
@Composable
fun EventFeedElementPreview() {
	CompositionLocalProvider(
		LocalFeedState provides FeedStateViewModel(),
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		EventFeedElement(getPreviewEvent())
	}
}
