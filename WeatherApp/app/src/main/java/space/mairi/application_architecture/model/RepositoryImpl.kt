package space.mairi.application_architecture.model

class RepositoryImpl : Repository{
    override fun getWeatherFromServer(): Weather = Weather()
    override fun getWeatherFomLocalStorageRus(): List<Weather> = getRussianCities()
    override fun getWeatherFomLocalStorageWorld(): List<Weather> = getWorldCities()

}