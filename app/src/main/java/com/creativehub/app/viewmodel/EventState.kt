package com.creativehub.app.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.creativehub.app.api.APIClient
import com.creativehub.app.api.getEvent
import com.creativehub.app.model.Event
import com.creativehub.app.model.User

class EventState(id: String, user: User?) : PublicationState<Event>(id, user) {
	override suspend fun fetchPublication(id: String, user: User?) {
		publication = APIClient.getEvent(id).getOrNull()
		getPublicationInfo(id, user)
	}
}

@Composable
fun rememberEventState(id: String, user: User?): EventState {
	val info = rememberSaveable(id, user, saver = PublicationState.Saver()) { EventState(id, user) }
	info.onRemembered()
	return info
}