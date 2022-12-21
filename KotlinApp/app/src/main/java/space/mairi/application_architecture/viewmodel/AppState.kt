package space.mairi.application_architecture.viewmodel

import space.mairi.application_architecture.model.Weather

sealed class AppState{
    data class Success(val weatherData : List<Weather>) : AppState()
    data class Error(val error : Throwable) : AppState()
    object Loading : AppState()
}


