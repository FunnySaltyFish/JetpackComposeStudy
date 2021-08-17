package com.funny.compose.study.ui.videoa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.funny.cmaterialcolors.MaterialColors.Companion.BlueA700
import com.funny.cmaterialcolors.MaterialColors.Companion.Green200
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AScreen() {
    //View
    // findViewById(box).setBackground(
    // Compose 声明
    // state -> 视图
    Column {
        val viewModel : AViewModel = viewModel()
        val colorState = viewModel.color.observeAsState(
            BlueA700
        )
        
        ABox(
            updateColor = {
                viewModel.updateColor(it)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        BBox(
            colorState.value
        )
    }
}

@Composable
fun ABox(
    updateColor : (color:Color)->Unit
) {
    Box(modifier = Modifier
        .size(100.dp)
        .background(BlueA700)
        .clickable {
            updateColor(
                Color(
                    Random.nextInt(255),
                    Random.nextInt(255),
                    Random.nextInt(255),
                    255
                )
            )
        })
}

@Composable
fun BBox(
    color: Color
) {
    Box(modifier = Modifier
        .size(100.dp)
        .background(color)
        )
}

@Composable
fun ScreenB() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it){ data->
                Snackbar(snackbarData = data,shape = AbsoluteRoundedCornerShape(8.dp),backgroundColor = Green200)
            }
        }
    ) {
        Button(onClick = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("你好")
            }
        }) {
            Text("Click me")
        }
    }
}