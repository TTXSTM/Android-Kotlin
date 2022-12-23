package space.mairi.movieapp.model

interface Repository {

    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorageNowPlaying(): List<Movie>
}