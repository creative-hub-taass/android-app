package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
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
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.viewmodel.LocalPostState
import com.creativehub.app.viewmodel.LocalUserState


@Composable
fun PostScreen(id: String) {
	val userState = LocalUserState.current
	val user = userState.user
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
		Column(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
		) {
			CreatorsList(postService.listUser)
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
			val tmpPublicationInfo = PublicationInfo(post,
													 postService.listUser.map { it.first },
													 postService.countLikes,
													 liked,
													 postService.listComments,
													 postService.listComments.size,
													 null)
			SocialBar(info = tmpPublicationInfo)
			OutlinedButton(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				onClick = { showComments = !showComments },
			) {
				Text(if (showComments) "Hide comments" else "Show comments")
			}
			AnimatedVisibility(showComments) {
				CommentsList(listCommentInfo = postService.listCommentsUser)
			}
		}
	}
}