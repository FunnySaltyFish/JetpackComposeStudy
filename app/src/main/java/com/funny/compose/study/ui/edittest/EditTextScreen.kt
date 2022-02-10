package com.funny.compose.study.ui.edittest

import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.funny.cmaterialcolors.MaterialColors

@Composable
fun EditTest() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
        Text(text = "Jetpack Compose-OutlineTextField")
        var text1 by remember {
            mutableStateOf("")
        }
        OutlinedTextField(value = text1, onValueChange = {text1 = it})

        // this one works abnormally
        Text(text = "Jetpack Compose-BasicTextField")
        var text2 by remember {
            mutableStateOf("")
        }
        BasicTextField(value = text2, onValueChange = { text2 = it }, modifier = Modifier.fillMaxWidth().border(2.dp,MaterialColors.BlueA200).padding(4.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "android.widget.EditText")
        AndroidView(factory = {
            return@AndroidView EditText(it).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                requestFocus()
            }
        })
    }
}