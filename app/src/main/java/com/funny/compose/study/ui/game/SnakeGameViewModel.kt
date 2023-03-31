package com.funny.compose.study.ui.game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*


class SnakeGameViewModel : ViewModel(){
    companion object {
        const val TAG = "SnakeGameVM"
        private val INITIAL_SNAKE = Snake(LinkedList<Point>().apply {
            add(Point(200f,200f))
            add(Point(220f,200f))
            add(Point(240f,200f))
        },40f, MoveDirection.LEFT)
    }

    val snakeState = mutableStateOf(SnakeState(
        snake = INITIAL_SNAKE,
        size = 400 to 400,
        blockSize = 20f,
        food = Point(0f, 0f)
    ))

    fun dispatch(gameAction: GameAction){
        snakeState.value = reduce(snakeState.value, gameAction)
    }

    private fun reduce(state: SnakeState, gameAction: GameAction): SnakeState {
        val snake = state.snake
        return when(gameAction){
            GameAction.GameTick ->  {
//                state.copy()
                val nextPos = snake.nextPos()
                Log.d(TAG, "reduce: snake move to: ${snake.head}")
                when {
                    nextPos == state.food -> state.copy(snake = snake.grow(nextPos))
                    state.collideWall(nextPos) || state.collideSelf(nextPos) -> state.copy(gameState = GameState.LOST)
                    else -> state.copy(snake = snake.move(nextPos)).also {
                        Log.d(
                            TAG,
                            "reduce: coped state:${state.hashCode()} snake:${state.snake.hashCode()}"
                        ) }
                }
            }
            GameAction.StartGame -> state.copy(gameState = GameState.PLAYING)

            is GameAction.MoveSnake -> state.copy(snake = state.snake.changeDirection(gameAction.direction))
            else -> state
        }
    }
}