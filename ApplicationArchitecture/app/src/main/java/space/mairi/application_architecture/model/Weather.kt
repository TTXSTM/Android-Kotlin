package space.mairi.application_architecture.model

data class Weather(
    val city: City = getDefualtCity(),
    val temperature: Int = 0,
    val feelLike: Int = 0
)

fun getDefualtCity() = City("Москва", 55.755826, 37.61729990000035)