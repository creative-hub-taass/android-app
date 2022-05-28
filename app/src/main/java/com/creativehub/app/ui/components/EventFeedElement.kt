package com.creativehub.app.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventFeedElement(info: PublicationInfo<Event>) {
	val event = info.publication
	val indication = LocalIndication.current
	val navigation = LocalNavigationState.current
	val context = LocalContext.current
	val locale = context.resources.configuration.locales[0]
	val dates = event.formatDates(locale)
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
				SocialBar(info)
				Text(
					text = event.name.trim(),
					modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
					style = Typography.h6
				)
				Text(
					text = dates,
					modifier = Modifier
						.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
						.alpha(0.7f),
					style = Typography.h6,
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
