package my.id.jeremia.stressdetectionapp

import android.R.id
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class QuestionActivity : AppCompatActivity() {
    var answerQ1 = "";
//    var answerQ2 = "";
    var answerQ3 = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val username = intent.getStringExtra("username")
        val programstudi = intent.getStringExtra("programstudi")

        val questionHeaderText = findViewById<TextView>(R.id.questionHeaderText)
        questionHeaderText.text = "Nama : ${username}\nProgram Studi : ${programstudi}"

        val dropdown = findViewById<Spinner>(R.id.spinnerPertanyaan3)
        val items = arrayOf("Opsi 1", "Opsi 2", "Opsi 3")
        val adapter = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter

        val p1 = findViewById<ChipGroup>(R.id.answerPertanyaan1)
        p1.setOnCheckedStateChangeListener{ chipGroup: ChipGroup, ints: MutableList<Int> ->
            Log.d("ANS",chipGroup.checkedChipId.toString() )
            val chipid = chipGroup.checkedChipId
            if(chipid == View.NO_ID){
                answerQ1 = ""
                return@setOnCheckedStateChangeListener
            }
            val chip: Chip = chipGroup.findViewById(chipid)
            answerQ1 = chip.text.toString()
        }

        val p3 = findViewById<Spinner>(R.id.spinnerPertanyaan3)
        p3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                answerQ3 = items[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                answerQ3 = ""
            }

        }


        findViewById<Button>(R.id.submitAnswer).setOnClickListener{
            startActivity(Intent(this, ResultActivity::class.java).apply {
                this.putExtra("answerQ1",answerQ1)
                this.putExtra("answerQ2", findViewById<EditText>(R.id.answerPertanyaan2).text.toString())
                this.putExtra("answerQ3", answerQ3)
            })
        }

    }
}