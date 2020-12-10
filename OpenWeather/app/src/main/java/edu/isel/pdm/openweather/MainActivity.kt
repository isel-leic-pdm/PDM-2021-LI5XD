package edu.isel.pdm.openweather

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.openweather.MainActivityViewModel.State
import edu.isel.pdm.openweather.databinding.ActivityMainBinding
import edu.isel.pdm.openweather.repo.WeatherInfo
import java.lang.IllegalStateException

/**
 * Activity used to collect the city name and fetch the corresponding weather info
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
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
        binding.fetchButton.isEnabled = state == State.IDLE
        binding.city.isEnabled = state == State.IDLE
        if (state == State.IDLE)
            binding.city.text.clear()
    }

    /**
     * Handles the completion of the asynchronous operation for fetching the weather information
     */
    private fun handleWeatherInfoCompletion(it: Result<WeatherInfo>?) {
        if (viewModel.state.value == State.COMPLETE && it?.isSuccess == true)
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
            val info = viewModel.weatherInfo?.value?.getOrNull() ?: throw IllegalStateException()
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
                viewModel.fetchWeatherInfo(cityName)
            }
        }
    }
}
