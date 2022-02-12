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