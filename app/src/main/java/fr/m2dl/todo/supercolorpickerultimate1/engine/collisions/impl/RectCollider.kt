package fr.m2dl.todo.supercolorpickerultimate1.engine.collisions.impl

import fr.m2dl.todo.supercolorpickerultimate1.engine.collisions.Collider
import kotlin.math.sqrt

class RectCollider(
        var globalX: Float = 0f,
        var globalY: Float = 0f,
        var width: Float = 0f,
        var height: Float = 0f
): Collider {

    override fun collidesWith(collider: Collider): Boolean {
        return when (collider) {
            is CircleCollider -> collidesWith(collider)
            is RectCollider -> collidesWith(collider)
            else              -> throw IllegalStateException("Bad Collider subclass")
        }
    }

    private fun collidesWith(collider: CircleCollider): Boolean {
        var testX = collider.globalX
        var testY = collider.globalY

        if (collider.globalX < globalX) {
            testX = globalX
        } else if (collider.globalX > globalX + width) {
            testX = globalX + width
        }

        if (collider.globalY < globalY) {
            testY = globalY
        } else if (collider.globalY > globalY + height) {
            testY = globalY + height
        }

        val distX = collider.globalX - testX
        val distY = collider.globalY - testY
        val dist = sqrt(distX * distX + distY * distY)

        return dist <= collider.radius
    }

    private fun collidesWith(collider: RectCollider): Boolean {
        return !(globalY + height < collider.globalY
                || globalY > collider.globalY + collider.height
                || globalX + width < collider.globalX
                || globalX > collider.globalX + collider.width)
    }
}
