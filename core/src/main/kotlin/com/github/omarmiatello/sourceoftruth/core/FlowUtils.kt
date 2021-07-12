package com.github.omarmiatello.sourceoftruth.core

import kotlinx.coroutines.flow.MutableStateFlow

public fun <T> MutableStateFlow<List<T>>.updateBy(
    keyOf: (T) -> Any,
    item: T,
): Boolean = mutateListWithOpResult { it.updateBy(keyOf, item) }

public fun <T> MutableStateFlow<List<T>>.removeBy(
    condition: (T) -> Boolean,
): T? {
    return mutateListWithOpResult { it.removeBy(condition) }
}

public fun <T> MutableList<T>.updateBy(
    keyOf: (T) -> Any,
    item: T,
): Boolean {
    val key = keyOf(item)
    val indexExt = indexBy { keyOf(it) == key }
    if (indexExt != null) {
        removeAt(indexExt)
        add(indexExt, item)
    } else {
        add(item)
    }
    return indexExt != null
}

public fun <T> MutableList<T>.removeBy(condition: (T) -> Boolean): T? =
    indexBy(condition)?.let { index ->
        removeAt(index)
    }

public fun <T, R> MutableStateFlow<List<T>>.mutateListWithOpResult(
    block: (MutableList<T>) -> R,
): R = value.toMutableList().let { mutableList ->
    val res = block(mutableList)
    value = mutableList
    res
}

public fun <T> List<T>.indexBy(condition: (T) -> Boolean): Int? =
    indexOfFirst(condition).takeIf { it != -1 }
