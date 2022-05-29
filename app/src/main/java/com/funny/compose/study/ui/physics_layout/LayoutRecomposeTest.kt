package com.funny.compose.study.ui.physics_layout

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.delay
data class Point(var x : Int, var y : Int)
private const val TAG = "LayoutRecomposeTest"
@Composable
fun LayoutRecomposeTest(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val positions = remember {
        mutableStateListOf(
            Point(0,0), Point(50,50)
        )
    }

    var yy by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = Unit){
        Log.d(TAG, "LayoutRecomposeTest: Recompose")
        while (true){
            delay(400)
            positions.forEach {
                it.x += 1
                it.y += 1
                yy += 1
//                Log.d(TAG, "LayoutRecomposeTest: $it")
            }
        }
    }

    Layout(
        modifier = modifier,
        content = content
    ) { measurables: List<Measurable>, constraints: Constraints ->
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(childConstraints) }
        // 宽度：最宽的一项
        val width = placeables.maxOf { it.width }
        // 高度：所有子微件高度之和
        val height = placeables.sumOf { it.height }
        layout(width, height) {
            // yy 为 mutableStateOf 可以
            val y = yy
//            Log.d(TAG, "LayoutRecomposeTest: $y")
            placeables.forEachIndexed { i , placeable ->
                placeable.placeRelative(0, derivedStateOf { positions[i].y }.value)
            }
        }
    }
}