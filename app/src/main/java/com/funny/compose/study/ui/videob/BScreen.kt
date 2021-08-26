package com.funny.compose.study.ui.videob

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.funny.cmaterialcolors.MaterialColors.Companion.Green200

@Composable
fun BScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var dialogState by remember {
        mutableStateOf(false)
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { state->
            SnackbarHost(hostState = state){ data->
                Snackbar(
                    snackbarData = data,
                    shape = AbsoluteRoundedCornerShape(8.dp),
                    backgroundColor = Green200
                )

            }
        }
    ){
        Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                dialogState = true
//                scope.launch {
//                    scaffoldState.snackbarHostState.showSnackbar("我是一个Snackbar")
//                }
            }) {
                Text("点我")
            }

            if(dialogState){
                AlertDialog(
                    onDismissRequest = {
                        dialogState = false
                    },
                    title = {
                        Text(text = "Title")
                    },
                    text = {
                        Text("This is message")
                    },
                    confirmButton = {
                        Button(onClick = { dialogState = false }) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { dialogState = false }) {
                            Text("取消")
                        }
                    }
                )
            }
        }

    }
}