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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.model.Post
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.ellipsize
import com.creativehub.app.util.getPreviewPost
import com.creativehub.app.viewmodel.FeedStateViewModel
import com.creativehub.app.viewmodel.LocalFeedState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostFeedElement(info: PublicationInfo<Post>) {
	val indication = LocalIndication.current
	val navigation = LocalNavigationState.current
	val post = info.publication
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(2.dp, 4.dp)) {
		Card(
			onClick = { navigation.navigate(Destination.Post.argRoute(post.id)) },
			modifier = Modifier.fillMaxWidth(),
			elevation = 4.dp,
			indication = indication
		) {
			val date = post.lastUpdate.toLocalDateTime(TimeZone.currentSystemDefault()).year
			Column(Modifier.padding(bottom = 8.dp)) {
				CreatorsBar(info)
				Text(
					text = "${post.title.trim()}, $date",
					modifier = Modifier.padding(8.dp),
					style = Typography.h6
				)
				Text(
					text = post.body.trim().ellipsize(400),
					modifier = Modifier.padding(8.dp),
					style = Typography.body1,
				)
				SocialBar(info)
			}
		}
	}
}

@Preview
@Composable
fun PostFeedElementPreview() {
	CompositionLocalProvider(
		LocalFeedState provides FeedStateViewModel(),
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		PostFeedElement(getPreviewPost())
	}
}
