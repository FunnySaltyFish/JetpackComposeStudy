package com.funny.compose.study.ui.event_test

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ClickEventTest() {
    val ctx = LocalContext.current
    var offset by remember {
        mutableStateOf(0f)
    }
    Box(Modifier.fillMaxSize()) {
        Button(
            onClick = { Toast.makeText(ctx, "我是Toast", Toast.LENGTH_SHORT).show() },
        ) {
            Text(text = "点我")
        }
        Column(
            Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offset.roundToInt()) }
                .draggable(rememberDraggableState(onDelta = { offset += it}), Orientation.Vertical)
        ) {
            repeat(20){ i->
                key(i) {
                    Text(text = "Item $i", modifier = Modifier.padding(20.dp))
                }
            }
        }
    }
}