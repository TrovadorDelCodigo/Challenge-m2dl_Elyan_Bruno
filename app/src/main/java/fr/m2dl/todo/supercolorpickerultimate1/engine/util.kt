package fr.m2dl.todo.supercolorpickerultimate1.engine

inline fun <T> Array<out T>.forEachOptimized(action: (T) -> Unit) {
    var i = 0
    while (i < size) {
        action(this[i])
        i++
    }
}

inline fun <T> List<T>.forEachOptimized(action: (T) -> Unit) {
    var i = 0
    while (i < size) {
        action(this[i])
        i++
    }
}
