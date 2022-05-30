package com.funny.compose.study.ui.physics_layout

import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import org.jbox2d.dynamics.Body

class PhysicsParentData(
    var physicsConfig: PhysicsConfig = PhysicsConfig(),
    var initialX: Float = 0f,
    var initialY: Float = 0f,
    var width: Int = 0,
    var height: Int = 0
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this

    var body : Body? = null

    val rotation
        get() = body?.angle?.times(180f)?.div(2*Math.PI)?.toFloat() ?: 0f

    val id = hashCode()
    val x : Float
        get() = body?.position?.x ?: 0f
    val y : Float
        get() = body?.position?.y ?: 0f

}