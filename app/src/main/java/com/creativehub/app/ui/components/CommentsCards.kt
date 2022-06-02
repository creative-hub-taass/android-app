package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.creativehub.app.model.CommentInfo
import com.creativehub.app.ui.theme.Typography
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CommentsList(listCommentInfo: List<CommentInfo>) {
	Column(
		modifier = Modifier.padding(8.dp)
	) {
		listCommentInfo.forEach { info ->
			Card(
				modifier = Modifier.padding(8.dp),
			) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 8.dp),
					verticalArrangement = Arrangement.Top,
					horizontalAlignment = Alignment.Start,
				) {
					CreatorsList(listOf(Pair(info.user, null)))
					Text(
						modifier = Modifier.padding(8.dp, 0.dp),
						text = info.comment.message,
						style = Typography.subtitle1
					)
					Text(
						text = info.comment.timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
							.toJavaLocalDateTime()
							.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)),
						modifier = Modifier
							.padding(8.dp, 0.dp)
							.alpha(0.7f)
							.fillMaxWidth(),
						style = Typography.caption,
						textAlign = TextAlign.End
					)
				}
			}
		}
	}
}