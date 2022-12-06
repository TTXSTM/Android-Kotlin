package space.mairi.kotlineapp

class Weather constructor(var town: String = "Moscow", var temperature: Int = 0){

    fun setWeather(town: String,
                   isHomeTown: Boolean = true,
                   temp : Int = 5){

        val param = if (isHomeTown) {
            "false"
        }else{
            "true"
        }

    }


}
