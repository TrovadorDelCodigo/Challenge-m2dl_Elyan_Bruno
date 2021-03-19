package fr.m2dl.todo.supercolorpickerultimate1.engine.signals.impl

import fr.m2dl.todo.supercolorpickerultimate1.engine.signals.SignalManager

class SignalManagerImpl: SignalManager {

    private val handlers = mutableMapOf<String, MutableSet<(Any) -> Unit>>()

    override fun subscribe(signalName: String, signalHandler: (Any) -> Unit) {
        if (handlers[signalName] == null) {
            handlers[signalName] = mutableSetOf()
        }
        handlers[signalName]?.add(signalHandler)
    }

    override fun unsubscribe(signalName: String, signalHandler: (Any) -> Unit) {
        handlers[signalName]?.remove(signalHandler)
    }

    override fun sendSignal(signalName: String, signalData: Any): Int {
        var nbReceived = 0
        // TODO Optimize it. Don't create iterators...
        handlers[signalName]?.forEach {
            nbReceived++
            it(signalData)
        }

        return nbReceived
    }
}
