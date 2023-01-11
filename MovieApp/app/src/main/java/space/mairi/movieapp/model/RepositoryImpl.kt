package space.mairi.movieapp.model

class RepositoryImpl : Repository{

    override fun getMovieFromServer(): Movie = Movie()

    override fun getMovieFromLocalStorageNowPlaying(): List<Movie> = getNowPlaying()


}