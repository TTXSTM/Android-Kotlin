package space.mairi.movieapp.utils

import space.mairi.movieapp.model.ItemDTO
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.MovieDTO
import space.mairi.movieapp.model.getDefualtMovie


fun convertDtoToModel(movieDTO: MovieDTO) : List<Movie> {
    val item: ItemDTO = movieDTO.items!!

    return listOf(Movie(getDefualtMovie(), item.title!!, item.fullTitle!!,
        item.year!!, item.releaseDate!!, item.imDbRating!!, item.runtimeMins!!))

}