package my.id.jeremia.stressdetectionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val username = intent.getStringExtra("username")
        val programstudi = intent.getStringExtra("programstudi")
        val answers = intent.getIntArrayExtra("answers")
        val score = intent.getDoubleExtra("score",0.0)

        findViewById<TextView>(R.id.resultUsername).text = username
        findViewById<TextView>(R.id.resultProdi).text = programstudi

        findViewById<TextView>(R.id.resultText).text =
            " ${score}"

    }
}