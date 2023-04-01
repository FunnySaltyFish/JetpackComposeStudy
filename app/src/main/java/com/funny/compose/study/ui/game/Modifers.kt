package com.funny.compose.study.ui.game

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import org.jbox2d.common.MathUtils.abs

@Stable
fun Modifier.square() =
    this.layout { measureable, constraints ->
        val size = minOf(constraints.maxWidth, constraints.maxHeight)
        layout(size, size) {
            val placeable = measureable.measure(constraints.copy(minWidth = size, minHeight = size, maxWidth = size, maxHeight = size))
            placeable.placeRelative(0, 0)
        }
    }

@Stable
fun Modifier.detectDirectionalMove(
    validMoveDelta: Float = 50f,
    updateDirection: (MoveDirection) -> Unit
) = this.pointerInput(Unit) {
    // 监听如下操作：
    // 手指从按下到滑动到抬起，移动的方向如果大于 validMoveDelta，则视为往对应方向产生了一次滑动
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        val start = down.position
        val up = waitForUpOrCancellation()
        up?.position?.let { end ->
            val dx = end.x - start.x
            val dy = end.y - start.y
            if (abs(dx) > validMoveDelta && abs(dx) > abs(dy)) {
                updateDirection(
                    if (dx > 0) MoveDirection.RIGHT else MoveDirection.LEFT
                )
            } else if (abs(dy) > validMoveDelta) {
                updateDirection(
                    if (dy > 0) MoveDirection.DOWN else MoveDirection.UP
                )
            }
        }
    }
}