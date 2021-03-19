package fr.m2dl.todo.supercolorpickerultimate1.engine

import fr.m2dl.todo.supercolorpickerultimate1.engine.events.GameInputEvent

interface GameEngineEvents {
    fun notifyEvent(event: GameInputEvent)
}