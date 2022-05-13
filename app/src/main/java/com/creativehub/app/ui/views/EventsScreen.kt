package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.creativehub.app.viewmodel.LocalUserState

@Composable
fun EventsScreen(navController: NavHostController) {
	val vm = LocalUserState.current
	Column(Modifier.fillMaxSize(),
		   verticalArrangement = Arrangement.Center,
		   horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (vm.isBusy) {
			CircularProgressIndicator()
		} else {
			Text("Hello ${vm.user?.nickname ?: "guest"}!")
		}
	}
}