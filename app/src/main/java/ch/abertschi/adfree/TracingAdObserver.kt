package ch.abertschi.adfree

import ch.abertschi.adfree.ad.AdEvent
import ch.abertschi.adfree.ad.AdObservable
import ch.abertschi.adfree.ad.AdObserver
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class TracingAdObserver : AdObserver {

    data class TracingEvent(val event: AdEvent, val time: Date)

    class CircularBuffer<T>(val capacity: Int) {

        private val array: Array<Any?> = Array(capacity) { null }
        private var start: Int = 0
        private var end: Int = 0

        fun put(obj: T) {
            synchronized(this) {
                array[end] = obj

                end += 1 % capacity
                if (end == start) {
                    start = (start++) % capacity
                }
            }
        }

        fun size(): Int {
            return if (end >= start) {
                end - start
            } else {
                capacity - end + start
            }
        }

        fun peek(): T {
            if (start == end) {
                throw IllegalStateException("buffer is empty")
            }
            return array[start] as T
        }

        fun get(): T {
            synchronized(this) {
                if (start == end) {
                    throw IllegalStateException("buffer is empty")
                }
                val res = array[start] as T
                start += 1 % capacity
                return res
            }
        }

        fun createCopy(): ArrayList<T> {
            val a = ArrayList<T>(size())
            synchronized(this) {
                for (item in iterator()) {
                    a.add(item)
                }
            }
            return a
        }

        fun iterator(): Iterator<T> {
            return object : Iterator<T> {
                var pointer: Int = start
                override fun hasNext(): Boolean {
                    return pointer != end
                }

                override fun next(): T {
                    if (!hasNext()) {
                        throw IllegalStateException("nothing left")
                    }
                    val v = array[pointer]
                    pointer += 1 % capacity
                    return v as T
                }
            }
        }
    }

    private val buffer: CircularBuffer<TracingEvent> = CircularBuffer(50)

    override fun onAdEvent(event: AdEvent, observable: AdObservable) {
        val data = TracingEvent(event, Date())
        buffer.put(data)
    }

    fun events() {
        val ex = buffer.createCopy().reversed().map { it.event }
    }
}