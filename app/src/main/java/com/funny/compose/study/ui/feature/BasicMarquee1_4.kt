package com.funny.compose.study.ui.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.funny.compose.study.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicMarqueeTest() {
    Column {
        Label(text = "Text")
        Text(
            text = "Hello World... This is FunnySaltyFish, a Jetpack Compose lover. I'm learning Jetpack Compose",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee()
        )
        Label(text = "Image Row")
        Row(modifier = Modifier.basicMarquee()) {
            (0..5).forEach { _ ->
                Image(modifier = Modifier.size(100.dp), painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "")
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text = text, style = MaterialTheme.typography.h5)
    Spacer(modifier = Modifier.height(8.dp))
}