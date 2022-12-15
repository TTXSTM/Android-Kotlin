package space.mairi.movieapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import space.mairi.movieapp.databinding.FragmentMainBinding
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.viewmodel.AppState
import java.util.Random
import kotlin.random.Random.Default.nextInt

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null

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

        val observer = Observer<AppState>{
            renderData(it)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        viewModel.getMovie()
    }


    private fun renderData(appState: AppState){

        when(appState){

            is AppState.Success -> {

                val movieData = appState.movieData

                binding.loadingLayout.visibility = View.GONE

                Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()

                setData(movieData)
            }

            is AppState.Loading -> {

                binding.loadingLayout.visibility = View.VISIBLE

            }

            is AppState.Error -> {

                binding.loadingLayout.visibility = View.GONE

                Snackbar.make(binding.mainView, "Error", Snackbar.LENGTH_LONG)

                    .setAction("Reload"){

                        viewModel.getMovie()

                    }
                    .show()
            }
        }
    }

    private fun setData(movieData: Movie){

        binding.movieName.text = movieData.name.movie

        binding.movieYear.text = movieData.year.toString()

        binding.movieRating.text = movieData.rating.toString()

    }


}