@file:Suppress("MagicNumber")

package com.github.omarmiatello.sourceoftruth.appsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.omarmiatello.sourceoftruth.core.SourceOfTruth
import com.github.omarmiatello.sourceoftruth.core.removeBy
import com.github.omarmiatello.sourceoftruth.core.updateBy
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    val extData = MutableStateFlow<List<ExtV>>(listOf())
    val sot = SourceOfTruth(
        externalFlow = extData,
        toInternal = { extV -> IntV(null, extV.name, extV.id) },
        key = { it.id },
        onSync = { list ->
            list.mapNotNull { intV ->
                if (intV.qr == null) {
                    ExtV(intV.id, intV.name)
                } else {
                    extData.removeBy { it.id == intV.id }
                    null
                }
            }.forEach { extV ->
                extData.updateBy({ it.id }, extV)
            }
        },
        onDelete = { key -> extData.removeBy { key == it.id } },
        scope = viewModelScope,
    )

    val steps = Steps(
        {
            extData.value = emptyList()
            sot.flush()
        } to """extData.value = emptyList()${"\n"}sot.flush()""",
        { extData.value += ExtV("x", "X1") } to """extData.value += ExtV("x", "X1")""",
        { extData.value += ExtV("y", "Y1") } to """extData.value += ExtV("y", "Y1")""",
        { sot.updateItem(IntV(null, "X2", "x")) } to """sot.updateItem(IntV(null, "X2", "x"))""",
        { sot.updateItem(IntV("QR1", "Y2", "y")) } to """sot.updateItem(IntV("QR1", "Y2", "y"))""",
        { sot.deleteByKey("x") } to """sot.deleteByKey("x")""",
        { sot.deleteByKey("y") } to """sot.deleteByKey("y")""",
        scope = viewModelScope,
    )
}
