package com.funny.compose.study.ui.videoc

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun CScreen() {
    //ButtonAndBox()
    ExpandableText()
}

@Composable
fun ButtonAndBox() {
    Column {
        var boxState : BoxState by remember{
            mutableStateOf(BoxState.Small)
        }
        val transition = updateTransition(targetState = boxState, label = "Box Transition")

        val animColor by transition.animateColor(label = "Color"){ state ->
            state.color
        }
        val sizeColor by transition.animateDp(label = "Size") { state ->
            state.size
        }

        Button(onClick = { boxState = !boxState }) {
            Text("点击我")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier
            .size(sizeColor)
            .background(animColor)
        )
    }
}

val text = """
    春江潮水连海平，海上明月共潮生。
    滟滟随波千万里，何处春江无月明！
    江流宛转绕芳甸，月照花林皆似霰；
    空里流霜不觉飞，汀上白沙看不见。
    江天一色无纤尘，皎皎空中孤月轮。
    江畔何人初见月？江月何年初照人？
    人生代代无穷已，江月年年望相似。
""".trimIndent()
@Composable
fun ExpandableText() {
    var expand by remember {
        mutableStateOf(false)
    }

    val rotateValue by animateFloatAsState(if(expand) -180f else 0f)

    Column(
        horizontalAlignment = Alignment.End
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()){
            Text(text = text, maxLines = if(expand) 10 else 2,modifier = Modifier.fillMaxWidth())
        }

        IconButton(onClick = { expand = !expand }) {
            Icon(
                Icons.Default.ArrowDropDown,
                "expand",
                modifier = Modifier.graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f,0f)
                    rotationX = rotateValue
                }
            )
        }
    }
}