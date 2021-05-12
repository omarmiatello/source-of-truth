package com.github.omarmiatello.sourceoftruth.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Suppress("LongParameterList")
public class SourceOfTruth<EXTERNAL : Any, INTERNAL : Any, KEY : Any>(
    externalFlow: Flow<List<EXTERNAL>>,
    toInternal: (EXTERNAL) -> INTERNAL,
    private val key: (INTERNAL) -> KEY,
    private val onSync: (List<INTERNAL>) -> Unit,
    private val onDelete: (KEY) -> Unit,
    scope: CoroutineScope,
    private val autoSync: Boolean = true,
) {
    private val ext: StateFlow<List<INTERNAL>> = externalFlow
        .map { list -> list.map { toInternal(it) } }
        .stateIn(scope, SharingStarted.WhileSubscribed(), emptyList())

    private val int: MutableStateFlow<List<INTERNAL>> = MutableStateFlow(emptyList())

    public val flow: StateFlow<List<INTERNAL>> = combine(ext, int) { ext, int ->
        ext.map {
            val indexItem = int.indexByItemKey(it)
            if (indexItem != null) {
                int[indexItem]
            } else {
                it
            }
        } + int.filter { ext.indexByItemKey(it) == null }
    }.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList())

    public fun updateItem(item: INTERNAL, sync: Boolean = autoSync) {
        int.updateBy(keyOf = { key(it) }, item = item)
        if (sync) sync()
    }

    public fun deleteItem(item: INTERNAL) {
        deleteByKey(key(item))
    }

    public fun deleteByKey(key: KEY) {
        int.removeBy { key(it) == key }
        onDelete(key)
    }

    public fun flush() {
        int.value = emptyList()
    }

    public fun sync() {
        onSync(int.value)
    }

    private fun List<INTERNAL>.indexByKey(key: KEY): Int? = indexBy { key(it) == key }

    private fun List<INTERNAL>.indexByItemKey(item: INTERNAL): Int? = indexByKey(key(item))
}
