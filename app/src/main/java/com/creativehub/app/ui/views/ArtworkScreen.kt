package com.creativehub.app.ui.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.viewmodel.LocalArtworkState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.creativehub.app.R
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getUserLikedPublication
import com.creativehub.app.model.*
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState


class Click(bool: Boolean){
	var showComments by mutableStateOf(bool)
}

@Composable
fun ArtworkScreen(id: String) {
	val vm = LocalUserState.current
	val artworkService = LocalArtworkState.current
	var liked: Boolean? = false
	var authorFollowed: Boolean? = false
	val click = remember {
		Click(false)
	}


	LaunchedEffect(Unit, block = {
		artworkService.fetchArtwork(id)
		if (vm.user != null) {
			liked = APIClient.getUserLikedPublication(id, vm.user!!.id).getOrNull()
			vm.user!!.inspirerIds.forEach { inspirer ->
				authorFollowed = (inspirer.compareTo(artworkService.listUser.first().first.id) == 0)
			}
		}
	})

	if (artworkService.artwork == null) {
		CircularProgressIndicator(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
				.wrapContentWidth(Alignment.CenterHorizontally)
		)
	} else {
		Column(Modifier
				   .padding(16.dp)
				   .fillMaxWidth()
				   .verticalScroll(rememberScrollState())) {
			Card(
				border = BorderStroke(2.dp, Color.Black),
				backgroundColor = Color.LightGray,
				modifier = Modifier.padding(5.dp)) {
				Column(
					Modifier
						.padding(16.dp)
						.fillMaxWidth()) {
					Text(
						text = "${artworkService.artwork?.name?.trim()}",
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
					LazyRow(
						modifier = Modifier.fillMaxWidth(),
						contentPadding = PaddingValues(2.dp)
					) {
						items(artworkService.listImages.size) { index ->
							if (artworkService.listImages[index].isNotEmpty()) {
								AsyncImage(
									model = ImageRequest.Builder(LocalContext.current)
										.data(artworkService.listImages[index])
										.crossfade(true)
										.build(),
									placeholder = painterResource(R.drawable.placeholder),
									contentDescription = "image",
									contentScale = ContentScale.Crop,
									modifier = Modifier.fillParentMaxSize()
								)
							}
						}
					}
					Row {
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
						val date =
							artworkService.artwork?.creationDateTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
						Icon(
							imageVector = Icons.Rounded.CalendarToday,
							contentDescription = "Icon",
							modifier = Modifier.padding(5.dp),
							tint = Color.Gray
						)

						Text(
							text = "$date",
							modifier = Modifier.padding(8.dp),
							fontWeight = FontWeight.Bold
						)
					}
					Text(
						text = "${artworkService.artwork?.description}",
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(10.dp),
						style = Typography.subtitle1
					)
					Row(
						Modifier
							.fillMaxWidth()
							.horizontalScroll(rememberScrollState())
					) {
						if (artworkService.artwork?.onSale == true) {
							Text(
								text = "On sale: ${artworkService.artwork!!.price} ${artworkService.artwork!!.currency} - ",
								fontWeight = FontWeight.Bold,
								style = Typography.subtitle1
							)
							Text(
								text = "${artworkService.artwork!!.availableCopies} available copies",
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
			if (artworkService.artwork != null) {
				val tmpPublicationInfo = PublicationInfo(artworkService.artwork!!,
														 listPublicUser.toList(),
														 artworkService.countLikes.value,
														 liked,
														 artworkService.listComments,
														 artworkService.listComments.size,
														 authorFollowed)
				SocialBar(info = tmpPublicationInfo)

				ClickableText(
					text = AnnotatedString("show comments"),
					modifier = Modifier.align(Alignment.CenterHorizontally),
					onClick = {
						click.showComments = click.showComments.not()
					})
			}
			if(click.showComments){
				CommentsList(listCommentInfo = artworkService.listCommentsUser)
			}
		}
	}
}

@Composable
fun CommentsList(listCommentInfo: SnapshotStateList<CommentInfo>) {
	Column(
		Modifier.padding(10.dp)
	){
		listCommentInfo.forEach { comment ->
			Card(
				backgroundColor = Color.LightGray,
				modifier = Modifier
					.padding(7.dp)
			) {
				Column(Modifier
						   .fillMaxWidth()
						   .padding(15.dp)){
					Text(
						text = "@${comment.user.nickname}",
						fontWeight = FontWeight.Bold,
						style = Typography.subtitle1
					)
					Text(
						modifier = Modifier.padding(10.dp),
						text = comment.comment.message,
						style = Typography.subtitle1
					)
				}
			}
		}
	}
}