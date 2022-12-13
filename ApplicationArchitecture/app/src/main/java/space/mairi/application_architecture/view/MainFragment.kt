package space.mairi.application_architecture.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentMainBinding
import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.viewmodel.AppState
import space.mairi.application_architecture.viewmodel.MainViewModel

class MainFragment : Fragment() {



    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

//        val observer0 = object :Observer<Any>{
//            override fun onChanged(t: Any?) {
//                renderData(t!!)
//            }
//
//        }

        val observer = Observer<AppState> {
            renderData(it)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        viewModel.getWeather()
    }

    private fun renderData(appState : AppState){

        when(appState){
            is AppState.Success -> {

                val weatherData = appState.weatherData

                binding.loadingLayout.visibility = View.GONE

                Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()

                setData(weatherData)
            }

            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE

            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE

                Snackbar.make(binding.mainView, "Error", Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.reload)){
                        viewModel.getWeather()
                    }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.city

        binding.cityCoordinates.text = String.format(
            "lt/ln: %s, %s",
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )

        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelLike.toString()

    }

}