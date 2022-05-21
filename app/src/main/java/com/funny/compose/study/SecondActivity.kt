package com.funny.compose.study

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.funny.compose.study.R
import java.util.*

class SecondActivity : AppCompatActivity() {
    var tv: TextView? = null
    lateinit var frameLayout: RelativeLayout
    lateinit var view1 : View
    lateinit var view2 : View
    private val TAG = "SecondActivity"
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        frameLayout = findViewById<RelativeLayout?>(R.id.frame).apply {
            setOnTouchListener { v, event ->
                Log.d(TAG, "frame::$event")
                false
            }
        }
        view1 = findViewById<View?>(R.id.view1).apply {
            setOnTouchListener { v, event ->
                Log.d(TAG, "view1::$event")
                true
            }
        }
        view2 = findViewById<View?>(R.id.view2).apply {
            setOnTouchListener { v, event ->
                Log.d(TAG, "view2::$event")
                true
            }
        }

        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
//                AlertDialog.Builder(this@SecondActivity)
//                    .setTitle("测试一下")
//                    .setMessage("哈哈哈")
//                    .create()
//                    .show()

                PopupWindow().apply {
                    width = 100
                    height = 100
                    setBackgroundColor(Color.GRAY)
                }.showAtLocation(this, Gravity.BOTTOM, 0 , 0)
            }
        }


    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }


    val time: String
        get() {
            val date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
            val day = date[Calendar.DATE]
            val month = date[Calendar.MONTH] + 1
            val year = date[Calendar.YEAR]
            return "$year-$month-$day"
        }
}