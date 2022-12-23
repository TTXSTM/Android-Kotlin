package space.mairi.movieapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.lifecycle.get
import com.google.android.material.snackbar.Snackbar
import space.mairi.movieapp.R
import space.mairi.movieapp.databinding.FragmentMainBinding
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.view.MainFragmentAdapter
import space.mairi.movieapp.view.details.DetailsFragment
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

    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val isDataSetNowPlaying : Boolean = true

    private val adapter = MainFragmentAdapter(object : MainFragmentAdapter.OnItemClickListener{
        override fun onItemClick(movie: Movie) {
            activity?.supportFragmentManager?.apply {

                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, movie)
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

        binding.mainFragmentRecylerView.adapter = adapter

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {renderData(it)})
        viewModel.getMovieFromLocalStorageNowPlaying()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val observer = Observer<AppState>{
            renderData(it)
        }

        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getMovie()
    }


    private fun renderData(appState: AppState){
        when(appState){

            is AppState.Success -> {

                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setMovie(appState.movieData)
            }

            is AppState.Loading -> {

                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE

                binding.mainFragmentRecylerView.showSnackbar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getMovieFromLocalStorageNowPlaying() }
                )
            }
        }
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