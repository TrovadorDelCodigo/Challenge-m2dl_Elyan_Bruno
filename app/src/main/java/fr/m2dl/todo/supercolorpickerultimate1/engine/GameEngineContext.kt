package fr.m2dl.todo.supercolorpickerultimate1.engine

import android.content.res.Resources
import fr.m2dl.todo.supercolorpickerultimate1.engine.gameobjects.GameObject
import fr.m2dl.todo.supercolorpickerultimate1.engine.signals.SignalManager

interface GameEngineContext {
    val viewport: GameViewport
    val resources: Resources
    val signalManager: SignalManager
    fun initGameObject(gameObject: GameObject)
    fun deinitGameObject(gameObject: GameObject)
}
