package com.creativehub.app.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getListUsers
import com.creativehub.app.model.Comment
import com.creativehub.app.model.CommentInfo

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