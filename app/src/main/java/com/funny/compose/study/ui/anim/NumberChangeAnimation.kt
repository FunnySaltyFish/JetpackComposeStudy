package com.funny.compose.study.ui.anim

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        var text by remember { mutableStateOf("103") }
//        NumberChangeAnimationText(text = text)
//
//        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//            // 加一 和 减一
//            listOf(1, -1).forEach { i ->
//                TextButton(onClick = {
//                    text = (text.toInt() + i).toString()
//                }) {
//                    Text(text = if (i == 1) "加一" else "减一")
//                }
//            }
//        }
//    }
    val text =  remember {
        mutableStateOf("————————————")
    }
    Column(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier
            .height(100.dp)
            .clickable {
                text.value = System.currentTimeMillis().toString()
            }
            .background(Color.Red), contentAlignment = Alignment.Center){
            Text("测试文本")
        }
        Box(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(Color.Red), contentAlignment = Alignment.Center){
            NumberChangeAnimatedText(text = text.value)
        }

        Box(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .background(Color.Red), contentAlignment = Alignment.Center){
            AutoIncreaseAnimatedNumber(number = 10000, durationMills = 11000)
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        delay(2000)
        text.value = "这是测试动画"
    })
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
                    slideIntoContainer(AnimatedContentScope.SlideDirection.Up) with
                            fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Up)
                }
            ) { char ->
                Text(text = char.toString(), modifier = modifier.padding(textPadding), fontSize = textSize, color = textColor, fontWeight = textWeight)
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
    val l = remember {
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