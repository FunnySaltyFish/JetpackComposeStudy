package com.funny.compose.study.ui.game

import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import java.util.*

enum class MoveDirection {
    RIGHT, UP, DOWN, LEFT;
}

/**
 * 代表相对位置
 * @property x Int
 * @property y Int
 * @constructor
 */
@Stable
data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    fun asOffset(blockSize: Size) = Offset(x * blockSize.width, y * blockSize.height)
}

@Stable
data class Snake(val body: LinkedList<Point>, val bodySize: Float, val direction: MoveDirection) {
    val head: Point
        get() = body.first

    private val headPlace
        get() = head

    fun nextPos() = when (direction) {
        MoveDirection.RIGHT -> Point(headPlace.x + 1, headPlace.y)
        MoveDirection.LEFT -> Point(headPlace.x - 1, headPlace.y)
        MoveDirection.UP -> Point(headPlace.x, headPlace.y - 1)
        MoveDirection.DOWN -> Point(headPlace.x, headPlace.y + 1)
    }

    fun grow(pos: Point) = this.apply {
        body.addFirst(pos)
    }

    fun move(pos: Point) = this.copy(body = this.body.apply {
        body.removeLast()
        body.addFirst(pos)
    })

    fun changeDirection(newDirection: MoveDirection) =
        // 无法直接反向
        if (direction.ordinal + newDirection.ordinal != 3) {
            this.copy(direction = newDirection)
        } else this

}