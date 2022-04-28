package com.creativehub.app.model

import kotlinx.datetime.Instant

interface Publication<T : Creation> {
	val id: String
	val timestamp: Instant
	val lastUpdate: Instant
	val creations: List<T>
}