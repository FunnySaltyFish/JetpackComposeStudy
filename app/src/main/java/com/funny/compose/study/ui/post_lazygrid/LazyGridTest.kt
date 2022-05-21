package com.funny.compose.study.ui.post_lazygrid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.funny.compose.study.ui.post_layout.randomColor
import com.funny.compose.study.ui.post_layout.rememberRandomColor
import kotlin.random.Random

@Composable
fun RandomColorBox(modifier: Modifier) {
    Box(modifier = modifier.background(rememberRandomColor()))
}

@Composable
fun SimpleLazyGrid(){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        // 固定两列
        columns = GridCells.Fixed(2) ,
        content = {
            items(12){
                RandomColorBox(modifier = Modifier.height(200.dp))
            }
        }
    )
}

@Composable
fun SimpleLazyGridWithSpace(){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        // 固定两列
        columns = GridCells.Fixed(2) ,
        content = {
            items(12){
                RandomColorBox(modifier = Modifier.height(200.dp))
            }
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    )
}

@Composable
fun SimpleLazyGridAda(){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        // 固定宽度，自适应列数
        columns = GridCells.Adaptive(200.dp) ,
        content = {
            items(12){
                RandomColorBox(modifier = Modifier.height(200.dp))
            }
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    )
}

@Composable
fun SimpleLazyGridWithSpan(){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        // 固定列数
        columns = GridCells.Fixed(3) ,
        content = {
            item(span = {
                GridItemSpan(maxLineSpan)
            }){
                RandomColorBox(modifier = Modifier.height(50.dp))
            }
            items(12){
                RandomColorBox(modifier = Modifier.height(200.dp))
            }
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    )

    LazyColumn {
        item {
            // 两个微件放在同一item里
            RandomColorBox(modifier = Modifier.size(40.dp))
            RandomColorBox(modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
fun SimpleLazyGridCustom(){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        // 自定义实现1:2:1
        columns = object  : GridCells {
            override fun Density.calculateCrossAxisCellSizes(
                availableSize: Int,
                spacing: Int
            ): List<Int> {
                // 总共三个元素，所以其实两个间隔
                // |元素|间隔|元素|间隔|元素|
                val availableSizeWithoutSpacing = availableSize - 2 * spacing
                // 小的两个大小即为剩余空间（总空间-间隔）/4
                val smallSize = availableSizeWithoutSpacing / 4
                // 大的那个就是除以2呗
                val largeSize = availableSizeWithoutSpacing / 2
                return listOf(smallSize, largeSize, smallSize)
            }
        },
        content = {
            item(span = {
                GridItemSpan(maxLineSpan)
            }){
                RandomColorBox(modifier = Modifier.height(50.dp))
            }
            items(12){
                RandomColorBox(modifier = Modifier.height(200.dp))
            }
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    )
}

// 下面是对自己实现瀑布流的尝试哈哈
// Grid绘制的时候要求每一行高度一样……所以整不起来啊

/**
 * 返回整形数组中最小的那一项对应的值
 * @receiver IntArray
 * @return Int
 */
fun IntArray.minIndex() : Int {
    var i = 0
    var min = Int.MAX_VALUE
    this.forEachIndexed { index, e ->
        if (e<min){
            min = e
            i = index
        }
    }
    return i
}

private class VerticalStaggeredArrangement(val columnNum : Int = 2) : Arrangement.Vertical {
    val heights = IntArray(columnNum)

    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        sizes.forEachIndexed { i, size ->
            val minIdx = heights.minIndex()
            outPositions[i] = heights[minIdx]
            heights[minIdx] += size
        }
    }
}

private fun Arrangement.verticalStaggered(columnNum : Int = 2) = VerticalStaggeredArrangement(columnNum)

// 然并不行……
@Composable
fun SimpleLazyGridStaggered(){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        // 固定两列
        columns = GridCells.Fixed(2) ,
        content = {
            items(12){
                RandomColorBox(modifier = Modifier.height(Random.nextInt(100, 200).dp))
            }
        },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.verticalStaggered(2),
        contentPadding = PaddingValues(12.dp)
    )
}