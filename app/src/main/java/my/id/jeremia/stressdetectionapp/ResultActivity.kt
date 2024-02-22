package my.id.jeremia.stressdetectionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val answerQ1 = intent.getStringExtra("answerQ1")
        val answerQ2 = intent.getStringExtra("answerQ2")
        val answerQ3 = intent.getStringExtra("answerQ3")

        findViewById<TextView>(R.id.resultText).text =
            "Q1 : ${answerQ1}\nQ2 : ${answerQ2}\nQ3 : ${answerQ3}"

    }
}