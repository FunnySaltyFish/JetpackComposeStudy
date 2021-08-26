package com.funny.compose.study.ui.videoc

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.funny.cmaterialcolors.MaterialColors.Companion.BlueA200
import com.funny.cmaterialcolors.MaterialColors.Companion.GreenA200

sealed class BoxState(val color : Color,val size : Dp){
    object Small : BoxState(BlueA200,100.dp)
    object Large : BoxState(GreenA200,200.dp)
    operator fun not() = when(this){
        Small -> Large
        Large -> Small
    }
}
