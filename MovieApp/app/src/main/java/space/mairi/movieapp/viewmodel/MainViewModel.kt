package space.mairi.movieapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.mairi.movieapp.model.Repository
import space.mairi.movieapp.model.RepositoryImpl
import space.mairi.movieapp.viewmodel.AppState
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserver : MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel(){

        fun getLiveData() = liveDataToObserver

        fun getMovie() = getDataFromLocalSource()

        private fun getDataFromLocalSource(){

            liveDataToObserver.value = AppState.Loading

            Thread{
                sleep(2000)

                liveDataToObserver.postValue(
                    AppState.Success(repositoryImpl.getMovieFomLocalStorage()))

            }.start()
        }
    }


