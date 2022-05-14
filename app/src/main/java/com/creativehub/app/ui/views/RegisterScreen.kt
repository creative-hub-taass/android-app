package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination.Feed
import com.creativehub.app.ui.navigation.Destination.Login
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel

@Composable
fun RegisterScreen(
	onRegisterClick: (nickname: String, email: String, password: String) -> Unit,
) {
	val vm = LocalUserState.current
	val navigation = LocalNavigationState.current
	ConstraintLayout(Modifier.fillMaxSize()) {
		val (card, skipBtn, loader) = createRefs()
		if (vm.isBusy) {
			CircularProgressIndicator(modifier = Modifier.constrainAs(loader) { centerTo(parent) })
		} else {
			RegisterCard(Modifier.constrainAs(card) { centerTo(parent) },
						 onRegisterClick)
		}
		TextButton(onClick = { navigation.navigate(Feed.route) },
				   modifier = Modifier.constrainAs(skipBtn) {
					   bottom.linkTo(parent.bottom, margin = 16.dp)
					   centerHorizontallyTo(parent)
				   }) {
			Text(stringResource(R.string.skip_login), style = Typography.button)
		}
	}
}

@Composable
fun RegisterCard(
	modifier: Modifier,
	onRegisterClick: (nickname: String, email: String, password: String) -> Unit,
) {
	var nickname by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var passwordVisible by remember { mutableStateOf(false) }
	val navigation = LocalNavigationState.current
	Card(modifier) {
		Column(
			modifier = Modifier.padding(24.dp, 24.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(stringResource(R.string.app_name), fontSize = 32.sp)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = nickname,
				onValueChange = { nickname = it },
				label = { Text(stringResource(R.string.nickname)) },
				singleLine = true
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = email,
				onValueChange = { email = it },
				label = { Text(stringResource(R.string.email)) },
				singleLine = true,
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = password,
				onValueChange = { password = it },
				label = { Text(stringResource(R.string.password)) },
				singleLine = true,
				visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				trailingIcon = {
					val image =
						if (passwordVisible) Icons.Filled.Visibility
						else Icons.Filled.VisibilityOff
					val description =
						if (passwordVisible) stringResource(R.string.hide_password)
						else stringResource(R.string.show_password)
					IconButton(onClick = { passwordVisible = !passwordVisible }) {
						Icon(imageVector = image, description)
					}
				}
			)
			Spacer(modifier = Modifier.height(16.dp))
			Button(onClick = { onRegisterClick(nickname, email, password) }) {
				Text(stringResource(R.string.sign_up).uppercase(), style = Typography.button)
			}
			Spacer(modifier = Modifier.height(16.dp))
			TextButton(onClick = { navigation.navigate(Login.route) }) {
				Text(text = stringResource(R.string.already_registered_call))
			}
		}
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
	CompositionLocalProvider(LocalUserState provides UserStateViewModel()) {
		CreativeHubTheme {
			Surface(
				modifier = Modifier.fillMaxSize(),
				color = MaterialTheme.colors.background
			) {
				RegisterScreen { _, _, _ -> }
			}
		}
	}
}