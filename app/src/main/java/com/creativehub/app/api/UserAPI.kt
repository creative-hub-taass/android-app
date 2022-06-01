package com.creativehub.app.api

import com.creativehub.app.model.User
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun APIClient.follow(followerId: String, followedId: String) = runCatching {
	APIClient().put("$USERS_BASE_URL/$followerId/follow/$followedId").body<User>()
}

suspend fun APIClient.unfollow(followerId: String, followedId: String) = runCatching {
	APIClient().put("$USERS_BASE_URL/$followerId/unfollow/$followedId").body<User>()
}