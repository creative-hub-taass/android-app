package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.rememberPostState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PostScreen(id: String) {
	val userState = LocalUserState.current
	val uriHandler = LocalUriHandler.current
	val user = userState.user
	val postState = rememberPostState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = postState.isLoading)
	SwipeRefresh(state = swipeRefreshState, onRefresh = {
		postState.refresh()
	}) {
		val post = postState.publication
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.background(MaterialTheme.colors.surface)
				.padding(bottom = 16.dp)
		) {
			CreatorsList(postState.creatorsInfo)
			if (post != null) {
				Text(
					text = post.title.trim(),
					modifier = Modifier.padding(8.dp),
					fontWeight = FontWeight.Bold,
					style = Typography.h6
				)
				MaterialRichText(
					modifier = Modifier.padding(8.dp),
				) {
					Markdown(content = post.body) {
						uriHandler.openUri(it)
					}
				}
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					val date = post.lastUpdate
						.toLocalDateTime(TimeZone.currentSystemDefault())
						.toJavaLocalDateTime()
						.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
					Icon(
						imageVector = Icons.Rounded.CalendarToday,
						contentDescription = "Icon",
						modifier = Modifier.padding(8.dp),
						tint = Color.Gray
					)
					Text(
						text = date
					)
				}
				SocialBar(info = postState.publicationInfo)
				AnimatedVisibility(
					visible = !postState.commentInfos.isNullOrEmpty(),
					modifier = Modifier.fillMaxWidth(),
				) {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						OutlinedButton(onClick = { showComments = !showComments }) {
							Text(if (showComments) "Hide comments" else "Show comments")
						}
						AnimatedVisibility(showComments) {
							CommentsList(postState.commentInfos ?: emptyList())
						}
					}
				}
			}
		}
	}
}
