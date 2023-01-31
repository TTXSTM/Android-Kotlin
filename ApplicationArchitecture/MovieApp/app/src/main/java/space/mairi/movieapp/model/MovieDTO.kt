package space.mairi.movieapp.model

data class MovieDTO(
    val id: String?,
    val title : String?,
    val fullTitle : String?,
    val year : String?,
    val releaseDate : String?,
    val runtimeMins : String?,
    val plot : String?,
    val imDbRating : String?,
    val image : String?,
    val genres : String?,
    val boxOffice: BoxOffice?,
    val contentRating: String?
)

data class BoxOffice(
    val budget : String?,
    val cumulativeWorldwideGross: String?
)



