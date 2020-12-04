package edu.isel.pdm.openweather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.isel.pdm.openweather.repo.WeatherInfo

/**
 * View model for the [MainActivity]
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    enum class State {
        IDLE,
        IN_PROGRESS
    }

    var weatherInfo: LiveData<Result<WeatherInfo>?>? = null
        private set

    val state: LiveData<State> = MutableLiveData(State.IDLE)

    /**
     * Asynchronously fetches the weather info for the given city and places the result in
     * [weatherInfo].
     *
     * @param city  the city name
     * @return the observable promise
     */
    fun fetchWeatherInfo(city: String): LiveData<Result<WeatherInfo>?> {
        if (state.value == State.IN_PROGRESS)
            throw IllegalStateException()

        (state as MutableLiveData<State>).value = State.IN_PROGRESS
        val promiseResult = getApplication<OpenWeatherApplication>().repository.fetchWeatherInfo(city)
        weatherInfo = promiseResult

        return promiseResult
    }
}