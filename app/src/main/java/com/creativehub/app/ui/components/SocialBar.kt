package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.creativehub.app.model.PublicationInfo
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.getPreviewArtwork

@Composable
fun SocialBar(info: PublicationInfo<*>) {
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
	ConstraintLayout(Modifier.fillMaxWidth()) {
		val (likeBtn, commentBtn, shareBtn, likesText, commentsText) = createRefs()
		IconButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(likeBtn) {
			start.linkTo(parent.start)
			top.linkTo(parent.top)
		}) {
			Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "like")
		}
		IconButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(commentBtn) {
			start.linkTo(likeBtn.end)
			top.linkTo(parent.top)
		}) {
			Icon(imageVector = Icons.Default.Chat, contentDescription = "comment")
		}
		IconButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(shareBtn) {
			end.linkTo(parent.end)
			top.linkTo(parent.top)
		}) {
			Icon(imageVector = Icons.Default.Share, contentDescription = "share")
		}
		Text(
			text = likeText,
			style = Typography.subtitle2,
			modifier = Modifier
				.alpha(0.66f)
				.constrainAs(likesText) {
					start.linkTo(parent.start, 8.dp)
					top.linkTo(likeBtn.bottom)
				}
		)
		Text(
			text = commentText,
			style = Typography.subtitle2,
			modifier = Modifier
				.alpha(0.66f)
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