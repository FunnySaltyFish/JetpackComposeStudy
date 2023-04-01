package com.funny.compose.study.ui.game

import androidx.compose.ui.graphics.Color
import com.funny.cmaterialcolors.MaterialColors


sealed class SnakeAssets(
    val foodColor: Color= MaterialColors.Orange700,
    val lineColor: Color= Color.LightGray.copy(alpha = 0.8f),
    val headColor: Color= MaterialColors.Red700,
    val bodyColor: Color= MaterialColors.Blue200
) {
    object SnakeAssets1: SnakeAssets()

    object SnakeAssets2: SnakeAssets(
        foodColor = MaterialColors.Purple700,
        lineColor = MaterialColors.Brown200.copy(alpha = 0.8f),
        headColor = MaterialColors.Blue700,
        bodyColor = MaterialColors.Pink300
    )

    override fun toString(): String {
        return this.javaClass.simpleName
    }

    companion object {
        val Saver = { assets: SnakeAssets ->
            assets.javaClass.simpleName
        }
        val Restorer = { str: String ->
            when(str) {
                "SnakeAssets1" -> SnakeAssets1
                "SnakeAssets2" -> SnakeAssets2
                else -> SnakeAssets1
            }
        }
    }
}