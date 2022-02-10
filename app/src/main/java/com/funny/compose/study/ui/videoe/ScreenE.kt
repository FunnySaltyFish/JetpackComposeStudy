package com.funny.compose.study.ui.videoe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.funny.cmaterialcolors.MaterialColors
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.min

@Composable
fun EScreen() {
    DraggableBox()
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Green)
}


@Composable
fun DraggableBox() {
    val size = 40
    val length = 4

    var offsetX by remember {
        mutableStateOf(0f)
    }
    val sizePx = with(LocalDensity.current){
        size.dp.toPx()
    }
    val draggableState = rememberDraggableState{
        offsetX = (offsetX + it).coerceIn(0f,sizePx*(length-1).toFloat())
    }
    Box(modifier = Modifier
        .width((length * size).dp)
        .height(size.dp)
        .background(color = MaterialColors.Grey500)){
        Box(
            modifier = Modifier
                .width(size.dp)
                .height(size.dp)
                .offset { IntOffset(offsetX.toInt(), 0) }
                .draggable(draggableState, Orientation.Horizontal)
                .background(color = MaterialColors.Blue700)
        )
    }

}