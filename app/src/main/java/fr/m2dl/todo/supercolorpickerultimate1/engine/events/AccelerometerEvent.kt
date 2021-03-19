package fr.m2dl.todo.supercolorpickerultimate1.engine.events

data class AccelerometerEvent(
        val x: Float,
        val y: Float,
        val z: Float
): GameInputEvent
