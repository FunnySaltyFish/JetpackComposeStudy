package com.funny.compose.study.ui.postb


data class SnakeState(
    val snake: Snake,
    val size: Pair<Int, Int>,
    val blockSize: Float,
    var food: Point,
    var gameState: GameState = GameState.WAITING
) {
    fun collideWall(nextPos: Point) =
        nextPos.x <= blockSize || nextPos.y <= blockSize || nextPos.x >= size.first - blockSize || nextPos.y >= size.second - blockSize

    fun collideSelf(nextPos: Point) =
        snake.body.any { it == nextPos }
}

sealed class GameAction {
    data class MoveSnake(val direction: MoveDirection) : GameAction()
    object GameTick : GameAction()
    object StartGame : GameAction()
    object LoseGame : GameAction()
    object RestartGame : GameAction()
    object QuitGame : GameAction()
}

enum class GameState {
    WAITING, PLAYING, LOST
}