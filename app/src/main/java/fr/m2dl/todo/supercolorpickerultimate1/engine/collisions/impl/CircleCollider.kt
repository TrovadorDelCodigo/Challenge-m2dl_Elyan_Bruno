package fr.m2dl.todo.supercolorpickerultimate1.engine.collisions.impl

import fr.m2dl.todo.supercolorpickerultimate1.engine.collisions.Collider
import kotlin.math.sqrt

class CircleCollider(
        var globalX: Float = 0f,
        var globalY: Float = 0f,
        var radius: Float = 0f
): Collider {

    override fun collidesWith(collider: Collider): Boolean {
        return when (collider) {
            is CircleCollider -> collidesWith(collider)
            is RectCollider -> collidesWith(collider)
            else              -> throw IllegalStateException("Bad Collider subclass")
        }
    }

    private fun collidesWith(collider: CircleCollider): Boolean {
        val distX = globalX - collider.globalX
        val distY = globalY - collider.globalY
        val dist = sqrt(distX * distX + distY * distY)

        return dist <= radius + collider.radius
    }

    private fun collidesWith(collider: RectCollider): Boolean {
        var testX = globalX
        var testY = globalY

        if (globalX < collider.globalX) {
            testX = collider.globalX
        } else if (globalX > collider.globalX + collider.width) {
            testX = collider.globalX + collider.width
        }

        if (globalY < collider.globalY) {
            testY = collider.globalY
        } else if (globalY > collider.globalY + collider.height) {
            testY = collider.globalY + collider.height
        }

        val distX = globalX - testX
        val distY = globalY - testY
        val dist = sqrt(distX * distX + distY * distY)

        return dist <= radius
    }
}
