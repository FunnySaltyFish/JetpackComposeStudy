package com.funny.compose.study.ui.game

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import com.funny.compose.study.ui.game.SnakeGameViewModel.Companion.COL_NUM
import com.funny.compose.study.ui.game.SnakeGameViewModel.Companion.ROW_NUM


@Stable
class SnakeState(
    val snake: Snake,
    val size: Pair<Int, Int>,
    val blockSize: Size,
    val food: Point,
    val gameState: GameState = GameState.WAITING,
    val difficulty: Float = 1f,
) {
    fun collideWall(nextPos: Point) =
        nextPos.x < 0 || nextPos.y < 0 || nextPos.x >= COL_NUM || nextPos.y >= ROW_NUM

    fun collideSelf(nextPos: Point) =
        snake.body.any { it == nextPos }

    fun getSleepTime() = (1000.0f / difficulty).toLong()

    fun getScore() = this.snake.body.size * 100

    fun copy(
        snake: Snake = this.snake,
        size: Pair<Int, Int> = this.size,
        blockSize: Size = this.blockSize,
        food: Point = this.food,
        gameState: GameState = this.gameState,
        difficulty: Float = this.difficulty
    ) = SnakeState(snake, size, blockSize, food, gameState, difficulty = difficulty)
}

sealed class GameAction {
    data class MoveSnake(val direction: MoveDirection) : GameAction()
    data class ChangeSize(val size: Pair<Int, Int>) : GameAction()
    object GameTick : GameAction()
    object StartGame : GameAction()
    object LoseGame : GameAction()
    object RestartGame : GameAction()
    object QuitGame : GameAction()

    override fun toString(): String {
        return when (this) {
            is MoveSnake -> "MoveSnake(direction=$direction)"
            is ChangeSize -> "ChangeSize(size=$size)"
            GameTick -> "GameTick"
            StartGame -> "StartGame"
            LoseGame -> "LoseGame"
            RestartGame -> "RestartGame"
            QuitGame -> "QuitGame"
        }
    }

}

enum class GameState {
    WAITING, PLAYING, LOST
}