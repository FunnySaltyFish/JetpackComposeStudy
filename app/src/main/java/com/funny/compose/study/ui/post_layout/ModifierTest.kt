package com.funny.compose.study.ui.post_layout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

private const val TAG = "ModifierTest"

@Composable
fun TraverseModifier() {
    val modifier = Modifier
        .size(40.dp)
        .background(Color.Gray)
        .clip(CircleShape)
    LaunchedEffect(modifier){
        modifier.foldIn(0){ index , element : Modifier.Element ->
            Log.d(TAG, "$index -> $element")
            index + 1
        }
    }
    Column() {
        
    }
    Box(modifier = modifier)
}

@Composable
fun ModifierSample1() {
    // 父元素
    Box(modifier = Modifier
        .width(200.dp)
        .height(300.dp)
        .background(Color.Yellow)){
        // 子元素
        Box(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
            .size(50.dp)
            .background(Color.Blue))
    }
}
/**
 * 一个修饰符[Modifier]，为父布局[Layout]提供数据.
 * 可在[Layout]的 measurement 和 positioning 过程中通过 [IntrinsicMeasurable.parentData] 读取到.
 * parent data 通常被用于告诉父布局：子微件应该如何测量和定位
 */
@Composable
fun ModifierSample2() {
    // 父元素
    Box(modifier = Modifier
        .width(200.dp)
        .height(300.dp)
        .background(Color.Yellow)){
        // 子元素
        Box(modifier = Modifier
            .align(Alignment.Center)
            .size(50.dp)
            .background(Color.Blue))
    }
}