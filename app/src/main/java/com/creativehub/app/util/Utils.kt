package com.creativehub.app.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
	locale: Locale = LocalContext.current.resources.configuration.locales[0],
	dateStyle: FormatStyle = FormatStyle.SHORT,
	timeStyle: FormatStyle = FormatStyle.SHORT,
): String {
	val dateFormat = DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle).withLocale(locale)
	val startDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	val endDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	return stringResource(resource, startDate, endDate)
}

@Composable
fun Double.toCurrencyString(locale: Locale = LocalContext.current.resources.configuration.locales[0]): String {
	return DecimalFormat.getCurrencyInstance(locale).format(this)
}
