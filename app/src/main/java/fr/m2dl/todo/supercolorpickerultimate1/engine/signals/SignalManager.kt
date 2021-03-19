package fr.m2dl.todo.supercolorpickerultimate1.engine.signals

interface SignalManager {
    fun subscribe(signalName: String, signalHandler: (Any)->Unit)
    fun unsubscribe(signalName: String, signalHandler: (Any) -> Unit)
    fun sendSignal(signalName: String, signalData: Any): Int
}
