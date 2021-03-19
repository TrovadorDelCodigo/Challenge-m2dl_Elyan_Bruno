package fr.m2dl.todo.supercolorpickerultimate1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
    var gameEngine: GameEngine? = null

    private var savedState: Bundle? = null

    private var score = 0
    private var nbParties = 0

    private val scoreHandler: (Any) -> Unit = { partScore ->
        if (partScore is Int) {
            score += partScore
            nbParties += 1
            if (nbParties == 9) {
                val intent = Intent(activity, GameOverActivity::class.java)
                intent.putExtra("score", score)
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }

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
        savedState = Bundle()
        gameEngine!!.saveState(savedState!!)
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
        if (savedState != null) {
            gameEngine!!.restoreState(savedState!!)
        }
        gameEngine!!.signalManager.subscribe("add-score-signal", scoreHandler)
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
