package com.funny.compose.study.ui.post_layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

@Composable
fun VerticalLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables: List<Measurable>, constrains: Constraints ->
        val placeables = measurables.map { it.measure(constrains) }
        // 宽度：最宽的一项
        val width = placeables.maxOf { it.width }
        // 高度：所有子微件高度之和
        val height = placeables.sumOf { it.height }
        layout(width, height){
            var y = 0
            placeables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
        }
    }
}