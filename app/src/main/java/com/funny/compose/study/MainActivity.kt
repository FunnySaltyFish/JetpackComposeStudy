package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.funny.compose.study.ui.theme.JetpackComposeStudyTheme
import com.funny.compose.study.ui.videoc.CScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStudyTheme {
                // A surface container using the 'background' color from the theme
                CScreen()
            }
        }
    }
}