package com.example.offloader.offloader

import kotlin.reflect.KFunction

class DeferredFunction(val function: KFunction<Any>, vararg val params: Any?) {
    @Suppress("UNCHECKED_CAST")
    operator fun <T> invoke(): T {
        return function.call(*params) as T
    }
}