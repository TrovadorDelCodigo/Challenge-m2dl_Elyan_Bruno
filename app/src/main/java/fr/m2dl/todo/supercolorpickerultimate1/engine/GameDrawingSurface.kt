package fr.m2dl.todo.supercolorpickerultimate1.engine

import android.graphics.Canvas

interface GameDrawingSurface {
    val viewport: GameViewport
    fun lockAndGetCanvas(): Canvas?
    fun unlockCanvas(canvas: Canvas)
}
