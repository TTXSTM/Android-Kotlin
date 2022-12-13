package space.mairi.application_architecture.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFomLocalStorage(): Weather
}