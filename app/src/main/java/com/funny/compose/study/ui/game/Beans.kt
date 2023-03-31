package com.funny.compose.study.ui.game

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.*

enum class MoveDirection {
    RIGHT, UP, DOWN, LEFT;
}

data class Point(var x: Float, var y: Float) {
    fun reset(newX: Float, newY: Float) = this.apply {
        x = newX
        y = newY
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    val asOffset : Offset
        get() = Offset(x, y)


}

data class Block(var place: Point, val color: Color)
data class Snake(val body: LinkedList<Point>, val bodySize: Float, var direction: MoveDirection) {
    val head: Point
        get() = body.first

    private val headPlace
        get() = head

//    fun move(grow:Boolean) {
//        val tail = body[body.size - 1]
//        val newBlock = if (grow)tail.copy() else tail
//        val headPlace = head.place
//        newBlock.place = when (direction) {
//            MoveDirection.RIGHT -> head.place.reset(headPlace.x + bodySize, headPlace.y)
//            MoveDirection.LEFT -> head.place.reset(headPlace.x - bodySize, headPlace.y)
//            MoveDirection.UP -> head.place.reset(headPlace.x, headPlace.y - bodySize)
//            MoveDirection.DOWN -> head.place.reset(headPlace.x, headPlace.y + bodySize)
//        }
//        body.add(0,newBlock)
//        if(!grow)body.remove(tail)
//    }

    fun nextPos() = when (direction) {
        MoveDirection.RIGHT -> Point(headPlace.x + bodySize, headPlace.y)
        MoveDirection.LEFT  -> Point(headPlace.x - bodySize, headPlace.y)
        MoveDirection.UP    -> Point(headPlace.x, headPlace.y - bodySize)
        MoveDirection.DOWN  -> Point(headPlace.x, headPlace.y + bodySize)
    }

    fun grow(pos : Point) = this.apply {
        body.addFirst(pos)
    }

    fun move(pos: Point) = this.copy(body = this.body.apply  {
        body.removeLast()
        body.addFirst(pos)
    } )

    fun changeDirection(newDirection : MoveDirection) = this.apply {
        // 无法直接反向
        if(direction.ordinal + newDirection.ordinal != 3){
            direction = newDirection
        }
    }
}