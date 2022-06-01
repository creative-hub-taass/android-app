package com.creativehub.app.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.creativehub.app.model.Event
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun String.ellipsize(n: Int): String {
	return if (length + 3 <= n) this
	else take(n) + "..."
}

@Composable
fun Event.formatDates(
	@StringRes resource: Int,
	dateStyle: FormatStyle = FormatStyle.SHORT,
	timeStyle: FormatStyle? = FormatStyle.SHORT,
): String {
	val locale = LocalConfiguration.current.locales[0]
	val formatter = when {
		timeStyle != null -> DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
		else -> DateTimeFormatter.ofLocalizedDate(dateStyle)
	}
	val dateFormat = formatter.withLocale(locale)
	val startDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	val endDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	return stringResource(resource, startDate, endDate)
}

@Composable
fun Double.toCurrencyString(locale: Locale = LocalConfiguration.current.locales[0]): String {
	val format = DecimalFormat.getCurrencyInstance(locale)
	if (this > 1000) {
		format.maximumFractionDigits = 0
	}
	return format.format(this)
}