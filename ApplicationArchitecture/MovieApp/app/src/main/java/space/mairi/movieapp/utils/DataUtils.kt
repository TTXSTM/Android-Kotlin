package space.mairi.movieapp.utils

import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.MovieDTO
import space.mairi.movieapp.room.HistoryEntity


fun convertDtoToModel(movieDTO: MovieDTO) : List<Movie> {
    val item: MovieDTO = movieDTO

    return listOf(Movie(item.id!!, item.title!!, item.year!!,
        item.imDbRating!!, item.releaseDate!!, item.fullTitle!!, item.runtimeMins!!, item.plot!!, item.image!!, item.genres!!, item.boxOffice!!, item.contentRating!!))
}

fun convertHistoryEntityToMovie(entityList : List<HistoryEntity>) : List<Movie> {
    return entityList.map {
        Movie(it.idm, it.title, it.full_title)
    }
}

fun convertMovieToEntity(movie: Movie) : HistoryEntity {
    return HistoryEntity(0, movie.id, movie.title, movie.full_title)
}