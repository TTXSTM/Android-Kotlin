package space.mairi.application_architecture.repository

import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.model.getRussianCities
import space.mairi.application_architecture.model.getWorldCities

class MainRepositoryImpl : MainRepository {
    override fun getWeatherFromServer(): Weather = Weather()
    override fun getWeatherFomLocalStorageRus(): List<Weather> = getRussianCities()
    override fun getWeatherFomLocalStorageWorld(): List<Weather> = getWorldCities()

}