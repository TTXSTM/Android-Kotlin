package space.mairi.movieapp.respository


import space.mairi.movieapp.model.MovieDTO


class DetailsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : DetailsRepository {

    override fun getMovieDetailsFromServer(
        id : String,
        callback: retrofit2.Callback<MovieDTO>
    ) {
        remoteDataSource.getMovieDetails(id, callback)
    }
}