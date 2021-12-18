package com.funny.compose.study.ui.posta

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random


private fun randomColor() = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SwipeToDismissSample() {
    var studentList by remember {
        mutableStateOf( (1..100).map { Student(it, "Student $it") } )
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(studentList, key = {item: Student -> item.id }){ item ->
            // 侧滑删除所需State
            val dismissState = rememberDismissState()
            // 按指定方向触发删除后的回调，在此处变更具体数据
            if(dismissState.isDismissed(DismissDirection.StartToEnd)){
                studentList = studentList.toMutableList().also {  it.remove(item) }
            }
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.fillMaxWidth().animateItemPlacement(),
                // 下面这个参数为触发滑动删除的移动阈值
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                // 允许滑动删除的方向
                directions = setOf(DismissDirection.StartToEnd),
                // "背景 "，即原来显示的内容被划走一部分时显示什么
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> Color.Red
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Localized description",
                            modifier = Modifier.scale(scale)
                        )
                    }
                }
            ) {
                // ”前景“ 显示的内容
                Card(
                    elevation = animateDpAsState(
                        if (dismissState.dismissDirection != null) 4.dp else 0.dp
                    ).value
                ) {
                    Text(item.name, Modifier.padding(8.dp), fontSize = 28.sp)
                }
            }
        }
    }
}