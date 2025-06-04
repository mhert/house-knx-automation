package houseknxautomation.infrastructure.eventbus

import kotlin.reflect.KClass

typealias LambdaBasedListener<T> = (event: T) -> Unit

interface ObjectBasedListener<T : Event> {
    operator fun invoke(event: T)
}

interface EventBus {
    fun dispatch(event: Event);

    fun <T> listen(type: KClass<T>, listener: LambdaBasedListener<T>) where T : Event

    fun <T> listen(type: KClass<T>, listener: ObjectBasedListener<T>) where T : Event
}
