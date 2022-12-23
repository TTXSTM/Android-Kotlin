package space.mairi.application_architecture.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import space.mairi.application_architecture.R
import space.mairi.application_architecture.model.Weather


class MainFragmentAdapter(private val onItemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>(){

    private var weatherData : List<Weather> = listOf()

    interface OnItemClickListener {
        fun onItemClick(weather: Weather)
    }

    fun setWeather(data : List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    inner class MainViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    weather.city.city

                setOnClickListener {
                    onItemClickListener?.onItemClick(weather)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
       return MainViewHolder(
           LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_item,
                                                        parent,
                                             false) as View)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }
}