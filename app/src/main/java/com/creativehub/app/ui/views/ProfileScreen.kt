package com.creativehub.app.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.viewmodel.LocalUserState
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ProfileScreen() {
	val vm = LocalUserState.current
	val navigation = LocalNavigationState.current
	Column(Modifier
			   .fillMaxSize()
			   .verticalScroll(rememberScrollState())
			   .padding(15.dp)
	) {
		if (vm.isBusy) {
			CircularProgressIndicator(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
					.wrapContentWidth(Alignment.CenterHorizontally)
			)
		} else if(vm.isLoggedIn){
			AsyncImage(
				model = ImageRequest.Builder(LocalContext.current)
					.data(vm.user?.creator?.avatar)
					.crossfade(true)
					.build(),
				contentDescription = "image",
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.size(200.dp)
					.clip(CircleShape),
			error = painterResource(R.drawable.placeholder))

			Divider()
			Row(modifier = Modifier.align(Alignment.End)) {
				AnimatedVisibility(vm.user?.creator != null) {
					Button(onClick = {
						navigation.navigate(Destination.Creator.argRoute(vm.user!!.id))
					}) {
						Text(text = "Creator page")
					}
				}
			}
			Card(border = BorderStroke(4.dp, Color.Gray),
				 backgroundColor = Color.White,
				 modifier = Modifier
					 .padding(5.dp)
					 .align(Alignment.CenterHorizontally)
					 .fillMaxWidth()) {
			Text(
				text = "Username: ${vm.user?.username}",
				modifier = Modifier.padding(10.dp),
				fontWeight = FontWeight.Bold
			)
			}
			Card(border = BorderStroke(4.dp, Color.Gray),
				 backgroundColor = Color.White,
				 modifier = Modifier
					 .padding(5.dp)
					 .align(Alignment.CenterHorizontally)
					 .fillMaxWidth()) {
				Text(
					text = "Nickname: ${vm.user?.nickname}",
					modifier = Modifier.padding(10.dp),
					fontWeight = FontWeight.Bold
				)
			}
			Card(border = BorderStroke(4.dp, Color.Gray),
				 backgroundColor = Color.White,
				 modifier = Modifier
					 .padding(5.dp)
					 .align(Alignment.CenterHorizontally)
					 .fillMaxWidth()) {
				Text(
					text = "E-mail: ${vm.user?.email}",
					modifier = Modifier.padding(10.dp),
					fontWeight = FontWeight.Bold
				)
			}
			if (vm.user?.creator != null) {
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .align(Alignment.CenterHorizontally)
						 .fillMaxWidth()) {
					Text(
						text = "Avatar: ${vm.user?.creator!!.avatar}",
						modifier = Modifier.padding(10.dp),
						fontWeight = FontWeight.Bold
					)
				}
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .align(Alignment.CenterHorizontally)
						 .fillMaxWidth()) {
					Row(modifier = Modifier
						.padding(10.dp)
						.fillMaxWidth()) {
						val date = vm.user!!.creator?.birthDate?.toJavaLocalDate()
							?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

						Icon(
							imageVector = Icons.Rounded.CalendarToday,
							contentDescription = "Icon",
							modifier = Modifier.padding(5.dp),
							tint = Color.Gray
						)

						Text(
							text = "Birth date: $date",
							modifier = Modifier.padding(8.dp),
							fontWeight = FontWeight.Bold
						)
					}
				}
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .align(Alignment.CenterHorizontally)
						 .fillMaxWidth()) {
					Text(
						text = "Name: ${vm.user!!.creator?.name}",
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
				}
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .fillMaxWidth()) {
					Text(
						text = "Surname: ${vm.user!!.creator?.surname}",
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
				}
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .align(Alignment.CenterHorizontally)
						 .fillMaxWidth()) {
					Text(
						text = "Creator Type: ${vm.user!!.creator?.creatorType}",
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
				}
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .fillMaxWidth()) {
					Text(
						text = "Biography: ${vm.user!!.creator?.bio}",
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
				}
				Card(border = BorderStroke(4.dp, Color.Gray),
					 backgroundColor = Color.White,
					 modifier = Modifier
						 .padding(5.dp)
						 .align(Alignment.CenterHorizontally)
						 .fillMaxWidth()) {
					Text(
						text = "Payment e-mail: ${vm.user!!.creator?.paymentEmail}",
						modifier = Modifier.padding(8.dp),
						fontWeight = FontWeight.Bold
					)
				}
			}
		} else {
			navigation.navigate(Destination.Login.route)
		}
	}
}