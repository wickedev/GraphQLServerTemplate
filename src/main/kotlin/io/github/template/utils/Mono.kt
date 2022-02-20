package io.github.template.utils

import reactor.core.publisher.Mono

fun <T> Mono<T>.get(): T {
    return this.toFuture().get()
}