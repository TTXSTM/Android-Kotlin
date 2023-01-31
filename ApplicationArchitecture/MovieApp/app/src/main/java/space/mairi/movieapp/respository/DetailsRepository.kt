package space.mairi.movieapp.respository

import retrofit2.Callback
import space.mairi.movieapp.model.MovieDTO


interface DetailsRepository {
    fun getMovieDetailsFromServer(id : String,
                                    callback: Callback<MovieDTO>)
}