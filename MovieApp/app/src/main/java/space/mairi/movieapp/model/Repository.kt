package space.mairi.movieapp.model

interface Repository {

    fun getMovieFromLocalStorageNowPlaying(): List<Movie>
    fun getMovieFromServer(): Movie
}