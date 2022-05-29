package com.creativehub.app.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val LightThemeColors = lightColors(
	primary = secondaryColor,
	primaryVariant = secondaryDarkColor,
	secondary = primaryColor,
	secondaryVariant = primaryLightColor,
	onPrimary = secondaryTextColor,
	onSecondary = primaryTextColor,
	background = primaryColor,
)

private val DarkThemeColors = darkColors(
	primary = primaryColor,
	primaryVariant = primaryLightColor,
	secondary = secondaryColor,
	secondaryVariant = secondaryLightColor,
	onPrimary = primaryTextColor,
	onSecondary = secondaryTextColor,
)

@Composable
fun CreativeHubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	val colors = if (darkTheme) {
		DarkThemeColors
	} else {
		LightThemeColors
	}
	MaterialTheme(
		colors = colors,
		typography = Typography,
		shapes = Shapes,
		content = content
	)
}
