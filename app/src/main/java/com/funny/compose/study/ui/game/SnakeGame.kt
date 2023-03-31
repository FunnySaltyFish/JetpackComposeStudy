package com.funny.compose.study.ui.game

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal val LocalSnakeAssets = compositionLocalOf { SnakeAssets }
private const val TAG = "SnakeGame"
@Composable
fun SnakeGame(
    modifier: Modifier = Modifier
) {
    val vm : SnakeGameViewModel = viewModel()
    val snakeState by vm.snakeState

    val scope = rememberCoroutineScope()
    var gameTickFlag = rememberSaveable {
        true
    }
    DisposableEffect(key1 = Unit){
        snakeState.snake.direction = MoveDirection.DOWN
        scope.launch {
            while (gameTickFlag){
                vm.dispatch(GameAction.GameTick)
                delay(1000)
            }
        }

        onDispose {
            gameTickFlag = false
        }
    }

    val snakeAssets = LocalSnakeAssets.current
    CompositionLocalProvider(LocalSnakeAssets provides snakeAssets) {
        Column(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = modifier){
                drawSnake(snakeState, snakeAssets)
            }
            Text(text = snakeState.snake.head.toString(), Modifier.weight(1f))
        }
    }
    

}

fun DrawScope.drawSnake(snakeState: SnakeState, snakeAssets : SnakeAssets){
    val size = Size(snakeState.blockSize, snakeState.blockSize)
    snakeState.snake.body.forEach {
        if(it == snakeState.snake.head){
            drawRect(snakeAssets.headColor, it.asOffset, size)
        } else{
            drawRect(snakeAssets.bodyColor, it.asOffset, size)
        }
    }
    Log.d(TAG, "drawSnake: ")
}