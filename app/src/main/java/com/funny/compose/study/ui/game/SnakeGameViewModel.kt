package com.funny.compose.study.ui.game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import com.funny.compose.study.App
import com.funny.data_saver.core.mutableDataSaverStateOf
import java.util.*
import kotlin.random.Random

class SnakeGameViewModel : ViewModel(){
    companion object {
        const val TAG = "SnakeGameVM"
        internal const val COL_NUM = 20
        internal const val ROW_NUM = 20
        private val INITIAL_SNAKE get() = Snake(LinkedList<Point>().apply {
            add(Point(10,10))
            add(Point(11,10))
            add(Point(12,10))
        },20f, MoveDirection.LEFT)
    }

    // This value will be automatically saved by using Preferences via https://github.com/FunnySaltyFish/ComposeDataSaver
    val historyBestScore = mutableDataSaverStateOf(App.DataSaverUtils, key = "history_best_score", initialValue = 0)

    val snakeState = mutableStateOf(SnakeState(
        snake = INITIAL_SNAKE,
        size = 400 to 400,
        blockSize = Size(20f, 20f),
        food = generateFood(INITIAL_SNAKE.body)
    ))

    fun dispatch(gameAction: GameAction){
        Log.d(TAG, "dispatch: event: $gameAction")
        snakeState.value = reduce(snakeState.value, gameAction)
    }

    private fun reduce(state: SnakeState, gameAction: GameAction): SnakeState {
        val snake = state.snake
        return when(gameAction){
            GameAction.GameTick ->  {
                val nextPos = snake.nextPos()
                when {
                    nextPos == snakeState.value.food -> {
                        val newBody = snake.grow(nextPos)
                        val newFood = generateFood(newBody.body)
                        val newDifficulty = (state.difficulty + 0.8f).coerceAtMost(10f)
                        state.copy(snake = newBody, food = newFood, difficulty = newDifficulty)
                    }
                    state.collideWall(nextPos) || state.collideSelf(nextPos) -> {
                        val score = state.getScore()
                        if (score > historyBestScore.value) {
                            historyBestScore.value = score
                        }
                        state.copy(gameState = GameState.LOST)
                    }
                    else -> state.copy(snake = snake.move(nextPos))
                }
            }
            GameAction.StartGame -> state.copy(gameState = GameState.PLAYING)
            GameAction.RestartGame -> state.copy(snake = INITIAL_SNAKE, food = generateFood(INITIAL_SNAKE.body), gameState = GameState.WAITING, difficulty = 1f)
            is GameAction.ChangeSize -> {
                val newSize = gameAction.size
                state.copy(size = newSize, blockSize = Size((newSize.first / COL_NUM).toFloat(), (newSize.second / ROW_NUM).toFloat()))
            }
            is GameAction.MoveSnake -> state.copy(snake = state.snake.changeDirection(gameAction.direction))
            else -> state
        }
    }

    private fun generateFood(body: List<Point>): Point {
        val x = Random.nextInt(0, COL_NUM)
        val y = Random.nextInt(0, ROW_NUM)
        val p = Point(x, y)
        return if (body.contains(p)) generateFood(body) else p
    }
}