package com.creativehub.app.util

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlin.reflect.KProperty

class SimpleState<T, A>(args: A, private val provider: @DisallowComposableCalls suspend (A) -> T) : RememberObserver {
	private var rememberScope: CoroutineScope? = null
	private val args by mutableStateOf(args)
	private var value by mutableStateOf<T?>(null)
	var isLoading by mutableStateOf(false)

	override fun onAbandoned() {
		clear()
	}

	override fun onForgotten() {
		clear()
	}

	private fun clear() {
		value = null
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	override fun onRemembered() {
		if (rememberScope != null) return
		// Create a new scope to observe state and execute requests while we're remembered.
		val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
		rememberScope = scope
		// Observe the current request and execute any emissions.
		scope.launch {
			snapshotFlow { args }
				.mapLatest {
					isLoading = true
					value = provider(it)
					isLoading = false
				}
				.collect()
		}
	}

	operator fun getValue(thisObj: Any?, property: KProperty<*>) = value
}

@Composable
fun <T, A> rememberState(args: A, provider: @DisallowComposableCalls suspend (A) -> T): SimpleState<T, A> {
	return remember(args) { SimpleState(args, provider) }.apply { onRemembered() }
}