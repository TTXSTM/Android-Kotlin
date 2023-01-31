package space.mairi.application_architecture.repository

import space.mairi.application_architecture.model.Weather

interface LocalRepository {
    fun getAllHistory() : List<Weather>
    fun saveEntity(weather: Weather)
}