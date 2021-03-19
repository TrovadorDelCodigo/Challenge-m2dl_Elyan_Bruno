package fr.m2dl.todo.supercolorpickerultimate1.engine

import fr.m2dl.todo.supercolorpickerultimate1.engine.events.AccelerometerEvent

interface AccelerometerEventListener {
    fun onAccelerometerEvent(event: AccelerometerEvent)
}
