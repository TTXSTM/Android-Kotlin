package space.mairi.movieapp.model

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import space.mairi.movieapp.BuildConfig
import space.mairi.movieapp.model.respository.MovieAPI

class RemoteDataSource {
    private val movieApi = Retrofit.Builder()
        .baseUrl("https://imdb-api.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(createOkHttpClient(MovieApiInterceptor()))
        .build().create(MovieAPI::class.java)

    fun getMovieDetails(id: String, callback: Callback<MovieDTO>) {
        movieApi.getMovie(BuildConfig.MOVIE_API_KEY, id).enqueue(callback)
    }

    private fun createOkHttpClient(interceptor: Interceptor) : OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY))

        return httpClient.build()
    }

    inner class MovieApiInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            return chain.proceed(chain.request())
        }
    }
}