package com.funny.compose.study.ui.physics_layout

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.*
import org.jbox2d.dynamics.contacts.Contact
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.properties.Delegates

class ComposableGroup(var physicsParentDatas: ArrayList<PhysicsParentData>){
    val childCount : Int
        get() = physicsParentDatas.size
    fun getChildAt(index : Int) = physicsParentDatas[index]
}

/**
 * Implementation for physics layout is found here, since we want to offer the main
 * layouts without requiring further extension (LinearLayout, RelativeLayout, etc.)
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class Physics constructor(val composableGroup: ComposableGroup) {

    companion object {
        private val TAG = Physics::class.java.simpleName
        const val NO_GRAVITY = 0.0f
        const val MOON_GRAVITY = 1.6f
        const val EARTH_GRAVITY = 9.8f
        const val JUPITER_GRAVITY = 24.8f

        // Size in DP of the bounds (world walls) of the view
        private const val BOUND_SIZE_DP = 20
        private const val FRAME_RATE = 1 / 60f

        const val ID_BOUND_TOP = 0
        const val ID_BOUND_LEFT = 1
        const val ID_BOUND_RIGHT = 2
        const val ID_BOUND_BOTTOM = 3

    }

    private val debugDraw = false
    private val debugLog = false

    /**
     * Set the number of velocity iterations the world will perform at each step.
     * Default is 8
     */
    var velocityIterations = 8

    /**
     * Set the number of position iterations the world will perform at each step.
     * Default is 3
     */
    var positionIterations = 3

    /**
     * Set the number of pixels per meter. Basically makes the world feel bigger or smaller
     * Default is 20dp. More pixels per meter = ui feeling bigger in the world (faster movement)
     */
    var pixelsPerMeter = 0f

    /**
     * Get the current Box2D [World] controlling the physics of this view
     */
    var world: World? = null
        private set

    /**
     * Enable/disable physics on the view
     */
    var isPhysicsEnabled = true

    /**
     * Enable/disable fling for this View
     */
    var isFlingEnabled = false

    /**
     * Enables/disables if the view has bounds or not
     */
    var hasBounds = true

    private var boundsSize = 1f
    private val bounds = mutableListOf<Bound>()
    private var gravityX = 0.0f
    private var gravityY = EARTH_GRAVITY

    private val debugPaint: Paint by lazy {
        val paint = Paint()
        paint.color = Color.MAGENTA
        paint.style = Paint.Style.STROKE
        paint
    }

    var density by Delegates.notNull<Float>()
    var width = 0
    var height = 0

    private var viewBeingDragged: View? = null
    private var onFlingListener: OnFlingListener? = null
    private var onCollisionListener: OnCollisionListener? = null
    private var onPhysicsProcessedListeners = mutableListOf<OnPhysicsProcessedListener>()

    private val contactListener: ContactListener = object : ContactListener {
        override fun beginContact(contact: Contact) {
            if (onCollisionListener != null) {
                onCollisionListener!!.onCollisionEntered(contact.fixtureA.m_userData as Int,
                        contact.fixtureB.m_userData as Int)
            }
        }

        override fun endContact(contact: Contact) {
            if (onCollisionListener != null) {
                onCollisionListener!!.onCollisionExited(contact.fixtureA.m_userData as Int,
                        contact.fixtureB.m_userData as Int)
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {}
        override fun postSolve(contact: Contact, impulse: ContactImpulse) {}
    }

    fun step(){
        world?.step(FRAME_RATE, velocityIterations, positionIterations)
    }

    fun metersToPixels(meters: Float): Float {
        return meters * pixelsPerMeter
    }

    fun pixelsToMeters(pixels: Float): Float {
        return pixels / pixelsPerMeter
    }

    private fun radiansToDegrees(radians: Float): Float {
        return radians / 3.14f * 180f
    }

    private fun degreesToRadians(degrees: Float): Float {
        return degrees / 180f * 3.14f
    }

    /**
     * Call this every time your view gets a call to onSizeChanged so that the world can
     * respond to this change.
     */
    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    /**
     * Call this in your ViewGroup if you plan on using fling
     * @return true if consumed, false otherwise
     */
    fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!isFlingEnabled) {
            return false
        }
        return true
    }

    /**
     * Recreate the physics world. Will traverse all views in the hierarchy, get their current
     * PhysicsConfigs and create a body in the world. This will override the current world if it exists.
     */
     fun createWorld(onBodyCreatedListener : (Body, Int) -> Unit) {
        // Null out all the bodies
        val oldBodiesArray = ArrayList<Body?>()
        for (i in 0 until composableGroup.childCount) {
            val body = composableGroup.getChildAt(i).body
            oldBodiesArray.add(body)
            composableGroup.getChildAt(i).body = null
        }
        bounds.clear()
        if (debugLog) {
            Log.d(TAG, "createWorld")
        }
        world = World(Vec2(gravityX, gravityY)).apply {
            setContactListener(contactListener)
        }
        if (hasBounds) {
            enableBounds()
        }
        for (i in 0 until composableGroup.childCount) {
            val body = createBody(composableGroup.getChildAt(i), oldBodiesArray[i])
            onBodyCreatedListener(body, i)
        }
    }

    private fun enableBounds() {
        hasBounds = true
        createBounds()
    }

    private fun disableBounds() {
        hasBounds = false
        for (body in bounds) {
            world?.destroyBody(body.body)
        }
        bounds.clear()
    }

    private fun createBounds() {
        val top = createBound(
                widthInPixels = width.toFloat(),
                heightInPixels = boundsSize,
                id = ID_BOUND_TOP,
                side = Bound.Side.TOP
        )
        bounds.add(top)

        val bottom = createBound(
                widthInPixels = width.toFloat(),
                heightInPixels = boundsSize,
                id = ID_BOUND_BOTTOM,
                side = Bound.Side.BOTTOM
        )
        bounds.add(bottom)

        val left = createBound(
                widthInPixels = boundsSize,
                heightInPixels = height.toFloat(),
                id = ID_BOUND_LEFT,
                side = Bound.Side.LEFT
        )
        bounds.add(left)

        val right = createBound(
                widthInPixels = boundsSize,
                heightInPixels = height.toFloat(),
                id = ID_BOUND_RIGHT,
                side = Bound.Side.RIGHT
        )
        bounds.add(right)
    }

    private fun createBound(widthInPixels: Float, heightInPixels: Float, id: Int, side: Bound.Side): Bound {
        val bodyDef = BodyDef()
        bodyDef.type = BodyType.STATIC
        val box = PolygonShape()
        val boxWidthMeters = pixelsToMeters(widthInPixels)
        val boxHeightMeters = pixelsToMeters(heightInPixels)
        box.setAsBox(boxWidthMeters, boxHeightMeters)
        val fixtureDef = createBoundFixtureDef(box, id)
        val pair = when (side) {
            Bound.Side.TOP -> Pair(0f, -boxHeightMeters)
            Bound.Side.BOTTOM -> Pair(0f, pixelsToMeters(height.toFloat()) + boxHeightMeters)
            Bound.Side.LEFT -> Pair(-boxWidthMeters, 0f)
            Bound.Side.RIGHT -> Pair(pixelsToMeters(width.toFloat()) + boxWidthMeters, 0f)
        }
        bodyDef.position[pair.first] = pair.second
        val body = world!!.createBody(bodyDef)
        body.createFixture(fixtureDef)
        return Bound(
                widthInPixels = widthInPixels,
                heightInPixels = heightInPixels,
                body = body,
                side = side
        )
    }

    private fun createBoundFixtureDef(box: PolygonShape, id: Int): FixtureDef {
        val fixtureDef = FixtureDef()
        fixtureDef.shape = box
        fixtureDef.density = 0.5f
        fixtureDef.friction = 0.3f
        fixtureDef.restitution = 0.5f
        fixtureDef.userData = id
        return fixtureDef
    }

    private fun createBody(physicsParentData: PhysicsParentData, oldBody: Body?): Body {
        val config = physicsParentData.physicsConfig
        val bodyDef = config.bodyDef
        bodyDef.position[pixelsToMeters(physicsParentData.initialX + physicsParentData.width / 2)] = pixelsToMeters(physicsParentData.initialY + physicsParentData.height / 2)
        if (oldBody != null) {
            bodyDef.angle = oldBody.angle
            bodyDef.angularVelocity = oldBody.angularVelocity
            bodyDef.linearVelocity = oldBody.linearVelocity
            bodyDef.angularDamping = oldBody.angularDamping
            bodyDef.linearDamping = oldBody.linearDamping
        } else {
            bodyDef.angularVelocity = degreesToRadians(physicsParentData.rotation)
        }
        val fixtureDef = config.fixtureDef
        fixtureDef.shape = if (config.shape == PhysicsShape.RECTANGLE) createBoxShape(physicsParentData) else createCircleShape(physicsParentData, config)
        fixtureDef.userData = physicsParentData.id
        val body = world!!.createBody(bodyDef)
        body.createFixture(fixtureDef)
        physicsParentData.body = body
        return body
    }

    private fun createBoxShape(physicsParentData: PhysicsParentData): PolygonShape {
        val box = PolygonShape()
        val boxWidth = pixelsToMeters(physicsParentData.width / 2.toFloat())
        val boxHeight = pixelsToMeters(physicsParentData.height / 2.toFloat())
        box.setAsBox(boxWidth, boxHeight)
        return box
    }

    private fun createCircleShape(physicsParentData: PhysicsParentData, config: PhysicsConfig): CircleShape {
        val circle = CircleShape()
        //radius was not set, set it to max of the width and height
        if (config.radius == -1f) {
            config.radius = max(physicsParentData.width / 2f, physicsParentData.height / 2f)
        }
        circle.m_radius = pixelsToMeters(config.radius)
        return circle
    }

    /**
     * Gives a random impulse to all the view bodies in the layout. Really just useful for testing,
     * but try it out if you want :)
     */
    fun giveRandomImpulse() {
        var body: Body?
        var impulse: Vec2
        val random = Random()
        for (i in 0 until composableGroup.childCount) {
            impulse = Vec2((random.nextInt(1000) - 1000).toFloat(), (random.nextInt(1000) - 1000).toFloat())
            body = composableGroup.getChildAt(i).body
            body?.applyLinearImpulse(impulse, body.position)
        }
    }

    private fun translateBodyToView(body: Body, view: View) {
        body.setTransform(
                Vec2(pixelsToMeters(view.x + view.width / 2),
                        pixelsToMeters(view.y + view.height / 2)),
                body.angle)
    }

    /**
     * Sets the fling listener
     *
     * @param onFlingListener listener that will respond to fling events
     */
    fun setOnFlingListener(onFlingListener: OnFlingListener?) {
        this.onFlingListener = onFlingListener
    }

    /**
     * Sets the collision listener
     *
     * @param onCollisionListener listener that will listen for collisions
     */
    fun setOnCollisionListener(onCollisionListener: OnCollisionListener?) {
        this.onCollisionListener = onCollisionListener
    }

    /**
     * Sets the size of the bounds and enables the bounds
     *
     * @param size the size of the bounds in dp
     */
    fun setBoundsSize(size: Float) {
        boundsSize = size * density
        if (hasBounds) {
            disableBounds()
        }
        enableBounds()
    }

    /**
     * Sets the gravity in the x direction for the world. Positive is right, negative is left.
     */
    fun setGravityX(newGravityX: Float) {
        setGravity(newGravityX, gravityY)
    }

    /**
     * The gravity in the x direction for the world. Positive is right, negative is left.
     */
    fun getGravityX(): Float {
        return gravityX
    }

    /**
     * Sets the gravity in the y direction for the world. Positive is down, negative is up.
     */
    fun setGravityY(newGravityY: Float) {
        setGravity(gravityX, newGravityY)
    }

    /**
     * The gravity in the x direction for the world. Positive is right, negative is left.
     */
    fun getGravityY(): Float {
        return gravityY
    }

    /**
     * Sets the gravity for the world. Positive x is right, negative is left. Positive
     * y is down, negative is up.
     */
    fun setGravity(gravityX: Float, gravityY: Float) {
        this.gravityX = gravityX
        this.gravityY = gravityY
        world?.gravity = Vec2(gravityX, gravityY)
    }

    /**
     * Returns the gravity of the world. Returns null if the world doesn't exist yet (view hasn't
     * called onLayout)
     */
    fun getGravity(): Vec2? {
        return world?.gravity
    }

    /**
     * Add a physics process listener
     */
    fun addOnPhysicsProcessedListener(listener: OnPhysicsProcessedListener) {
        onPhysicsProcessedListeners.add(listener)
    }

    /**
     * Remove a physics process listener
     */
    fun removeOnPhysicsProcessedListener(listener: OnPhysicsProcessedListener?) {
        onPhysicsProcessedListeners.remove(listener)
    }

    /**
     * Interface that allows hooks into the layout so that you can process or modify physics bodies each time that JBox2D processes physics
     */
    interface OnPhysicsProcessedListener {

        /**
         * Physics has been processed. Commence doing things that you want to do such as applying additional forces
         * @param physics the [Physics] that belongs to the view
         * @param world the Box2d world
         */
        fun onPhysicsProcessed(physics: Physics, world: World)
    }

    /**
     * A controller that will receive the drag events.
     */
    interface OnFlingListener {
        fun onGrabbed(grabbedView: View?)
        fun onReleased(releasedView: View?)
    }

    /**
     * Alerts you to collisions between views within the layout
     */
    interface OnCollisionListener {
        /**
         * Called when a collision is entered between two bodies. ViewId can also be
         * R.id.physics_layout_bound_top,
         * R.id.physics_layout_bound_bottom, R.id.physics_layout_bound_left, or
         * R.id.physics_layout_bound_right.
         * If view was not assigned an id, the return value will be [View.NO_ID].
         *
         * @param viewIdA view id of body A
         * @param viewIdB view id of body B
         */
        fun onCollisionEntered(viewIdA: Int, viewIdB: Int)

        /**
         * Called when a collision is exited between two bodies. ViewId can also be
         * R.id.physics_layout_bound_top,
         * R.id.physics_layout_bound_bottom, R.id.physics_layout_bound_left, or
         * R.id.physics_layout_bound_right.
         * If view was not assigned an id, the return value will be [View.NO_ID].
         *
         * @param viewIdA view id of body A
         * @param viewIdB view id of body B
         */
        fun onCollisionExited(viewIdA: Int, viewIdB: Int)
    }
}
