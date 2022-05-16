package com.creativehub.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creativehub.app.ui.theme.Typography

@Composable
fun ErrorItem(
	message: String,
	modifier: Modifier = Modifier,
	onClickRetry: () -> Unit,
) {
	Row(modifier = modifier.padding(16.dp),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically) {
		Column(
			modifier = Modifier.wrapContentHeight(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Icon(imageVector = Icons.Default.Error, contentDescription = "error", Modifier.size(72.dp))
			Text(
				text = message,
				modifier = Modifier
					.wrapContentHeight()
					.padding(8.dp),
				style = Typography.h6
			)
			OutlinedButton(onClick = onClickRetry) {
				Text(text = "Retry")
			}
		}
	}
}

@Preview
@Composable
fun ErrorItemPreview() {
	ErrorItem(message = "Network error", Modifier.fillMaxSize()) {}
}