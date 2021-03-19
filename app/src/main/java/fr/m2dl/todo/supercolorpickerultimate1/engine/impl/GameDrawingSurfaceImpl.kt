package fr.m2dl.todo.supercolorpickerultimate1.engine.impl

import android.graphics.Canvas
import android.view.SurfaceView
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameDrawingSurface
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameViewport

class GameDrawingSurfaceImpl(
        private  val surfaceView: SurfaceView
): GameDrawingSurface {

    // TODO Use canvas size !
    override val viewport = GameViewport(surfaceView.width.toFloat(), surfaceView.height.toFloat())

    override fun lockAndGetCanvas(): Canvas? =
        surfaceView.holder.lockCanvas()

    override fun unlockCanvas(canvas: Canvas) {
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}
