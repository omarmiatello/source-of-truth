package com.github.omarmiatello.sourceoftruth.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

@Suppress("LongParameterList")
public class SourceOfTruth<EXTERNAL : Any, INTERNAL : Any, KEY : Any>(
    externalFlow: Flow<List<EXTERNAL>>,
    toInternal: (EXTERNAL) -> INTERNAL,
    private val key: (INTERNAL) -> KEY,
    private val onSync: (List<INTERNAL>) -> Unit,
    private val onDelete: (KEY) -> Unit,
    scope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(),
    private val autoSync: Boolean = true,
    private val canOverrideExternal: Boolean = true,
    private val onConflict: OnConflict<INTERNAL> = OnConflict.KeepAllInternal(),
    private val onCombine: OnCombine<INTERNAL> = OnCombine.InternalOnlyHasPriority(),
) {
    private val externalStateFlow: StateFlow<List<INTERNAL>> = externalFlow
        .map { list -> list.map { toInternal(it) } }
        .stateIn(scope, sharingStarted, emptyList())

    private val internalStateFlow: MutableStateFlow<List<INTERNAL>> = MutableStateFlow(emptyList())

    public val flow: StateFlow<List<INTERNAL>> = combine(
        externalStateFlow,
        internalStateFlow,
    ) { ext, int ->
        onCombine(
            fromExternal = if (canOverrideExternal) {
                ext.map {
                    val indexItem = int.indexByItemKey(it)
                    if (indexItem != null) {
                        onConflict(
                            externalItem = it,
                            internalItem = int[indexItem],
                        )
                    } else {
                        it
                    }
                }
            } else ext,
            internalOnly = int.filter { ext.indexByItemKey(it) == null }
        )
    }.stateIn(scope, sharingStarted, emptyList())

    public fun updateItem(item: INTERNAL, sync: Boolean = autoSync) {
        internalStateFlow.updateBy(keyOf = { key(it) }, item = item)
        if (sync) sync()
    }

    public fun deleteItem(item: INTERNAL) {
        deleteByKey(key(item))
    }

    public fun deleteByKey(key: KEY) {
        internalStateFlow.removeBy { key(it) == key }
        onDelete(key)
    }

    public fun flush() {
        internalStateFlow.value = emptyList()
    }

    public fun sync() {
        onSync(internalStateFlow.value)
    }

    private fun List<INTERNAL>.indexByKey(key: KEY): Int? = indexBy { key(it) == key }

    private fun List<INTERNAL>.indexByItemKey(item: INTERNAL): Int? = indexByKey(key(item))
}

public interface OnConflict<INTERNAL> {
    public operator fun invoke(externalItem: INTERNAL, internalItem: INTERNAL): INTERNAL

    public class KeepAllInternal<INTERNAL> : OnConflict<INTERNAL> {
        override fun invoke(externalItem: INTERNAL, internalItem: INTERNAL): INTERNAL = internalItem
    }
}

public interface OnCombine<INTERNAL> {
    public operator fun invoke(
        fromExternal: List<INTERNAL>,
        internalOnly: List<INTERNAL>
    ): List<INTERNAL>

    public class InternalOnlyHasPriority<INTERNAL> : OnCombine<INTERNAL> {
        override fun invoke(
            fromExternal: List<INTERNAL>,
            internalOnly: List<INTERNAL>
        ): List<INTERNAL> = internalOnly + fromExternal
    }

    public class ExternalHasPriority<INTERNAL> : OnCombine<INTERNAL> {
        override fun invoke(
            fromExternal: List<INTERNAL>,
            internalOnly: List<INTERNAL>
        ): List<INTERNAL> = fromExternal + internalOnly
    }
}

