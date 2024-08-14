package my.id.jeremia.stressdetectionapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensors")
data class Sensor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sesID: String,
    val tipesensor: String,
    val nilaisensor:Int,
)