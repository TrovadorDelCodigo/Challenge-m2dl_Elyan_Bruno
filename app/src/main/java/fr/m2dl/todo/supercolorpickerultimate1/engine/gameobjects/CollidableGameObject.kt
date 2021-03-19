package fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects

import fr.m2dl.todo.supercolorpickerultimate1.engine.collisions.Collider
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameEngineCollisions
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameEngineContext
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameViewport

/**
 * Represents a GameObject that can be checked for collisions.
 * Don't forget to set the bounds of the collider in each update() call.
 */
abstract class CollidableGameObject<T: Collider>(
        initialX: Float,
        initialY: Float,
        val collider: T
): GameObject(initialX, initialY) {

    private lateinit var gameEngineCollisions: GameEngineCollisions

    fun initInternals(gameEngineCollisions: GameEngineCollisions,
                      gameEngineContext: GameEngineContext, viewport: GameViewport) {
        super.initInternals(gameEngineContext, viewport)
        this.gameEngineCollisions = gameEngineCollisions
    }

    fun checkCollisions(): List<GameObject> {
        return gameEngineCollisions.checkCollisions(this)
    }
}
