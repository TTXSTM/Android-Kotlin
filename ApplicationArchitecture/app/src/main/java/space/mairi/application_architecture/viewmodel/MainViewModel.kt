package space.mairi.application_architecture.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.mairi.application_architecture.app.AppState
import space.mairi.application_architecture.repository.MainRepository
import space.mairi.application_architecture.repository.MainRepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl : MainRepository = MainRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun  getWeather() = getDataFromLocalSource(true)

    fun getWeatherFomLocalStorageRus() = getDataFromLocalSource(true)
    fun getWeatherFomLocalStorageWorld() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRus : Boolean){
        liveDataToObserve.value = AppState.Loading

        Thread{
            sleep(2000)

            liveDataToObserve.postValue(
                AppState.Success(
                    if(isRus){
                        repositoryImpl.getWeatherFomLocalStorageRus()
                    }else{
                        repositoryImpl.getWeatherFomLocalStorageWorld()
                    }
                )
            )
        }.start()
    }
}