package com.creativehub.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.model.CreationType
import com.creativehub.app.model.PublicUser
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Shapes
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.getPreviewCreations
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun CreatorsList(creators: List<Pair<PublicUser, CreationType?>>?) {
	val context = LocalContext.current
	val navigation = LocalNavigationState.current
	val scrollState = rememberScrollState()
	Column(
		modifier = Modifier
			.padding(8.dp)
			.heightIn(min = 32.dp)
			.fillMaxWidth()
			.placeholder(
				visible = creators == null,
				highlight = PlaceholderHighlight.shimmer()
			)
			.horizontalScroll(scrollState),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	) {
		creators?.forEach { (user, type) ->
			val creator = user.creator
			Row(
				modifier = Modifier
					.padding(bottom = 4.dp)
					.then(if (creator != null) {
						Modifier.clickable {
							navigation.navigate(Destination.Creator.argRoute(user.id))
						}
					} else Modifier),
				horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.CenterVertically
			) {
				if (creator != null) {
					AsyncImage(
						model = ImageRequest.Builder(context)
							.data(creator.avatar)
							.crossfade(true)
							.build(),
						contentDescription = "profile picture",
						modifier = Modifier
							.padding(end = 8.dp)
							.size(32.dp)
							.border(1.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
							.clip(CircleShape),
						contentScale = ContentScale.Crop
					)
				}
				Column(
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.Start
				) {
					Text(
						text = user.nickname,
						style = Typography.body1,
						fontWeight = FontWeight.Bold
					)
					if (creator != null) {
						Text(
							text = "@${user.username}",
							style = Typography.caption,
						)
					}
				}
				if (type != null) {
					Text(
						modifier = Modifier
							.padding(start = 8.dp, top = 1.dp)
							.alpha(0.7f)
							.border(1.dp, MaterialTheme.colors.onBackground, Shapes.small)
							.padding(4.dp, 2.dp),
						text = type.name.toUpperCase(LocaleList.current),
						style = Typography.caption,
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun CreatorsListPreview() {
	CompositionLocalProvider(LocalNavigationState provides rememberNavController()) {
		CreatorsList(getPreviewCreations())
	}
}