package space.mairi.movieapp.view.details

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import space.mairi.movieapp.BuildConfig
import space.mairi.movieapp.model.MovieDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieLoader (
    private val listener : MovieLoaderListener,
    private val id : String
) {
    interface MovieLoaderListener {
        fun onLoaded(movieDTO: MovieDTO)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadMovie() =
        try {
            val uri =
                URL("https://imdb-api.com/en/API/Title/${BuildConfig.MOVIE_API_KEY}/${id}/FullActor")

            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpsURLConnection

                try {
                    urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000

                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(bufferedReader)

                    val movieDTO : MovieDTO = Gson().fromJson(response, MovieDTO::class.java)

                    handler.post {
                        listener.onLoaded(movieDTO)
                    }

                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()

                    // Вставить снекбар

                    handler.post {
                        listener.onFailed(e)
                    }
                } finally {
                    urlConnection.disconnect()
                }
            }.start()

        } catch (e : MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader) : String {
        return reader.lines().collect(Collectors.joining("\n"))
    }


}

