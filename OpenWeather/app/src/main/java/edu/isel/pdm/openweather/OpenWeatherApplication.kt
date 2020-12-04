package edu.isel.pdm.openweather

import android.app.Application
import android.util.Log
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import edu.isel.pdm.openweather.repo.OpenWeatherRepository

/**
 * Holds the globally accessible dependencies (the Service Locator) and event handlers.
 */
class OpenWeatherApplication : Application() {

    /**
     * The application's tag for logging purposes
     */
    val appTag by lazy { getString(R.string.app_name) }

    /**
     * The application's repository
     */
    val repository by lazy {
        OpenWeatherRepository(
                appTag,
                Volley.newRequestQueue(this),
                jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        )
    }

    override fun onCreate() {
        super.onCreate()
        Log.v(appTag, "onCreate() on ${this.javaClass.simpleName}")
    }
}