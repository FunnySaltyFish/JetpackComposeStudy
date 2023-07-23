package com.funny.compose.study.ui.post_layout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private const val TAG = "ParentDataTest"
fun Modifier.count(num: Int) = this
    .drawWithContent {
        drawIntoCanvas { canvas ->
            val paint = android.graphics
                .Paint()
                .apply {
                    textSize = 40F
                }
            canvas.nativeCanvas.drawText(num.toString(), 0F, 40F, paint)
        }
        // 绘制 Box 自身内容
        drawContent()
    }
    .then(
        // 这部分是 父级数据修饰符
        CountNumParentData(num)
    )

class CountNumParentData(var countNum: Int) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@CountNumParentData
}

//interface CountNumScope {
//    @Stable
//    fun Modifier.count(num : Int) : Modifier
//}
//
//class CountNumScopeImpl : CountNumScope {
//    @Stable
//    override fun Modifier.count(num: Int): Modifier = this.then(
//        CountNumParentData(num)
//    )
//}


@Composable
fun CountChildrenNumber(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var num = 0
    Layout(
        modifier = modifier,
        content = content
    ) { measurables: List<Measurable>, constraints: Constraints ->
        val placeables = measurables.map {
            if (it.parentData is CountNumParentData) {
                num += (it.parentData as CountNumParentData).countNum
            }
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }
        // 宽度：最宽的一项
        val width = placeables.maxOf { it.width }
        // 高度：所有子微件高度之和
        val height = placeables.sumOf { it.height }
        Log.d(TAG, "CountChildrenNumber: 总价格是：$num")
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
fun CountNumTest() {
    CountChildrenNumber {
        repeat(5) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(randomColor())
                    .count(Random.nextInt(30, 100))
            )
        }
    }
}