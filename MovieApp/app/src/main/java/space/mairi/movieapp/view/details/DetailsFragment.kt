package space.mairi.movieapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import space.mairi.movieapp.databinding.FragmentDetailsBinding
import space.mairi.movieapp.model.Movie

class DetailsFragment : Fragment(){
    private var _binding :  FragmentDetailsBinding? = null

    private val binding get() = _binding!!

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

        val movie = arguments?.getParcelable<Movie>(BUNDLE_EXTRA)

        if(movie != null){
            binding.movieTime.text = String.format(
                "%s min",
                movie.name.min.toString()
            )

            binding.movieBudget.text = String.format(
                "Budget: %s $",
                movie.name.budget.toString()
            )

            binding.movieRevenue.text = String.format(
                "Revenue: %s $",
                movie.name.revenue.toString()
            )

            binding.movieReleseDate.text = String.format(
                "Relese date: (%s)",
                movie.name.date
            )

            binding.movieName.text = movie.name.movie
            binding.movieGenre.text = movie.name.genre
            binding.movieDescription.text = movie.name.descriptor
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}