package space.mairi.application_architecture.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import space.mairi.application_architecture.BuildConfig
import space.mairi.application_architecture.model.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.locks.Condition
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"

private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class DetailsService (name : String = "DetailService") : IntentService(name) {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(p0: Intent?) {

        p0?.let {
            val lat = p0.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = p0.getDoubleExtra(LONGITUDE_EXTRA, 0.0)

            if(lat == 0.0 && lon == 0.0) {
                onEmptyData()
            }else {
                loadWeather(lat.toString(), lon.toString())
            }

        } ?: run {
            onEmptyIntent()
        }
    }

    private fun loadWeather(lat : String, lon : String) {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat${lat}&lon=${lon}")

            lateinit var urlConnection : HttpURLConnection

            try {
                urlConnection = uri.openConnection() as HttpURLConnection
                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty(
                        REQUEST_API_KEY,
                        BuildConfig.WEATHER_API_KEY
                    )
                }

                val weatherDTO : WeatherDTO =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                        WeatherDTO::class.java
                    )

                onResponse(weatherDTO)

            }catch (e : Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }

        }catch (e : MalformedURLException) {
            onMalformedURL()
        }
    }

    private fun onResponse(weatherDTO: WeatherDTO) {
        val fact = weatherDTO.fact

        fact?.let {
            onSuccessfulResponse(fact.temp, fact.feels_like, fact.condition)
        } ?: run {
            onEmptyResponse()
        }
    }

    private fun onSuccessfulResponse(temp: Int?, feelsLike: Int?, condition: String?) {
        //val broadcastInt = Intent(DETAILS_INTENT_FILTER)
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_SUCCESS_EXTRA)

        broadcastIntent.apply {
            putExtra(DETAILS_TEMP_EXTRA, temp)
            putExtra(DETAILS_FEELS_LIKE_EXTRA, feelsLike)
            putExtra(DETAILS_CONDITION_EXTRA, condition)
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