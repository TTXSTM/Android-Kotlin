package space.mairi.movieapp.model.respository


import space.mairi.movieapp.model.MovieDTO
import space.mairi.movieapp.model.RemoteDataSource


class DetailsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : DetailsRepository{

    override fun getMovieDetailsFromServer(
        id : String,
        callback: retrofit2.Callback<MovieDTO>) {

        remoteDataSource.getMovieDetails(id, callback)
    }
}