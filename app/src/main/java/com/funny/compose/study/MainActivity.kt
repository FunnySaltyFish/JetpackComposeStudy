package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.funny.cmaterialcolors.MaterialColors
import com.funny.compose.study.ui.anim.NumberChangeAnimationTextTest
import com.funny.compose.study.ui.event_test.ClickEventTest
import com.funny.compose.study.ui.feature.BasicMarqueeTest
import com.funny.compose.study.ui.feature.FlowRowTest
import com.funny.compose.study.ui.feature.HorizontalPagerWithIndicator
import com.funny.compose.study.ui.game.SnakeGame
import com.funny.compose.study.ui.like_keep.FakeKeep
import com.funny.compose.study.ui.markdowntest.MarkdownTest
import com.funny.compose.study.ui.nav.NavigationTest
import com.funny.compose.study.ui.others.RememberTest
import com.funny.compose.study.ui.pager.VerticalPagerTest
import com.funny.compose.study.ui.physics_layout.PhysicsLayoutTest
import com.funny.compose.study.ui.post_layout.CountNumTest
import com.funny.compose.study.ui.post_layout.VerticalLayout
import com.funny.compose.study.ui.post_layout.VerticalLayoutWithIntrinsic
import com.funny.compose.study.ui.post_layout.WaterfallFlowLayout
import com.funny.compose.study.ui.post_layout.WeightedVerticalLayoutTest
import com.funny.compose.study.ui.post_layout.randomColor
import com.funny.compose.study.ui.post_layout.rememberRandomColor
import com.funny.compose.study.ui.post_lazygrid.SimpleLazyGrid
import com.funny.compose.study.ui.post_lazygrid.SimpleLazyGridAda
import com.funny.compose.study.ui.post_lazygrid.SimpleLazyGridWithSpace
import com.funny.compose.study.ui.refresh.SwipeToRefreshTest
import com.funny.compose.study.ui.saveable.SimpleNavigationWithSaveableStateSample
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

// 下面可能在 Android Studio 中报错：
// “Type inference failed. Expected type mismatch: inferred type is @Composable () -> Unit but () -> Unit was expected”
// 这是 Kotlin 插件的问题，已经提交了 bug 反馈，可以正常编译运行

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
        "点击事件传递" to { ClickEventTest() },
        "动画变化的文本" to { NumberChangeAnimationTextTest() },
        "跨屏状态保存（Google官方示例）" to { SimpleNavigationWithSaveableStateSample() },
        "Navigation使用" to { NavigationTest() },
        "PagerTest" to { VerticalPagerTest() },
        "RememberTest" to { RememberTest() },
        "MVI 贪吃蛇小游戏" to { SnakeGame() },
        "1.4:PagerWithIndicator" to { HorizontalPagerWithIndicator() },
        "1.4:FlowRow" to { FlowRowTest() },
        "1.4:跑马灯效果" to { BasicMarqueeTest() },
    )


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun Catalog() {
    var content: (@Composable ()-> Unit)? by remember {
        mutableStateOf(null)
    }
    AnimatedContent(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        targetState = content,
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
                verticalItemSpacing = 8.dp,
                // item 和 item 之间的横向间距
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                itemsIndexed(pages, key = { _, p -> p.first }){ _, pair ->
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