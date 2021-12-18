package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.funny.compose.study.ui.posta.FScreen
import com.funny.compose.study.ui.posta.PopularBooksDemo
import com.funny.compose.study.ui.theme.JetpackComposeStudyTheme

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeStudyTheme {
                // A surface container using the 'background' color from the theme
                FScreen()
            }
//            PopularBooksDemo()
        }
    }
}