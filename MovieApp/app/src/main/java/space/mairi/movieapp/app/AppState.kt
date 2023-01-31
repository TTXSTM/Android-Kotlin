package space.mairi.movieapp.app

import space.mairi.movieapp.model.Movie

sealed class AppState {
    data class Success(val movieData: List<Movie>) : AppState()
    data class Error(val error : Throwable) : AppState()
    object Loading : AppState()
}