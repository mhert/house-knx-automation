package houseknxautomation.infrastructure.eventbus.logging

import houseknxautomation.infrastructure.eventbus.Event
import houseknxautomation.infrastructure.eventbus.ObjectBasedListener
import org.slf4j.Logger

class ObjectBasedListenerLoggingFacade<T : Event>(
    private val logger: Logger,
    private val listener: ObjectBasedListener<T>
): ObjectBasedListener<T> {
    override fun invoke(event: T) {
        logger.info("" + listener::class.simpleName + " Listener received " + event::class.java.simpleName)

        listener.invoke(event)
    }
}
