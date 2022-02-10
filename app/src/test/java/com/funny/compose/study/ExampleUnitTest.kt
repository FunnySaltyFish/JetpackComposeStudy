package com.funny.compose.study

import com.funny.compose.study.ui.postb.MoveDirection
import com.funny.compose.study.ui.postb.Point
import com.funny.compose.study.ui.postb.Snake
import com.funny.compose.study.ui.postb.SnakeState
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun stateTest(){
        val state = SnakeState(snake = Snake(LinkedList<Point>().apply {
            add(Point(200f,200f))
            add(Point(220f,200f))
            add(Point(240f,200f))
        },40f, MoveDirection.LEFT),
            size = 400 to 400,
            blockSize = 20f,
            food = Point(0f, 0f)
        )
        println(state.hashCode())

        val state1=state
        println(state1.hashCode())

        val state2 = state.copy(blockSize = 21f)
        println(state2.hashCode())
    }


}