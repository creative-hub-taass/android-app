package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.creativehub.app.R
import com.creativehub.app.ui.LocalNavigationState
import com.creativehub.app.ui.components.RegisterCard
import com.creativehub.app.ui.navigation.Destination
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel

@Composable
fun RegisterScreen(
	onRegisterClick: (nickname: String, email: String, password: String) -> Unit,
) {
	val userState = LocalUserState.current
	val navigation = LocalNavigationState.current
	if (userState.isLoggedIn) {
		LaunchedEffect(key1 = Unit) {
			navigation.navigate(route = Destination.Feed.route) {
				popUpTo(route = Destination.Register.route) {
					inclusive = true
				}
			}
		}
		return
	}
	ConstraintLayout(Modifier.fillMaxSize()) {
		val (card, skipBtn, loader) = createRefs()
		if (userState.isBusy) {
			CircularProgressIndicator(modifier = Modifier.constrainAs(loader) { centerTo(parent) })
		} else {
			RegisterCard(Modifier.constrainAs(card) { centerTo(parent) },
						 onRegisterClick)
		}
		TextButton(onClick = { navigation.navigate(Destination.Feed.route) },
				   modifier = Modifier.constrainAs(skipBtn) {
					   bottom.linkTo(parent.bottom, margin = 16.dp)
					   centerHorizontallyTo(parent)
				   }) {
			Text(stringResource(R.string.skip_login), style = Typography.button)
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