package com.creativehub.app.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getListUsers
import com.creativehub.app.model.Comment
import com.creativehub.app.model.CommentInfo
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

fun Event.formatDates(locale: Locale): String {
	val dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(locale)
	val startDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	val endDate = startDateTime.toLocalDateTime(TimeZone.currentSystemDefault())
		.toJavaLocalDateTime().format(dateFormat)
	return "$startDate - $endDate"
}

suspend fun fetchUsersOfComments(listComments: SnapshotStateList<Comment>): SnapshotStateList<CommentInfo> {
	val listId = mutableStateListOf<String>()
	listComments.forEach { comment ->
		if (!listId.contains(comment.userId))
			listId.add(comment.userId)
	}
	val result = APIClient.getListUsers(listId)
	val listCommentsUser = mutableStateListOf<CommentInfo>()
	if (result.getOrNull() != null) {
		result.getOrNull()!!.forEach { publicUser ->
			listComments.forEach { comment ->
				if (publicUser.id.compareTo(comment.userId) == 0) {
					listCommentsUser.add(CommentInfo(comment, publicUser))
				}
			}
		}
	}
	return listCommentsUser
}

fun Double.toCurrencyString(locale: Locale): String {
	return DecimalFormat.getCurrencyInstance(locale).format(this)
}