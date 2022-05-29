package com.funny.compose.study.ui.physics_layout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    content : @Composable PhysicsLayoutScope.()->Unit,
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

    // 这一行很重要？会强制重组
//    val places by remember(recompose) {
//        mutableStateOf(parentDataList.map { physics.metersToPixels(it.x).toInt() to physics.metersToPixels(it.y).toInt() })
//    }

    LaunchedEffect(initialized){
        Log.d(TAG, "PhysicsLayout: launchedEffect ${parentDataList.size} ${physics.width}")
        if (parentDataList.isEmpty()) return@LaunchedEffect
        if (physics.width * physics.height == 0) return@LaunchedEffect
        physics.createWorld { body, i ->
            parentDataList[i].body = body
            Log.d(TAG, "PhysicsLayout: createBody: $body")
        }
        physics.giveRandomImpulse()
    }

    LaunchedEffect(key1 = Unit){
        while (true){
            delay(16)
            physics.step()
//            Log.d(TAG, "PhysicsLayout: ${physics.world?.bodyList?.position}")
            recompose++
        }
    }

    Layout(content = { PhysicsLayoutScopeInstance.content() }, modifier = modifier){ measurables, constraints ->
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
        if (!initialized) {
            initialized = true
        }
        layout(constraints.maxWidth, constraints.maxHeight){
            placeables.forEachIndexed { i, placeable ->
                // 正确设置各body大小
                parentDataList[i].width = placeable.width
                parentDataList[i].height = placeable.height

                val x = physics.metersToPixels(parentDataList[i].x).toInt()
                val y = physics.metersToPixels(parentDataList[i].y).toInt()

                val c = recompose // 这行代码什么用也没有，目的是触发重新Layout

//                Log.d(TAG, "PhysicsLayout: x : $x y : $y")
//                Log.d(TAG, "PhysicsLayout: $recompose")
                placeable.place(x, y)
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
        RandomColorBox(modifier = Modifier
            .size(40.dp)
            .physics(physicsConfig))
        RandomColorBox(modifier = Modifier.size(50.dp).physics(physicsConfig, 100f, 100f))
    }
}