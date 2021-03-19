package fr.m2dl.todo.supercolorpickerultimate1.engine.impl

import android.graphics.Canvas
import android.content.res.Resources
import android.os.Bundle
import fr.m2dl.todo.supercolorpickerultimate1.engine.*
import fr.m2dl.todo.supercolorpickerultimate1.engine.events.*
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.CollidableGameObject
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject
import fr.m2dl.todo.supercolorpickerultimate1.engine.signals.impl.SignalManagerImpl

class GameEngineImpl(
    override var framesPerSecond: Int,
    private val gameDrawingSurface: GameDrawingSurface,
    override val resources: Resources
) : GameEngine {

    override val viewport: GameViewport
        get() = gameDrawingSurface.viewport

    override val signalManager = SignalManagerImpl()

    private var savedInstanceState: Bundle? = null

    private lateinit var gameEngineThread: GameEngineThread

    private var gameObjectTree: GameObject? = null

    private var newScene = false

    override fun setSceneRoot(gameObject: GameObject) {
        if (gameObjectTree != null) {
            gameObjectTree?.removeChildren()
            deinitGameObject(gameObjectTree!!)
        }
        gameObjectTree = gameObject
        initGameObject(gameObject)
    }

    override fun start() {
        if (gameObjectTree != null) {
            gameEngineThread = GameEngineThread(gameDrawingSurface, this)
            gameEngineThread.running = true
            gameEngineThread.start()
        } else {
            throw IllegalStateException("Set GameObjects tree root before calling start().")
        }
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        gameEngineThread.running = false
        gameEngineThread.join()
    }

    override fun initGameObject(gameObject: GameObject) {
        if (gameObject is CollidableGameObject<*>) {
            gameObject.initInternals(this, this, viewport)
        } else {
            gameObject.initInternals(this, viewport)
        }
        gameObject.init()
        if (savedInstanceState != null && gameObject is Saveable) {
            gameObject.load(savedInstanceState!!)
        }
    }

    override fun deinitGameObject(gameObject: GameObject) {
        gameObject.deinit()
    }

    fun updateGameObjects(delta: Long) {
        updateGameObject(gameObjectTree!!, delta)
    }

    private fun updateGameObject(gameObject: GameObject, delta: Long) {
        gameObject.update(delta)
        gameObject.children.forEachOptimized {
            updateGameObject(it, delta)
        }
    }

    fun drawGameObjects(canvas: Canvas) {
        drawGameObject(gameObjectTree!!, canvas)
    }

    private fun drawGameObject(gameObject: GameObject, canvas: Canvas) {
        gameObject.draw(canvas)
        gameObject.children.forEachOptimized {
            drawGameObject(it, canvas)
        }
    }

    override fun restoreState(bundle: Bundle) {
        savedInstanceState = bundle
    }

    override fun saveState(bundle: Bundle) {
        saveGameObjectStates(gameObjectTree!!, bundle)
    }

    private fun saveGameObjectStates(gameObject: GameObject, bundle: Bundle) {
        if (gameObject is Saveable) {
            gameObject.save(bundle)
        }
        gameObject.children.forEachOptimized {
            saveGameObjectStates(it, bundle)
        }
    }

    override fun checkCollisions(collidableGameObject: CollidableGameObject<*>): List<GameObject> {
        val collisions = mutableListOf<GameObject>()
        checkCollision(collidableGameObject, gameObjectTree!!, collisions)
        return collisions
    }

    private fun checkCollision(
        collidableGameObject: CollidableGameObject<*>,
        gameObject: GameObject,
        collisions: MutableList<GameObject>
    ) {
        if (collidableGameObject !== gameObject &&
            gameObject is CollidableGameObject<*> &&
            collidableGameObject.collider.collidesWith(gameObject.collider)
        ) {
            collisions += gameObject
        }
        gameObject.children.forEachOptimized {
            checkCollision(collidableGameObject, it, collisions)
        }
    }

    override fun notifyEvent(event: GameInputEvent) {
        when (event) {
            is AccelerometerEvent ->
                notifyAccelerometerEvent(gameObjectTree!!, event)
            is TouchScreenEvent ->
                notifyTouchScreenEvent(gameObjectTree!!, event)
        }
    }

    private fun notifyAccelerometerEvent(gameObject: GameObject, event: AccelerometerEvent) {
        if (gameObject is AccelerometerEventListener) {
            gameObject.onAccelerometerEvent(event)
        }
        gameObject.children.forEachOptimized {
            notifyAccelerometerEvent(it, event)
        }
    }

    private fun notifyTouchScreenEvent(gameObject: GameObject, event: TouchScreenEvent) {
        if (gameObject is TouchScreenEventListener) {
            gameObject.onTouchScreenEvent(event)
        }
        gameObject.children.forEachOptimized {
            notifyTouchScreenEvent(it, event)
        }
    }
}
