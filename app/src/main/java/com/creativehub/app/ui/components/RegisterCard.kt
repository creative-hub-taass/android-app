package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.Typography

@OptIn(ExperimentalComposeUiApi::class)
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
			Text(
				buildAnnotatedString {
					pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
					append("creative")
					pushStyle(SpanStyle(fontWeight = FontWeight.Black))
					append("Hub")
				},
				fontSize = 32.sp
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = nickname,
				onValueChange = {
					nickname = it
				},
				label = { Text(stringResource(R.string.nickname)) },
				singleLine = true,
				modifier = Modifier.autofill(listOf(AutofillType.NewUsername), onFill = {
					nickname = it
				})
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = email,
				onValueChange = {
					email = it
				},
				label = { Text(stringResource(R.string.email)) },
				singleLine = true,
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
				modifier = Modifier.autofill(listOf(AutofillType.EmailAddress), onFill = {
					email = it
				})
			)
			Spacer(modifier = Modifier.height(16.dp))
			OutlinedTextField(
				value = password,
				onValueChange = {
					password = it
				},
				label = { Text(stringResource(R.string.password)) },
				singleLine = true,
				visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				modifier = Modifier.autofill(listOf(AutofillType.NewPassword), onFill = {
					password = it
				}),
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
			TextButton(onClick = { navigation.navigate(Destination.Login.route) }) {
				Text(text = stringResource(R.string.already_registered_call))
			}
		}
	}
}