package space.mairi.application_architecture.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentMainBinding
import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.view.details.DetailsFragment
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
    private var isDataSetRus : Boolean = true

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemClickListener {
        override fun onItemClick(weather: Weather) {
            val fragmentManager = activity?.supportFragmentManager

            if(fragmentManager != null){
                val bundle = Bundle()

                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)

                fragmentManager.beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener{
            changeWeatherDataSet()
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {renderData(it)})
        viewModel.getWeatherFomLocalStorageRus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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

                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)

            }

            is AppState.Loading -> {

                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE

            }

            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE

                Snackbar.make(binding.mainFragmentFAB, "Error", Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.reload)){
                        viewModel.getWeatherFomLocalStorageRus()
                    }
                    .show()
            }
        }
    }
    private fun changeWeatherDataSet(){
        if(isDataSetRus){
            viewModel.getWeatherFomLocalStorageWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }else{
            viewModel.getWeatherFomLocalStorageRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }

        isDataSetRus = !isDataSetRus
    }
}