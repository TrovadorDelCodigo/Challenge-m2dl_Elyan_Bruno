package fr.m2dl.todo.supercolorpickerultimate1.engine.impl

import android.graphics.Canvas
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameDrawingSurface
import kotlin.math.max
import kotlin.system.measureTimeMillis

class GameEngineThread(
        private val gameDrawingSurface: GameDrawingSurface,
        private val gameEngine: GameEngineImpl
): Thread() {

    var running = false

    private var frameTimeMillis = 0L

    private val targetTimeMillis
        get() = (1000 / gameEngine.framesPerSecond).toLong()

    override fun run() {
        var canvas: Canvas? = null

        while (running) {
            try {
                canvas = gameDrawingSurface.lockAndGetCanvas()
                synchronized(gameDrawingSurface) {
                    frameTimeMillis = measureTimeMillis {
                        gameEngine.updateGameObjects(targetTimeMillis)
                        gameEngine.drawGameObjects(canvas!!)
                    }
                }
            } catch (exp: Exception) {
                // Nothing here
            } finally {
                if (canvas != null) {
                    try {
                        gameDrawingSurface.unlockCanvas(canvas)
                        sleep(max(targetTimeMillis - frameTimeMillis, 1L))
                    } catch (exp: Exception) {
                        exp.printStackTrace()
                    }
                }
            }
        }
    }
}
