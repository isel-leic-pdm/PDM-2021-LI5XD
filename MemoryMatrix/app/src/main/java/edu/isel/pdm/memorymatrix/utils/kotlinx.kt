package edu.isel.pdm.memorymatrix.utils

import android.os.Handler
import android.os.Looper

/**
 * Runs the given action on the main thread after the specified time interval.
 *
 * @param millis    The minimum delay expressed in milliseconds.
 * @param action    The action to be executed by the main thread.
 */
fun runDelayed(millis: Long, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, millis)
}

/**
 * Version of [lazy] that defaults to a non-thread safe implementation to be used when
 * the lazy use is confined to a single thread
 *
 * @param initializer   The function used to initialize the underlying instance
 * @return the lazy instance data holder
 */
fun <T> confinedLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)
