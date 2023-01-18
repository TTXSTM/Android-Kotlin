package space.mairi.movieapp.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import space.mairi.movieapp.databinding.FragmentDetailsBinding
import space.mairi.movieapp.model.ItemDTO
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.MovieDTO

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

private const val MOVIE_TITLE_INVALID = " "
private const val MOVIE_FULL_TITLE_INVALID = " "
private const val MOVIE_YEAR_INVALID = " "
private const val MOVIE_RELEASE_DATE_INVALID = " "
private const val MOVIE_RUNTIME_MINS_INVALID = " "
private const val MOVIE_PLOT_INVALID = " "
private const val MOVIE_IMDB_RATING_INVALID = " "
private const val MOVIE_TIME_INVALID = " "
private const val MOVIE_BUDGET_INVALID = " "
private const val PROCESS_ERROR = "Обработка ошибки"

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

                DETAILS_RESPONSE_SUCCESS_EXTRA -> displayMovie(
                    MovieDTO(
                        ItemDTO(
                            p1.getStringExtra(
                                DETAILS_TITLE_EXTRA
                            ),
                            p1.getStringExtra(
                                DETAILS_FULL_TITLE_EXTRA
                            ),
                            p1.getStringExtra(
                                DETAILS_YEAR_EXTRA
                            ),
                            p1.getStringExtra(
                                DETAILS_RELEASE_DATE_EXTRA
                            ),
                            p1.getStringExtra(
                                DETAILS_RUNTIME_MINS_EXTRA
                            ),
                            p1.getStringExtra(
                                DETAILS_PLOT_EXTRA
                            ),
                            p1.getStringExtra(
                                DETAILS_IMDB_RATING_EXTRA
                            )
                        )
                    )
                )

            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviBundle = arguments?.getParcelable<Movie>(BUNDLE_EXTRA) ?: Movie()

        getMovie()
    }

    private fun getMovie() {
        with(binding) {
            mainView.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
        }

        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(ID_EXTRA, moviBundle.name.id)
                putExtra(MOVIE_EXTRA, moviBundle.name.movie)
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

    ///// !
    private fun displayMovie(movieDTO: MovieDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            movieName.text = movieDTO.items?.title
            movieReleseDate.text = movieDTO.items?.releaseDate
            movieTime.text = movieDTO.items?.runtimeMins
            movieDescription.text = movieDTO.items?.plot
            movieOriginalName.text = movieDTO.items?.fullTitle
        }
    }
}