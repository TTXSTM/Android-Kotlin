package space.mairi.application_architecture.model

class RepositoryImpl : Repository{

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFomLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFomLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }

}