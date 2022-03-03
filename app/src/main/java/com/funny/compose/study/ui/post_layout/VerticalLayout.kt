package com.funny.compose.study.ui.post_layout

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints

@Composable
fun VerticalLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables: List<Measurable>, constraints: Constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        // 宽度：最宽的一项
        val width = placeables.maxOf { it.width }
        // 高度：所有子微件高度之和
        val height = placeables.sumOf { it.height }
        layout(width, height) {
            var y = 0
            placeables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
        }
    }
}

@Composable
fun VerticalLayoutWithIntrinsic(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val measurePolicy = object : MeasurePolicy {
        override fun MeasureScope.measure(
            measurables: List<Measurable>,
            constraints: Constraints
        ): MeasureResult {
            val placeables = measurables.map { it.measure(constraints) }
            // 宽度：最宽的一项
            val width = placeables.maxOf { it.width }
            // 高度：所有子微件高度之和
            val height = placeables.sumOf { it.height }
            return layout(width, height) {
                var y = 0
                placeables.forEach {
                    it.placeRelative(0, y)
                    y += it.height
                }
            }
        }

        override fun IntrinsicMeasureScope.maxIntrinsicWidth(
            measurables: List<IntrinsicMeasurable>,
            height: Int
        ): Int {
            var width = 0
            measurables.forEach {
                val childWidth = it.maxIntrinsicWidth(height)
                if (childWidth > width) width = childWidth
            }
            return width
        }

        override fun IntrinsicMeasureScope.minIntrinsicWidth(
            measurables: List<IntrinsicMeasurable>,
            height: Int
        ): Int {
            var width = Int.MAX_VALUE
            measurables.forEach {
                val childWidth = it.maxIntrinsicWidth(height)
                if (childWidth < width) width = childWidth
            }
            return width
        }
    }

    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = measurePolicy
    )
}