package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.toggleLike
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.CreatorsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.rememberPostState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import kotlinx.coroutines.launch
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
	val coroutineScope = rememberCoroutineScope()
	val postState = rememberPostState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = postState.isLoading)
	SwipeRefresh(state = swipeRefreshState, onRefresh = {
		postState.refresh()
	}) {
		val post = postState.publication
		val scrollState = rememberScrollState()
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(scrollState)
				.background(MaterialTheme.colors.background)
				.padding(bottom = 72.dp)
		) {
			CreatorsList(postState.creatorsInfo)
			Text(
				text = post?.title?.trim() ?: "",
				modifier = Modifier
					.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
					.placeholder(post == null, highlight = PlaceholderHighlight.shimmer())
					.fillMaxWidth(),
				fontWeight = FontWeight.Bold,
				style = Typography.h6
			)
			val date = post?.lastUpdate
				?.toLocalDateTime(TimeZone.currentSystemDefault())
				?.toJavaLocalDateTime()
				?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))
			Text(
				text = date ?: "",
				modifier = Modifier
					.padding(8.dp, 0.dp)
					.placeholder(post == null, highlight = PlaceholderHighlight.shimmer())
					.fillMaxWidth(),
				style = Typography.caption
			)
			MaterialRichText(
				modifier = Modifier
					.padding(8.dp, 4.dp)
					.placeholder(post == null, highlight = PlaceholderHighlight.shimmer())
					.fillMaxWidth(),
			) {
				Markdown(content = post?.body ?: "") {
					uriHandler.openUri(it)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			AnimatedVisibility(post != null) {
				SocialBar(
					postState.publicationInfo,
					onLikeClick = { info, user ->
						coroutineScope.launch {
							APIClient.toggleLike(info, user)
							postState.refresh()
						}
					},
					onCommentClick = {
						showComments = true
						coroutineScope.launch {
							scrollState.animateScrollTo(Int.MAX_VALUE)
						}
					}
				)
				Spacer(modifier = Modifier.height(8.dp))
			}
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
