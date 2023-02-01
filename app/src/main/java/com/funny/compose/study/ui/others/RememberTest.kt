package com.funny.compose.study.ui.others

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun RememberTest() {
    var name by remember { mutableStateOf("hello compose") }
    LaunchedEffect(key1 = Unit){
        while (true){
            name = "hello compose"
            delay(1000)
        }
    }
    ShowCharLength(name)
}

@Composable
fun ShowCharLength(value: String) {
    LaunchedEffect(key1 = Unit){
        Log.d("ShowCharLength", "Composition Happens ")
    }
    Text(value)
}