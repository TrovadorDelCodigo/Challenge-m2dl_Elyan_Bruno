package fr.m2dl.todo.supercolorpickerultimate1.engine.collisions

interface Collider {
    fun collidesWith(collider: Collider): Boolean
}
