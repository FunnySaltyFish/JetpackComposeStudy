package com.funny.compose.study.ui.post_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

interface VerticalScope {
    @Stable
    fun Modifier.weight(weight: Float) : Modifier
}

class WeightParentData(val weight: Float=0f) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@WeightParentData
}

object VerticalScopeInstance : VerticalScope {
    @Stable
    override fun Modifier.weight(weight: Float): Modifier = this.then(
        WeightParentData(weight)
    )
}

/**
 * FunnySaltyFish 2022-03-10
 * @param modifier Modifier
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<VerticalScope, Unit>
 */
@Composable
fun WeightedVerticalLayout(
    modifier: Modifier = Modifier,
    content: @Composable VerticalScope.() -> Unit
) {
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        // 获取各weight值
        val weights = measurables.map {
            (it.parentData as WeightParentData).weight
        }
        val totalHeight = constraints.maxHeight
        val totalWeight = weights.sum()

        val placeables = measurables.mapIndexed { i, mesurable ->
            // 根据比例计算高度
            val h = (weights[i] / totalWeight * totalHeight).roundToInt()
            mesurable.measure(constraints.copy(minHeight = h, maxHeight = h))
        }
        // 宽度：最宽的一项
        val width = placeables.maxOf { it.width }

        layout(width, totalHeight) {
            var y = 0
            placeables.forEachIndexed { i, placeable ->
                placeable.placeRelative(0, y)
                // 按比例设置大小
                y += placeable.height
            }
        }
    }
    Layout(modifier = modifier, content = { VerticalScopeInstance.content() }, measurePolicy=measurePolicy)
}

@Composable
fun WeightedVerticalLayoutTest() {
    WeightedVerticalLayout(Modifier.padding(16.dp).height(200.dp)) {
        Box(modifier = Modifier.width(40.dp).weight(1f).background(randomColor()))
        Box(modifier = Modifier.width(40.dp).weight(2f).background(randomColor()))
        Box(modifier = Modifier.width(40.dp).weight(7f).background(randomColor()))
    }
}