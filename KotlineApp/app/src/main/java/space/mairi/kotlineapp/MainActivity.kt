package space.mairi.kotlineapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object : Comparator<Weather> {
        private val EXTRA_ACTIVITYNAME = MainActivity::class.simpleName

        fun getIntent() : Intent{
            val intent = Intent()

            return intent
        }

        override fun compare(p0: Weather?, p1: Weather?): Int {
            TODO("Not yet implemented")
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Repository.getWeatherList()

        val w : Weather = Weather("SPB" , 9)
        var town = "Москва"
        town = "Саратов"

        w.town = "Саратов"

        val weatherList = ArrayList<Weather>()
        weatherList.add(Weather())

        w.setWeather("Mos",  isHomeTown = false, temp = 0)

        val getappname = getAppName("ssdfdf", "dsfse")

        var note = Note("sdfsdf", "sdfsdfsdf", 88)

        val newCopy = note.copy(title = "Санкт-Петербург")

        val thread : Thread = Thread(object  : Runnable {
            override fun run(){
                TODO("Not yet implemented")
            }
        })

        thread.start()



    }
}
