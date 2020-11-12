package edu.isel.pdm.memorymatrix

import android.app.Application
import android.util.Log

/**
 *
 */
class MemoryMatrixApplication : Application() {

    // Using a resource string merely for demonstration purposes
    private val appTag by lazy { getString(R.string.app_name) }

    override fun onCreate() {
        super.onCreate()
        Log.v(appTag, "onCreate() on ${this.javaClass.simpleName}")
    }
}