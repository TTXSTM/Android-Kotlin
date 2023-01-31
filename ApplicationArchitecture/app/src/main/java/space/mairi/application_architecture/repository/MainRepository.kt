package space.mairi.application_architecture.repository

import space.mairi.application_architecture.model.Weather

interface MainRepository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFomLocalStorageRus(): List<Weather>
    fun getWeatherFomLocalStorageWorld(): List<Weather>

}