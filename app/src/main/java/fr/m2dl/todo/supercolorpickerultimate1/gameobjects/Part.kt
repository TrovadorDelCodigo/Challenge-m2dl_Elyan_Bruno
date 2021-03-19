package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import fr.m2dl.todo.supercolorpickerultimate1.R
import fr.m2dl.todo.supercolorpickerultimate1.engine.cache.bitmap.BitmapCache
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject

const val PART_PICTURE_MARGIN = 4

class Part(
    x: Float,
    y: Float,
    var size: Float,
    val color: Int
) : GameObject(x, y) {
    private var isPictureLocked: Boolean = false
    var picture: Bitmap? = null

    override fun init() {
       picture = BitmapCache.getCachedBitmap(size.toInt() - PART_PICTURE_MARGIN * 2,
           size.toInt() - PART_PICTURE_MARGIN * 2,
           resources, R.drawable.background01, true)
    }

    override fun deinit() { }

    override fun update(delta: Long) { }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(globalX, globalY, globalX + size,
            globalY + size, Paint().also { it.color = color })
        if (picture != null && !isPictureLocked) {
            canvas.drawBitmap(picture!!, globalX + PART_PICTURE_MARGIN, globalY + PART_PICTURE_MARGIN, Paint())
        }
    }

    fun compareColor() {
    }
}
