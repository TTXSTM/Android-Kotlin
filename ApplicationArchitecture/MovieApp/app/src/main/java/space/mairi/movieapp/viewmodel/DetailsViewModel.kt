package space.mairi.movieapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.mairi.movieapp.app.App.Companion.getHistoryDao
import space.mairi.movieapp.app.AppState
import space.mairi.movieapp.model.Movie
import space.mairi.movieapp.model.MovieDTO
import space.mairi.movieapp.respository.RemoteDataSource
import space.mairi.movieapp.respository.DetailsRepository
import space.mairi.movieapp.respository.DetailsRepositoryImpl
import space.mairi.movieapp.respository.LocalRepositoryImpl
import space.mairi.movieapp.utils.convertDtoToModel

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel (
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource()),
    private val historyResponseImpl : LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    private val callback = object : Callback<MovieDTO> {
        override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
            val serverResponse: MovieDTO? = response.body()

            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null){
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: MovieDTO) : AppState {
            val item = serverResponse

            return if (item == null || item.year == null
                || item.plot == null || item.fullTitle == null
                || item.imDbRating == null || item.releaseDate == null
                || item.runtimeMins == null) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel((serverResponse)))
            }
        }

    }

    fun getMovie(id : String) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getMovieDetailsFromServer(id, callback)
    }

    fun saveCityToDB(movie: Movie){
        historyResponseImpl.saveEntity(movie)
    }

}