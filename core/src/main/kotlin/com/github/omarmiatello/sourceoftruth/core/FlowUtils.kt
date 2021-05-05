package com.github.omarmiatello.sourceoftruth.core

import kotlinx.coroutines.flow.MutableStateFlow

public fun <T> MutableStateFlow<List<T>>.updateBy(
    keyOf: (T) -> Any, item: T
) {
    mutateList { it.updateBy(keyOf, item) }
}

public fun <T> MutableStateFlow<List<T>>.removeBy(
    condition: (T) -> Boolean
): Boolean {
    var res = false
    mutateList { res = it.removeBy(condition) }
    return res
}

public fun <T> MutableList<T>.updateBy(
    keyOf: (T) -> Any,
    item: T,
) {
    val key = keyOf(item)
    val indexExt = indexBy { keyOf(it) == key }
    if (indexExt != null) {
        removeAt(indexExt)
        add(indexExt, item)
    } else {
        add(item)
    }
}

public fun <T> MutableList<T>.removeBy(
    condition: (T) -> Boolean,
): Boolean {
    val indexExt = indexBy(condition)
    if (indexExt != null) removeAt(indexExt)
    return indexExt != null
}

public fun <T> MutableStateFlow<List<T>>.mutateList(
    block: (MutableList<T>) -> Unit,
) {
    value = value.toMutableList().also(block)
}

public fun <T> List<T>.indexBy(
    condition: (T) -> Boolean,
): Int? {
    return indexOfFirst(condition).takeIf { it != -1 }
}
