package com.creativehub.app.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
	secondaryVariant = secondaryDarkColor,
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
	val systemUiController = rememberSystemUiController()
	SideEffect {
		// Update all of the system bar colors to be transparent, and use
		// dark icons if we're in light theme
		systemUiController.setSystemBarsColor(
			color = colors.secondaryVariant
		)
	}
}
