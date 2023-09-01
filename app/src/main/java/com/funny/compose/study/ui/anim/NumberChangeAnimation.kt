package com.funny.compose.study.ui.anim

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun NumberChangeAnimationTextTest() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf("103") }
        NumberChangeAnimatedText(text = text)

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
        Spacer(modifier = Modifier.height(16.dp))

        val longText = remember {
            mutableStateOf("————————————")
        }

        LaunchedEffect(key1 = Unit, block = {
            delay(2000)
            longText.value = "这是测试动画"
        })

        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            NumberChangeAnimatedText(text = longText.value)
        }

        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            AutoIncreaseAnimatedNumber(number = 10000, durationMills = 11000)
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NumberChangeAnimatedText(
    modifier: Modifier = Modifier,
    text: String,
    textPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
    textSize: TextUnit = 24.sp,
    textColor: Color = Color.Black,
    textWeight: FontWeight = FontWeight.Normal,
) {
    Row(modifier = modifier) {
        text.forEach {
            AnimatedContent(
                targetState = it,
                transitionSpec = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) with
                            fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
                },
                label = "NumberChange"
            ) { char ->
                Text(
                    text = char.toString(),
                    modifier = modifier.padding(textPadding),
                    fontSize = textSize,
                    color = textColor,
                    fontWeight = textWeight
                )
            }
        }
    }
}

@Composable
fun AutoIncreaseAnimatedNumber(
    modifier: Modifier = Modifier,
    startAnim: Boolean = true,
    number: Int,
    durationMills: Int = 16000,
    textPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
    textSize: TextUnit = 24.sp,
    textColor: Color = Color.Black,
    textWeight: FontWeight = FontWeight.Normal
) {
    // 动画，Animatable 相关介绍可以见 https://compose.funnysaltyfish.fun/docs/design/animation/animatable?source=trans
    val animatedNumber = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    // 数字格式化后的长度
    val l = remember(number) {
        number.toString().length
    }

    // Composable 进入 Composition 阶段，且 startAnim 为 true 时开启动画
    LaunchedEffect(number, startAnim) {
        if (startAnim)
            animatedNumber.animateTo(
                targetValue = number.toFloat(),
                animationSpec = tween(durationMillis = durationMills)
            )
    }

    NumberChangeAnimatedText(
        modifier = modifier,
        text = "%0${l}d".format(animatedNumber.value.roundToInt()),
        textPadding = textPadding,
        textColor = textColor,
        textSize = textSize,
        textWeight = textWeight
    )
}