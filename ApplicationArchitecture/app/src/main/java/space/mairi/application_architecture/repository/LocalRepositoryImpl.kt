package space.mairi.application_architecture.repository

import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.repository.LocalRepository
import space.mairi.application_architecture.room.HistoryDao
import space.mairi.application_architecture.utils.convertHistoryEntityToWeather
import space.mairi.application_architecture.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource : HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
       localDataSource.insert(convertWeatherToEntity(weather))
    }

}