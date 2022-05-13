package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.creativehub.app.R
import com.creativehub.app.ui.components.LoginCard
import com.creativehub.app.ui.navigation.Destination.Feed
import com.creativehub.app.ui.theme.CreativeHubTheme
import com.creativehub.app.ui.theme.Typography
import com.creativehub.app.viewmodel.LocalUserState
import com.creativehub.app.viewmodel.UserStateViewModel

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
			Text(stringResource(R.string.skip_login), style = Typography.button)
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