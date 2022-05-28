package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.creativehub.app.model.CommentInfo
import com.creativehub.app.ui.theme.Typography

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