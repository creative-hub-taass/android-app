package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getUserLikedPublication
import com.creativehub.app.model.PublicUser
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalArtworkState
import com.creativehub.app.viewmodel.LocalUserState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ArtworkScreen(id: String) {
	val userState = LocalUserState.current
	val user = userState.user
	val artworkService = LocalArtworkState.current
	var liked by rememberSaveable { mutableStateOf(false) }
	var showComments by rememberSaveable { mutableStateOf(false) }
	val artwork = artworkService.artwork

	LaunchedEffect(Unit) {
		artworkService.fetchArtwork(id)
		if (user != null) {
			liked = APIClient.getUserLikedPublication(id, user.id).getOrDefault(false)
		}
	}

	if (artwork == null) {
		CircularProgressIndicator(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
				.wrapContentWidth(Alignment.CenterHorizontally)
		)
	} else {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
		) {
			Card(
				border = BorderStroke(2.dp, Color.Black),
				backgroundColor = Color.LightGray,
				modifier = Modifier.padding(5.dp)) {
				Column(
					modifier = Modifier
						.padding(16.dp)
						.fillMaxWidth()
				) {
					Text(
						text = artwork.name.trim(),
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
					Row(
						modifier = Modifier
							.horizontalScroll(rememberScrollState())
							.padding(10.dp)
							.align(Alignment.CenterHorizontally)
							.fillMaxWidth()
					) {
						artworkService.listImages.forEach { image ->
							AsyncImage(
								model = ImageRequest.Builder(LocalContext.current)
									.data(image)
									.crossfade(true)
									.build(),
								placeholder = painterResource(R.drawable.placeholder),
								contentDescription = "image",
								contentScale = ContentScale.Crop
							)
						}
					}
					Row(
						modifier = Modifier
							.horizontalScroll(rememberScrollState())
							.padding(top = 5.dp)
					) {
						artworkService.listUser.forEach { user ->
							Text(
								text = user.first.nickname,
								fontWeight = FontWeight.Bold,
								modifier = Modifier.padding(10.dp),
								style = Typography.subtitle1
							)

							Text(
								text = user.second.name,
								modifier = Modifier.padding(10.dp),
								fontStyle = FontStyle.Italic,
								style = Typography.subtitle1
							)
						}
					}
					Row {
						val date = artwork.creationDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
							.toJavaLocalDateTime().format(
								DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
						Icon(
							imageVector = Icons.Rounded.CalendarToday,
							contentDescription = "Icon",
							modifier = Modifier.padding(5.dp),
							tint = Color.Gray
						)

						Text(
							text = date,
							modifier = Modifier.padding(8.dp),
							fontWeight = FontWeight.Bold
						)
					}
					Text(
						text = artwork.description,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(5.dp),
						style = Typography.subtitle1
					)
					Row(
						Modifier
							.fillMaxWidth()
							.horizontalScroll(rememberScrollState())
					) {
						if (artwork.onSale) {
							Text(
								text = "On sale: ${artwork.price} ${artwork.currency} - ",
								fontWeight = FontWeight.Bold,
								style = Typography.subtitle1
							)
							Text(
								text = "${artwork.availableCopies} available copies",
								fontWeight = FontWeight.Bold,
								style = Typography.subtitle1
							)
						}
					}
				}
			}

			val listPublicUser = mutableListOf<PublicUser>()
			artworkService.listUser.forEach { pair ->
				listPublicUser.add(pair.first)
			}
			val tmpPublicationInfo = PublicationInfo(artwork,
													 listPublicUser.toList(),
													 artworkService.countLikes.value,
													 liked,
													 artworkService.listComments,
													 artworkService.listComments.size,
													 null)
			SocialBar(info = tmpPublicationInfo)
			OutlinedButton(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				onClick = { showComments = !showComments },
			) {
				Text("Show comments")
			}
			AnimatedVisibility(showComments) {
				CommentsList(listCommentInfo = artworkService.listCommentsUser)
			}
		}
	}
}