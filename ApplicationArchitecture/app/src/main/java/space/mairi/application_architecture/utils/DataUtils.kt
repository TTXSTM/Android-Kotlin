package space.mairi.application_architecture.utils

import space.mairi.application_architecture.model.*
import space.mairi.application_architecture.room.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact : FactDTO = weatherDTO.fact!! // !!! - ЭТО РИСК!!!

    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!, fact.condition!!))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>) : List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
    }
}

fun convertWeatherToEntity(weather: Weather) : HistoryEntity {
    return HistoryEntity(0, weather.city.city, weather.temperature, weather.condition)
}