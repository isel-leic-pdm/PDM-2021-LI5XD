package edu.isel.pdm.openweather.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.fasterxml.jackson.databind.ObjectMapper
import edu.isel.pdm.openweather.BuildConfig

/**
 * The application's repository holding globally relevant information.
 *
 * Implementation note: Currently the information is always fetched from the remote Web API, but it
 * could be cached (with an expiration date) for future use.
 */
class OpenWeatherRepository(
        private val appTag: String,
        private val queue: RequestQueue,
        private val mapper: ObjectMapper) {

    /**
     * Asynchronous operation for fetching the weather information for the given city.
     *
     * @param cityName  the city name
     * @return the result promise
     */
    fun fetchWeatherInfo(cityName: String): LiveData<Result<WeatherInfo>?> {
        val result = MutableLiveData<Result<WeatherInfo>?>()
        val request = GetWeatherInfoRequest(
            "$WEATHER_INFO_URL?q=$cityName&appId=${BuildConfig.appId}",
                mapper,
            { result.value = Result.success(modelFromDTO(it)) },
            { result.value = Result.failure(it) }
        )

        queue.add(request)
        return result
    }
}