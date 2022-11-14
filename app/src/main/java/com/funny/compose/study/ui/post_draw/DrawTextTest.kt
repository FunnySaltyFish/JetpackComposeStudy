package com.funny.compose.study.ui.post_draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawTextTest() {
    val textMeasurer = rememberTextMeasurer(cacheSize = 8)
    Canvas(modifier = Modifier.fillMaxSize()){
        drawText(textMeasurer, "Hello World\n This is a simple text", style = TextStyle(color = Color.Black))
    }
}