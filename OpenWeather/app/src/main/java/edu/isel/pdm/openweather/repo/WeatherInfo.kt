package edu.isel.pdm.openweather.repo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Represents weather information for the current day. It is the application's model.
 *
 * @property cityName           the city name
 * @property description        the weather description
 * @property iconUrl            the URL of the icon associated to the current weather conditions
 * @property windSpeed          the wind speed (in meters/second)
 * @property currTemperature    the current temperature
 * @property maxTemperature     today's maximum temperature
 * @property minTemperature     today's minimum temperature
 * @property humidity           the air's humidity (in percentage)
 */
@Parcelize
data class WeatherInfo(
    val cityName: String,
    val description: String,
    val iconUrl: String,
    val windSpeed: Double,
    val windDirection: Double,
    val currTemperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val humidity: Int
) : Parcelable

/**
 * Maps a DTO instance to the corresponding model instance
 */
fun modelFromDTO(dto: WeatherInfoDTO) = WeatherInfo(
    dto.cityName,
    dto.weather[0].description,
    getWeatherIconURL(dto.weather[0].icon),
    dto.wind.speed,
    dto.wind.deg,           // TODO: Specify as a cardinal point based direction
    dto.main.temperature,
    dto.main.maxTemperature,
    dto.main.minTemperature,
    dto.main.humidity
)
