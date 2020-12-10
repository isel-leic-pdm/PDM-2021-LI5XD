package edu.isel.pdm.openweather.repo

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
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
     *
     * Design note: using promise style (LiveData is a life cycle aware observable promise)
     */
    fun fetchWeatherInfo(cityName: String): LiveData<Result<WeatherInfo>?> {
        Log.v(appTag, "OpenWeatherRepository.fetchWeatherInfo on ${Thread.currentThread().name}")
        val result = MutableLiveData<Result<WeatherInfo>?>()
        val request = GetWeatherInfoRequest(
            "$WEATHER_INFO_URL?q=$cityName&units=metric&appId=${BuildConfig.appId}",
                mapper,
            {
                Log.v(appTag, "OpenWeatherRepository.fetchWeatherInfo success on ${Thread.currentThread().name}")
                result.value = Result.success(modelFromDTO(it))
            },
            {
                Log.v(appTag, "OpenWeatherRepository.fetchWeatherInfo error on ${Thread.currentThread().name}")
                result.value = Result.failure(it)
            }
        )

        queue.add(request)
        return result
    }

    /**
     * Asynchronous operation that fetches an image from the given URL.
     *
     * @param success   callback used to signal request completion success
     * @param error     callback used to signal that an error occurred while trying to get the image
     *
     * Design note: using callback style.
     * What are the risks of using this approach? Consider the following: what could happen if it
     * was called from an Activity directly?
     */
    fun fetchWeatherImage(
        url: String, maxWidth: Int, maxHeight: Int,
        success: (Bitmap) -> Unit, error: (Throwable) -> Unit) {

        Log.v(appTag, "OpenWeatherRepository.fetchWeatherImage on ${Thread.currentThread().name}")
        val request = ImageRequest(
            "$url?appId=${BuildConfig.appId}",
                {
                    Log.v(appTag, "OpenWeatherRepository.fetchWeatherImage success on ${Thread.currentThread().name}")
                    success(it)
                },
                maxWidth,
                maxHeight,
                ImageView.ScaleType.FIT_XY,
                Bitmap.Config.ALPHA_8,
                {
                    Log.v(appTag, "OpenWeatherRepository.fetchWeatherImage error on ${Thread.currentThread().name}")
                    error(it)
                }
        )

        queue.add(request)
    }

}