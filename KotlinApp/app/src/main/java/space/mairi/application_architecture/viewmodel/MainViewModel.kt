package space.mairi.application_architecture.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.mairi.application_architecture.model.Repository
import space.mairi.application_architecture.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve : MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl : Repository = RepositoryImpl()) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun  getWeather() = getDataFromLocalSource(true)

    fun getWeatherFomLocalStorageRus() = getDataFromLocalSource(true)
    fun getWeatherFomLocalStorageWorld() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRus : Boolean){
        liveDataToObserve.value = AppState.Loading

        Thread{
            sleep(2000)

            liveDataToObserve.postValue(AppState.Success(
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