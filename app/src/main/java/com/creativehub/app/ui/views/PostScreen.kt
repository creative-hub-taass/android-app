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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getUserLikedPublication
import com.creativehub.app.model.PublicUser
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalPostState
import com.creativehub.app.viewmodel.LocalUserState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Composable
fun PostScreen(id: String) {
	val vm = LocalUserState.current
	val user = vm.user
	val postService = LocalPostState.current
	val post = postService.post
	var liked by rememberSaveable { mutableStateOf(false) }
	var showComments by rememberSaveable { mutableStateOf(false) }

	LaunchedEffect(Unit) {
		postService.fetchPost(id)
		if (user != null) {
			liked = APIClient.getUserLikedPublication(id, user.id).getOrDefault(false)
		}
	}

	if (post == null) {
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
				Column(Modifier
						   .padding(16.dp)
						   .fillMaxWidth()) {
					Text(
						text = post.title,
						modifier = Modifier
							.padding(8.dp)
							.align(Alignment.CenterHorizontally),
						fontWeight = FontWeight.ExtraBold,
						fontStyle = FontStyle.Italic
					)
					Text(
						text = post.body,
						modifier = Modifier
							.padding(10.dp)
							.align(Alignment.Start),
						fontWeight = FontWeight.Medium
					)
				}
			}
			Card(
				border = BorderStroke(2.dp, Color.Black),
				backgroundColor = Color.LightGray,
				modifier = Modifier.padding(5.dp)
			) {
				Column {
					Row(modifier = Modifier
						.horizontalScroll(rememberScrollState())
						.fillMaxWidth()
						.padding(top = 5.dp)) {
						postService.listUser.forEach { user ->
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
					Row(
						modifier = Modifier.padding(10.dp)
					) {
						val date = post.lastUpdate.toLocalDateTime(TimeZone.currentSystemDefault())
							.toJavaLocalDateTime().format(
								DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
						Icon(
							imageVector = Icons.Rounded.CalendarToday,
							contentDescription = "Icon",
							modifier = Modifier.padding(5.dp),
							tint = Color.Gray
						)
						Text(
							text = "Last update: $date",
							modifier = Modifier.padding(8.dp),
							fontWeight = FontWeight.Bold
						)
					}
				}
			}


			val listPublicUser = mutableListOf<PublicUser>()
			postService.listUser.forEach { pair ->
				listPublicUser.add(pair.first)
			}
			val tmpPublicationInfo = PublicationInfo(post,
													 listPublicUser.toList(),
													 postService.countLikes.value,
													 liked,
													 postService.listComments,
													 postService.listComments.size,
													 null)
			SocialBar(info = tmpPublicationInfo)
			OutlinedButton(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				onClick = { showComments = !showComments },
			) {
				Text("Show comments")
			}
			AnimatedVisibility(showComments) {
				CommentsList(listCommentInfo = postService.listCommentsUser)
			}
		}
	}
}