package space.mairi.application_architecture.model

class RepositoryImpl : Repository{
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFomLocalStorage(): Weather {
        return Weather()
    }

}