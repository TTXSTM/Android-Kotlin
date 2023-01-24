package space.mairi.movieapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_main_recycle_item.*
import space.mairi.movieapp.R
import space.mairi.movieapp.databinding.FragmentDetailsBinding
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.utils.showSnackBar
import space.mairi.movieapp.viewmodel.AppState
import space.mairi.movieapp.viewmodel.DetailsViewModel

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"

const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TITLE_EXTRA = "TITLE"
const val DETAILS_FULL_TITLE_EXTRA = "FULL TITLE"
const val DETAILS_YEAR_EXTRA = "YEAR"
const val DETAILS_RELEASE_DATE_EXTRA = "RELEASE DATE"
const val DETAILS_RUNTIME_MINS_EXTRA = "RUNTIME MINS"
const val DETAILS_PLOT_EXTRA = "PLOT"
const val DETAILS_IMDB_RATING_EXTRA = "IMBD RATING"

private const val MAIN_LINK= "https://imdb-api.com/en/API/Title/"

class DetailsFragment : Fragment(){
    private var _binding :  FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var moviBundle : Movie

    private val viewModel : DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    companion object{
        const val BUNDLE_EXTRA = "movie"

        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

       viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer {
           renderData(it)
       })

        requestMovie()
    }

    private fun requestMovie() {
        viewModel.getMovie(moviBundle.name.id)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE

                setMovie(appState.movieData[0])
            }

            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }

            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE

                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        requestMovie()
                    })
            }
        }
    }

    private fun setMovie(movie: Movie) {
        with(binding) {
            val title = moviBundle
            movieName.text = title.name.movie
            movieOriginalName.text = title.full_title
            movieTime.text = title.runtime_mins
            movieDescription.text = title.plot
            movieReleseDate.text = title.release_date
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}