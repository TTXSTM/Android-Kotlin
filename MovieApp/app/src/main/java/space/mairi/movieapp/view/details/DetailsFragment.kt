package space.mairi.movieapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import space.mairi.movieapp.databinding.FragmentDetailsBinding
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.MovieDTO

class DetailsFragment : Fragment(){
    private var _binding :  FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviBundle : Movie

    private val onLoaderListener : MovieLoader.MovieLoaderListener =
        object : MovieLoader.MovieLoaderListener {
            override fun onLoaded(movieDTO: MovieDTO) {
                displayMovie(movieDTO)
            }

            override fun onFailed(throwable: Throwable) {
                Snackbar.make(binding.mainView, "Loaded Failed", Snackbar.LENGTH_LONG)
            }
        }

    companion object{
        const val BUNDLE_EXTRA = "movie"

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

        moviBundle = arguments?.getParcelable<Movie>(BUNDLE_EXTRA) ?: Movie()

        with(binding) {
            mainView.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        val loader = MovieLoader(onLoaderListener, moviBundle.lang, moviBundle.stat)
        loader.loadMovie()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///// !
    private fun displayMovie(movieDTO: MovieDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            movieName.text = movieDTO.items?.title
            movieReleseDate.text = movieDTO.items?.releaseState
            movieTime.text = movieDTO.items?.runtimeStr
            movieDescription.text = movieDTO.items?.plot
            movieOriginalName.text = movieDTO.items?.fullTitle
        }
    }
}