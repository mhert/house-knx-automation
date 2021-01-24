package infrastructure.eventbus

import kotlin.reflect.KClass

class SynchronousEventBus : EventBus {

    private val listeners = mutableMapOf<String, MutableList<LambdaBasedListener<Event>>>()

    override fun dispatch(event: Event) {
        val listeners = listeners[event::class.toString()]
        listeners?.forEach { it(event) }
    }

    override fun <T : Event> listen(type: KClass<T>, listener: LambdaBasedListener<T>) {
        if (!listeners.containsKey(type.toString())) {
            listeners[type.toString()] = mutableListOf<LambdaBasedListener<Event>>()
        }

        @Suppress("UNCHECKED_CAST")
        // We are sure that the listener is a listener for Event, because we check for T : Event
        listeners[type.toString()]?.add(listener as LambdaBasedListener<Event>)
    }

    override fun <T : Event> listen(type: KClass<T>, listener: ObjectBasedListener<T>) {
        listen(type) { event -> listener(event) }
    }
}
