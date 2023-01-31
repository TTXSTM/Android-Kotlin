package space.mairi.movieapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import space.mairi.movieapp.R
import space.mairi.movieapp.databinding.FragmentDetailsBinding
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.utils.showSnackBar
import space.mairi.movieapp.app.AppState
import space.mairi.movieapp.model.MovieDTO
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
//
class DetailsFragment : Fragment() {
    private var _binding :  FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var moviBundle : Movie

    private val viewModel : DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    private var mColorCheckBox: CheckBox? = null

    companion object{
        const val BUNDLE_EXTRA = "movie"
//
        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
//
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviBundle = arguments?.getParcelable<Movie>(BUNDLE_EXTRA) ?: Movie()

       viewModel.detailsLiveData.observe(viewLifecycleOwner, { renderData(it) })

        requestMovie()
    }

    private fun requestMovie() {
        viewModel.getMovie(moviBundle.id)
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
            val title = movie
                if (movie.contentRating == "NC-17"){
                    binding.mainView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        {
                            requestMovie()
                        })
                }else{
                    movieGenre.text = title.genres

                    saveMovie(title)

                    movieBudget.text = title.boxOffice.budget
                    movieRevenue.text = title.boxOffice.cumulativeWorldwideGross
                    movieOriginalName.text = title.full_title
                    movieName.text = title.title
                    movieTime.text = String.format(
                        "%s min",
                        title.runtime_mins
                    )
                    movieDescription.text = title.plot
                    movieReleseDate.text = title.release_date


                    Picasso
                        .get()
                        .load(title.image)
                        .into(binding.movieIcon)
                }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun saveMovie(movie: Movie) {
        viewModel.saveCityToDB(
            Movie(
                movie.id,
                movie.title,
                movie.full_title
            )
        )
    }
}