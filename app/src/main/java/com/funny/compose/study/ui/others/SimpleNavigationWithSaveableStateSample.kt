package com.funny.compose.study.ui.saveable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleNavigationWithSaveableStateSample() {
    @Composable
    fun <T : Any> Navigation(
        currentScreen: T,
        modifier: Modifier = Modifier,
        content: @Composable (T) -> Unit
    ) {
        // create SaveableStateHolder.
        val saveableStateHolder = rememberSaveableStateHolder()
        Box(modifier) {
            // Wrap the content representing the `currentScreen` inside `SaveableStateProvider`.
            // Here you can also add a screen switch animation like Crossfade where during the
            // animation multiple screens will be displayed at the same time.
            saveableStateHolder.SaveableStateProvider(currentScreen) {
                content(currentScreen)
            }
        }
    }

    Column {
        var screen by rememberSaveable { mutableStateOf("screen1") }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = { screen = "screen1" }) {
                Text("Go to screen1")
            }
            TextButton(onClick = { screen = "screen2" }) {
                Text("Go to screen2")
            }
        }
        Navigation(screen, Modifier.fillMaxSize()) { currentScreen ->
            if (currentScreen == "screen1") {
                Screen1()
            } else {
                Screen2()
            }
        }
    }
}

@Composable
fun Screen1() {
    var counter by rememberSaveable { mutableStateOf(0) }
    TextButton(onClick = { counter++ }) {
        Text("Counter=$counter on Screen1")
    }
}

@Composable
fun Screen2() {
    Text("Screen2")
}

@Composable
fun Button(modifier: Modifier = Modifier, onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier
            .clickable(onClick = onClick)
            .background(Color(0xFF6200EE), RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        content()
    }
}