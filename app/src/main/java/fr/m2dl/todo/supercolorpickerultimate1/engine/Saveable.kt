package fr.m2dl.todo.supercolorpickerultimate1.engine

import android.os.Bundle

interface Saveable {
    fun save(bundle: Bundle)
    fun load(bundle: Bundle)
}
