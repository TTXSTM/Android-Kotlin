package space.mairi.application_architecture.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFomLocalStorageRus(): List<Weather>
    fun getWeatherFomLocalStorageWorld(): List<Weather>

}