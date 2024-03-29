package space.mairi.application_architecture.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.mairi.application_architecture.app.App.Companion.getHistoryDao
import space.mairi.application_architecture.app.AppState


import space.mairi.application_architecture.repository.RemoteDataSource
import space.mairi.application_architecture.model.Weather
import space.mairi.application_architecture.model.WeatherDTO
import space.mairi.application_architecture.repository.DetailsRepository
import space.mairi.application_architecture.repository.DetailsRepositoryImpl
import space.mairi.application_architecture.repository.LocalRepositoryImpl
import space.mairi.application_architecture.utils.convertDtoToModel


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryIMPL: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource()),
    private val historyResponseImpl : LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDao())
) : ViewModel(){

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()

            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null){
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO) : AppState {
            val fact = serverResponse.fact

            return if (fact == null || fact.temp == null || fact.feels_like ==
                null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel((serverResponse)))
            }
        }
    }

    fun getWeather(lat: Double, lon: Double){
        detailsLiveData.value = AppState.Loading
        detailsRepositoryIMPL.getWeatherDetailsFromServer(lat, lon, callback)
    }

    fun saveCityToDB(weather: Weather) {
        historyResponseImpl.saveEntity(weather)
    }
}