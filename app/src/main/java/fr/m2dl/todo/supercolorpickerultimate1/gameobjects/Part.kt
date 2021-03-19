package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.*
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import android.hardware.SensorManager
import android.os.Bundle
import fr.m2dl.todo.supercolorpickerultimate1.engine.AccelerometerEventListener
import fr.m2dl.todo.supercolorpickerultimate1.engine.Saveable
import fr.m2dl.todo.supercolorpickerultimate1.engine.events.AccelerometerEvent
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

private const val PART_PICTURE_MARGIN = 4
private const val SHAKE_THRESHOLD_GRAVITY = 2.7F
private const val SHAKE_SLOP_TIME_MS = 100
private const val SHAKE_COUNT_RESET_TIME_MS = 200
private const val SHAKE_MIN_COUNT = 5

enum class PartState {
    EMPTY,
    PICTURE,
    PICTURE_LOCKED
}

private const val SEND_SCORE_SIGNAL = "add-score-signal"

class Part(
    val id: Int,
    x: Float,
    y: Float,
    var size: Float,
    var color: Int
) : GameObject(x, y), AccelerometerEventListener, Saveable {

    var state = PartState.EMPTY

    private val paint = Paint()
    private val srcRect = Rect()
    private val dstRect = Rect()
    private var scoreSent = false

    var picture: Bitmap? = null
        set(value) {
            if (state != PartState.PICTURE_LOCKED) {
                state = when (value) {
                    null -> PartState.EMPTY
                    else -> PartState.PICTURE
                }
                field = value
            }
        }

    private var shakeTimestamp = 0L
    private var shakeCount = 0

    override fun init() {
    }

    override fun deinit() { }

    override fun update(delta: Long) {
        if (state == PartState.PICTURE && !scoreSent) {
            println("----------------------------------------------- send signal $id")
            // signalManager.sendSignal(SEND_SCORE_SIGNAL, getScore())
            // scoreSent = true
        }
    }

    override fun draw(canvas: Canvas) {
        paint.color = color
        canvas.drawRect(globalX, globalY, globalX + size,
            globalY + size, paint)

        if (picture != null) {
            srcRect.apply {
                top = 0
                left = 0
                right = min(size.toInt() - 2 * PART_PICTURE_MARGIN, picture!!.width)
                bottom = min(size.toInt() - 2 * PART_PICTURE_MARGIN, picture!!.height)
            }
            dstRect.apply {
                top = (globalY + PART_PICTURE_MARGIN).toInt()
                left = (globalX + PART_PICTURE_MARGIN).toInt()
                right = (globalX + size - PART_PICTURE_MARGIN).toInt()
                bottom = (globalY + size - PART_PICTURE_MARGIN).toInt()
            }
            paint.alpha = if (state == PartState.PICTURE_LOCKED) 255 else 70
            canvas.drawBitmap(picture!!, srcRect, dstRect, paint)
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

    private fun getScore(): Int {
        val pictureDominantColor = getDominantColor(picture)
        val absDiffRed = abs(color.red - pictureDominantColor.red)
        val absDiffGreen = abs(color.green - pictureDominantColor.green)
        val absDiffBlue = abs(color.blue - pictureDominantColor.blue)

        println("${color.blue} > ${color.green} > ${color.red}")
        println("${pictureDominantColor.blue} < ${pictureDominantColor.green} < ${pictureDominantColor.red}")
        println("$absDiffBlue - $absDiffGreen - $absDiffRed")
        return when {
            absDiffBlue <= 10 && absDiffGreen <= 10 && absDiffRed <= 10 -> 200
            absDiffBlue <= 100 && absDiffGreen <= 100 && absDiffRed <= 100 -> 100
            absDiffBlue <= 200 && absDiffGreen <= 200 && absDiffRed <= 200 -> 50
            absDiffBlue <= 100 && absDiffGreen <= 100 || absDiffRed <= 100 -> 25
            absDiffBlue <= 100 || absDiffGreen <= 100 && absDiffRed <= 100 -> 25
            absDiffBlue <= 100 || absDiffGreen <= 100 || absDiffRed <= 100 -> 5
            else -> 0
        }
    }

    override fun onAccelerometerEvent(event: AccelerometerEvent) {
        checkForShaking(event)
    }

    private fun checkForShaking(event: AccelerometerEvent) {
        val x = event.x
        val y = event.y
        val z = event.z

        val gX = x / SensorManager.GRAVITY_EARTH;
        val gY = y / SensorManager.GRAVITY_EARTH;
        val gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        val gForce = sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis();
            // ignore shake events too close to each other
            if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            // reset the shake count after N seconds of no shakes
            if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                shakeCount = 0;
            }

            shakeTimestamp = now;
            shakeCount++;

            if (shakeCount == SHAKE_MIN_COUNT && state == PartState.PICTURE) {
                lockPicture()
            }
        }
    }

    private fun lockPicture() {
        println("Picture locked!")
        signalManager.sendSignal(SEND_SCORE_SIGNAL, getScore())
        state = PartState.PICTURE_LOCKED
    }

    override fun save(bundle: Bundle) {
        bundle.putParcelable("part-$id-picture", picture)
        bundle.putInt("part-$id-state", state.ordinal)
        bundle.putInt("part-$id-color", color)
    }

    override fun load(bundle: Bundle) {
        picture = bundle.getParcelable("part-$id-picture")
        state = when(bundle.getInt("part-$id-state")!!) {
            PartState.EMPTY.ordinal -> PartState.EMPTY
            PartState.PICTURE.ordinal -> PartState.PICTURE
            PartState.PICTURE_LOCKED.ordinal -> PartState.PICTURE_LOCKED
            else -> throw IllegalStateException("Unknown state")
        }
        color = bundle.getInt("part-$id-color")!!
    }
}
