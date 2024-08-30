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

    suspend fun getCameraData(sessID: String): List<Sensor>{
        return databaseService.sensorDAO().getBySessionAndSensors(sessID, "camera")
    }
    suspend fun getBPMData(sessID: String): List<Sensor>{
        return databaseService.sensorDAO().getBySessionAndSensors(sessID, "bpm")
    }
    suspend fun getHumidityData(sessID: String): List<Sensor>{
        return databaseService.sensorDAO().getBySessionAndSensors(sessID, "humidity")
    }
    suspend fun getTemperatureData(sessID: String): List<Sensor>{
        return databaseService.sensorDAO().getBySessionAndSensors(sessID, "temperature")
    }
    suspend fun getMicrophoneData(sessID: String): List<Sensor>{
        return databaseService.sensorDAO().getBySessionAndSensors(sessID, "mic")
    }

    fun averageSensor(data:List<Sensor>) : Float{
        var sum = 0
        data.forEach{
            sum += it.nilaisensor
        }
        return sum.toFloat()/data.size
    }

    fun checkMicrophone(data:List<Sensor>):Boolean{
        var status = false
        data.forEach{
            if(it.nilaisensor == 1){
                status = true
            }
        }
        return status
    }

    fun getTextToPrint(v: TextView, sessID: String){

        viewModelScope.launch{
            val camera = averageSensor(getCameraData(sessID))
            val bpm = averageSensor(getBPMData(sessID))
            val humidity = averageSensor(getHumidityData(sessID))
            val temperature = averageSensor(getTemperatureData(sessID))
            val mic = checkMicrophone(getMicrophoneData(sessID))

            v.text = "Camera : ${camera}\nBPM : ${bpm}\nHumidity : ${humidity}\nTemperature : ${temperature}\nMic : ${mic}"
        }
    }

}