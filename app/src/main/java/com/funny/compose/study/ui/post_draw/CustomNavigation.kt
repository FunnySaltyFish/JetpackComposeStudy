package com.funny.compose.study.ui.post_draw

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.drawable.Icon
import android.util.Log
import androidx.annotation.IntRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.funny.bottomnavigation.FunnyBottomNavigation
import com.funny.cmaterialcolors.MaterialColors
import com.funny.compose.study.R
import com.funny.compose.study.ui.post_layout.VerticalScopeInstance.weight
import kotlin.random.Random

data class NavState(var selectedItemIndex: Int = 0)
data class NavConfig(val color: Color)
private const val TAG = "CustomNavigation"

@Composable
fun CustomNavTest() {
    val navState by remember {
        mutableStateOf( NavState(0))
    }
    val navConfig = NavConfig(MaterialColors.Blue600)
    val icons = intArrayOf(R.drawable.ic_bin, R.drawable.ic_run, R.drawable.ic_favorites)
//    CustomNavigation(
//        modifier = Modifier.fillMaxWidth(),
//        navState = navState,
//        navConfig = navConfig,
//        icons = icons,
//        iconSize = Size(80f, 80f)
//    ) {}
    AndroidView(
        factory = { context ->
            FunnyBottomNavigation(context, null).apply {
                initIconButtons(icons)
                onItemClickListener = object : FunnyBottomNavigation.OnItemClickListener{
                    override fun onClick(position: Int) {
                        Log.d(TAG, "onClick: $position clicked!")
                    }
                }
            }
        },
        update = {

        }
    )
}

@Composable
fun CustomNavigation(
    modifier: Modifier,
    icons: IntArray,
    iconSize: Size,
    navState: NavState,
    onItemClick: (Int) -> Unit = {},
    navConfig: NavConfig,
    content: @Composable RowScope.() -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalContext.current.resources
    var mNeedToClickIconButton : IconButton? = null
    var mLastClickedIconButton : IconButton? by remember {
        mutableStateOf(null)
    }

    var animating by remember {
        mutableStateOf(false)
    }

    val progress by animateIntAsState(
        targetValue = if (animating) 100 else 0,
        animationSpec = tween(
            1000,

        ),
        finishedListener = {
            Log.d(TAG, "CustomNavigation: animation finished")
        })

    LaunchedEffect(key1 = progress){
        Log.d(TAG, "CustomNavigation: progress: $progress")
    }

//    val progress = 0

    var mLastPage = 0
    var iconButtons : List<IconButton> = arrayListOf()

    fun startClickAnimation() {
        if (!animating) {
            animating = true
        }
    }

    /**
     * 清空已经进行的动画参数
     */
    fun resetProgress() {
        mNeedToClickIconButton?.let {
            it.clickProgress = 100
            it.transformProgress = 0
        }
        mLastClickedIconButton?.let {
            it.clickProgress = 0
            it.transformProgress = 0
        }
    }

    fun moveTo(page: Int, hasAnimation: Boolean = true, performClick: Boolean = true) {
        require(!(page < 0 || page >= iconButtons.size)) { "Illegal page index! Please make sure that page is from 0 to (the number of buttons - 1)." }
        if (animating) return
        if (mLastPage != page) {
            mNeedToClickIconButton?.clickProgress = 0 //在开始动画之前先把之前完成的点击进度清零
            mLastClickedIconButton?.clickProgress = 0

            mLastClickedIconButton = iconButtons[mLastPage]
            mNeedToClickIconButton = iconButtons[page]
            val direction =
                if (mNeedToClickIconButton!!.imageX < mLastClickedIconButton!!.imageX) NavChangeDirection.RIGHT_TO_LEFT else NavChangeDirection.LEFT_TO_RIGHT
            mLastClickedIconButton!!.direction = (direction)

            mNeedToClickIconButton!!.direction = (direction)
            if (hasAnimation) startClickAnimation() else resetProgress()
            mLastPage = page
        }
        if (performClick) {
            onItemClick(page)
        }
    }

    Row(modifier
        .height(56.dp)
        .drawWithCache {
            Log.d(TAG, "CustomNavigation: 绘制部分初始化")
            val paint = Paint()
                .asFrameworkPaint()
                .apply {
                    color = navConfig.color.value.toInt()
                }
            val transformPathMeasure = PathMeasure()
            val w = size.width
            val h = size.height
            iconButtons = initIconButtons(
                icons,
                context,
                w.toInt(),
                h.toInt(),
                iconSize.width.toInt(),
                iconSize.height.toInt(),
                navConfig.color.value.toInt()
            )

            mLastClickedIconButton = iconButtons
                .get(mLastPage)
                .also { it.clickProgress = (100) }
            mNeedToClickIconButton = null

            val transformPaths = arrayListOf<Path>()

            onDrawWithContent {
                drawIntoCanvas { canvas ->
                    if (icons.isEmpty()) return@drawIntoCanvas
                    iconButtons.forEach {
                        it.drawSelf(canvas.nativeCanvas)
                    }
                    drawTransformAnimation(
                        canvas.nativeCanvas,
                        mLastClickedIconButton,
                        mNeedToClickIconButton,
                        progress,
                        transformPathMeasure = transformPathMeasure,
                        transformPaths = transformPaths,
                        highlightColor = navConfig.color.value.toInt(),
                        imageHeight = iconSize.height.toInt(),
                        mPaint = paint
                    )
                }
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    iconButtons.forEach {
                        if (it.isClicked(tapOffset.x, tapOffset.y)) {
                            Log.d(TAG, "CustomNavigation: ${it.id} is clicked!")
                            moveTo(it.id)
                            return@forEach
                        }
                    }
                }
            )
        }
        .selectableGroup(), content = content, horizontalArrangement = Arrangement.SpaceBetween)
}

private fun DrawScope.drawTransformAnimation(
    canvas: Canvas,
    mLastClickedIconButton: IconButton?,
    mNeedToClickIconButton: IconButton?,
    @IntRange(from = 0, to = 100) progress:  Int,
    transformPaths : ArrayList<Path>,
    imageHeight : Int,
    highlightColor : Int,
    mPaint : android.graphics.Paint,
    transformPathMeasure: PathMeasure
){
    if (mLastClickedIconButton == null || mNeedToClickIconButton == null) return
    if (mNeedToClickIconButton === mLastClickedIconButton) return
    Log.d(TAG, "drawTransformAnimation: ")
    val startX : Float
    val startY : Float
    val endX : Float
    val endY : Float
    if (progress == 0) {
        val direction =
            if (mNeedToClickIconButton.imageX < mLastClickedIconButton.imageX) NavChangeDirection.RIGHT_TO_LEFT else NavChangeDirection.LEFT_TO_RIGHT
        if (direction == NavChangeDirection.LEFT_TO_RIGHT) {
            val rightCenter = mLastClickedIconButton.imageRightCenter
            startX = rightCenter[0]
            startY = rightCenter[1]
            val leftCenter = mNeedToClickIconButton.imageLeftCenter
            endX = leftCenter[0]
            endY = leftCenter[1]
        } else {
            val rightCenter = mNeedToClickIconButton.imageRightCenter
            endX = rightCenter[0]
            endY = rightCenter[1]
            val leftCenter = mLastClickedIconButton.imageLeftCenter
            startX = leftCenter[0]
            startY = leftCenter[1]
        }
        val num = Random.nextInt(4, 8)
        transformPaths.clear()
        for (i in 0 until num) {
            val path = Path()
            path.moveTo(startX, startY)
            path.quadTo(
                getRandomFloat(startX, endX),
                getRandomFloat(startY, endY) + Random.nextInt(-imageHeight * 2, imageHeight * 2),
                endX,
                endY
            )
            transformPaths.add(path)
        }
    } else if (progress >= 1) {
        val position = FloatArray(2)
        val tan = FloatArray(2)
        var radius: Float
        for (path in transformPaths) {
            transformPathMeasure.setPath(path, false)
            transformPathMeasure.getPosTan(
                progress / 100f * transformPathMeasure.length,
                position,
                tan
            )
            radius = if (progress <= 50) {
                progress / 100f * imageHeight / 2
            } else {
                (1 - progress / 100f) * imageHeight / 2
            }
            mPaint.color = highlightColor
            canvas.drawCircle(position[0], position[1], radius, mPaint)
        }
    }
}

private fun getRandomFloat(min: Float, max: Float): Float {
    return min + Random.nextFloat() * (max - min)
}

@Composable
fun CustomNavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    enabled : Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: @Composable () -> Unit,
) {
    val ripple = rememberRipple()
    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = ripple
            )
            .weight(1f),
        contentAlignment = Alignment.Center
    ){
        icon()
    }
}