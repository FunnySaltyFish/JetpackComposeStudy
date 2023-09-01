package com.funny.compose.study.ui.post_layout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.launch

enum class SwipeLayoutState {
    Main, Foreground
}

private const val TAG = "SwipeCrossFadeLayout"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeCrossFadeLayout(
    modifier: Modifier = Modifier,
    state: SwipeableState<SwipeLayoutState> = rememberSwipeableState(initialValue = SwipeLayoutState.Main),
    mainUpper: @Composable () -> Unit,
    mainLower: @Composable () -> Unit,
    foreground: @Composable () -> Unit,
) {
    var mainUpperHeight by remember { mutableStateOf(0) }
    var mainLowerHeight by remember { mutableStateOf(1) }
    SubcomposeLayout(
        modifier =  modifier.swipeable(
            state,
            anchors = mapOf(
                0f to SwipeLayoutState.Main,
                mainLowerHeight.toFloat() to SwipeLayoutState.Foreground
            ),
            thresholds =  { _, _ ->
                FractionalThreshold(0.3f)
            },
            orientation = Orientation.Vertical
        ),
    ) { constraints: Constraints ->
        val mainLowerPlaceable = subcompose(MAIN_LOWER_KEY, mainLower).first().measure(
            constraints.copy(minHeight = 0, maxHeight = constraints.maxHeight)
        )

        mainLowerHeight = mainLowerPlaceable.height

        val mainUpperPlaceable = subcompose(MAIN_UPPER_KEY, mainUpper).first().measure(
            constraints.copy(minHeight = constraints.maxHeight - mainLowerHeight, maxHeight = constraints.maxHeight)
        )
        mainUpperHeight = mainUpperPlaceable.height

        val progress = (state.offset.value / mainLowerHeight).coerceIn(0f, 1f)
        val foregroundPlaceable = subcompose(FOREGROUND_KEY, foreground).first().measure(
            constraints.copy(minHeight = constraints.maxHeight, maxHeight = constraints.maxHeight)
        )


        layout(constraints.maxWidth, constraints.maxHeight) {
            val scale = lerp(1f, 0.8f, progress)
            mainLowerPlaceable.placeRelativeWithLayer(0, mainUpperHeight) {
                alpha = 1f - progress
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0.5f, 0f)
            }

            mainUpperPlaceable.placeRelativeWithLayer(0, 0) {
                alpha = 1f - progress
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0.5f, 1f)
            }

            if (progress > 0.01f) {
                foregroundPlaceable.placeRelativeWithLayer(0, lerp(-mainLowerHeight, 0, progress)) {
                    alpha = progress
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeCrossFadeLayoutTest() {
    val state = rememberSwipeableState(initialValue = SwipeLayoutState.Main)
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // 因为前景是列表，如果滑到底部仍然有多余的滑动距离，就关闭
                // Log.d("NestedScrollConnection", "onPostScroll: $available")
                // 读者可以自行运行这行代码，滑动列表到底部后仍然上滑，看看上面会打印什么，就能明白这个 available 的作用了
                return if (available.y < 0 && source == NestedScrollSource.Drag) {
                    state.performDrag(available.toFloat()).toOffset()
                } else {
                    Offset.Zero
                }
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                state.performFling(velocity = Offset(available.x, available.y).toFloat())
                return available
            }

            private fun Float.toOffset(): Offset = Offset(0f, this)

            private fun Offset.toFloat(): Float = this.y
        }
    }
    SwipeCrossFadeLayout(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        mainLower = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Blue)
            )
        },
        mainUpper = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
            )
        },
        foreground = {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Green)
//            )
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
                    ) {
                items(100) {
                    Text(text = "Item $it")
                }
            }
        }
    )
}


private const val MAIN_UPPER_KEY = "main_upper"
private const val MAIN_LOWER_KEY = "main_lower"
private const val FOREGROUND_KEY = "foreground"

private fun lerp(start: Int, end: Int, fraction: Float): Int {
    return (start + fraction * (end - start)).toInt()
}

private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return (start + fraction * (end - start))
}

