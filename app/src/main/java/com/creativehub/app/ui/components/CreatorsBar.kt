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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.getPreviewArtwork

@Composable
fun CreatorsBar(info: PublicationInfo<*>) {
	val creators = info.creators.orEmpty()
	val context = LocalContext.current
	val navigation = LocalNavigationState.current
	val scrollState = rememberScrollState()
	Row(
		modifier = Modifier
			.padding(8.dp)
			.horizontalScroll(scrollState),
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically
	) {
		Box {
			for ((i, creator) in creators.withIndex()) {
				AsyncImage(
					model = ImageRequest.Builder(context)
						.data(creator.creator?.avatar)
						.crossfade(true)
						.build(),
					contentDescription = "profile picture",
					modifier = Modifier
						.padding(start = (i * 20).dp)
						.zIndex((creators.size - i).toFloat())
						.size(33.dp)
						.border(1.dp, MaterialTheme.colors.onSurface, CircleShape)
						.clip(CircleShape)
						.clickable { navigation.navigate(Destination.Creator.argRoute(creator.id)) },
					contentScale = ContentScale.Crop
				)
			}
		}
		for ((i, creator) in creators.withIndex()) {
			Text(
				text = creator.nickname,
				modifier = Modifier
					.padding(start = 4.dp)
					.clickable { navigation.navigate(Destination.Creator.argRoute(creator.id)) },
				style = Typography.body1,
				fontWeight = FontWeight.Bold
			)
			if (i < creators.size - 1) {
				Text(text = ",", style = Typography.body1, fontWeight = FontWeight.Bold)
			}
		}
	}
}

@Preview
@Composable
fun CreatorsBarPreview() {
	CompositionLocalProvider(LocalNavigationState provides rememberNavController()) {
		CreatorsBar(getPreviewArtwork())
	}
}