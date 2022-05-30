package com.funny.compose.study

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.funny.compose.study.ui.edittest.EditTest
import com.funny.compose.study.ui.like_keep.FakeKeep
import com.funny.compose.study.ui.markdowntest.MarkdownTest
import com.funny.compose.study.ui.physics_layout.LayoutRecomposeTest
import com.funny.compose.study.ui.physics_layout.PhysicsLayoutTest
import com.funny.compose.study.ui.post_draw.CustomNavTest
import com.funny.compose.study.ui.post_layout.*
import com.funny.compose.study.ui.post_lazygrid.*
import com.funny.compose.study.ui.posta.FScreen
import com.funny.compose.study.ui.posta.PopularBooksDemo
import com.funny.compose.study.ui.postb.SnakeGame
import com.funny.compose.study.ui.theme.JetpackComposeStudyTheme
import com.funny.compose.study.ui.videod.ScreenD
import com.funny.compose.study.ui.videoe.EScreen

class MainActivity : ComponentActivity() {
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
//                CustomLayoutTest()
//                TraverseModifier()
//                ModifierSample1()
//                ModifierSample2()
//                CountNumTest()
//                WeightedVerticalLayoutTest()
//                Button(onClick = {
//                    val intent : Intent = Intent(this@MainActivity, SecondActivity::class.java)
//                    startActivity(intent)
//                        //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//
//                }) {
//                    Text(text = "跳转到第二个")
//                }
//                CustomNavTest()
//                ScreenD()
//                SimpleLazyGrid()
//                SimpleLazyGridWithSpace()
//                SimpleLazyGridAda()
//                SimpleLazyGridWithSpan()
//                SimpleLazyGridCustom()
//                SimpleLazyGridStaggered()
//                PhysicsLayoutTest()
//                LayoutRecomposeTest(Modifier.fillMaxSize()) {
//                    RandomColorBox(modifier = Modifier.size(50.dp))
//                    RandomColorBox(modifier = Modifier.size(60.dp))
//                }
                FakeKeep()
            }
//            PopularBooksDemo()
        }
    }
}