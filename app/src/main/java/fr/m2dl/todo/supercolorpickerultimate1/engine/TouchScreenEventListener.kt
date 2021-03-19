package fr.m2dl.todo.supercolorpickerultimate1.engine

import fr.m2dl.todo.supercolorpickerultimate1.engine.events.TouchScreenEvent

interface TouchScreenEventListener {
    fun onTouchScreenEvent(event: TouchScreenEvent)
}
