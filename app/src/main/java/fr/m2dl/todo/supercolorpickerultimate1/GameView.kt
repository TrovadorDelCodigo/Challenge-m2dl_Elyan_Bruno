package fr.m2dl.todo.supercolorpickerultimate1

import android.app.Activity
import android.content.Intent
import android.view.SurfaceHolder
import android.view.SurfaceView
import fr.m2dl.todo.supercolorpickerultimate1.engine.GameEngine
import fr.m2dl.todo.supercolorpickerultimate1.engine.events.*
import fr.m2dl.todo.supercolorpickerultimate1.engine.impl.GameDrawingSurfaceImpl
import fr.m2dl.todo.supercolorpickerultimate1.engine.impl.GameEngineImpl

class GameView(
    private val activity: Activity
) : SurfaceView(activity), SurfaceHolder.Callback {

    private val defaultFps = 60
    private var gameEngine: GameEngine? = null

    private val gameOverSignalHandler: (Any) -> Unit = { score ->
        if (score is Int) {
            val intent = Intent(activity, GameOverActivity::class.java)
            intent.putExtra("score", score)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                stopGame()
            } catch (exp: InterruptedException) {
                exp.printStackTrace()
            }
            retry = false
        }
    }

    private fun startGame() {
        gameEngine = GameEngineImpl(defaultFps, GameDrawingSurfaceImpl(this), activity.resources)
        gameEngine!!.signalManager.subscribe("game-over", gameOverSignalHandler)
        populateGameWorld()
        gameEngine?.start()
    }

    private fun stopGame() {
        gameEngine?.stop()
    }

    private fun populateGameWorld() {
        gameEngine?.setSceneRoot(fr.m2dl.todo.supercolorpickerultimate1.gameobjects.Scene())
    }

    fun notifyEvent(event: GameInputEvent) {
        gameEngine?.notifyEvent(event)
    }
}
