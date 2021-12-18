package com.funny.compose.study.ui.posta

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@ExperimentalFoundationApi
@Composable
fun ShuffleSample() {
    var list by remember { mutableStateOf(listOf("A", "B", "C", "D", "E")) }
    LazyColumn {
        item {
            Button(onClick = { list = list.shuffled() }) {
                Text("打乱顺序")
            }
        }
        items(items = list, key = { it }) {
            Text("列表项：$it", Modifier.animateItemPlacement())
        }
    }
}