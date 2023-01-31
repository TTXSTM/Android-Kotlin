package space.mairi.movieapp.respository

import space.mairi.movieapp.model.Movie

interface LocalRepository {
    fun getAllHistory() : List<Movie>
    fun saveEntity(weather: Movie)
}