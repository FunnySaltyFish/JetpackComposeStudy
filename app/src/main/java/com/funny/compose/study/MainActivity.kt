package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.funny.compose.study.ui.theme.JetpackComposeStudyTheme
import com.funny.compose.study.ui.videoa.ScreenB
import com.funny.compose.study.ui.videob.BScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStudyTheme {
                // A surface container using the 'background' color from the theme
                //ScreenB(
                BScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeStudyTheme {
        Greeting("Android")
    }
}