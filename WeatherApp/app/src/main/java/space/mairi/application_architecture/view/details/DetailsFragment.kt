package space.mairi.application_architecture.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentDetailsBinding
import space.mairi.application_architecture.model.Weather

class DetailsFragment : Fragment() {
    private var _binding : FragmentDetailsBinding? = null

    private val binding get()= _binding!!

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->  
            weather.city.also {
                with(binding){
                    binding.cityName.text = it.city

                    binding.cityCoordinates.text = String.format(
                        getString(R.string.city_coordinates),
                        it.lat.toString(),
                        it.lon.toString()
                    )

                    binding.temperatureValue.text = weather.temperature.toString()
                    binding.feelsLikeValue.text = weather.feelsLike.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}