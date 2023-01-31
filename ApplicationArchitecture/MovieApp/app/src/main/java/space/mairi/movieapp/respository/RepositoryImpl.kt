package space.mairi.movieapp.respository

import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.getNowPlaying

class RepositoryImpl : Repository {

    override fun getMovieFromServer(): Movie = Movie()

    override fun getMovieFromLocalStorageNowPlaying(): List<Movie> = getNowPlaying()


}