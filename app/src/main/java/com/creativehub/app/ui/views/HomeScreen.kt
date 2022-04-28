package com.creativehub.app.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.creativehub.app.R
import com.creativehub.app.viewmodel.UserState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
	val vm = UserState.current
	val coroutineScope = rememberCoroutineScope()
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(id = R.string.app_name)) },
				actions = {
					IconButton(onClick = {
						coroutineScope.launch {
							vm.signOut()
						}
					}) {
						Icon(Icons.Filled.ExitToApp, null)
					}
				}
			)
		},
	) {
		Column(
			Modifier
				.fillMaxSize()
				.padding(16.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			if (vm.isBusy) {
				CircularProgressIndicator()
			} else {
				Text("Hello ${vm.user?.nickname}!")
			}
		}
	}
}