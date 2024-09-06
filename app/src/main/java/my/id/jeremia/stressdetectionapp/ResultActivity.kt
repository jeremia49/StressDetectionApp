package my.id.jeremia.stressdetectionapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private val resultViewModel : ResultViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val username = intent.getStringExtra("username")
        val programstudi = intent.getStringExtra("programstudi")
        val score = intent.getDoubleExtra("score",0.0)
        val sessionID = intent.getStringExtra("sessionID") ?: ""


        findViewById<TextView>(R.id.resultUsername).text = "Nama : ${username}"
        findViewById<TextView>(R.id.resultProdi).text = "Program Studi : ${programstudi}"

        findViewById<TextView>(R.id.resultText).text =
            "Hasil Dari Pertanyaan : ${score}"

//        val getData = viewModelS

        resultViewModel.getTextToPrint(findViewById<TextView>(R.id.sensorText), sessionID)


    }
}