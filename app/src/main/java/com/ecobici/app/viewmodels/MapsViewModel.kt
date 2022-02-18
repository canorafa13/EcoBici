package com.ecobici.app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecobici.app.classes.EcoBici
import com.ecobici.app.classes.Stations
import com.ecobici.app.connection.ApiRepository
import com.ecobici.app.utils.ProccessException
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject

class MapsViewModel: ViewModel() {
    val stations: MutableLiveData<List<Stations>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()


    fun getStations(id: String){
        viewModelScope.launch {
            ApiRepository().get(id, object: ApiRepository.Callback{
                override fun success(response: JSONObject) {
                    Log.e("SUCCESS", response.toString())
                    try {
                        stations.value = Gson().fromJson(response.toString(), EcoBici::class.java).network.stations
                    }catch (e: Exception){
                        error.value = ProccessException.getError(e)
                    }
                }

                override fun error(error: Exception) {
                    this@MapsViewModel.error.value = ProccessException.getError(error)
                }
            })
        }
    }
}