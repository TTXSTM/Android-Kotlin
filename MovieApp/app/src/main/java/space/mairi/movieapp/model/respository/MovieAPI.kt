package space.mairi.movieapp.model.respository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import space.mairi.movieapp.model.ItemDTO
import space.mairi.movieapp.model.MovieDTO


interface MovieAPI {
    @GET("en/API/Title/")
    fun getMovie(
        token : String,
        @Query("id") id: String
    ) : Call<MovieDTO>
}