package com.funny.compose.study.ui.anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList

data class Student(val name: String, val age: Int) {
    override fun toString() = "I am $name, $age years old"
}

val list = List(100) {
    Student("name$it", it)
}.toImmutableList()

@Composable
fun LazyListSlideInFromRightAnim() {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) {
            val animatedProgress = remember { Animatable(initialValue = 300f) }
            LaunchedEffect(Unit) {
                animatedProgress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(700, easing = FastOutSlowInEasing)
                )
            }
            StudentItem(Modifier.background(MaterialTheme.colors.surface).graphicsLayer(translationX = animatedProgress.value), it)
        }
    }
}

@Composable
fun LazyListFadeInAnim() {
    LazyColumn(
        contentPadding = PaddingValues(12.dp)
    ) {
        items(list) {
            val animatedProgress = remember { Animatable(initialValue = 0f) }
            LaunchedEffect(Unit) {
                animatedProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(700, easing = FastOutSlowInEasing)
                )
            }
            StudentItem(Modifier.graphicsLayer(alpha = animatedProgress.value), it)
        }
    }
}

@Composable
private fun StudentItem(modifier: Modifier, student: Student) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        text = student.toString(),
        style = MaterialTheme.typography.subtitle1
    )
}