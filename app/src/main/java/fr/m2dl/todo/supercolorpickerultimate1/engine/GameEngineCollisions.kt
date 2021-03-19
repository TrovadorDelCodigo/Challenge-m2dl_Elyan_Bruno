package fr.m2dl.todo.supercolorpickerultimate1.engine

import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.CollidableGameObject
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject

interface GameEngineCollisions {
    fun checkCollisions(collidableGameObject: CollidableGameObject<*>): List<GameObject>
}
