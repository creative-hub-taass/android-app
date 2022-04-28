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
import com.creativehub.app.R
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.UserState
import com.creativehub.app.viewmodel.UserStateViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var passwordVisible by remember { mutableStateOf(false) }
	val coroutineScope = rememberCoroutineScope()
	val vm = UserState.current
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (vm.isBusy) {
			CircularProgressIndicator()
		} else {
			Card {
				Column(
					modifier = Modifier.padding(24.dp, 24.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text("creativeHub", fontSize = 32.sp)
					Spacer(modifier = Modifier.height(16.dp))
					OutlinedTextField(
						value = email,
						onValueChange = { email = it },
						label = { Text("E-mail") },
						singleLine = true,
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
					)
					Spacer(modifier = Modifier.height(16.dp))
					OutlinedTextField(
						value = password,
						onValueChange = { password = it },
						label = { Text("Password") },
						singleLine = true,
						visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
						trailingIcon = {
							val image = if (passwordVisible) Icons.Filled.Visibility
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
					Button(onClick = {
						coroutineScope.launch {
							vm.signIn(email, password)
						}
					}) {
						Text(text = stringResource(R.string.login).uppercase(),
							 style = Typography.button)
					}
				}
			}
		}
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
	CompositionLocalProvider(UserState provides UserStateViewModel()) {
		CreativeHubTheme {
			Surface(
				modifier = Modifier.fillMaxSize(),
				color = MaterialTheme.colors.background
			) {
				LoginScreen()
			}
		}
	}
}