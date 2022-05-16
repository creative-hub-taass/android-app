package com.creativehub.app.api.util

data class PagingLinks(val first: String?, val last: String?, val next: String?, val prev: String?)

fun List<String>.parsePagingLinks(): PagingLinks {
	val prev = find { it.contains("prev") }?.split(";")?.firstOrNull()?.trim('<', '>', ' ')
	val next = find { it.contains("next") }?.split(";")?.firstOrNull()?.trim('<', '>', ' ')
	val first = find { it.contains("first") }?.split(";")?.firstOrNull()?.trim('<', '>', ' ')
	val last = find { it.contains("last") }?.split(";")?.firstOrNull()?.trim('<', '>', ' ')
	return PagingLinks(first, last, next, prev)
}