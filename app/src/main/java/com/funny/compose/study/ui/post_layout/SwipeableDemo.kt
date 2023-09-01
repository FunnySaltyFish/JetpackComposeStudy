package com.funny.compose.study.ui.post_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

private enum class Status {
    OPEN, CLOSE
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableDemo() {
    val blockSize = 48.dp
    val blockSizePx = with(LocalDensity.current) { blockSize.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = Status.CLOSE)
    Box(
        modifier = Modifier
            .size(height = blockSize, width = blockSize * 4)
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .offset {
                    // 读取 swipeableState 的 offset 值，设置为 Box 的偏移量
                    IntOffset(swipeableState.offset.value.toInt(), 0)
                }
                .swipeable(
                    state = swipeableState,
                    // 关键参数 anchors，表示 offset 和自定义状态的对应关系
                    anchors = mapOf(
                        0f to Status.CLOSE,
                        blockSizePx * 3 to Status.OPEN
                    ),
                    // 关键参数 thresholds，表示位置到达多少时，自动切换到下一个状态
                    thresholds = { from, to ->
                        if (from == Status.CLOSE) {
                            FractionalThreshold(0.3f)
                        } else {
                            FractionalThreshold(0.5f)
                        }
                    },
                    // orientation，表示滑动方向
                    orientation = Orientation.Horizontal
                )
                .size(blockSize)
                .background(Color.DarkGray)
        )
    }
}