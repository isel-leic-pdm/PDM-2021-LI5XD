package edu.isel.pdm.openweather.repo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    val windDirection: CardinalPoint,
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
    windDegreesToDirection(dto.wind.deg),
    dto.main.temperature,
    dto.main.maxTemperature,
    dto.main.minTemperature,
    dto.main.humidity
)

/**
 * Enumeration that corresponds to the existing cardinal points.
 */
enum class CardinalPoint {
    N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NWN;
}

/**
 * Utility method that converts between wind direction expressed in degrees to its corresponding
 * direction expressed in cardinal points.
 * @param wind The wind direction, expressed in degrees.
 * @return The corresponding cardinal point.
 */
private fun windDegreesToDirection(wind: Double): CardinalPoint {
    val fullCircle = 360
    val slotSize = (fullCircle / CardinalPoint.values().size).toDouble()
    return CardinalPoint.values()[((wind + slotSize / 2) % fullCircle / slotSize).toInt()]
}
