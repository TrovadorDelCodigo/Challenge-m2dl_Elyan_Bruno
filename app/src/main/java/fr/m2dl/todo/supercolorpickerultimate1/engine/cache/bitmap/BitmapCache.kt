package fr.m2dl.todo.supercolorpickerultimate1.engine.cache.bitmap

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapCache {
    private data class BitmapCacheKey(
            val scaledWidth: Int,
            val resources: Resources,
            val resId: Int
    )

    private val cache = mutableMapOf<BitmapCacheKey, Bitmap>()

    fun getCachedBitmap(scaledWidth: Int, resources: Resources, resId: Int, hasAlpha: Boolean): Bitmap {
        val key = BitmapCacheKey(scaledWidth, resources, resId)
        val entry = cache[key]

        return if (entry == null) {
            val nonScaledBitmap = BitmapFactory.decodeResource(resources, resId)
            nonScaledBitmap.setHasAlpha(hasAlpha)
            val ratio = scaledWidth / nonScaledBitmap.width.toFloat()
            val scaledHeight = (ratio * nonScaledBitmap.height).toInt()
            val scaledBitmap = Bitmap.createScaledBitmap(nonScaledBitmap, scaledWidth, scaledHeight, true)

            cache[key] = scaledBitmap
            scaledBitmap
        } else {
            entry
        }
    }

    fun getCachedBitmap(scaledWidth: Int, scaledHeight: Int, resources: Resources, resId: Int, hasAlpha: Boolean): Bitmap {
        val key = BitmapCacheKey(scaledWidth, resources, resId)
        val entry = cache[key]

        return if (entry == null) {
            val nonScaledBitmap = BitmapFactory.decodeResource(resources, resId)
            nonScaledBitmap.setHasAlpha(hasAlpha)
            val scaledBitmap = Bitmap.createScaledBitmap(nonScaledBitmap, scaledWidth, scaledHeight, true)

            cache[key] = scaledBitmap
            scaledBitmap
        } else {
            entry
        }
    }
}
