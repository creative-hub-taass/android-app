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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.ui.components.CommentsList
import com.creativehub.app.ui.components.SocialBar
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.rememberEventState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun EventScreen(id: String) {
	val userState = LocalUserState.current
	val user = userState.user
	val eventState = rememberEventState(id, user)
	var showComments by rememberSaveable { mutableStateOf(false) }
	val event = eventState.publication
	if (event == null) {
		CircularProgressIndicator(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
				.wrapContentWidth(Alignment.CenterHorizontally)
		)
	} else {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
		) {
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
						text = event.name.trim(),
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
					AsyncImage(
						model = ImageRequest.Builder(LocalContext.current)
							.data(event.image)
							.crossfade(true)
							.build(),
						placeholder = painterResource(R.drawable.placeholder),
						contentDescription = "image",
						contentScale = ContentScale.Crop,
						modifier = Modifier.align(Alignment.CenterHorizontally)
					)
					Row(
						modifier = Modifier
							.horizontalScroll(rememberScrollState())
							.padding(top = 5.dp)
					) {
						eventState.creatorsInfo?.forEach { user ->
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
					Text(
						text = event.description,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(5.dp),
						style = Typography.subtitle1
					)
				}
			}
			Card(
				border = BorderStroke(2.dp, Color.Black),
				backgroundColor = Color.LightGray,
				modifier = Modifier.padding(5.dp)) {
				Box {
					Column {
						Row {
							Column {
								Row(modifier = Modifier
									.padding(10.dp)
									.fillMaxWidth()) {
									val dateStart = event.startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
										.toJavaLocalDateTime().format(
											DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
									val dateEnd = event.endDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
										.toJavaLocalDateTime().format(
											DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
									Icon(
										imageVector = Icons.Rounded.CalendarToday,
										contentDescription = "Icon",
										modifier = Modifier.padding(5.dp),
										tint = Color.Gray
									)

									Text(
										text = "Start: ${dateStart} - End: ${dateEnd}",
										modifier = Modifier.padding(8.dp),
										fontWeight = FontWeight.Bold
									)
								}
								Text(
									text = "Location: ${event.locationName}",
									modifier = Modifier.padding(8.dp),
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
			}
			SocialBar(info = eventState.publicationInfo)
			OutlinedButton(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				onClick = { showComments = !showComments },
			) {
				Text("Show comments")
			}
			AnimatedVisibility(showComments) {
				CommentsList(eventState.commentInfos ?: emptyList())
			}
		}
	}
}

