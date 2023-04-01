package com.funny.compose.study.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

internal val LocalSnakeAssets: ProvidableCompositionLocal<SnakeAssets> = staticCompositionLocalOf { SnakeAssets.SnakeAssets1 }
private const val TAG = "SnakeGame"

@Composable
fun SnakeGame(
    modifier: Modifier = Modifier
) {
    val vm: SnakeGameViewModel = viewModel()
    val snakeState by vm.snakeState

    LaunchedEffect(key1 = snakeState.gameState) {
        if (snakeState.gameState != GameState.PLAYING) return@LaunchedEffect
        while (true) {
            vm.dispatch(GameAction.GameTick)
            delay(snakeState.getSleepTime())
        }
    }

    val snakeAssets by ThemeConfig.savedSnakeAssets
    CompositionLocalProvider(LocalSnakeAssets provides snakeAssets) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (snakeState.gameState) {
                GameState.PLAYING -> Playing(snakeState, snakeAssets, vm::dispatch)
                GameState.LOST -> Lost(snakeState.getScore(), vm.historyBestScore.value, vm::dispatch)
                GameState.WAITING -> Waiting(vm::dispatch)
            }
        }
    }
}

@Composable
fun ColumnScope.Waiting(dispatchAction: (GameAction) -> Unit) {
    OutlinedButton(onClick = { dispatchAction(GameAction.StartGame) }) {
        Text(text = "开始游戏")
    }
    Spacer(modifier = Modifier.height(16.dp))
    val snakeAssets by ThemeConfig.savedSnakeAssets
    var expanded by remember { mutableStateOf(false)  }
    OutlinedButton(onClick = { expanded = true }) {
        Text(text = "选择主题：$snakeAssets")
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ThemeConfig.themeList.forEach { theme ->
                DropdownMenuItem(onClick = {
                    ThemeConfig.savedSnakeAssets.value = theme
                    expanded = false
                }) {
                    Text(text = theme.toString())
                }
            }
        }
    }
}

@Composable
fun ColumnScope.Playing(
    snakeState: SnakeState,
    snakeAssets: SnakeAssets,
    dispatchAction: (GameAction) -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .square()
            .onGloballyPositioned {
                val size = it.size
                dispatchAction(GameAction.ChangeSize(size.width to size.height))
            }
            .detectDirectionalMove {
                dispatchAction(GameAction.MoveSnake(it))
            }
    ) {
        drawBackgroundGrid(snakeState, snakeAssets)
        drawSnake(snakeState, snakeAssets)
        drawFood(snakeState, snakeAssets)
    }
}

@Composable
fun ColumnScope.Lost(
    score: Int,
    bestHistoryScore: Int,
    dispatchAction: (GameAction) -> Unit
) {
    Text(text = "糟糕，失败了！\n您的分数：$score，历史最佳：$bestHistoryScore", textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(onClick = { dispatchAction(GameAction.RestartGame) }) {
        Text(text = "再来一次", textAlign = TextAlign.Center)
    }
}


private fun DrawScope.drawSnake(snakeState: SnakeState, snakeAssets: SnakeAssets) {
    val size = snakeState.blockSize
    snakeState.snake.body.forEach {
        val offset = it.asOffset(snakeState.blockSize)
        if (it == snakeState.snake.head) {
            drawRect(snakeAssets.headColor, offset, size)
        } else {
            drawRect(snakeAssets.bodyColor, offset, size)
        }
    }
}

fun DrawScope.drawFood(snakeState: SnakeState, snakeAssets: SnakeAssets) {
    val size = snakeState.blockSize
    val offset = snakeState.food.asOffset(snakeState.blockSize)
    drawRect(snakeAssets.foodColor, offset, size)
}

private fun DrawScope.drawBackgroundGrid(snakeState: SnakeState, snakeAssets: SnakeAssets) {
    val (width, height) = snakeState.size
    for (x in 0..width step snakeState.blockSize.width.toInt()) {
        drawLine(
            snakeAssets.lineColor,
            start = Offset(x.toFloat(), 0f),
            end = Offset(x.toFloat(), height.toFloat()),
            strokeWidth = 1f
        )
    }
    for (y in 0..height step snakeState.blockSize.height.toInt()) {
        drawLine(
            snakeAssets.lineColor,
            start = Offset(0f, y.toFloat()),
            end = Offset(width.toFloat(), y.toFloat()),
            strokeWidth = 1f
        )
    }
}