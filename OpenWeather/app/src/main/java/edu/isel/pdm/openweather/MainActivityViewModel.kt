package edu.isel.pdm.openweather

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import edu.isel.pdm.openweather.repo.WeatherInfo
import java.lang.Thread.currentThread
import java.util.concurrent.Executors

/**
 * For demo purposes
 */
fun doAHugeAmountOfWork(result: Result<WeatherInfo>?, completion: (Result<WeatherInfo>?) -> Unit) {

    val mainHandler = Handler(Looper.getMainLooper())
    Executors.newSingleThreadExecutor().execute {
        Log.v("Open", "working hard on ${currentThread().name}")
        Thread.sleep(5000)
        mainHandler.post { completion(result) }
    }
}

/**
 * View model for the [MainActivity]
 *
 * Design note: Notice the broken up asynchronous interface provided by the view model. The async
 * call [fetchWeatherInfo] and its completion [state] and [weatherInfo] are separated.
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    enum class State {
        IDLE,
        IN_PROGRESS,
        COMPLETE
    }

    /**
     * The last fetched weather info, if any
     *
     * Implementation note: be aware of the use of Result from kotlin's stdlib here
     * See restrictions:
     * https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md#encapsulate-successful-or-failed-function-execution
     */
    val weatherInfo: LiveData<Result<WeatherInfo>?> = MediatorLiveData()

    /**
     * The current state of the view model
     */
    val state: LiveData<State> = MutableLiveData(State.IDLE)

    /**
     * Asynchronously fetches the weather info for the given city and places the result in
     * [weatherInfo].
     *
     * @param city  the city name
     * @return the observable promise
     */
    fun fetchWeatherInfo(city: String): LiveData<Result<WeatherInfo>?> {

        val app = getApplication<OpenWeatherApplication>()
        Log.v(app.appTag, "MainActivityViewModel.fetchWeatherInfo on ${currentThread().name}")
        if (state.value == State.IN_PROGRESS)
            throw IllegalStateException()

        (state as MutableLiveData<State>).value = State.IN_PROGRESS
        val mediator = weatherInfo as MediatorLiveData<Result<WeatherInfo>?>
        val source = app.repository.fetchWeatherInfo(city)

        mediator.addSource(source) {
            Log.v(app.appTag, "MainActivityViewModel.fetchWeatherInfo completion on ${currentThread().name}")
            state.value = State.COMPLETE

            doAHugeAmountOfWork(it) { finalResult ->
                weatherInfo.value = finalResult
                mediator.removeSource(source)
            }
        }
        return weatherInfo
    }

    /**
     * Sets the view model state to [State.IDLE].
     */
    fun setToIdle() {
        (state as MutableLiveData<State>).value = State.IDLE
    }
}