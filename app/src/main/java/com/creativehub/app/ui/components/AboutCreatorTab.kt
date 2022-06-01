package com.creativehub.app.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.ellipsize
import com.creativehub.app.viewmodel.CreatorState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.creativehub.app.viewmodel.rememberCreatorState
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutCreatorTab(creatorState: CreatorState) {
	val navigation = LocalNavigationState.current
	val indication = LocalIndication.current
	val uriHandler = LocalUriHandler.current
	val items = creatorState.posts.orEmpty()
	val bio = creatorState.creator?.creator?.bio
	Column(
		modifier = Modifier.padding(8.dp),
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top,
	) {
		if (!bio.isNullOrBlank()) {
			Box(modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 8.dp)) {
				Card(
					modifier = Modifier.fillMaxWidth(),
					elevation = 4.dp
				) {
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(16.dp)
					) {
						Text(
							text = "Biography",
							modifier = Modifier.padding(bottom = 4.dp),
							style = Typography.h6,
						)
						MaterialRichText {
							Markdown(content = bio) {
								uriHandler.openUri(it)
							}
						}
					}
				}
			}
		}
		Text(
			text = "Posts",
			modifier = Modifier.padding(8.dp),
			style = Typography.h6
		)
		for (post in items) {
			Box(modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 4.dp)) {
				Card(
					onClick = { navigation.navigate(Destination.Post.argRoute(post.id)) },
					modifier = Modifier.fillMaxWidth(),
					elevation = 4.dp,
					indication = indication
				) {
					val lastUpdate = post.lastUpdate.toLocalDateTime(TimeZone.currentSystemDefault())
						.toJavaLocalDateTime()
						.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(16.dp)
					) {
						Text(
							text = lastUpdate,
							modifier = Modifier
								.padding(bottom = 4.dp)
								.alpha(0.7f),
							style = Typography.caption,
						)
						Text(
							text = post.title.trim(),
							modifier = Modifier.padding(bottom = 4.dp),
							style = Typography.h6
						)
						Text(
							text = post.body.trim().ellipsize(300),
							style = Typography.body1,
						)
					}
				}
			}
		}
		if (!creatorState.isLoading && items.isEmpty()) {
			Text(
				text = "No posts yet",
				modifier = Modifier
					.padding(8.dp, 32.dp)
					.fillMaxWidth(),
				style = Typography.body1,
				textAlign = TextAlign.Center
			)
		}
	}
}

@Preview
@Composable
fun AboutCreatorTabPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		val creatorState = rememberCreatorState(id = "", user = null)
		AboutCreatorTab(creatorState)
	}
}
