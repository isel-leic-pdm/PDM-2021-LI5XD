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

    private fun displayUI() {
        val state = viewModel.state.value
        binding.fetchButton.isEnabled = state != State.IN_PROGRESS
        binding.city.isEnabled = state != State.IN_PROGRESS
        if (state == State.IDLE)
            binding.city.text.clear()
    }

    private fun handleWeatherInfoCompletion(it: Result<WeatherInfo>?) {
        if (it != null) {
            if (it.isFailure)
                displayError()
            else
                navigateToWeatherActivity()
        }

    }

    private fun displayError() {
        Toast.makeText(
            this,
            resources.getString(R.string.error_msg_couldntget),
            Toast.LENGTH_LONG
        ).show()

    }

    private fun navigateToWeatherActivity() {
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

        // TODO: Change implementation so that only one call to observe exists
        viewModel.weatherInfo?.observe(this) {
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
                // TODO: Change implementation so that we can remove this call to observe
                viewModel.fetchWeatherInfo(cityName).observe(this) {
                    handleWeatherInfoCompletion(it)
                }
            }
        }
    }
}