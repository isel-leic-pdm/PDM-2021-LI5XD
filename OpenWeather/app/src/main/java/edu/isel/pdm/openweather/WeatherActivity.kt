package edu.isel.pdm.openweather

import android.app.Application
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.isel.pdm.openweather.databinding.ActivityWeatherBinding
import edu.isel.pdm.openweather.repo.WeatherInfo
import java.lang.Thread.currentThread
import java.text.DateFormat.getDateInstance
import java.util.*

const val WEATHER_EXTRA_KEY = "WeatherActivity.WeatherInfo.Extra"

/**
 * View model for the [WeatherActivity]
 *
 * Design note: Notice the broken up asynchronous interface provided by the view model. The async
 * call [fetchWeatherImage] and its completion [imageBitmap] are separated.
 */
class WeatherActivityViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * The last fetched image bitmap.
     *
     * Implementation note: be aware of the use of Result from kotlin's stdlib here
     * See restrictions here:
     * https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md#encapsulate-successful-or-failed-function-execution
     */
    val imageBitmap: LiveData<Result<Bitmap>?> = MutableLiveData()

    /**
     * Asynchronous operation that fetches the image for the given URL, placing the resulting
     * bitmap in [imageBitmap]
     */
    fun fetchWeatherImage(url: String, width: Int, height: Int) {
        val app = getApplication<OpenWeatherApplication>()
        Log.v(app.appTag, "WeatherActivityViewModel.fetchWeatherImage on ${currentThread().name}")
        app.repository.fetchWeatherImage(
            url,
            width,
            height,
            {
                Log.v(app.appTag, "WeatherActivityViewModel.fetchWeatherImage success on ${currentThread().name}")
                (imageBitmap as MutableLiveData<Result<Bitmap>?>).value = Result.success(it)
            },
            {
                Log.v(app.appTag, "WeatherActivityViewModel.fetchWeatherImage failure on ${currentThread().name}")
                (imageBitmap as MutableLiveData<Result<Bitmap>?>).value = Result.failure(it)
            }
        )
    }
}

/**
 * Displays the weather information received as an Intent extra identified by [WEATHER_EXTRA_KEY].
 */
class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherActivityViewModel by viewModels()
    private val binding: ActivityWeatherBinding by confinedLazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val info: WeatherInfo = intent.getParcelableExtra(WEATHER_EXTRA_KEY)
            ?: throw IllegalArgumentException()

        displayWeatherInfo(info)

        viewModel.imageBitmap.observe(this) {
            if (it?.isSuccess == true) {
                binding.weatherImage.background = null
                binding.weatherImage.setImageBitmap(it.getOrNull())
            }
            else binding.weatherImage.setBackgroundResource(android.R.drawable.ic_menu_gallery)
        }

        viewModel.fetchWeatherImage(info.iconUrl, binding.weatherImage.width, binding.weatherImage.height)
    }

    /**
     * Helper function used to display the given weather information.
     *
     * @param info  the weather information
     */
    private fun displayWeatherInfo(info: WeatherInfo) {
        binding.cityName.text = info.cityName
        binding.description.text = info.description
        val humidityFormatString = resources.getString(R.string.text_humidity_placeholder)
        binding.humidity.text = String.format(humidityFormatString, info.humidity)
        val temperatureFormatString = resources.getString(R.string.text_temp_placeholder)
        binding.temp.text = String.format(temperatureFormatString, info.currTemperature.toInt())
        binding.maxTemp.text = String.format(temperatureFormatString, info.maxTemperature.toInt())
        binding.minTemp.text = String.format(temperatureFormatString, info.minTemperature.toInt())

        binding.date.text = getDateInstance().format(Date())
        val windFormatString = resources.getString(R.string.text_wind_placeholder)
        binding.wind.text = String.format(windFormatString, info.windSpeed, info.windDirection)
    }
}