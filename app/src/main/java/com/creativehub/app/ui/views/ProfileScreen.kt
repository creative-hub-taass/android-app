package com.creativehub.app.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creativehub.app.BuildConfig
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ProfileScreen() {
	val userState = LocalUserState.current
	val navigation = LocalNavigationState.current
	val uriHandler = LocalUriHandler.current
	val user = userState.user
	val creator = user?.creator
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		floatingActionButton = {
			if (user != null) {
				if (creator != null) {
					ExtendedFloatingActionButton(
						text = {
							Text(text = "PUBLIC PAGE")
						},
						icon = {
							Icon(Icons.Default.Launch, contentDescription = null)
						},
						onClick = {
							navigation.navigate(Destination.Creator.argRoute(user.id))
						},
						backgroundColor = MaterialTheme.colors.primary,
						contentColor = MaterialTheme.colors.onPrimary,
					)
				} else {
					ExtendedFloatingActionButton(
						text = {
							Text(text = "UPGRADE")
						},
						icon = {
							Icon(Icons.Default.OpenInBrowser, contentDescription = null)
						},
						onClick = {
							uriHandler.openUri("${BuildConfig.CLIENT_URL}/upgrade-request")
						},
						backgroundColor = MaterialTheme.colors.primary,
						contentColor = MaterialTheme.colors.onPrimary,
					)
				}
			}
		}
	) {
		if (userState.isBusy) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator()
			}
		} else if (user == null) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center,
			) {
				Card {
					Column(
						modifier = Modifier.padding(16.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Center
					) {
						Text(
							text = "Login or register to view your profile",
							modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
							style = Typography.body1
						)
						Button(
							onClick = { navigation.navigate(Destination.Login.route) }
						) {
							Text(text = "LOGIN")
						}
					}
				}
			}
		} else {
			Column(Modifier
					   .fillMaxSize()
					   .verticalScroll(rememberScrollState())
					   .padding(bottom = 72.dp)
			) {
				Card(
					modifier = Modifier
						.padding(8.dp)
						.fillMaxWidth(),
				) {
					Row(
						modifier = Modifier
							.padding(16.dp, 12.dp)
							.height(IntrinsicSize.Min),
						horizontalArrangement = Arrangement.Start,
						verticalAlignment = Alignment.CenterVertically,
					) {
						if (creator != null) {
							AsyncImage(
								model = ImageRequest.Builder(LocalContext.current)
									.data(creator.avatar)
									.crossfade(true)
									.build(),
								contentDescription = "image",
								contentScale = ContentScale.Crop,
								modifier = Modifier
									.padding(end = 16.dp)
									.size(96.dp)
									.border(1.dp, MaterialTheme.colors.onSurface, CircleShape)
									.clip(CircleShape),
								error = painterResource(R.drawable.placeholder),
								placeholder = painterResource(R.drawable.placeholder),
							)
						}
						Column(
							modifier = Modifier.fillMaxHeight(),
							horizontalAlignment = Alignment.Start,
							verticalArrangement = Arrangement.Center,
						) {
							Text(
								text = user.nickname,
								style = Typography.h6,
							)
							if (creator != null) {
								Text(
									text = "@${user.username}",
									style = Typography.body1
								)
							}
						}
					}
				}
				Card(
					modifier = Modifier
						.padding(8.dp, 0.dp)
						.fillMaxWidth(),
				) {
					Column(
						modifier = Modifier
							.padding(16.dp, 12.dp)
							.fillMaxWidth(),
					) {
						Text(
							text = "Your profile",
							style = Typography.h6,
						)
						Spacer(modifier = Modifier.height(8.dp))
						Text(
							text = "User ID",
							style = Typography.caption
						)
						Text(
							text = user.id,
							modifier = Modifier.padding(bottom = 12.dp),
							style = Typography.body1,
						)
						Text(
							text = "E-mail",
							style = Typography.caption
						)
						Text(
							text = user.email,
							modifier = Modifier.padding(bottom = 12.dp),
							style = Typography.body1,
						)
						if (creator != null) {
							val date = user.creator.birthDate.toJavaLocalDate()
								.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
							Text(
								text = "Name",
								style = Typography.caption
							)
							Text(
								text = creator.name,
								modifier = Modifier.padding(bottom = 12.dp),
								style = Typography.body1,
							)
							Text(
								text = "Surname",
								style = Typography.caption
							)
							Text(
								text = creator.surname,
								modifier = Modifier.padding(bottom = 12.dp),
								style = Typography.body1,
							)
							Text(
								text = "Birth date",
								style = Typography.caption
							)
							Text(
								text = date,
								modifier = Modifier.padding(bottom = 12.dp),
								style = Typography.body1,
							)
							Text(
								text = "Payment e-mail",
								style = Typography.caption
							)
							Text(
								text = creator.paymentEmail,
								modifier = Modifier.padding(bottom = 12.dp),
								style = Typography.body1,
							)
							Text(
								text = "Creator type",
								style = Typography.caption
							)
							Text(
								text = creator.creatorType,
								modifier = Modifier.padding(bottom = 12.dp),
								style = Typography.body1,
							)
							Text(
								text = "Biography",
								style = Typography.caption
							)
							Text(
								text = creator.bio.ifEmpty { "-" },
								modifier = Modifier.padding(bottom = 12.dp),
								style = Typography.body1,
							)
						}
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun ProfileScreenPreview() {
	val userStateViewModel = UserStateViewModel()
	//userStateViewModel.user = getPreviewCreator()
	CompositionLocalProvider(
		LocalUserState provides userStateViewModel,
		LocalNavigationState provides rememberNavController()
	) {
		ProfileScreen()
	}
}