package space.mairi.movieapp.model

class RepositoryImpl : Repository{

    override fun getMovieFromServer(): Movie {
        return Movie()
    }

    override fun getMovieFomLocalStorage(): Movie {
        return Movie()
    }
}