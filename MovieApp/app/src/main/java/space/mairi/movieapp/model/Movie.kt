package space.mairi.movieapp.model

data class Movie(

    val name: MovieInf = getDefualtMovie(),
    val year: Int = 0,
    val rating: Int = 0

)

fun getDefualtMovie() = MovieInf("Эмма", 2020, 6.9)