package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject

class Scene: GameObject() {
    private val paint = Paint().also { it.color = Color.MAGENTA }

    override fun init() {
        addChild(ColorMatrix())
    }

    override fun deinit() { }

    override fun update(delta: Long) { }

    override fun draw(canvas: Canvas) { }

}

