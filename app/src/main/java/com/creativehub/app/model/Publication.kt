package com.creativehub.app.model

import java.time.Instant
import java.util.*

open class Publication<T : Creation>(
	open val id: UUID,
	open val timestamp: Instant,
	open val lastUpdate: Instant,
	open val creations: List<T>,
)