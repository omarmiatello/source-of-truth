package com.github.omarmiatello.sourceoftruth.appsample

import Steps
import androidx.lifecycle.ViewModel
import com.github.omarmiatello.sourceoftruth.core.SourceOfTruth
import com.github.omarmiatello.sourceoftruth.core.removeBy
import com.github.omarmiatello.sourceoftruth.core.updateBy
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    val extData = MutableStateFlow<List<ExtV>>(listOf())
    val sot = SourceOfTruth(
        externalFlow = extData,
        toInternal = { IntV(null, name, id) },
        key = { id },
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
    )

    val steps = Steps(
        { extData.value = emptyList(); sot.flush() } to """extData.value = emptyList();${"\n"}sot.flush()""",
        { extData.value += ExtV("x", "X1") } to """extData.value += ExtV("x", "X1")""",
        { extData.value += ExtV("y", "Y1") } to """extData.value += ExtV("y", "Y1")""",
        { sot.updateItem(IntV(null, "X2", "x"), sync = false) } to
                """sot.updateItem(IntV(null, "X2", "x"), sync = false)""",
        { sot.sync() } to """sot.sync()""",
        { sot.updateItem(IntV("QR1", "Y2", "y"), sync = false) } to
                """sot.updateItem(IntV("QR1", "Y2", "y"), sync = false)""",
        { sot.sync() } to """sot.sync()""",
        { sot.deleteByKey("x") } to """sot.deleteByKey("x")""",
        { sot.deleteByKey("y") } to """sot.deleteByKey("y")""",
    )
}