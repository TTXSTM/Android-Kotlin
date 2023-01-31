package space.mairi.movieapp.respository

import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.MovieDTO
import space.mairi.movieapp.room.HistoryDao
import space.mairi.movieapp.utils.convertHistoryEntityToMovie
import space.mairi.movieapp.utils.convertMovieToEntity

class LocalRepositoryImpl (private val localDataSource : HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Movie> {
        return convertHistoryEntityToMovie(localDataSource.all())
    }

    override fun saveEntity(movie: Movie) {
        localDataSource.insert(convertMovieToEntity(movie))
    }

}