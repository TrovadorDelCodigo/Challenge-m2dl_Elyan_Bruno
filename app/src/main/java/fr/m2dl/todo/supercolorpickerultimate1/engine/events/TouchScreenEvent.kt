package fr.m2dl.todo.supercolorpickerultimate1.engine.events

data class TouchScreenEvent(
        val x: Float,
        val y: Float,
        val action: Int
): GameInputEvent
