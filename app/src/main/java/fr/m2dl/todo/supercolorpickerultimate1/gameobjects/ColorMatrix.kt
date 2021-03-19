package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject
import kotlin.random.Random

private const val ADJUST_TOP = 100f
private const val PARTS_NUMBER = 3
class ColorMatrix : GameObject() {
    val parts = mutableListOf<Part>()

    override fun init() {
        val partSize = viewport.width / 5f
        var rnd = Random
        var partId = 0
        for (i in 1..PARTS_NUMBER) {
            for (j in 1..PARTS_NUMBER) {
                val color = Color.rgb(
                    rnd.nextInt(256),
                    rnd.nextInt(256),
                    rnd.nextInt(256))
                parts += Part(partId, globalX + partSize * j,
                    globalY + partSize * i - ADJUST_TOP, partSize, color)
                partId++
            }
        }
        parts.forEach(this::addChild)
    }

    override fun deinit() { }

    override fun update(delta: Long) { }

    override fun draw(canvas: Canvas) { }
}