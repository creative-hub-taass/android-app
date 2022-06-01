package com.creativehub.app.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

abstract class BusyViewModel : ViewModel() {
	protected var _isBusy by mutableStateOf(false)

	val isBusy get() = _isBusy

	protected inline fun <T> runBusy(block: () -> T): T {
		_isBusy = true
		val result = block()
		_isBusy = false
		return result
	}

}