package fr.m2dl.todo.supercolorpickerultimate1.gameobjects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import fr.m2dl.todo.supercolorpickerultimate1.engine.TouchScreenEventListener
import fr.m2dl.todo.supercolorpickerultimate1.engine.events.TouchScreenEvent
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject
import kotlin.math.sqrt

private const val MARGIN = 10f
private const val DRAGGING_THRESHOLD = 50f

const val PICTURE_TAKEN_SIGNAL = "picture-taken"
const val PICTURE_RECEIVED_SIGNAL = "picture-received"

enum class PictureState {
    NO_PICTURE,
    PICTURE_SELECTED,
    DRAG_START,
    DRAGGING,
    DRAG_END
}

class Picture: GameObject(), TouchScreenEventListener {
    private val bitmapPaint = Paint()
    private var bitmap: Bitmap? = null

    private var initialX = 0f
    private var initialY = 0f
    private var width = 0f
    private var height = 0f

    private var state = PictureState.NO_PICTURE

    private var dragStartPosX = 0f
    private var dragStartPosY = 0f
    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    private var previousPartWithPicture: Part? = null

    private val onPictureTaken: (Any) -> Unit = { picture ->
        if (picture is Bitmap) {
            signalManager.sendSignal(PICTURE_RECEIVED_SIGNAL, true)
            state = PictureState.PICTURE_SELECTED
            bitmap = picture
            previousPartWithPicture = null
            resizeAndMoveToDefaultLocation(picture.width.toFloat(), picture.height.toFloat())
        }
    }

    override fun init() {
        signalManager.subscribe(PICTURE_TAKEN_SIGNAL, onPictureTaken)
    }

    override fun deinit() {
    }

    override fun update(delta: Long) {
        when (state) {
            PictureState.PICTURE_SELECTED -> moveTo(initialX, initialY)
            PictureState.DRAG_END -> state = PictureState.PICTURE_SELECTED
        }
    }

    override fun draw(canvas: Canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, globalX, globalY, bitmapPaint)
        }
    }

    override fun onTouchScreenEvent(event: TouchScreenEvent) {
        when (state) {
            PictureState.PICTURE_SELECTED ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (event.x >= globalX && event.x < globalX + width
                            && event.y >= globalY && event.y < globalY + height)
                        state = PictureState.DRAG_START
                        dragStartPosX = event.x
                        dragStartPosY = event.y
                        dragOffsetX = event.x - globalX
                        dragOffsetY = event.y - globalY
                    }
                }
            PictureState.DRAG_START ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> state = PictureState.DRAG_END
                    MotionEvent.ACTION_MOVE -> {
                        if (dragDistance(event.x, event.y) >= DRAGGING_THRESHOLD) {
                            state = PictureState.DRAGGING
                        }
                    }
                }
            PictureState.DRAGGING ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> state = PictureState.DRAG_END
                    MotionEvent.ACTION_MOVE -> {
                        moveTo(event.x - dragOffsetX, event.y - dragOffsetY)
                        tryPutPictureInColorMatrixPart()
                    }
                }
        }
    }

    private fun dragDistance(x: Float, y: Float): Float {
        val distX = x - dragStartPosX
        val distY = y - dragStartPosY

        return sqrt(distX * distX + distY * distY)
    }

    private fun resizeAndMoveToDefaultLocation(newWidth: Float, newHeight: Float) {
        width = newWidth
        height = newHeight
        initialX = viewport.width / 2f - width / 2f
        initialY = viewport.height / 2f + MARGIN
        moveTo(initialX, initialY)
    }

    private fun findColorMatrixPartUnderPicture(): Part? {
        val parts = parent!!.children.filterIsInstance<ColorMatrix>()[0].parts
        val dragX = globalX + dragOffsetX
        val dragY = globalY + dragOffsetY

        return parts.find {
            dragX >= it.globalX && dragX < it.globalX + it.size
                    && dragY >= it.globalY && dragY < it.globalY + it.size
        }
    }

    private fun tryPutPictureInColorMatrixPart() {
        val part = findColorMatrixPartUnderPicture()
        if (part != null) {
            previousPartWithPicture?.picture = null
            previousPartWithPicture = part
            part.picture = bitmap
        }
    }
}
