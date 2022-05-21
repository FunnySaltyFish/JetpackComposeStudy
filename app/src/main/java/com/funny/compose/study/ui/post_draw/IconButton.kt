package com.funny.compose.study.ui.post_draw

import android.content.Context
import android.graphics.*
import android.util.Log
import com.funny.compose.study.ui.post_draw.BitmapUtils.getBitmapFromResources
import com.funny.compose.study.ui.post_draw.BitmapUtils.getScaledBitmap

class IconButton(
    var context: Context,
    var resId: Int,
    var x: Float, //左上角位置
    var y: Float,
    var width: Int,
    var height: Int,
    var imageWidth: Int,
    var imageHeight: Int,
    var srcColor: Int
) {
    var backgroundColor = Color.parseColor("#6e6c6f")
    var paddingLeft = 0
    var paddingRight = 0
    var paddingTop = 0
    var paddingBottom = 0
    var imageX = 0f
    var imageY = 0f
    lateinit var bitmap: Bitmap
    lateinit var paint: Paint
    private var xfermode: PorterDuffXfermode? = null
    var clickProgress = 0
    var transformProgress = 0
    var progressRect: RectF? = null
    private val maxScaleTimes = 1.5f
    var direction: NavChangeDirection? = null

    var id = 0

    companion object {
        private const val TAG = "IconButton"
    }

    private fun initVars() {
        imageX = x + (width - imageWidth) / 2f
        imageY = y + (height - imageHeight) / 2f
    }

    private fun initGraphics() {
        bitmap = getBitmapFromResources(context.resources, resId)
        bitmap = getScaledBitmap(
            bitmap, imageWidth - paddingLeft - paddingRight, imageHeight - paddingTop - paddingBottom
        )

        paint = Paint().also {
            it.color = srcColor
            it.alpha = 0
            it.isAntiAlias = true
        }
        progressRect = RectF()
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    fun isClicked(actionX: Float, actionY: Float): Boolean {
        return actionX >= imageX && actionX <= imageX + imageWidth && actionY >= imageY && actionY <= imageY + imageHeight
    }

    /**
     * @description 绘制基本的bitmap和动画
     * @param canvas 绘制的画板
     * @return void
     */
    fun drawSelf(canvas: Canvas) {
        if (clickProgress > 0) { //正在处于被点击状态
            paint.alpha = 255
            canvas.save()
            if (clickProgress in 60..80) {

                val scale = 1 + (maxScaleTimes - 1f) / 20f * (clickProgress - 60)
                Log.d(TAG, "$id drawSelf 6-8: px:${scale}")
                canvas.scale(
                    scale,
                    scale,
                    centerImageX,
                    centerImageY
                )
            } else if(clickProgress > 80) {
                val scale = maxScaleTimes - (maxScaleTimes - 1f) / 20f * (clickProgress - 80)
                Log.d(TAG, "$id drawSelf: px 8-10:${maxScaleTimes}")
                canvas.scale(
                    scale,
                    scale,
                    centerImageX,
                    centerImageY
                )
            }

            paint.alpha = 255
            val centerCircle = if(direction==NavChangeDirection.LEFT_TO_RIGHT){//左向右减少
                imageLeftCenter
            }else{
                imageRightCenter
            }
            val radius = clickProgress / 100f * 2.236f * imageWidth / 2
            canvas.drawBitmap(bitmap, imageX, imageY, paint)
            paint.style = Paint.Style.FILL
            val layerId =
                canvas.saveLayer(imageX, imageY, imageX + imageWidth, imageY + imageHeight, paint)
            paint.color = srcColor
            canvas.drawCircle(centerCircle[0],centerCircle[1],radius, paint)
            paint.xfermode = xfermode
            paint.color = backgroundColor
            canvas.drawBitmap(bitmap, imageX, imageY, paint)
            paint.xfermode = null
            canvas.restoreToCount(layerId)
            //Log.d(TAG, "drawSelf: transformProgress:${transformProgress}")
            //Log.d(TAG, "drawSelf: radius:${radius}")

            //恢复到缩放前状态
            canvas.restore()
            paint.color = srcColor
            paint.style = Paint.Style.STROKE
            if (clickProgress <= 50) {
                paint.alpha = clickProgress * 255 / 100
                paint.strokeWidth = 24 * clickProgress / 100f
            } else {
                paint.alpha = 255 - clickProgress * 255 / 100
                paint.strokeWidth = 24 * (1 - clickProgress / 100f)
            }
            canvas.drawCircle(
                imageX + imageWidth / 2f,
                imageY + imageHeight / 2f,
                0.75f * imageHeight * clickProgress / 100f,
                paint
            )
        } else {
            paint.color = backgroundColor
            canvas.drawBitmap(bitmap, imageX, imageY, paint)
        }

        if (transformProgress > 0) {
            paint.alpha = 255
            val centerCircle = if(direction==NavChangeDirection.LEFT_TO_RIGHT){//左向右减少
                imageRightCenter
            }else{
                imageLeftCenter
            }
            val radius = (100f - transformProgress) / 100f * 2.236f * imageWidth / 2
            paint.style = Paint.Style.FILL
            val layerId =
                canvas.saveLayer(imageX, imageY, imageX + imageWidth, imageY + imageHeight, paint)
            paint.color = srcColor
            canvas.drawCircle(centerCircle[0],centerCircle[1],radius, paint)
            paint.xfermode = xfermode
            paint.color = backgroundColor
            canvas.drawBitmap(bitmap, imageX, imageY, paint)
            paint.xfermode = null
            canvas.restoreToCount(layerId)
            //Log.d(TAG, "drawSelf: transformProgress:${transformProgress}")
            //Log.d(TAG, "drawSelf: radius:${radius}")
        }
    }

    val imageLeftCenter: FloatArray
        get() = floatArrayOf(imageX, imageY + imageHeight / 2f)
    val imageRightCenter: FloatArray
        get() = floatArrayOf(imageX + imageWidth, imageY + imageHeight / 2f)

    private val centerImageX : Float
        get() = imageX + imageWidth / 2f

    private val centerImageY : Float
        get() = imageY + imageHeight / 2f

    init {
        initVars()
        initGraphics()
    }
}

fun initIconButtons(iconIds: IntArray, mContext: Context, mViewWidth:Int, mViewHeight:Int, imageWidth:Int, imageHeight:Int, highlightColor:Int): List<IconButton> {
    val itemWidth = mViewWidth / iconIds.size
    return iconIds.mapIndexed { i, id ->
        val currentX = i * itemWidth
        IconButton(
            mContext,
            id,
            currentX.toFloat(),
            0f,
            itemWidth,
            mViewHeight,
            imageWidth,
            imageHeight,
            highlightColor
        ).apply {
            this.id = i
        }
    }
}