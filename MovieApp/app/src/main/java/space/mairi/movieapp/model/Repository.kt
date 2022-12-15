package space.mairi.movieapp.model

interface Repository {

    fun getMovieFromServer(): Movie

    fun getMovieFomLocalStorage(): Movie
}