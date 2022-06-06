package com.creativehub.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.CreatorState
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.creativehub.app.viewmodel.rememberCreatorState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.coroutines.launch

@Composable
fun CreatorPageHeader(creatorState: CreatorState) {
	val context = LocalContext.current
	val userState = LocalUserState.current
	val navigation = LocalNavigationState.current
	val scope = rememberCoroutineScope()
	val creatorUser = creatorState.creator
	val creator = creatorUser?.creator
	ConstraintLayout(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
	) {
		val (left, center, right) = createRefs()
		Column(
			modifier = Modifier
				.constrainAs(left) {
					start.linkTo(parent.start)
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
				},
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start
		) {
			AsyncImage(
				model = ImageRequest.Builder(context)
					.data(creator?.avatar)
					.crossfade(true)
					.build(),
				contentDescription = "profile picture",
				modifier = Modifier
					.padding(bottom = 8.dp)
					.size(72.dp)
					.border(1.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
					.clip(CircleShape)
					.placeholder(creatorUser == null, highlight = PlaceholderHighlight.shimmer()),
				contentScale = ContentScale.Crop,
				error = painterResource(R.drawable.placeholder),
				placeholder = painterResource(R.drawable.placeholder),
			)
			Text(
				text = creatorUser?.nickname ?: "",
				modifier = Modifier
					.widthIn(min = 128.dp)
					.placeholder(creatorUser == null, highlight = PlaceholderHighlight.shimmer())
					.wrapContentWidth(Alignment.Start),
				style = Typography.subtitle1,
				fontWeight = FontWeight.Bold
			)
			Text(
				text = "@${creatorUser?.username}",
				modifier = Modifier
					.padding(top = 2.dp)
					.widthIn(min = 128.dp)
					.placeholder(creatorUser == null, highlight = PlaceholderHighlight.shimmer())
					.wrapContentWidth(Alignment.Start),
				style = Typography.body2,
			)
		}
		Column(
			modifier = Modifier
				.fillMaxHeight()
				.constrainAs(center) {
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
					start.linkTo(left.end, 8.dp)
					end.linkTo(right.start, 8.dp)
				},
			verticalArrangement = Arrangement.SpaceAround,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Column(
				modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Text(
					text = "Fans",
					style = Typography.body2,
				)
				Text(
					text = creatorUser?.fanIds?.size?.toString() ?: "",
					modifier = Modifier
						.padding(8.dp, 0.dp)
						.width(72.dp)
						.align(Alignment.CenterHorizontally)
						.placeholder(creatorUser == null, highlight = PlaceholderHighlight.shimmer())
						.wrapContentWidth(Alignment.CenterHorizontally),
					style = Typography.h6,
					fontWeight = FontWeight.Bold
				)
			}
			Column(
				modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Inspirers",
					style = Typography.body2,
				)
				Text(
					text = creatorUser?.inspirerIds?.size?.toString() ?: "",
					modifier = Modifier
						.padding(8.dp, 0.dp)
						.width(72.dp)
						.placeholder(creatorUser == null, highlight = PlaceholderHighlight.shimmer())
						.wrapContentWidth(Alignment.CenterHorizontally),
					style = Typography.h6,
					fontWeight = FontWeight.Bold
				)
			}
		}
		Column(
			modifier = Modifier
				.constrainAs(right) {
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
					end.linkTo(parent.end)
				},
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if (creatorState.isFollowed == true) {
				OutlinedButton(
					onClick = {
						scope.launch {
							userState.unfollow(creatorState.id)
						}
					}
				) {
					Text("Unfollow")
				}
			} else {
				Button(
					onClick = {
						if (userState.isLoggedIn) {
							scope.launch {
								userState.follow(creatorState.id)
							}
						} else {
							navigation.navigate(Destination.Login.route)
						}
					}
				) {
					Text("Follow")
				}
			}
		}
	}
}

@Preview
@Composable
fun CreatorPageHeaderPreview() {
	CompositionLocalProvider(
		LocalUserState provides UserStateViewModel(),
		LocalNavigationState provides rememberNavController()
	) {
		val creatorState = rememberCreatorState(id = "", user = null)
		CreatorPageHeader(creatorState)
	}
}
