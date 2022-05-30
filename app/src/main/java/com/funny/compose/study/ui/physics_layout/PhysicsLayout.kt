package com.funny.compose.study.ui.physics_layout

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.funny.cmaterialcolors.MaterialColors
import com.funny.compose.study.R
import com.funny.compose.study.ui.post_lazygrid.RandomColorBox
import kotlinx.coroutines.delay
import org.jbox2d.common.Vec2

private const val TAG = "PhysicsLayout"

interface PhysicsLayoutScope {
    @Stable
    fun Modifier.physics(physicsConfig: PhysicsConfig, initialX : Float = 0f, initialY : Float = 0f) : Modifier
}

object PhysicsLayoutScopeInstance : PhysicsLayoutScope {
    override fun Modifier.physics(
        physicsConfig: PhysicsConfig,
        initialX: Float,
        initialY: Float
    ): Modifier = this.then(PhysicsParentData(physicsConfig, initialX, initialY))
}

data class PhysicsLayoutState(val physics: Physics = Physics(ComposableGroup(arrayListOf())))

@Composable
fun PhysicsLayout(
    modifier: Modifier = Modifier,
    physicsLayoutState: PhysicsLayoutState = remember { PhysicsLayoutState() },
    boundColor: Color? = MaterialColors.Blue600,
    boundSize : Float? = 20f,
    gravity : Vec2 = Vec2(0f, 9.8f),
    content : @Composable PhysicsLayoutScope.()->Unit
){
    val parentDataList = physicsLayoutState.physics.composableGroup.physicsParentDatas
    val physics = physicsLayoutState.physics
    val density = LocalDensity.current
    var initialized by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = density){
        physics.density = density.density
        physics.pixelsPerMeter = with(density){
            10.dp.toPx()
        }
    }

    var recompose by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(initialized, gravity, boundSize){
        Log.d(TAG, "PhysicsLayout: launchedEffect ${parentDataList.size} ${physics.width}")
        if (!initialized) return@LaunchedEffect
        if (parentDataList.isEmpty()) return@LaunchedEffect
        if (physics.width * physics.height == 0) return@LaunchedEffect
        physics.createWorld { body, i ->
            parentDataList[i].body = body
//            Log.d(TAG, "PhysicsLayout: createBody: $body")
        }
        physics.world?.gravity = gravity
        if (boundSize != null && boundSize > 0){
            physics.setBoundsSize(boundSize)
        }
        physics.giveRandomImpulse()
    }

    LaunchedEffect(key1 = Unit){
        while (true){
            delay(16)
            physics.step() // 模拟 16ms
//            Log.d(TAG, "PhysicsLayout: ${physics.world?.bodyList?.position}")
            recompose++
        }
    }

    val drawBoundModifier = remember {
        Modifier.drawWithContent {
            if (physics.hasBounds && boundColor != null){
                // 绘制 bound
                val s = physics.boundsSizeInPixel
                val w = physics.width.toFloat()
                val h = physics.height.toFloat()
                drawRect(boundColor, Offset.Zero, Size(w,s))
                drawRect(boundColor, Offset(0f, h-s), Size(w,s))
                drawRect(boundColor, Offset(0f, s), Size(s, h - 2 * s))
                drawRect(boundColor, Offset(w - s, s), Size(s, h - 2 * s))
            }
            drawContent()
        }
    }
    Layout(content = { PhysicsLayoutScopeInstance.content() }, modifier = modifier.then(drawBoundModifier)){ measurables, constraints ->
        if (!initialized) {
            physics.setSize(constraints.maxWidth, constraints.maxHeight)
        }

        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.mapIndexed { index,  measurable ->
            val physicsParentData = (measurable.parentData as? PhysicsParentData) ?: PhysicsParentData()
            Log.d(TAG, "PhysicsLayout: init : $initialized")
            if (!initialized){
                parentDataList.add(index, physicsParentData)
                Log.d(TAG, "PhysicsLayout: addParentData: $physicsParentData")
            }
            measurable.measure(childConstraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight){
            placeables.forEachIndexed { i, placeable: Placeable ->
                // 正确设置各body大小
                parentDataList[i].width = placeable.width
                parentDataList[i].height = placeable.height

                val x = physics.metersToPixels(parentDataList[i].x).toInt() - placeable.width / 2
                val y = physics.metersToPixels(parentDataList[i].y).toInt() - placeable.height / 2

                val c = recompose // 这行代码什么用也没有，目的是触发重新 Layout

//                Log.d(TAG, "PhysicsLayout: x : $x y : $y")
//                Log.d(TAG, "PhysicsLayout: $recompose")
                placeable.place(x, y)
            }
        }.also {
            // 各类初始化只进行一次即可
            if (!initialized) {
                initialized = true
            }
        }
    }
}


val physicsConfig = PhysicsConfig()
@Composable
fun PhysicsLayoutTest() {
    PhysicsLayout(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(Color.LightGray)) {
//        RandomColorBox(modifier = Modifier
//            .size(40.dp)
//            .physics(physicsConfig))
        RandomColorBox(modifier = Modifier
            .clip(CircleShape)
            .size(50.dp)
            .physics(physicsConfig, 300f, 500f))
//        RandomColorBox(modifier = Modifier
//            .size(60.dp)
//            .physics(physicsConfig.copy(shape = PhysicsShape.CIRCLE)))
//        var checked by remember {
//            mutableStateOf(false)
//        }
//        Checkbox(checked = checked, onCheckedChange = { checked = it })
//        Card(modifier = Modifier
//            .clip(CircleShape)
//            .physics(physicsConfig.copy(shape = PhysicsShape.CIRCLE), initialX = 200f)) {
//            Image(painter = painterResource(id = R.drawable.bg_avator), contentDescription = "", modifier = Modifier.size(100.dp))
//        }
//        LazyColumn(modifier = Modifier
//            .height(100.dp)
//            .background(MaterialColors.Orange200)
//            .physics(physicsConfig, initialY = 300f)){
//            items(10){
//                Text(text = "FunnySaltyFish", modifier = Modifier.padding(8.dp), fontWeight = W500, fontSize = 18.sp)
//            }
//        }
    }
}