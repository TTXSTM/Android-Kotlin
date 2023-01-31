package space.mairi.movieapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.mairi.movieapp.respository.Repository
import space.mairi.movieapp.respository.RepositoryImpl
import space.mairi.movieapp.app.AppState
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserver : MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel(){

        fun getLiveData() = liveDataToObserver

        fun getMovie() = getDataFromLocalSource(true)


        fun getMovieFromLocalStorageNowPlaying() = getDataFromLocalSource(true)

        private fun getDataFromLocalSource(isNowPlaying : Boolean){

            liveDataToObserver.value = AppState.Loading

            Thread{
                sleep(2000)

                liveDataToObserver.postValue(
                    AppState.Success(repositoryImpl.getMovieFromLocalStorageNowPlaying()))

            }.start()
        }
    }


