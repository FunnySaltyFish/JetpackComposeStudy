package com.funny.compose.study.ui.videoa

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.funny.cmaterialcolors.MaterialColors.Companion.BlueA700

class AViewModel : ViewModel(){
    val color = MutableLiveData(BlueA700)

    fun updateColor(newColor: Color){
        this.color.value = newColor
    }
}