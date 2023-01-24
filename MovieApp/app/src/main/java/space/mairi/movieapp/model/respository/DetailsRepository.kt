package space.mairi.movieapp.model.respository

import retrofit2.Callback
import space.mairi.movieapp.model.MovieDTO


interface DetailsRepository {
    fun getMovieDetailsFromServer(id : String,
                                    callback: Callback<MovieDTO>)
}