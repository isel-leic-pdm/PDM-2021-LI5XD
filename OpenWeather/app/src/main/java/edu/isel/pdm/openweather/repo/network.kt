package edu.isel.pdm.openweather.repo

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * DTO that represents the payload of a response to requests for obtaining weather information
 * (implemented through [GetWeatherInfoRequest])
 */
class WeatherInfoDTO(
    @JsonProperty("name") val cityName: String,
    val weather: Array<WeatherEntry>,
    val wind: Wind,
    val main: WeatherParameters
) {
    class Wind(val speed: Double, val deg: Double)

    class WeatherParameters(
        @JsonProperty("temp") val temperature: Double,
        @JsonProperty("temp_min") val minTemperature: Double,
        @JsonProperty("temp_max") val maxTemperature: Double,
        val humidity: Int
    )

    data class WeatherEntry(val description: String, val icon: String)
}


/**
 * Gets the URL of the icon with the given name
 */
fun getWeatherIconURL(iconName: String): String = "$BASE_URL$IMAGE_PATH$iconName$IMAGE_EXTENSION"

/**
 * Implementation of the request for obtaining the weather information
 */
class GetWeatherInfoRequest(
    url: String,
    private val mapper: ObjectMapper,
    success: Response.Listener<WeatherInfoDTO>,
    error: Response.ErrorListener
) : JsonRequest<WeatherInfoDTO>(Request.Method.GET, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<WeatherInfoDTO> {
        val currenciesDto = mapper.readValue(String(response.data), WeatherInfoDTO::class.java)
        return Response.success(currenciesDto, null)
    }
}