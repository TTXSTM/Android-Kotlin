package space.mairi.application_architecture.repository

import retrofit2.Callback
import space.mairi.application_architecture.model.WeatherDTO


interface DetailsRepository {
    fun getWeatherDetailsFromServer(lat: Double,
                                    lon: Double,
                                    callback: Callback<WeatherDTO>)

}