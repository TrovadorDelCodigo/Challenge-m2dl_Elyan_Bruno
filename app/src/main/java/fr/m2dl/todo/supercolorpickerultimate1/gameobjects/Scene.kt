package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject

class Scene: GameObject() {

    private val paint = Paint().also { it.color = Color.BLACK }

    override fun init() {
        addChild(ColorMatrix())
        addChild(Picture())
    }

    override fun deinit() { }

    override fun update(delta: Long) { }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, viewport.width, viewport.height, paint)
    }
}
