package space.mairi.application_architecture.view.main

import android.content.Context
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
import space.mairi.application_architecture.app.AppState
import space.mairi.application_architecture.viewmodel.MainViewModel

private const val IS_WORLD_KEY = "IS_WORLD_KEY"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var isDataSetRus : Boolean = true

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemClickListener {
        override fun onItemClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {

                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val observer = Observer<AppState> {
            renderData(it)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        showListOfTowns()
    }

    private fun showListOfTowns() {
        activity?.let {
            if(it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                changeWeatherDataSet()
            } else {
                viewModel.getWeatherFomLocalStorageRus()
            }
        }
    }

    private fun saveListOfTowns() {
        activity?.let {
            with (it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, !isDataSetRus)
                apply()
            }
        }
    }

    private fun renderData(appState : AppState){
        when(appState){
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }

            is AppState.Loading -> {

                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE

                binding.mainFragmentFAB.showSnackbar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFomLocalStorageRus() }
                )
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
        }.also { isDataSetRus = !isDataSetRus }

        saveListOfTowns()
    }

    private fun View.showSnackbar(
        text : String,
        actionText : String,
        action : (View) -> Unit,
        lenght : Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, lenght).setAction(actionText, action).show()
    }
}