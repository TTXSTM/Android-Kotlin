package space.mairi.movieapp.viewmodel

import space.mairi.movieapp.model.Movie

sealed class AppState {
    data class Success(val movieData : Movie) : AppState()

    data class Error(val error : Throwable) : AppState()

    object Loading : AppState()
}