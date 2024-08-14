package my.id.jeremia.stressdetectionapp

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.id.jeremia.stressdetectionapp.database.AppDatabase
import my.id.jeremia.stressdetectionapp.database.DatabaseService
import my.id.jeremia.stressdetectionapp.database.entity.Sensor
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    ) : ViewModel() {
    init{

    }

    fun insertData(sessID:String, sensor:String, value:Int) {
        viewModelScope.launch {
            databaseService.sensorDAO().insert(Sensor(sesID=sessID, tipesensor = sensor, nilaisensor = value))
        }
    }

    suspend fun getData(sessID:String): List<Sensor> {
        return databaseService.sensorDAO().getAllSensors()
    }

    fun getTextToPrint(v: TextView, sessID: String){
        viewModelScope.launch{
            val z = getData(sessID).joinToString(separator = "\n") {
                it.id.toString() + it.tipesensor + it.nilaisensor
            }
            v.text = z
        }
    }

}