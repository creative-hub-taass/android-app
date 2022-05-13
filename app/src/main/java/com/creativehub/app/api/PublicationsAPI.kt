package com.creativehub.app.api

import com.creativehub.app.model.Creation
import com.creativehub.app.model.PublicUser
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

suspend fun APIClient.getCreators(creations: List<Creation>) = runCatching {
	APIClient().post("$USERS_BASE_URL/-/public") {
		contentType(ContentType.Application.Json)
		setBody(creations.map { it.user })
	}.body<List<PublicUser>>()
		.zip(creations)
		.filter { it.first.id == it.second.user }
		.map { Pair(it.first, it.second.creationType) }
}
