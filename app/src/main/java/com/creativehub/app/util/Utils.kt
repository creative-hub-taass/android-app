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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getListUsers
import com.creativehub.app.model.Comment
import com.creativehub.app.model.CommentInfo

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

class Click(bool: Boolean){
	var showComments by mutableStateOf(bool)
}

suspend fun fetchUsersofComments(listComments: SnapshotStateList<Comment>): SnapshotStateList<CommentInfo> {
	val listId = mutableStateListOf<String>()
	listComments.forEach { comment ->
		if(!listId.contains(comment.userId))
			listId.add(comment.userId)
	}
	val result = APIClient.getListUsers(listId)
	val listCommentsUser = mutableStateListOf<CommentInfo>()
	if(result.getOrNull() != null){
		result.getOrNull()!!.forEach { publicUser ->
			listComments.forEach { comment ->
				if (publicUser.id.compareTo(comment.userId) == 0){
					listCommentsUser.add(CommentInfo(comment, publicUser))
				}
			}
		}
	}
	return listCommentsUser
}