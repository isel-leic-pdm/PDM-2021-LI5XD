package edu.isel.pdm.openweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.openweather.databinding.ActivityWeatherBinding
import edu.isel.pdm.openweather.repo.WeatherInfo

const val WEATHER_EXTRA_KEY = "WeatherActivity.WeatherInfo.Extra"

/**
 * Displays the weather information received as an Intent extra identified by [WEATHER_EXTRA_KEY].
 */
class WeatherActivity : AppCompatActivity() {

    private val binding: ActivityWeatherBinding by confinedLazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    private val info: WeatherInfo? by confinedLazy {
        intent.getParcelableExtra(WEATHER_EXTRA_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (info == null) {
            throw IllegalArgumentException()
        }
    }
}