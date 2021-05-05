package com.github.omarmiatello.sourceoftruth.appsample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.omarmiatello.sourceoftruth.appsample.theme.AppSampleTheme
import kotlinx.coroutines.flow.StateFlow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    AppSampleLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppSampleLayout() {
    val vm = viewModel<MainViewModel>()
    Column {
        Text(
            text = """data class ExtV(val id: String, val name: String)
                    |
                    |data class IntV(val qr: String?, val name: String, val id: String)
                    |
                    |val extData = MutableStateFlow<List<ExtV>>(listOf())
                    |
                    |val sot = SourceOfTruth(
                    |    externalFlow = extData,
                    |    toInternal = { IntV(null, name, id) },
                    |    key = { id },
                    |    onSync = { list -> ... },
                    |    onDelete = { key -> extData.removeBy { key == it.id } },
                    |)""".trimMargin(),
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(Color.LightGray)
                .padding(4.dp),
            style = MaterialTheme.typography.caption,
            fontFamily = FontFamily.Monospace
        )
        vm.steps.Buttons()
        Box(Modifier.padding(16.dp)) {
            StateFlowLayout("Steps", vm.steps.stepStrings, 350.dp) {
                Text(
                    text = it,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .background(Color.LightGray)
                        .padding(4.dp),
                    style = MaterialTheme.typography.caption,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        Row(Modifier.padding(16.dp)) {
            StateFlowLayout(
                title = "External data",
                flow = vm.extData
            ) {
                ExtVLayout(it)
            }
            StateFlowLayout(
                title = "Source of truth",
                flow = vm.sot.flow
            ) {
                IntVLayout(it)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> StateFlowLayout(
    title: String,
    flow: StateFlow<List<T>>,
    width: Dp = 200.dp,
    content: @Composable (T) -> Unit,
) {
    val list1 by flow.collectAsState()
    LazyColumn(
        modifier = Modifier
            .width(width)
            .animateContentSize(),
    ) {
        stickyHeader {
            Text(
                text = title,
                color = Color.Red,
                style = MaterialTheme.typography.body2
            )
        }
        items(list1) { content(it) }
    }
}

@Composable
private fun ExtVLayout(it: ExtV) {
    Row(Modifier.padding(vertical = 8.dp)) {
        Text(
            text = it.id,
            style = MaterialTheme.typography.caption
        )
        Text(it.name)
    }
}

@Composable
private fun IntVLayout(it: IntV) {
    Row(Modifier.padding(vertical = 8.dp)) {
        Text(
            text = it.id,
            style = MaterialTheme.typography.caption
        )
        Text(it.name)
        Text(
            text = it.qr ?: "No QR",
            style = MaterialTheme.typography.caption
        )
    }
}
