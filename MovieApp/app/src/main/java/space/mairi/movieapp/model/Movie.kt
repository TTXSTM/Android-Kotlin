package space.mairi.movieapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val name: MovieInf = getDefualtMovie(),
    val year: String = "2020",
    val rating: String = "0.0",
    val full_title : String = "Emma",
    val release_date : String = "16-02-2022",
    val runtime_mins : String = "112 min",
    val plot : String = "plot"

) : Parcelable

fun getDefualtMovie() = MovieInf("tt9214832", "Эмма")


fun getNowPlaying() = listOf(
        Movie(MovieInf("tt9214832","Эмма"),
            "2020", "6.9"),

        Movie(MovieInf("tt8368406", "Вивариум"),
            "2019", "6.1"),

        Movie(MovieInf("tt0353969","Воспоминания об убийстве"),
            "2003", "7.7"),

        Movie(MovieInf( "tt1586680", "Бесстыжие"),
            "2022", "8.9"),

        Movie(MovieInf( "tt0120689", "Зеленая миля"),
            "1999", "6.5"),

        Movie(MovieInf("tt1160419","Дюна"),
            "2020", "6.3"),

        Movie(MovieInf( "tt9243804","Легенда о Зелёном Рыцаре"),
            "2019", "6.1"),

        Movie(MovieInf("tt4244994","Последняя дуэль"),
            "2020", "6.2"),

        Movie(MovieInf( "tt9770150","Земля кочевников"),
            "2020", "6.8"),

    )
