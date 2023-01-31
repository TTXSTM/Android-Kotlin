package space.mairi.movieapp.respository

import retrofit2.Call
import retrofit2.http.*
import space.mairi.movieapp.BuildConfig
import space.mairi.movieapp.model.MovieDTO


interface MovieAPI {
    @GET("en/API/Title/${BuildConfig.MOVIE_API_KEY}/{id}")
    fun getMovie(
        @Header("api") token : String,
        @Path("id") id : String
    ) : Call<MovieDTO>

}