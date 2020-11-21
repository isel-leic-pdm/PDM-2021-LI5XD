package edu.isel.pdm.memorymatrix

import android.app.Application
import android.util.Log

private const val GLOBAL_PREFS = "GlobalPreferences"

/**
 * Holds the globally accessible dependencies and event handlers.
 */
class MemoryMatrixApplication : Application() {

    // Using a resource string merely for demonstration purposes
    val appTag by lazy { getString(R.string.app_name) }

    val gameRepository by lazy {
        GameRepository(getSharedPreferences(GLOBAL_PREFS, MODE_PRIVATE))
    }

    override fun onCreate() {
        super.onCreate()
        Log.v(appTag, "onCreate() on ${this.javaClass.simpleName}")
    }
}