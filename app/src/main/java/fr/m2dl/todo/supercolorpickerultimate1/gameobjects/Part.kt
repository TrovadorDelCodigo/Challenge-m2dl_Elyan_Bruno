package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import fr.m2dl.todo.supercolorpickerultimate1.R
import fr.m2dl.todo.supercolorpickerultimate1.engine.cache.bitmap.BitmapCache
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject
import kotlin.math.abs

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

    override fun update(delta: Long) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(globalX, globalY, globalX + size,
            globalY + size, Paint().also { it.color = color })
        if (picture != null && !isPictureLocked) {
            canvas.drawBitmap(picture!!, globalX + PART_PICTURE_MARGIN, globalY + PART_PICTURE_MARGIN, Paint())
        }
    }

    private fun getDominantColor(bitmap: Bitmap?): Int {
        if (bitmap == null) return Color.TRANSPARENT

        var redBucket = 0
        var greenBucket = 0
        var blueBucket = 0

        var pixelCount = bitmap.width * bitmap.height

        for (y in 0..bitmap.height-1)
        {
            for (x in 0..bitmap.width-1)
            {
                var color = bitmap.getPixel(x, y)
                redBucket += Color.red(color)
                greenBucket += Color.green(color)
                blueBucket += Color.blue(color)
            }
        }

        return Color.rgb(
        redBucket / pixelCount,
        greenBucket / pixelCount,
        blueBucket / pixelCount);
    }

    fun getScore(): Int {
        val pictureDominantColor = getDominantColor(picture)
        val absDiffRed = abs(color.red - pictureDominantColor.red)
        val absDiffGreen = abs(color.green - pictureDominantColor.green)
        val absDiffBlue = abs(color.blue - pictureDominantColor.blue)

        /** test
        println("${color.blue} > ${color.green} > ${color.red}")
        println("${pictureDominantColor.blue} < ${pictureDominantColor.green} < ${pictureDominantColor.red}")
        println("$absDiffBlue - $absDiffGreen - $absDiffRed")
        */
        return when {
            absDiffBlue <= 10 && absDiffGreen <= 10 && absDiffRed <= 10 -> 200
            absDiffBlue <= 100 && absDiffGreen <= 100 && absDiffRed <= 100 -> 100
            absDiffBlue <= 200 && absDiffGreen <= 200 && absDiffRed <= 200 -> 10
            else -> 0
        }
    }

}
