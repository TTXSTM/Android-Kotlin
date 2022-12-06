package space.mairi.kotlineapp

object Repository {
    private var weatherList: List<Weather> = listOf<Weather>()
        set(value) {
            field = value
        }

    val hasData : Boolean
        get() = weatherList.size != 0

    fun getWeatherList(): List<Weather> = weatherList

    fun blabla(parameter : String, pos : Int) : Int {
        return 0
    }

    fun func2(){
        for(weather in weatherList){
            println(weather.town)
        }

        for (i in 10 downTo 1 step 2){
            println("Hello Kotlin!")
        }
        for (i in 0 until weatherList.size) {
            if (weatherList[i] == weather){
                weatherList.set(i, weather)
            return
        }  }
    }

    fun printString(vararg string: String) {
        for (s in string) {
        println(s)
        }
    }
}