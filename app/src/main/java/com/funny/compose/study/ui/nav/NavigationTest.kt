package com.funny.compose.study.ui.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

// 这个文件在研究 Navigation 库的 saveState 和 restoreState
// 它让页面切走在切回来时能够记住页面的数据
// 前提是使用 rememberSaveable
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationTest() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController = navController, startDestination ="screen1"){
        composable("screen1") { Screen1(navController) }
        composable("screen2") { Screen2(navController) }
        composable("screen3") { Screen3(navController) }
    }
}

@Composable
fun Screen1(navHostController: NavHostController) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = {
            navHostController.navigateSingleTop("screen2")
        }) {
            Text("Screen 2")
        }

        TextButton(onClick = {
            navHostController.navigateSingleTop("screen3")
        }) {
            Text("Screen 3")
        }
    }
}

@Composable
fun Screen2(navHostController: NavHostController) {
    var number by rememberSaveable { mutableStateOf(0) }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        TextButton(onClick = {
            number++
        }) {
            Text("Increment")
        }
        Text("Number: $number")

        TextButton(onClick = {
            navHostController.navigateSingleTop("screen1")
        }) {
            Text(text = "To Screen1")
        }
    }
}

@Composable
fun Screen3(navHostController: NavHostController) {
    val lazyListState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(8.dp), state = lazyListState) {
        items(100) {
            Text("Item $it, Click Me To Go To Screen1", Modifier.fillMaxWidth().clickable {
                navHostController.navigateSingleTop("screen1")
            }.padding(8.dp))
        }
    }
}

fun NavHostController.navigateSingleTop(route: String, popUpToMain: Boolean = true){
    val navController = this
    navController.navigate(route) {
        //当底部导航导航到在非首页的页面时，执行手机的返回键 回到首页
        if (popUpToMain) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
                //currentScreen = TranslateScreen.MainScreen
            }
        }
        //从名字就能看出来 跟activity的启动模式中的SingleTop模式一样 避免在栈顶创建多个实例
        launchSingleTop = true
        //切换状态的时候保存页面状态
        restoreState = true
    }
}