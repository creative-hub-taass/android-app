package com.creativehub.app.util

import com.creativehub.app.model.Event
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun String.ellipsize(n: Int): String {
	return if (length + 3 <= n) this
	else take(n) + "..."
}

fun Event.formatDates(locale: Locale): String {
	val dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale)
	val startDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	val endDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	return "$startDate - $endDate"
}

fun Collection<LatLng>.toLatLngBounds(): LatLngBounds? {
	if (isEmpty()) return null
	val builder = LatLngBounds.builder()
	forEach { builder.include(it) }
	return builder.build()
}

fun emptyLatLngBounds() = LatLngBounds.builder().include(LatLng(0.0, 0.0)).build()


