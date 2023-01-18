package space.mairi.application_architecture.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentDetailsBinding
import space.mairi.application_architecture.model.FactDTO
import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.model.WeatherDTO

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"

const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"

private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"



class DetailsFragment : Fragment() {
    private var _binding : FragmentDetailsBinding? = null
    private val binding get()= _binding!!

    private lateinit var weatherBundle : Weather

    private val onLoadListener : WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {
            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)


            }

            override fun onFailed(throwable : Throwable) {
                Snackbar.make(binding.mainView, "Loaded Failed", Snackbar.LENGTH_LONG)
            }

        }

    private val loadResultReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, p1: Intent) {

            val strExtra = p1.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)

           when (strExtra) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)

               DETAILS_RESPONSE_SUCCESS_EXTRA -> displayWeather(
                   WeatherDTO(
                       FactDTO(
                           p1.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
                           p1.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                           p1.getStringExtra(
                               DETAILS_CONDITION_EXTRA
                           )
                       )
                   )
               )
           }
        }

    }

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather()

//        with(binding) {
//            mainView.visibility = View.GONE
//            loadingLayout.visibility = View.VISIBLE
//        }
//
//        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
//        loader.loadWeather()

        getWeather()
    }

    private fun getWeather() {
        with(binding) {
            mainView.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(LATITUDE_EXTRA, weatherBundle.city.lat)
                putExtra(LONGITUDE_EXTRA, weatherBundle.city.lon)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultReceiver)
        }

        _binding = null
    }

    private fun displayWeather(weatherDTO : WeatherDTO) {
        Log.d("TAG", "displayWeather")
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            weatherBundle.city.also {
                with(binding){
                    binding.cityName.text = it.city
                    binding.cityCoordinates.text = String.format(
                        getString(R.string.city_coordinates),
                        it.lat.toString(),
                        it.lon.toString()
                    )
                }
            }
            weatherCondition.text = weatherDTO.fact?.condition
            binding.temperatureValue.text = weatherDTO.fact?.temp.toString()
            binding.feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }
}