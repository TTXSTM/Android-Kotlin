package space.mairi.movieapp.view.details

import android.app.IntentService
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import space.mairi.movieapp.BuildConfig
import space.mairi.movieapp.model.MovieDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors

const val ID_EXTRA = "ID"
const val MOVIE_EXTRA = "MOVIE"

private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000

class DetailsService (name : String = "DetailsService") : IntentService(name){

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(p0: Intent?) {

        p0?.let {
            val id = p0.getStringExtra(ID_EXTRA)
            val movie = p0.getStringExtra(MOVIE_EXTRA)

            if (id == null && movie == null) {
                onEmptyData()
            }else{
                loadMovie(id.toString(), movie.toString())
            }
        } ?: run {
            onEmptyIntent()
        }
    }

    private fun loadMovie(id : String, movie : String) {
        try {
            val uri =
                URL("https://imdb-api.com/en/API/Title/${BuildConfig.MOVIE_API_KEY}/${id}/FullActor")

            lateinit var urlConnection: HttpURLConnection

            try {
                urlConnection = uri.openConnection() as HttpURLConnection

                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                }

                val movieDTO : MovieDTO =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                        MovieDTO::class.java
                    )

                onResponse(movieDTO)

            } catch (e : Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }

        }catch (e : MalformedURLException) {
            onMalformedURL()
        }
    }

    private fun onResponse(movieDTO: MovieDTO) {
        val items = movieDTO.items

        items?.let {
            onSuccessfulResponse(items.title, items.fullTitle, items.plot, items.year,
                items.runtimeMins, items.imDbRating, items.releaseDate)

        } ?: run {
            onEmptyResponse()
        }
    }

    private fun onSuccessfulResponse(title : String?, fullTitle : String?,
        plot : String?, year : String?,
        runtimeMins : String?, imDBRating : String?,
        releaseDate : String?){

        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_SUCCESS_EXTRA)

        broadcastIntent.apply {
            putExtra(DETAILS_TITLE_EXTRA, title)
            putExtra(DETAILS_FULL_TITLE_EXTRA, fullTitle)
            putExtra(DETAILS_YEAR_EXTRA, year)
            putExtra(DETAILS_RELEASE_DATE_EXTRA, releaseDate)
            putExtra(DETAILS_RUNTIME_MINS_EXTRA, runtimeMins)
            putExtra(DETAILS_PLOT_EXTRA, plot)
            putExtra(DETAILS_IMDB_RATING_EXTRA, imDBRating)
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)

    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result : String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

}

