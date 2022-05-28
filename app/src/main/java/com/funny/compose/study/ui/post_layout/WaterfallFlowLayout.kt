package com.funny.compose.study.ui.post_layout

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

private const val TAG = "Waterfall"

fun IntArray.minIndex() : Int {
    var i = 0
    var min = Int.MAX_VALUE
    this.forEachIndexed { index, e ->
        if (e<min){
            min = e
            i = index
        }
    }
    return i
}

@Composable
fun WaterfallFlowLayout(
    modifier: Modifier = Modifier.size(40.dp),
    columns: Int = 2,  // 横向几列
    content: @Composable ()->Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables: List<Measurable>, constrains: Constraints ->
        val itemWidth = constrains.maxWidth / columns
        val itemConstraints = constrains.copy(minWidth = itemWidth, maxWidth = itemWidth)
        val placeables = measurables.map { it.measure(itemConstraints) }
        // 记录当前各列高度
        val heights = IntArray(columns)
        layout(width = constrains.maxWidth, height = constrains.maxHeight){
            placeables.forEach { placeable ->
                val minIndex = heights.minIndex()
//                Log.d(TAG, "WaterfallFlowLayout: $minIndex ${placeable.height}")
                placeable.placeRelative(itemWidth * minIndex, heights[minIndex])
                heights[minIndex] += placeable.height
            }
        }
    }


}