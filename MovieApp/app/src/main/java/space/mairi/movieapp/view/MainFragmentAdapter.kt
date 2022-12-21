package space.mairi.movieapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import space.mairi.movieapp.R
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.ui.main.MainFragment


class MainFragmentAdapter(private val onItemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>(){

    private var movieData : List<Movie> = listOf()

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }
    fun setMovie(data : List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    inner class MainViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(movie: Movie){
            itemView.findViewById<TextView>(R.id.mainFragmentRecylerItemTextView).text =
                movie.name.movie

            itemView.findViewById<TextView>(R.id.movie_year).text =
                movie.year.toString()

            itemView.findViewById<TextView>(R.id.movie_rating).text =
                movie.rating.toString()

            itemView.setOnClickListener{
                onItemClickListener?.onItemClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycle_item,
                                                        parent,
                                                        false) as View)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount(): Int {
        return movieData.size
    }
}
