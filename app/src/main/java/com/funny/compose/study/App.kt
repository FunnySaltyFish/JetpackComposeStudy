package com.funny.compose.study

import android.app.Application
import com.funny.compose.study.ui.game.SnakeAssets
import com.funny.data_saver.core.DataSaverConverter
import com.funny.data_saver.core.DataSaverInterface
import com.funny.data_saver.core.DataSaverPreferences

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        ctx = this
        DataSaverUtils = DataSaverPreferences(this)

        DataSaverConverter.registerTypeConverters(save = SnakeAssets.Saver, restore = SnakeAssets.Restorer)
    }

    companion object {
        lateinit var ctx: Application
        lateinit var DataSaverUtils: DataSaverInterface
    }
}