package fr.m2dl.todo.supercolorpickerultimate1

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import fr.m2dl.todo.supercolorpickerultimate1.engine.events.AccelerometerEvent
import fr.m2dl.todo.supercolorpickerultimate1.engine.events.TouchScreenEvent
import fr.m2dl.todo.supercolorpickerultimate1.gameobjects.PICTURE_RECEIVED_SIGNAL
import fr.m2dl.todo.supercolorpickerultimate1.gameobjects.PICTURE_TAKEN_SIGNAL

private const val REQUEST_IMAGE_CAPTURE = 1

class GameActivity : Activity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gameView: GameView

    private lateinit var musicIds: Array<Int>
    private val mediaPlayers = mutableListOf<MediaPlayer>()
    private val startMusicHandler = Handler(Looper.getMainLooper())

    private lateinit var picture: Bitmap
    private val transmitPictureHandler = Handler(Looper.getMainLooper())
    private val sendPicture = object : Runnable {
        override fun run() {
            val nbReceived = gameView.gameEngine?.signalManager?.sendSignal(PICTURE_TAKEN_SIGNAL, picture)
            if (nbReceived == 0) {
                transmitPictureHandler.postDelayed(this, 50)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setupSensorManagerAndSensors()
        gameView = GameView(this, savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameViewWrapper = findViewById<LinearLayout>(R.id.gameViewWrapper)
        gameViewWrapper.addView(gameView)

        setupMediaPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyMediaPlayers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        gameView.saveGameState(outState)
    }

    private fun setupWindow() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setupMediaPlayers() { }

    private fun destroyMediaPlayers() { }

    private fun setupSensorManagerAndSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val values = event.values
        gameView.notifyEvent(AccelerometerEvent(values[0], values[1], values[2]))
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gameView.notifyEvent(TouchScreenEvent(event!!.x, event.y, event.action))
        return true
    }

    fun takePhoto(view: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.cannot_take_photo, Toast.LENGTH_LONG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            picture = data!!.extras!!.get("data") as Bitmap
            transmitPictureHandler.postDelayed(sendPicture, 50)
        }
    }
}
