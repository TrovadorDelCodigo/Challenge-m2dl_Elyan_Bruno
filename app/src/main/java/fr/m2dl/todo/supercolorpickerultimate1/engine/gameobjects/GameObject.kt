package fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects

import android.graphics.Canvas
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameEngineContext
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameViewport
import fr.m2dl.todo.supercolorpickerultimate1.engine.forEachOptimized

abstract class GameObject(
    initialX: Float = 0.0f,
    initialY: Float = 0.0f
) {
    /**
     * X coordinate relative to the parent.
     */
    var x: Float = initialX
        private set

    /**
     * Y coordinate relative to the parent.
     */
    var y: Float = initialY
        private set

    /**
     * Global X coordinate.
     * Make sure to use global coordinates when drawing or checking collisions.
     */
    val globalX: Float
        get() = x + (parent?.x ?: 0f)

    /**
     * Global Y coordinate.
     * Make sure to use global coordinates when drawing or checking collisions.
     */
    val globalY: Float
        get() = y + (parent?.y ?: 0f)

    val resources
        get() = gameEngineContext.resources

    val signalManager
        get() = gameEngineContext.signalManager

    lateinit var viewport: GameViewport
        private set

    private lateinit var gameEngineContext: GameEngineContext

    var parent: GameObject? = null

    private val mutableChildren = mutableListOf<GameObject>()
    val children: List<GameObject> = mutableChildren

    fun initInternals(gameEngineContext: GameEngineContext, viewport: GameViewport) {
        this.viewport = viewport
        this.gameEngineContext = gameEngineContext
    }

    fun moveTo(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun addChild(gameObject: GameObject) {
        gameObject.parent = this
        mutableChildren += gameObject
        gameEngineContext.initGameObject(gameObject)
        // We place it relative to its parent by default
        gameObject.moveTo(gameObject.x, gameObject.y)
    }

    fun removeChildren() {
        children.forEachOptimized {
            it.removeChildren()
            gameEngineContext.deinitGameObject(it)
        }
        mutableChildren.removeAll { true }
    }

    /**
     * Called on GameObject creation.
     * Use it to setup GameObject children.
     * Be careful on the order you add children or the scene will not render properly.
     */
    abstract fun init()

    /**
     * Called when the GameObject is removed from the tree
     */
    abstract fun deinit()

    /**
     * Called periodically to update GameObject state
     * @param delta Time since last invocation
     */
    abstract fun update(delta: Long)

    /**
     * Called each frame to draw the GameObject
     * @param canvas The canvas to draw on
     */
    abstract fun draw(canvas: Canvas)
}
