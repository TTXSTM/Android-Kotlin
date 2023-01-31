package space.mairi.movieapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Movie(
    val id: String = "tt9214832",
    val title: String = "Title",
    val year: String = "0000",
    val rating: String = "0.0",
    val release_date: String = "00-00-0000",
    val full_title: String = "FullTitle",
    val runtime_mins: String = "000 min",
    val plot: String = "Plot",
    val image: String = "https://m.media-amazon.com/images/M/MV5BOGRiODEzM2QtOTUyYi00MWRlLTg4MzMtZGI0YmUzNWUyMjQ0XkEyXkFqcGdeQXVyMDA4NzMyOA@@._V1_Ratio0.6762_AL_.jpg",
    val genres : String = "Genres",
    val boxOffice: @RawValue BoxOffice = getDefualtBudget(),
    val contentRating: String = "PG"
) : Parcelable

fun getDefualtBudget() = BoxOffice("$10,000,000 (estimated)", "$26,404,660")


fun getNowPlaying() = listOf(
        Movie("tt9214832","Эмма",
            "2020", "6.9"),

        Movie("tt8368406", "Вивариум",
            "2019", "6.1"),

        Movie("tt0353969","Воспоминания об убийстве",
            "2003", "7.7"),

        Movie( "tt1586680", "Бесстыжие",
            "2022", "8.9"),

        Movie( "tt0120689", "Зеленая миля",
            "1999", "6.5"),

        Movie("tt1160419","Дюна",
            "2020", "6.3"),

        Movie( "tt9243804","Легенда о Зелёном Рыцаре",
            "2019", "6.1"),

        Movie("tt4244994","Последняя дуэль",
            "2020", "6.2"),

        Movie( "tt9770150","Земля кочевников",
            "2020", "6.8"),

        Movie("tt2278871", "Blue Is the Warmest Colour",
            "2013", "7.7"),

    )
