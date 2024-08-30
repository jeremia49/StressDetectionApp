package my.id.jeremia.stressdetectionapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.id.jeremia.stressdetectionapp.database.DatabaseService
import my.id.jeremia.stressdetectionapp.database.entity.Sensor
import javax.inject.Inject

@HiltViewModel
class SetupCameraViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    ) : ViewModel() {

    init {

    }

    fun insertData(sessID:String, value:Int) {
        viewModelScope.launch {
            databaseService.sensorDAO().insert(Sensor(sesID=sessID, tipesensor = "camera", nilaisensor = value))
        }
    }

}