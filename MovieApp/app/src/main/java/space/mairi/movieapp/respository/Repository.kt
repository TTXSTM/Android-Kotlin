package space.mairi.movieapp.respository

import space.mairi.movieapp.model.Movie

interface Repository {
    fun getMovieFromLocalStorageNowPlaying(): List<Movie>
    fun getMovieFromServer(): Movie
}