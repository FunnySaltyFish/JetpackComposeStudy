package com.funny.compose.study.ui.others

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

// 回复文章 https://juejin.cn/post/7194816465290133560
// 全部回复：
/**
 * 两点个人想说的：
1. “我们需要频繁展示相同的数据，如果使用Text() 直接进行展示，就会每次就会重新计算”，这句话是不正确的，就你这个例子来看，你的 `ShowCharLength` 这个 Composable 是 restartable 且 skippable 的 （参考 juejin.cn），且唯一的参数是 Stable 的。也就是，如果 value 的内容不变，这个 Composable 会被完全跳过重组，你可以运行 github.com 自行查看输出以验证
2. 如果某值依赖于其他值，且频繁变化（速度快过 UI ），那么可以使用 derivedStateOf 避免不必要的重组，这也是官方给出的建议 （见视频：Jetpack Compose 性能提升最佳实践 www.youtube.com）
Compose 想学深并不是一件容易的事，望共同进步
 */
@Composable
fun RememberTest() {
    var name by remember { mutableStateOf("hello compose") }
    LaunchedEffect(key1 = Unit){
        while (true){
            name = "hello compose"
            delay(1000)
        }
    }
    ShowCharLength(name)
}

@Composable
fun ShowCharLength(value: String) {
    LaunchedEffect(key1 = Unit){
        Log.d("ShowCharLength", "Composition Happens ")
    }
    Text(value)
}