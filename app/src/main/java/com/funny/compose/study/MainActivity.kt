package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.funny.cmaterialcolors.MaterialColors
import com.funny.compose.study.ui.like_keep.FakeKeep
import com.funny.compose.study.ui.markdowntest.MarkdownTest
import com.funny.compose.study.ui.physics_layout.PhysicsLayoutTest
import com.funny.compose.study.ui.post_draw.DrawTextTest
import com.funny.compose.study.ui.post_layout.*
import com.funny.compose.study.ui.post_lazygrid.SimpleLazyGrid
import com.funny.compose.study.ui.post_lazygrid.SimpleLazyGridAda
import com.funny.compose.study.ui.post_lazygrid.SimpleLazyGridWithSpace
import com.funny.compose.study.ui.refresh.SwipeToRefreshTest
import com.funny.compose.study.ui.theme.JetpackComposeStudyTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStudyTheme {
                Catalog()
            }
        }
    }
}

val pages: List<Pair<String, @Composable ()->Unit>> =
    arrayListOf(
        "物理引擎+自定义布局" to { PhysicsLayoutTest() },
        "高仿Keep周界面（自定义绘制）" to { FakeKeep() },
        "自定义布局（1-1）：简易纵向布局" to {
            VerticalLayout {
                (1..5).forEach { _ ->
                    Box(modifier = Modifier
                        .size(40.dp)
                        .background(randomColor()))
                }
            }
        },
        "自定义布局（1-2）：简易瀑布流" to {
                WaterfallFlowLayout(
                    modifier = Modifier.fillMaxWidth(),
                    columns = 3
                ) {
                    (1..10).forEach { _ ->
                        Box(modifier = Modifier
                            .height(Random.nextInt(50, 100).dp)
                            .background(randomColor()))
                    }
                }
        },
        "自定义布局（3）：固有特性测量（最小）" to {
            val text = arrayOf("Funny","Salty","Fish","is","Very","Salty")
            VerticalLayoutWithIntrinsic(
                Modifier
                    .width(IntrinsicSize.Min)
                    .padding(12.dp)
                    .background(MaterialColors.Yellow100)) {
                text.forEach {
                    Text(text = it, fontSize = 24.sp)
                }
            }
        },
        "自定义布局（3）：固有特性测量（最大）" to {
            val text = arrayOf("Funny","Salty","Fish","is","Very","Salty")
            VerticalLayoutWithIntrinsic(
                Modifier
                    .width(IntrinsicSize.Max)
                    .padding(12.dp)
                    .background(MaterialColors.Yellow100)) {
                text.forEach {
                    Text(text = it, fontSize = 24.sp)
                }
            }
        },
        "自定义布局（4-1）：ParentData之咸鱼的地摊(输出见logcat)" to { CountNumTest()},
        "自定义布局（4-1）：ParentData之自定义weight" to { WeightedVerticalLayoutTest() },
        "简易网格布局" to { SimpleLazyGrid() },
        "简易网格布局（带内边距）" to { SimpleLazyGridWithSpace() },
        "简易网格布局（自适应宽度，请横屏测试）" to { SimpleLazyGridAda() },
        "Markdown测试" to { MarkdownTest() },
        "下拉刷新测试" to { SwipeToRefreshTest() },
        "DrawScope.drawText 测试" to { DrawTextTest() }
    )


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun Catalog() {
    var content: (@Composable ()-> Unit)? by remember {
        mutableStateOf(null)
    }
    AnimatedContent(
        targetState = content, Modifier.fillMaxSize(),
        transitionSpec = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Right, tween(500)) with fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, tween(500))
        },
    ) {
        when(it){
            null -> LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                // 整体内边距
                contentPadding = PaddingValues(8.dp, 8.dp),
                // item 和 item 之间的纵向间距
                verticalArrangement = Arrangement.spacedBy(4.dp),
                // item 和 item 之间的横向间距
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                itemsIndexed(pages, key = { _, p -> p.first }){ i, pair ->
                    Card(
                        modifier = Modifier.clickable { content = pair.second },
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = rememberRandomColor().copy(0.8f)
                    ) {
                        Text(text = pair.first, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(CenterHorizontally)
                            .padding(16.dp), color = Color.White)
                    }
                }
            }
            else -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {
                BackHandler {
                    content = null
                }
                it()
            }
        }
    }
}