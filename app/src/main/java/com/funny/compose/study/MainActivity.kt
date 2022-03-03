package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import com.funny.compose.study.ui.edittest.EditTest
import com.funny.compose.study.ui.markdowntest.MarkdownTest
import com.funny.compose.study.ui.post_layout.CustomLayoutTest
import com.funny.compose.study.ui.post_layout.ModifierSample1
import com.funny.compose.study.ui.post_layout.TraverseModifier
import com.funny.compose.study.ui.posta.FScreen
import com.funny.compose.study.ui.posta.PopularBooksDemo
import com.funny.compose.study.ui.postb.SnakeGame
import com.funny.compose.study.ui.theme.JetpackComposeStudyTheme
import com.funny.compose.study.ui.videoe.EScreen

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_edit)
//
//        val composeView = findViewById<ComposeView>(R.id.compose_view)
//        composeView.setContent {
//            EditTest()
//        }


        setContent {

            JetpackComposeStudyTheme {
                // A surface container using the 'background' color from the theme
//                SnakeGame(
//                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f).background(Color.DarkGray)
//                )
//                EScreen()
//                EditTest()
//                MarkdownTest()
                CustomLayoutTest()
//                TraverseModifier()
//                ModifierSample1()
            }
//            PopularBooksDemo()
        }
    }
}