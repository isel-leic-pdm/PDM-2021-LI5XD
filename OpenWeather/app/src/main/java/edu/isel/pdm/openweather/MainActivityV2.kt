package edu.isel.pdm.openweather

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import edu.isel.pdm.openweather.databinding.ActivityMainBinding
import edu.isel.pdm.openweather.repo.WeatherInfo
import java.lang.Thread.currentThread

/**
 * View model for the [MainActivityV2]
 */
class MainActivityV2ViewModel(application: Application) : AndroidViewModel(application) {

    enum class State {
        IDLE,
        IN_PROGRESS
    }

    val cityName: MutableLiveData<String> = MutableLiveData()

    val weatherInfo: LiveData<Result<WeatherInfo>?> = Transformations.switchMap(cityName) {
        val app = getApplication<OpenWeatherApplication>()
        Log.v(app.appTag, "MainActivityViewModel.fetchWeatherInfo on ${currentThread().name}")

        if (state.value == State.IDLE) {
            (state as MutableLiveData<State>).value = State.IN_PROGRESS
            getApplication<OpenWeatherApplication>().repository.fetchWeatherInfo(it)
        }
        else throw IllegalStateException()
    }

    /**
     * The current state of the view model
     */
    val state: LiveData<State> = MutableLiveData(State.IDLE)

    /**
     * Sets the view model state to [State.IDLE].
     */
    fun setToIdle() {
        (state as MutableLiveData<State>).value = State.IDLE
    }
}

/**
 * Alternative implementation [MainActivity].
 *
 * Design note: Notice the differences in the associated View Model...
 */
class MainActivityV2 : AppCompatActivity() {

    private val viewModel: MainActivityV2ViewModel by viewModels()
    private val binding: ActivityMainBinding by confinedLazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val cityName: String
        get() = binding.city.text.toString().trim()

    /**
     * Displays the UI according to the screen's state
     */
    private fun displayUI() {
        val state = viewModel.state.value
        binding.fetchButton.isEnabled = state == MainActivityV2ViewModel.State.IDLE
        binding.city.isEnabled = state == MainActivityV2ViewModel.State.IDLE
        if (state == MainActivityV2ViewModel.State.IDLE)
            binding.city.text.clear()
    }

    /**
     * Handles the completion of the asynchronous operation for fetching the weather information
     */
    private fun handleWeatherInfoCompletion(it: Result<WeatherInfo>?) {
        if (viewModel.state.value != MainActivityV2ViewModel.State.IDLE && it?.isSuccess == true)
            navigateToWeatherActivity()
        else
            displayError()
    }

    /**
     * Displays an indication that the weather information could not be fetched
     */
    private fun displayError() {
        Toast.makeText(
            this,
            resources.getString(R.string.error_msg_couldntget),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Navigates to the activity used to display the weather information, clearing the view model
     * state in the process
     */
    private fun navigateToWeatherActivity() {
        viewModel.setToIdle()
        startActivity(Intent(this, WeatherActivity::class.java).apply {
            val info = viewModel.weatherInfo.value?.getOrNull() ?: throw java.lang.IllegalStateException()
            putExtra(WEATHER_EXTRA_KEY, info)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.state.observe(this) {
            displayUI()
        }

        viewModel.weatherInfo.observe(this) {
            handleWeatherInfoCompletion(it)
        }

        binding.fetchButton.setOnClickListener {
            if (cityName.isBlank()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.empty_city_toast),
                    Toast.LENGTH_LONG
                ).show()
                displayUI()
            }
            else {
                viewModel.cityName.value = cityName
            }
        }
    }
}