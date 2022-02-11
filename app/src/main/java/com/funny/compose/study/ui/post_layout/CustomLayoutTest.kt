package com.funny.compose.study.ui.post_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

fun randomColor() = Color(Random.nextInt(255),Random.nextInt(255),Random.nextInt(255))

@Composable
fun CustomLayoutTest() {
//    VerticalLayout() {
//        (1..5).forEach { _ ->
//            Box(modifier = Modifier.size(40.dp).background(randomColor()))
//        }
//    }
    WaterfallFlowLayout(
        columns = 3
    ) {
        (1..10).forEach { _ ->
            Box(modifier = Modifier.height(Random.nextInt(50, 100).dp).background(randomColor()))
        }
    }
}