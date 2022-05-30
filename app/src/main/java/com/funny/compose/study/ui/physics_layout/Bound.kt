package com.funny.compose.study.ui.physics_layout

import org.jbox2d.dynamics.Body

/**
 * A bound around the edge of the view
 */
data class Bound(
    val widthInPixels: Float,
    val heightInPixels: Float,
    val body: Body,
    val side: Side
) {
    enum class Side {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM
    }
}
