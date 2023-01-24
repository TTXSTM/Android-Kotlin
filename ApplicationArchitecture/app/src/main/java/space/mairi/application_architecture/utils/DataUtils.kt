package space.mairi.application_architecture.utils

import space.mairi.application_architecture.model.FactDTO
import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.model.WeatherDTO
import space.mairi.application_architecture.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact : FactDTO = weatherDTO.fact!! // !!! - ЭТО РИСК!!!

    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!, fact.condition!!))
}