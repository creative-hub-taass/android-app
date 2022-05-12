package com.creativehub.app.ui.views

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.Destination.Feed
import com.creativehub.app.Destination.Register
import com.creativehub.app.R
import com.creativehub.app.ui.components.SignInButton
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.util.GoogleAuthResultContract
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
	navController: NavController,
	onLoginClick: (email: String, password: String) -> Unit,
	onSocialLoginClick: (email: String, nickname: String, token: String) -> Unit,
) {
	val vm = LocalUserState.current
	ConstraintLayout(Modifier.fillMaxSize()) {
		val (loginCard, skipBtn, loader) = createRefs()
		if (vm.isBusy) {
			CircularProgressIndicator(modifier = Modifier.constrainAs(loader) { centerTo(parent) })
		} else {
			LoginCard(navController,
					  Modifier.constrainAs(loginCard) { centerTo(parent) },
					  onLoginClick,
					  onSocialLoginClick)
		}
		TextButton(onClick = { navController.navigate(Feed.route) },
				   modifier = Modifier.constrainAs(skipBtn) {
					   bottom.linkTo(parent.bottom, margin = 16.dp)
					   centerHorizontallyTo(parent)
				   }) {
			Text(stringResource(R.string.skip_login),
				 textDecoration = TextDecoration.Underline,
				 style = Typography.button)
		}
	}
}

@Composable
fun LoginCard(
	navController: NavController,
	modifier: Modifier,
	onLoginClick: (email: String, password: String) -> Unit,
	onSocialLoginClick: (email: String, nickname: String, token: String) -> Unit,
) {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var passwordVisible by remember { mutableStateOf(false) }
	val context = LocalContext.current
	val googleAuthLauncher = rememberLauncherForActivityResult(GoogleAuthResultContract()) { task ->
		try {
			val account = task?.getResult(ApiException::class.java)
			val mail = account?.email
			val nickname = account?.displayName
			val token = account?.idToken
			if (mail != null && nickname != null) {
				onSocialLoginClick(mail, nickname, token ?: "")
			} else Toast.makeText(context, "Google sign in failed", Toast.LENGTH_SHORT).show()
		} catch (e: ApiException) {
			e.printStackTrace()
			Toast.makeText(context, "Google sign in failed", Toast.LENGTH_SHORT).show()
		}
	}
	Card(modifier) {
		Column(
			modifier = Modifier.padding(24.dp, 24.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(stringResource(R.string.app_name), fontSize = 32.sp)
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
			Button(onClick = { onLoginClick(email, password) }) {
				Text(stringResource(R.string.login).uppercase(), style = Typography.button)
			}
			Spacer(modifier = Modifier.height(16.dp))
			SignInButton(
				text = "Sign in with Google",
				loadingText = "Signing in...",
				isLoading = false,
				icon = painterResource(id = R.drawable.ic_google_logo),
				onClick = {
					googleAuthLauncher.launch(1)
				}
			)
			SignInButton(
				text = "Sign in with Facebook",
				loadingText = "Signing in...",
				isLoading = false,
				icon = painterResource(id = R.drawable.ic_facebook_logo),
				onClick = {
					TODO()
				}
			)
			Spacer(modifier = Modifier.height(16.dp))
			TextButton(onClick = { navController.navigate(Register.route) }) {
				Text(text = stringResource(R.string.not_registered_call))
			}
		}
	}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
	CompositionLocalProvider(LocalUserState provides UserStateViewModel()) {
		CreativeHubTheme {
			val navController = rememberNavController()
			Surface(
				modifier = Modifier.fillMaxSize(),
				color = MaterialTheme.colors.background
			) {
				LoginScreen(navController, { _, _ -> }, { _, _, _ -> })
			}
		}
	}
}