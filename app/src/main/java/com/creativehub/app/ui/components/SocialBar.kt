package com.creativehub.app.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.creativehub.app.model.Artwork
import com.creativehub.app.model.Event
import com.creativehub.app.model.Post
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.getPreviewArtwork
import com.creativehub.app.viewmodel.LocalFeedState
import com.creativehub.app.viewmodel.LocalUserState
import kotlinx.coroutines.launch

@Composable
fun SocialBar(info: PublicationInfo<*>) {
	val userState = LocalUserState.current
	val feedState = LocalFeedState.current
	val navigation = LocalNavigationState.current
	val coroutineScope = rememberCoroutineScope()
	val user = userState.user
	val destination = when (info.publication) {
		is Artwork -> Destination.Artwork
		is Event -> Destination.Event
		is Post -> Destination.Post
	}
	val shareIntent = Intent.createChooser(Intent().apply {
		action = Intent.ACTION_SEND
		putExtra(Intent.EXTRA_TEXT, info.publication.url)
		type = "text/plain"
	}, "Share this ${destination.label.lowercase()}")
	val context = LocalContext.current
	val likeText = when (val likes = info.likes) {
		null, 0 -> "Be the first to like"
		1 -> "Liked by $likes person"
		else -> "Liked by $likes people"
	}
	val commentText = when (val commentsCount = info.commentsCount) {
		null, 0 -> "No comments"
		1 -> "$commentsCount comment"
		else -> "$commentsCount comments"
	}
	val userLiked = info.userLiked
	val likeIcon = if (userLiked == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder
	val commentDestination = destination.argRoute(info.publication.id)
	ConstraintLayout(Modifier.fillMaxWidth()) {
		val (likeBtn, commentBtn, shareBtn, likesText, commentsText) = createRefs()
		IconButton(onClick = {
			if (user != null) {
				coroutineScope.launch {
					feedState.toggleLike(info, user.id)
				}
			} else navigation.navigate(Destination.Login.route)
		}, modifier = Modifier.constrainAs(likeBtn) {
			start.linkTo(parent.start)
			top.linkTo(parent.top)
		}) {
			Icon(likeIcon, contentDescription = "like")
		}
		IconButton(onClick = {
			if (userState.isLoggedIn) {
				navigation.navigate(commentDestination)
			} else navigation.navigate(Destination.Login.route)
		}, modifier = Modifier.constrainAs(commentBtn) {
			start.linkTo(likeBtn.end)
			top.linkTo(parent.top)
		}) {
			Icon(imageVector = Icons.Default.Chat, contentDescription = "comment")
		}
		IconButton(onClick = {
			context.startActivity(shareIntent)
		}, modifier = Modifier.constrainAs(shareBtn) {
			end.linkTo(parent.end)
			top.linkTo(parent.top)
		}) {
			Icon(imageVector = Icons.Default.Share, contentDescription = "share")
		}
		Text(
			text = likeText,
			style = Typography.subtitle2,
			modifier = Modifier
				.alpha(0.7f)
				.constrainAs(likesText) {
					start.linkTo(parent.start, 8.dp)
					top.linkTo(likeBtn.bottom)
				}
		)
		Text(
			text = commentText,
			style = Typography.subtitle2,
			modifier = Modifier
				.alpha(0.7f)
				.constrainAs(commentsText) {
					end.linkTo(parent.end, 8.dp)
					top.linkTo(likesText.top)
				}
		)
	}
}

@Preview
@Composable
fun SocialBarPreview() {
	SocialBar(getPreviewArtwork())
}