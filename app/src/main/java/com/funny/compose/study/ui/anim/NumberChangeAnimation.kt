package com.funny.compose.study.ui.anim

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NumberChangeAnimationText(
    modifier: Modifier = Modifier,
    text: String,
    textPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
    textSize: TextUnit = 24.sp,
    textColor: Color = Color.Black
) {
    Row(modifier = modifier) {
        text.forEach {
            AnimatedContent(
                targetState = it,
                transitionSpec = {
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Up) with
                            fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Up)
                }
            ) { char ->
                Text(text = char.toString(), modifier = modifier.padding(textPadding), fontSize = textSize, color = textColor)
            }
        }
    }
}

@Composable
fun NumberChangeAnimationTextTest() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf("103") }
        NumberChangeAnimationText(text = text)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            // 加一 和 减一
            listOf(1, -1).forEach { i ->
                TextButton(onClick = {
                    text = (text.toInt() + i).toString()
                }) {
                    Text(text = if (i == 1) "加一" else "减一")
                }
            }
        }
    }
}