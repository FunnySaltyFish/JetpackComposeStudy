package com.funny.compose.study.ui.post_lazycolumn

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class FunnyLazyListState(var firstVisibleItemIndex : Int, var firstVisibleItemOffset : Int) : ScrollableState {
    override val isScrollInProgress: Boolean
        get() = TODO("Not yet implemented")

    override fun dispatchRawDelta(delta: Float): Float {
        TODO("Not yet implemented")
    }

    override suspend fun scroll(
        scrollPriority: MutatePriority,
        block: suspend ScrollScope.() -> Unit
    ) {
        TODO()
    }
}

@Composable
fun rememberFunnyLazyListState()  = remember {
    FunnyLazyListState(0, 0)
}