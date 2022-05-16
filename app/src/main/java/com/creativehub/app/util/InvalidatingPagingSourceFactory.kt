package com.creativehub.app.util

import androidx.paging.PagingSource

class InvalidatingPagingSourceFactory<K : Any, V : Any, PS : PagingSource<K, V>>(val factory: () -> PS) : () -> PS {
	private val list = mutableListOf<PagingSource<K, V>>()

	override fun invoke(): PS = factory().also { list.add(it) }

	fun invalidate() {
		while (list.isNotEmpty()) {
			list.removeFirst().invalidate()
		}
	}
}