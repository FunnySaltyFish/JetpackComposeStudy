package com.funny.compose.study

import com.funny.compose.study.ui.game.MoveDirection
import com.funny.compose.study.ui.game.Point
import com.funny.compose.study.ui.game.Snake
import com.funny.compose.study.ui.game.SnakeState
import org.junit.Test

import java.text.SimpleDateFormat
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

    @Test
    fun createData(){
        val calendar = Calendar.getInstance(Locale.CHINA)
        println(getDateRange("2022-2-28","2022-5-29"))
    }

    // 获取日期范围（用于折线图)
    fun getDateRange(startDate: String, endDate: String): List<String> {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val ssdf = SimpleDateFormat("MM/dd")
        val calendar = Calendar.getInstance()
        val sDate = sdf.parse(startDate)
        calendar.time = sDate
        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = sDate
        val dateList = ArrayList<String>()
        dateList.add(ssdf.format(sDate))
        val edate = sdf.parse(endDate)
        while (calendarEnd.time.before(edate)) {
            calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
            val tempDate = ssdf.format(calendarEnd.time)
            dateList.add(tempDate)
        }
        return dateList
    }



}